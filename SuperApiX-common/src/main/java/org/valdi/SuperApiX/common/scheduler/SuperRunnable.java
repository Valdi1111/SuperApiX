package org.valdi.SuperApiX.common.scheduler;

import org.valdi.SuperApiX.common.plugin.StoreLoader;
import org.valdi.SuperApiX.common.scheduler.task.SuperTask;

import java.util.concurrent.TimeUnit;

public abstract class SuperRunnable implements Runnable {
    private final StoreLoader plugin;
    private SuperTask task;

    public SuperRunnable(StoreLoader plugin) {
        this.plugin = plugin;
    }

    public synchronized boolean isCancelled() throws IllegalStateException {
        this.checkScheduled();
        return this.task.isCancelled();
    }

    public synchronized void cancel() throws IllegalStateException {
        plugin.getScheduler().cancelTask(this.getTaskId());
    }

    public synchronized SuperTask runTask() throws IllegalArgumentException, IllegalStateException {
        this.checkNotYetScheduled();
        return this.setupTask(plugin.getScheduler().runTask(this));
    }

    public synchronized SuperTask runTaskAsynchronously() throws IllegalArgumentException, IllegalStateException {
        this.checkNotYetScheduled();
        return this.setupTask(plugin.getScheduler().runTaskAsynchronously(this));
    }

    public synchronized SuperTask runTaskLater(long delay, TimeUnit unit) throws IllegalArgumentException, IllegalStateException {
        this.checkNotYetScheduled();
        return this.setupTask(plugin.getScheduler().runTaskLater(this, delay, unit));
    }

    public synchronized SuperTask runTaskLaterAsynchronously(long delay, TimeUnit unit) throws IllegalArgumentException, IllegalStateException {
        this.checkNotYetScheduled();
        return this.setupTask(plugin.getScheduler().runTaskLaterAsynchronously(this, delay, unit));
    }

    public synchronized SuperTask runTaskTimer(long interval, TimeUnit unit) throws IllegalArgumentException, IllegalStateException {
        this.checkNotYetScheduled();
        return this.setupTask(plugin.getScheduler().runTaskTimer(this, interval, unit));
    }

    public synchronized SuperTask runTaskTimerAsynchronously(long interval, TimeUnit unit) throws IllegalArgumentException, IllegalStateException {
        this.checkNotYetScheduled();
        return this.setupTask(plugin.getScheduler().runTaskTimerAsynchronously(this, interval, unit));
    }

    public synchronized SuperTask runTaskTimer(long delay, long interval, TimeUnit unit) throws IllegalArgumentException, IllegalStateException {
        this.checkNotYetScheduled();
        return this.setupTask(plugin.getScheduler().runTaskTimer(this, delay, interval, unit));
    }

    public synchronized SuperTask runTaskTimerAsynchronously(long delay, long interval, TimeUnit unit) throws IllegalArgumentException, IllegalStateException {
        this.checkNotYetScheduled();
        return this.setupTask(plugin.getScheduler().runTaskTimerAsynchronously(this, delay, interval, unit));
    }

    public synchronized int getTaskId() throws IllegalStateException {
        this.checkScheduled();
        return this.task.getTaskId();
    }

    private void checkScheduled() {
        if (this.task == null) {
            throw new IllegalStateException("Not scheduled yet");
        }
    }

    private void checkNotYetScheduled() {
        if (this.task != null) {
            throw new IllegalStateException("Already scheduled as " + this.task.getTaskId());
        }
    }

    private SuperTask setupTask(SuperTask task) {
        this.task = task;
        return task;
    }
}
