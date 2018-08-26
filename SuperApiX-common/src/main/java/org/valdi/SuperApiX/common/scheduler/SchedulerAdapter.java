package org.valdi.SuperApiX.common.scheduler;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public interface SchedulerAdapter {

    /**
     * Gets an async executor instance
     *
     * @return an async executor instance
     */
    public Executor async();

    /**
     * Gets a sync executor instance
     *
     * @return a sync executor instance
     */
    public Executor sync();

    /**
     * Executes a task async
     *
     * @param task the task
     */
    public default void executeAsync(Runnable task) {
        async().execute(task);
    }

    /**
     * Executes a task sync
     *
     * @param task the task
     */
    public default void executeSync(Runnable task) {
        sync().execute(task);
    }

    /**
     * Executes the given task with a delay.
     *
     * @param task the task
     * @param delay the delay
     * @param unit the unit of delay
     * @return the resultant task instance
     */
    public SchedulerTask asyncLater(Runnable task, long delay, TimeUnit unit);

    /**
     * Executes the given task with a delay.
     *
     * @param task the task
     * @param delay the delay
     * @param unit the unit of delay
     * @return the resultant task instance
     */
    public SchedulerTask syncLater(Runnable task, long delay, TimeUnit unit);

    /**
     * Executes the given task repeatedly at a given interval.
     *
     * @param task the task
     * @param interval the interval
     * @param unit the unit of interval
     * @return the resultant task instance
     */
    public SchedulerTask asyncRepeating(Runnable task, long interval, TimeUnit unit);

    /**
     * Executes the given task repeatedly at a given interval.
     *
     * @param task the task
     * @param interval the interval
     * @param unit the unit of interval
     * @return the resultant task instance
     */
    public SchedulerTask syncRepeating(Runnable task, long interval, TimeUnit unit);

    /**
     * Executes the given task repeatedly at a given interval.
     *
     * @param task the task
     * @param interval the interval
     * @param unit the unit of interval
     * @return the resultant task instance
     */
    public SchedulerTask asyncDelayedRepeating(Runnable task, long delay, long interval, TimeUnit unit);

    /**
     * Executes the given task repeatedly at a given interval.
     *
     * @param task the task
     * @param interval the interval
     * @param unit the unit of interval
     * @return the resultant task instance
     */
    public SchedulerTask syncDelayedRepeating(Runnable task, long delay, long interval, TimeUnit unit);

    /**
     * Shuts down this executor instance
     */
    public void shutdown();

}
