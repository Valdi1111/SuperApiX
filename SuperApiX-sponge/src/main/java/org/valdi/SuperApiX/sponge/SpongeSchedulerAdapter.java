package org.valdi.SuperApiX.sponge;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.SpongeExecutorService;
import org.spongepowered.api.scheduler.Task;
import org.valdi.SuperApiX.common.ISuperBootstrap;
import org.valdi.SuperApiX.common.scheduler.SchedulerAdapter;
import org.valdi.SuperApiX.common.scheduler.SchedulerTask;
import org.valdi.SuperApiX.common.utils.Iterators;

public class SpongeSchedulerAdapter implements SchedulerAdapter {
    private final ISuperBootstrap bootstrap;
    
    private final Scheduler scheduler;
    private final SpongeExecutorService sync;
    private final SpongeExecutorService async;
    
    private final Set<Task> tasks = Collections.newSetFromMap(new WeakHashMap<>());

    public SpongeSchedulerAdapter(ISuperBootstrap bootstrap, Scheduler scheduler, SpongeExecutorService sync, SpongeExecutorService async) {
        this.bootstrap = bootstrap;
        this.scheduler = scheduler;
        this.sync = sync;
        this.async = async;
    }

    @Override
    public Executor async() {
        return this.async;
    }

    @Override
    public Executor sync() {
        return this.sync;
    }

    @Override
    public void executeAsync(Runnable runnable) {
        this.scheduler
        .createTaskBuilder()
        .async()
        .execute(runnable)
        .submit(this.bootstrap);
    }

    @Override
    public void executeSync(Runnable runnable) {
        this.scheduler
        .createTaskBuilder()
        .execute(runnable)
        .submit(this.bootstrap);
    }

    @Override
    public SchedulerTask asyncLater(Runnable task, long delay, TimeUnit unit) {
        Task t = this.scheduler.createTaskBuilder()
                .async()
                .delay(delay, unit)
                .execute(task)
                .submit(this.bootstrap);

        this.tasks.add(t);
        return t::cancel;
    }

    @Override
    public SchedulerTask syncLater(Runnable task, long delay, TimeUnit unit) {
        Task t = this.scheduler.createTaskBuilder()
                .delay(delay, unit)
                .execute(task)
                .submit(this.bootstrap);

        this.tasks.add(t);
        return t::cancel;
    }

    @Override
    public SchedulerTask asyncRepeating(Runnable task, long interval, TimeUnit unit) {
        Task t = this.scheduler.createTaskBuilder()
                .async()
                .interval(interval, unit)
                .execute(task)
                .submit(this.bootstrap);

        this.tasks.add(t);
        return t::cancel;
    }

    @Override
    public SchedulerTask syncRepeating(Runnable task, long interval, TimeUnit unit) {
        Task t = this.scheduler.createTaskBuilder()
                .interval(interval, unit)
                .execute(task)
                .submit(this.bootstrap);

        this.tasks.add(t);
        return t::cancel;
    }

    @Override
    public SchedulerTask asyncDelayedRepeating(Runnable task, long delay, long interval, TimeUnit unit) {
        Task t = this.scheduler.createTaskBuilder()
                .async()
                .interval(interval, unit)
                .delay(delay, unit)
                .execute(task)
                .submit(this.bootstrap);

        this.tasks.add(t);
        return t::cancel;
    }

    @Override
    public SchedulerTask syncDelayedRepeating(Runnable task, long delay, long interval, TimeUnit unit) {
        Task t = this.scheduler.createTaskBuilder()
                .interval(interval, unit)
                .delay(delay, unit)
                .execute(task)
                .submit(this.bootstrap);

        this.tasks.add(t);
        return t::cancel;
    }

    @Override
    public void shutdown() {
        Iterators.iterate(this.tasks, Task::cancel);
    }

}
