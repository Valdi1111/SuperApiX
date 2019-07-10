package org.valdi.SuperApiX.common.scheduler.task;

import org.valdi.SuperApiX.common.plugin.StoreLoader;

import java.util.concurrent.*;

public class SuperFuture<T> extends SimpleTask implements Future<T> {
    private final Callable<T> callable;
    private T value;
    private Exception exception = null;

    public SuperFuture(Callable<T> callable, StoreLoader plugin, int id) {
        super(plugin, null, id, -1L);
        this.callable = callable;
    }

    @Override
    public synchronized boolean cancel(boolean mayInterruptIfRunning) {
        if (this.getPeriod() != -1L) {
            return false;
        }
        this.setPeriod(-2L);
        return true;
    }

    @Override
    public boolean isDone() {
        long period = this.getPeriod();
        return period != -1L && period != -3L;
    }

    @Override
    public T get() throws CancellationException, InterruptedException, ExecutionException {
        try {
            return this.get(0L, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            throw new Error(e);
        }
    }

    @Override
    public synchronized T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        timeout = unit.toMillis(timeout);
        long period = this.getPeriod();
        long timestamp = timeout > 0L ? System.currentTimeMillis() : 0L;
        while (period == -1L || period == -3L) {
            this.wait(timeout);
            period = this.getPeriod();
            if (period != -1L && period != -3L) {
                break;
            }
            if (timeout == 0L) {
                continue;
            }
            long oldTimestamp = timestamp;
            timestamp = System.currentTimeMillis();
            if ((timeout += oldTimestamp - timestamp) > 0L) {
                continue;
            }
            throw new TimeoutException();
        }
        if (period == -2L) {
            throw new CancellationException();
        }
        if (period == -4L) {
            if (this.exception == null) {
                return this.value;
            }
            throw new ExecutionException(this.exception);
        }
        throw new IllegalStateException("Expected -1 to -4, got " + period);
    }

    @Override
    public void run() {
        synchronized (this) {
            if (this.getPeriod() == -2L) {
                return;
            }
            this.setPeriod(-3L);
        }
        try {
            try {
                this.value = this.callable.call();
            } catch (Exception e) {
                this.exception = e;
                synchronized (this) {
                    this.setPeriod(-4L);
                    this.notifyAll();
                    return;
                }
            }
        } catch (Throwable throwable) {
            synchronized (this) {
                this.setPeriod(-4L);
                this.notifyAll();
            }
            throw throwable;
        }
        synchronized (this) {
            this.setPeriod(-4L);
            this.notifyAll();
        }
    }

    @Override
    public synchronized boolean cancel0() {
        if (this.getPeriod() != -1L) {
            return false;
        }

        this.setPeriod(-2L);
        this.notifyAll();
        return true;
    }
}
