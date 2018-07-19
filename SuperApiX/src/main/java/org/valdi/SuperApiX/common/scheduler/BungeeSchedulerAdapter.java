package org.valdi.SuperApiX.common.scheduler;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import org.valdi.SuperApiX.common.utils.Iterators;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class BungeeSchedulerAdapter implements SchedulerAdapter {
    private final Plugin bootstrap;

    private final Executor executor;
    private final Set<ScheduledTask> tasks = Collections.newSetFromMap(new WeakHashMap<>());

    public BungeeSchedulerAdapter(Plugin bootstrap) {
        this.bootstrap = bootstrap;
        this.executor = r -> bootstrap.getProxy().getScheduler().runAsync(bootstrap, r);
    }

    @Override
    public Executor async() {
        return this.executor;
    }

    @Override
    public Executor sync() {
        return this.executor;
    }

    @Override
    public SchedulerTask asyncLater(Runnable task, long delay, TimeUnit unit) {
        ScheduledTask t = this.bootstrap.getProxy().getScheduler().schedule(this.bootstrap, task, delay, unit);
        
        this.tasks.add(t);
        return t::cancel;
    }

	@Override
	public SchedulerTask syncLater(Runnable task, long delay, TimeUnit unit) {
        ScheduledTask t = this.bootstrap.getProxy().getScheduler().schedule(this.bootstrap, task, delay, unit);
        
        this.tasks.add(t);
        return t::cancel;
	}

    @Override
    public SchedulerTask asyncRepeating(Runnable task, long interval, TimeUnit unit) {
        ScheduledTask t = this.bootstrap.getProxy().getScheduler().schedule(this.bootstrap, task, 0L, interval, unit);
        
        this.tasks.add(t);
        return t::cancel;
    }

	@Override
	public SchedulerTask syncRepeating(Runnable task, long interval, TimeUnit unit) {
        ScheduledTask t = this.bootstrap.getProxy().getScheduler().schedule(this.bootstrap, task, 0L, interval, unit);
        
        this.tasks.add(t);
        return t::cancel;
	}

    @Override
    public SchedulerTask asyncDelayedRepeating(Runnable task, long delay, long interval, TimeUnit unit) {
        ScheduledTask t = this.bootstrap.getProxy().getScheduler().schedule(this.bootstrap, task, delay, interval, unit);
        
        this.tasks.add(t);
        return t::cancel;
    }

	@Override
	public SchedulerTask syncDelayedRepeating(Runnable task, long delay, long interval, TimeUnit unit) {
        ScheduledTask t = this.bootstrap.getProxy().getScheduler().schedule(this.bootstrap, task, delay, interval, unit);
        
        this.tasks.add(t);
        return t::cancel;
	}

    @Override
    public void shutdown() {
        Iterators.iterate(this.tasks, ScheduledTask::cancel);
    }

}
