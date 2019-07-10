package org.valdi.SuperApiX.common.scheduler;

import org.valdi.SuperApiX.common.scheduler.task.SuperTask;
import org.valdi.SuperApiX.common.scheduler.task.SuperWorker;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public interface SuperScheduler {

    /**
     * Gets a sync executor instance
     *
     * @return a sync executor instance
     */
    default Executor sync() {
        return this::scheduleSyncDelayedTask;
    }

    /**
     * Gets an async executor instance
     *
     * @return an async executor instance
     */
    default Executor async() {
        return this::scheduleAsyncDelayedTask;
    }

    /**
     * Calls a method on the main thread and returns a Future object.
     * This task will be executed by the main server thread.
     * <ul>
     * <li>Note: The Future.get() methods must NOT be called from the main thread.</li>
     * <li>Note2: There is at least an average of 10ms latency until the isDone() method returns true.</li>
     * </ul>
     *
     * @param callable Task to be executed
     * @param <T>      The callable's return type
     * @return Future object related to the task
     */
    <T> Future<T> callSyncMethod(Callable<T> callable);

    /**
     * Removes task from scheduler.
     *
     * @param taskId Id number of the task to be removed
     */
    void cancelTask(int taskId);

    /**
     * Removes all tasks from the scheduler.
     */
    void cancelTasks();

    /**
     * Check if the task currently running.
     * A repeating task might not be running currently, but will be
     * running in the future. A task that has finished, and does not
     * repeat, will not be running ever again.
     * <p>
     * Explicitly, a task is running if there exists a thread for it,
     * and that thread is alive.
     *
     * @param taskId The task to check.
     * @return If the task is currently running.
     */
    boolean isCurrentlyRunning(int taskId);

    /**
     * Check if the task queued to be run later.
     * If a repeating task is currently running, it might not be queued
     * now but could be in the future. A task that is not queued, and
     * not running, will not be queued again.
     *
     * @param taskId The task to check.
     * @return If the task is queued to be run.
     */
    boolean isQueued(int taskId);

    /**
     * Returns a list of all active workers.
     * This list contains asynch tasks that are being executed by separate
     * threads.
     *
     * @return Active workers
     */
    List<SuperWorker> getActiveWorkers();

    /**
     * Returns a list of all pending tasks. The ordering of the tasks is
     * not related to their order of execution.
     *
     * @return Pending tasks
     */
    List<SuperTask> getPendingTasks();

    default int scheduleSyncDelayedTask(Runnable task) {
        return this.scheduleSyncDelayedTask(task, 0L, TimeUnit.MILLISECONDS);
    }

    @Deprecated
    default int scheduleAsyncDelayedTask(Runnable task) {
        return this.scheduleAsyncDelayedTask(task, 0L, TimeUnit.MILLISECONDS);
    }

    default int scheduleSyncDelayedTask(Runnable task, long delay, TimeUnit unit) {
        return this.scheduleSyncRepeatingTask(task, delay, -1L, unit);
    }

    @Deprecated
    default int scheduleAsyncDelayedTask(Runnable task, long delay, TimeUnit unit) {
        return this.scheduleAsyncRepeatingTask(task, delay, -1L, unit);
    }

    default int scheduleSyncRepeatingTask(Runnable task, long delay, long period, TimeUnit unit) {
        return this.runTaskTimer(task, delay, period, unit).getTaskId();
    }

    @Deprecated
    default int scheduleAsyncRepeatingTask(Runnable task, long delay, long period, TimeUnit unit) {
        return this.runTaskTimerAsynchronously(task, delay, period, unit).getTaskId();
    }

    /**
     * Executes a task sync
     *
     * @param task the task
     * @return the resultant task instance
     */
    default SuperTask runTask(Runnable task) {
        return this.runTaskLater(task, 0L, TimeUnit.MILLISECONDS);
    }

    /**
     * Executes a task sync
     *
     * @param task the task
     */
    default void runTask(Consumer<SuperTask> task) throws IllegalArgumentException {
        this.runTaskLater(task, 0L, TimeUnit.MILLISECONDS);
    }

    /**
     * Executes a task async
     *
     * @param task the task
     * @return the resultant task instance
     */
    default SuperTask runTaskAsynchronously(Runnable task) {
        return this.runTaskLaterAsynchronously(task, 0L, TimeUnit.MILLISECONDS);
    }

    /**
     * Executes a task async
     *
     * @param task the task
     */
    default void runTaskAsynchronously(Consumer<SuperTask> task) throws IllegalArgumentException {
        this.runTaskLaterAsynchronously(task, 0L, TimeUnit.MILLISECONDS);
    }

    /**
     * Executes the given task with a delay.
     *
     * @param task  the task
     * @param delay the delay
     * @param unit  the unit of delay
     * @return the resultant task instance
     */
    default SuperTask runTaskLater(Runnable task, long delay, TimeUnit unit) {
        return this.runTaskTimer(task, delay, -1L, unit);
    }

    /**
     * Executes the given task with a delay.
     *
     * @param task  the task
     * @param delay the delay
     * @param unit  the unit of delay
     */
    default void runTaskLater(Consumer<SuperTask> task, long delay, TimeUnit unit) throws IllegalArgumentException {
        this.runTaskTimer(task, delay, -1L, unit);
    }

    /**
     * Executes the given task with a delay.
     *
     * @param task  the task
     * @param delay the delay
     * @param unit  the unit of delay
     * @return the resultant task instance
     */
    default SuperTask runTaskLaterAsynchronously(Runnable task, long delay, TimeUnit unit) {
        return this.runTaskTimerAsynchronously(task, delay, -1L, unit);
    }

    /**
     * Executes the given task with a delay.
     *
     * @param task  the task
     * @param delay the delay
     * @param unit  the unit of delay
     */
    default void runTaskLaterAsynchronously(Consumer<SuperTask> task, long delay, TimeUnit unit) throws IllegalArgumentException {
        this.runTaskTimerAsynchronously(task, delay, -1L, unit);
    }

    /**
     * Executes the given task repeatedly at a given interval.
     *
     * @param task     the task
     * @param interval the interval
     * @param unit     the unit of interval
     * @return the resultant task instance
     */
    default SuperTask runTaskTimer(Runnable task, long interval, TimeUnit unit) {
        return this.runTaskTimer(task, 0L, interval, unit);
    }

    /**
     * Executes the given task repeatedly at a given interval.
     *
     * @param task     the task
     * @param interval the interval
     * @param unit     the unit of interval
     */
    default void runTaskTimer(Consumer<SuperTask> task, long interval, TimeUnit unit) throws IllegalArgumentException {
        this.runTaskTimer(task, 0L, interval, unit);
    }

    /**
     * Executes the given task repeatedly at a given interval.
     *
     * @param task     the task
     * @param interval the interval
     * @param unit     the unit of interval
     * @return the resultant task instance
     */
    default SuperTask runTaskTimerAsynchronously(Runnable task, long interval, TimeUnit unit) {
        return this.runTaskTimerAsynchronously(task, 0L, interval, unit);
    }

    /**
     * Executes the given task repeatedly at a given interval.
     *
     * @param task     the task
     * @param interval the interval
     * @param unit     the unit of interval
     */
    default void runTaskTimerAsynchronously(Consumer<SuperTask> task, long interval, TimeUnit unit) throws IllegalArgumentException {
        this.runTaskTimerAsynchronously(task, 0L, interval, unit);
    }

    /**
     * Executes the given task repeatedly at a given interval.
     *
     * @param task     the task
     * @param interval the interval
     * @param unit     the unit of interval
     * @return the resultant task instance
     */
    SuperTask runTaskTimer(Runnable task, long delay, long interval, TimeUnit unit);

    /**
     * Executes the given task repeatedly at a given interval.
     *
     * @param task     the task
     * @param interval the interval
     * @param unit     the unit of interval
     */
    void runTaskTimer(Consumer<SuperTask> task, long delay, long interval, TimeUnit unit) throws IllegalArgumentException;

    /**
     * Executes the given task repeatedly at a given interval.
     *
     * @param task     the task
     * @param interval the interval
     * @param unit     the unit of interval
     * @return the resultant task instance
     */
    SuperTask runTaskTimerAsynchronously(Runnable task, long delay, long interval, TimeUnit unit);

    /**
     * Executes the given task repeatedly at a given interval.
     *
     * @param task     the task
     * @param interval the interval
     * @param unit     the unit of interval
     */
    void runTaskTimerAsynchronously(Consumer<SuperTask> task, long delay, long interval, TimeUnit unit) throws IllegalArgumentException;

    /**
     * Shuts down this executor instance
     */
    void shutdown();

}
