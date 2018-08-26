package org.valdi.SuperApiX.bukkit;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.valdi.SuperApiX.common.scheduler.SchedulerAdapter;
import org.valdi.SuperApiX.common.scheduler.SchedulerTask;
import org.valdi.SuperApiX.common.utils.Iterators;

public class BukkitSchedulerAdapter implements SchedulerAdapter {
	private final JavaPlugin bootstrap;
	
    private final Executor sync;
    private final Executor async;
    
    private final Set<BukkitTask> tasks = Collections.newSetFromMap(new WeakHashMap<>());

    public BukkitSchedulerAdapter(JavaPlugin bootstrap) {
    	this.bootstrap = bootstrap;
        this.sync = r -> bootstrap.getServer().getScheduler().scheduleSyncDelayedTask(bootstrap, r);
        this.async = r -> bootstrap.getServer().getScheduler().scheduleAsyncDelayedTask(bootstrap, r);
    }

    @Override
    public Executor sync() {
        return this.sync;
    }

	@Override
	public Executor async() {
        return this.async;
	}

	@Override
    public void executeSync(Runnable task) {
		if(task instanceof BukkitRunnable) {
			((BukkitRunnable) task).runTask(bootstrap);
		} else {
			bootstrap.getServer().getScheduler().runTask(bootstrap, task);
		}
	}

	@Override
    public void executeAsync(Runnable task) {
		if(task instanceof BukkitRunnable) {
			((BukkitRunnable) task).runTaskAsynchronously(bootstrap);
		} else {
			bootstrap.getServer().getScheduler().runTaskAsynchronously(bootstrap, task);
		}
	}

	@Override
	public SchedulerTask asyncLater(Runnable task, long delay, TimeUnit unit) {
		BukkitTask t;
		if(task instanceof BukkitRunnable) {
			t = ((BukkitRunnable) task).runTaskLaterAsynchronously(bootstrap, getTimeInTicks(delay, unit));
		} else {
			t = bootstrap.getServer().getScheduler().runTaskLaterAsynchronously(bootstrap, task, getTimeInTicks(delay, unit));
		}

        this.tasks.add(t);
		return t::cancel;
	}

	@Override
	public SchedulerTask syncLater(Runnable task, long delay, TimeUnit unit) {
		BukkitTask t;
		if(task instanceof BukkitRunnable) {
			t = ((BukkitRunnable) task).runTaskLater(bootstrap, getTimeInTicks(delay, unit));
		} else {
			t = bootstrap.getServer().getScheduler().runTaskLater(bootstrap, task, getTimeInTicks(delay, unit));
		}

        this.tasks.add(t);
		return t::cancel;
	}

	@Override
	public SchedulerTask asyncRepeating(Runnable task, long interval, TimeUnit unit) {
		BukkitTask t;
		if(task instanceof BukkitRunnable) {
			t = ((BukkitRunnable) task).runTaskTimerAsynchronously(bootstrap, 0L, getTimeInTicks(interval, unit));
		} else {
			t = bootstrap.getServer().getScheduler().runTaskTimerAsynchronously(bootstrap, task, 0L, getTimeInTicks(interval, unit));
		}

        this.tasks.add(t);
		return t::cancel;
	}

	@Override
	public SchedulerTask syncRepeating(Runnable task, long interval, TimeUnit unit) {
		BukkitTask t;
		if(task instanceof BukkitRunnable) {
			t = ((BukkitRunnable) task).runTaskTimer(bootstrap, 0L, getTimeInTicks(interval, unit));
		} else {
			t = bootstrap.getServer().getScheduler().runTaskTimer(bootstrap, task, 0L, getTimeInTicks(interval, unit));
		}

        this.tasks.add(t);
		return t::cancel;
	}

	@Override
	public SchedulerTask asyncDelayedRepeating(Runnable task, long delay, long interval, TimeUnit unit) {
		BukkitTask t;
		if(task instanceof BukkitRunnable) {
			t = ((BukkitRunnable) task).runTaskTimerAsynchronously(bootstrap, getTimeInTicks(interval, unit), getTimeInTicks(interval, unit));
		} else {
			t = bootstrap.getServer().getScheduler().runTaskTimerAsynchronously(bootstrap, task, getTimeInTicks(interval, unit), getTimeInTicks(interval, unit));
		}

        this.tasks.add(t);
		return t::cancel;
	}

	@Override
	public SchedulerTask syncDelayedRepeating(Runnable task, long delay, long interval, TimeUnit unit) {
		BukkitTask t;
		if(task instanceof BukkitRunnable) {
			t = ((BukkitRunnable) task).runTaskTimer(bootstrap, getTimeInTicks(interval, unit), getTimeInTicks(interval, unit));
		} else {
			t = bootstrap.getServer().getScheduler().runTaskTimer(bootstrap, task, getTimeInTicks(interval, unit), getTimeInTicks(interval, unit));
		}

        this.tasks.add(t);
		return t::cancel;
	}

	@Override
	public void shutdown() {
        Iterators.iterate(this.tasks, BukkitTask::cancel);
	}
	
	private long getTimeInTicks(long time, TimeUnit unit) {
		switch(unit) {
			case NANOSECONDS:
				return time / 1000 / 1000 / 50;
			case MICROSECONDS:
				return time / 1000 / 50;
			case MILLISECONDS:
				return time / 50;
			case SECONDS:
				return time * 20;
			case MINUTES:
				return time * 20 * 60;
			case HOURS:
				return time * 20 * 60 * 60;
			case DAYS:
				return time * 20 * 60 * 60 * 24;
		}
		return 0;
	}

}