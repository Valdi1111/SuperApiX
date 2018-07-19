package org.valdi.SuperApiX.common.scheduler;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import org.valdi.SuperApiX.common.utils.Iterators;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.scheduler.Task;
import cn.nukkit.scheduler.TaskHandler;

public class NukkitSchedulerAdapter implements SchedulerAdapter {
	private final Plugin bootstrap;
	
    private final Executor sync;
    private final Executor async;
    
    private final Set<TaskHandler> tasks = Collections.newSetFromMap(new WeakHashMap<>());

    public NukkitSchedulerAdapter(Plugin bootstrap) {
    	this.bootstrap = bootstrap;
        this.sync = r -> bootstrap.getServer().getScheduler().scheduleTask(bootstrap, r, false);
        this.async = r -> bootstrap.getServer().getScheduler().scheduleTask(bootstrap, r, true);
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
		if(task instanceof Task) {
			bootstrap.getServer().getScheduler().scheduleTask(bootstrap, (Task) task, false);
		} else {
			bootstrap.getServer().getScheduler().scheduleTask(bootstrap, task, false);
		}
    }
	
	@Override
    public void executeAsync(Runnable task) {
		if(task instanceof AsyncTask) {
			bootstrap.getServer().getScheduler().scheduleAsyncTask(bootstrap, (AsyncTask) task);
		} else if(task instanceof Task) {
			bootstrap.getServer().getScheduler().scheduleTask(bootstrap, (Task) task, true);
		} else {
			bootstrap.getServer().getScheduler().scheduleTask(bootstrap, task, true);
		}
    }

	@Override
	public SchedulerTask asyncLater(Runnable task, long delay, TimeUnit unit) {
		TaskHandler t;
		if(task instanceof Task) {
			t = bootstrap.getServer().getScheduler().scheduleDelayedTask((Task) task, getTimeInTicks(delay, unit), true);
		} else {
			t = bootstrap.getServer().getScheduler().scheduleDelayedTask(bootstrap, task, getTimeInTicks(delay, unit), true);
		}

        this.tasks.add(t);
		return t::cancel;
	}

	@Override
	public SchedulerTask syncLater(Runnable task, long delay, TimeUnit unit) {
		TaskHandler t;
		if(task instanceof Task) {
			t = bootstrap.getServer().getScheduler().scheduleDelayedTask((Task) task, getTimeInTicks(delay, unit), false);
		} else {
			t = bootstrap.getServer().getScheduler().scheduleDelayedTask(bootstrap, task, getTimeInTicks(delay, unit), false);
		}

        this.tasks.add(t);
		return t::cancel;
	}

	@Override
	public SchedulerTask asyncRepeating(Runnable task, long interval, TimeUnit unit) {
		TaskHandler t;
		if(task instanceof Task) {
			t = bootstrap.getServer().getScheduler().scheduleRepeatingTask((Task) task, getTimeInTicks(interval, unit), true);
		} else {
			t = bootstrap.getServer().getScheduler().scheduleRepeatingTask(bootstrap, task, getTimeInTicks(interval, unit), true);
		}

        this.tasks.add(t);
		return t::cancel;
	}

	@Override
	public SchedulerTask syncRepeating(Runnable task, long interval, TimeUnit unit) {
		TaskHandler t;
		if(task instanceof Task) {
			t = bootstrap.getServer().getScheduler().scheduleRepeatingTask((Task) task, getTimeInTicks(interval, unit), false);
		} else {
			t = bootstrap.getServer().getScheduler().scheduleRepeatingTask(bootstrap, task, getTimeInTicks(interval, unit), false);
		}

        this.tasks.add(t);
		return t::cancel;
	}

	@Override
	public SchedulerTask asyncDelayedRepeating(Runnable task, long delay, long interval, TimeUnit unit) {
		TaskHandler t;
		if(task instanceof Task) {
			t = bootstrap.getServer().getScheduler().scheduleDelayedRepeatingTask((Task) task, getTimeInTicks(delay, unit), getTimeInTicks(interval, unit), true);
		} else {
			t = bootstrap.getServer().getScheduler().scheduleDelayedRepeatingTask(bootstrap, task, getTimeInTicks(delay, unit), getTimeInTicks(interval, unit), true);
		}

        this.tasks.add(t);
		return t::cancel;
	}

	@Override
	public SchedulerTask syncDelayedRepeating(Runnable task, long delay, long interval, TimeUnit unit) {
		TaskHandler t;
		if(task instanceof Task) {
			t = bootstrap.getServer().getScheduler().scheduleDelayedRepeatingTask((Task) task, getTimeInTicks(delay, unit), getTimeInTicks(interval, unit), false);
		} else {
			t = bootstrap.getServer().getScheduler().scheduleDelayedRepeatingTask(bootstrap, task, getTimeInTicks(delay, unit), getTimeInTicks(interval, unit), false);
		}

        this.tasks.add(t);
		return t::cancel;
	}

	@Override
	public void shutdown() {
        Iterators.iterate(this.tasks, TaskHandler::cancel);
	}
	
	private int getTimeInTicks(long timeL, TimeUnit unit) {
		int time = (int) timeL;
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