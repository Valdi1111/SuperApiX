package org.valdi.SuperApiX.common.scheduler.task;

import org.valdi.SuperApiX.common.plugin.StoreLoader;

import java.util.function.Consumer;

public class SimpleTask implements SuperTask, Runnable {
    private volatile SimpleTask next;
    public static final int ERROR = 0;
    public static final int NO_REPEATING = -1;
    public static final int CANCEL = -2;
    public static final int PROCESS_FOR_FUTURE = -3;
    public static final int DONE_FOR_FUTURE = -4;
    private volatile long period;
    private long nextRun;
    private final Runnable rTask;
    private final Consumer<SuperTask> cTask;
    private final StoreLoader plugin;
    private final int id;

    public SimpleTask() {
        this(null, null, -1, -1L);
    }

    public SimpleTask(Object task) {
        this(null, task, -1, -1L);
    }

    public SimpleTask(StoreLoader plugin, Object task, int id, long period) {
        super();

        this.plugin = plugin;
        this.next = null;

        if (task instanceof Runnable) {
            this.rTask = (Runnable)task;
            this.cTask = null;
        }
        else if (task instanceof Consumer) {
            this.cTask = (Consumer)task;
            this.rTask = null;
        }
        else {
            if (task != null) {
                throw new AssertionError("Illegal task class " + task);
            }
            this.rTask = null;
            this.cTask = null;
        }

        this.id = id;
        this.period = period;
    }

    @Override
    public void run() {
        if (this.rTask != null) {
            this.rTask.run();
        } else {
            this.cTask.accept(this);
        }
    }

    @Override
    public int getTaskId() {
        return id;
    }

    @Override
    public StoreLoader getOwner() {
        return plugin;
    }

    @Override
    public boolean isSync() {
        return true;
    }

    @Override
    public boolean isCancelled() {
        if (this.period == -2L) {
            return true;
        }
        return false;
    }

    @Override
    public void cancel() {
        plugin.getScheduler().cancelTask(this.id);
    }

    public long getPeriod() {
        return this.period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public long getNextRun() {
        return this.nextRun;
    }

    public void setNextRun(long nextRun) {
        this.nextRun = nextRun;
    }

    public SimpleTask getNext() {
        return this.next;
    }

    public void setNext(SimpleTask next) {
        this.next = next;
    }

    public Class<?> getTaskClass() {
        return this.rTask != null ? this.rTask.getClass() : (this.cTask != null ? this.cTask.getClass() : null);
    }

    public boolean cancel0() {
        this.setPeriod(-2L);
        return true;
    }

    public String getTaskName() {
        return this.getTaskClass() == null ? "Unknown" : this.getTaskClass().getName();
    }
}
