package org.valdi.SuperApiX.common.scheduler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.valdi.SuperApiX.common.plugin.StoreLoader;
import org.valdi.SuperApiX.common.scheduler.task.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class SimpleScheduler implements SuperScheduler {
    private static final int RECENT_TICKS = 30;

    private final StoreLoader plugin;

    private final AtomicInteger ids;
    private volatile SimpleTask head;
    private final AtomicReference<SimpleTask> tail;
    private final PriorityQueue<SimpleTask> pending;
    private final List<SimpleTask> temp;
    private final ConcurrentHashMap<Integer, SimpleTask> runners;
    private volatile SimpleTask currentTask;
    private volatile int currentTick;
    private final Executor executor;
    private SuperAsyncDebugger debugHead;
    private SuperAsyncDebugger debugTail;

    public SimpleScheduler(StoreLoader plugin) {
        this.plugin = plugin;

        this.ids = new AtomicInteger(1);
        this.head = new SimpleTask();
        this.tail = new AtomicReference<>(this.head);
        this.pending = new PriorityQueue<>(10, new Comparator<SimpleTask>() {
            @Override
            public int compare(SimpleTask o1, SimpleTask o2) {
                int value = Long.compare(o1.getNextRun(), o2.getNextRun());
                return value != 0 ? value : Integer.compare(o1.getTaskId(), o2.getTaskId());
            }
        });
        this.temp = new ArrayList<>();
        this.runners = new ConcurrentHashMap<>();
        this.currentTask = null;
        this.currentTick = -1;
        this.executor = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat(plugin.getName() + " Thread - %d").build());
        this.debugHead = new SuperAsyncDebugger(-1, null, null) {
            @Override
            public StringBuilder debugTo(StringBuilder string) {
                return string;
            }
        };
        this.debugTail = this.debugHead;
    }

    @Override
    public <T> Future<T> callSyncMethod(Callable<T> callable) {
        SimpleScheduler.validate(callable);
        SuperFuture<T> future = new SuperFuture<>(callable, plugin, this.nextId());
        this.handle(future, 0L);
        return future;
    }

    @Override
    public void cancelTask(final int taskId) {
        if (taskId <= 0) {
            return;
        }
        SimpleTask task = this.runners.get(taskId);
        if (task != null) {
            task.cancel0();
        }
        task = new SimpleTask(new Runnable(){

            @Override
            public void run() {
                if (!this.check(SimpleScheduler.this.temp)) {
                    this.check(SimpleScheduler.this.pending);
                }
            }

            private boolean check(Iterable<SimpleTask> collection) {
                Iterator<SimpleTask> tasks = collection.iterator();
                while (tasks.hasNext()) {
                    SimpleTask task = tasks.next();
                    if (task.getTaskId() != taskId) {
                        continue;
                    }
                    task.cancel0();
                    tasks.remove();
                    if (task.isSync()) {
                        SimpleScheduler.this.runners.remove(taskId);
                    }
                    return true;
                }
                return false;
            }
        });
        this.handle(task, 0L);
        SimpleTask taskPending = this.head.getNext();
        while (taskPending != null) {
            if (taskPending == task) {
                return;
            }
            if (taskPending.getTaskId() == taskId) {
                taskPending.cancel0();
            }
            taskPending = taskPending.getNext();
        }
    }

    @Override
    public void cancelTasks() {
        SimpleTask task = new SimpleTask(new Runnable(){

            @Override
            public void run() {
                this.check(SimpleScheduler.this.pending);
                this.check(SimpleScheduler.this.temp);
            }

            void check(Iterable<SimpleTask> collection) {
                Iterator<SimpleTask> tasks = collection.iterator();
                while (tasks.hasNext()) {
                    SimpleTask task = tasks.next();
                    if (!task.getOwner().equals(plugin)) {
                        continue;
                    }
                    task.cancel0();
                    tasks.remove();
                    if (!task.isSync()) {
                        continue;
                    }
                    SimpleScheduler.this.runners.remove(task.getTaskId());
                }
            }
        });
        this.handle(task, 0L);
        SimpleTask taskPending = this.head.getNext();
        while (taskPending != null) {
            if (taskPending == task) {
                break;
            }
            if (taskPending.getTaskId() != -1 && taskPending.getOwner().equals(plugin)) {
                taskPending.cancel0();
            }
            taskPending = taskPending.getNext();
        }
        for (SimpleTask runner : this.runners.values()) {
            if (!runner.getOwner().equals(plugin)) {
                continue;
            }
            runner.cancel0();
        }
    }

    @Override
    public boolean isCurrentlyRunning(int taskId) {
        SimpleTask task = this.runners.get(taskId);
        if (task == null) {
            return false;
        }
        if (task.isSync()) {
            return task == this.currentTask;
        }
        SimpleAsyncTask asyncTask = (SimpleAsyncTask)task;
        LinkedList<SuperWorker> linkedList = asyncTask.getWorkers();
        synchronized (linkedList) {
            return !asyncTask.getWorkers().isEmpty();
        }
    }

    @Override
    public boolean isQueued(int taskId) {
        if (taskId <= 0) {
            return false;
        }
        SimpleTask task = this.head.getNext();
        while (task != null) {
            if (task.getTaskId() == taskId) {
                return task.getPeriod() >= -1L;
            }
            task = task.getNext();
        }
        task = this.runners.get(taskId);
        return task != null && task.getPeriod() >= -1L;
    }

    @Override
    public List<SuperWorker> getActiveWorkers() {
        ArrayList<SuperWorker> workers = new ArrayList<>();
        for (SimpleTask taskObj : this.runners.values()) {
            if (taskObj.isSync()) {
                continue;
            }
            SimpleAsyncTask task = (SimpleAsyncTask)taskObj;
            LinkedList<SuperWorker> linkedList = task.getWorkers();
            synchronized (linkedList) {
                workers.addAll(task.getWorkers());
            }
        }
        return workers;
    }

    @Override
    public List<SuperTask> getPendingTasks() {
        ArrayList<SimpleTask> truePending = new ArrayList<>();
        SimpleTask task = this.head.getNext();
        while (task != null) {
            if (task.getTaskId() != -1) {
                truePending.add(task);
            }
            task = task.getNext();
        }
        ArrayList<SuperTask> pending = new ArrayList<>();
        for (SimpleTask task2 : this.runners.values()) {
            if (task2.getPeriod() < -1L) continue;
            pending.add(task2);
        }
        for (SimpleTask task2 : truePending) {
            if (task2.getPeriod() < -1L || pending.contains(task2)) continue;
            pending.add(task2);
        }
        return pending;
    }

    @Override
    public SuperTask runTaskTimer(Runnable task, long delay, long period, TimeUnit unit) {
        return this.runTaskTimer((Object)task, delay, period, unit);
    }

    @Override
    public void runTaskTimer(Consumer<SuperTask> task, long delay, long period, TimeUnit unit) throws IllegalArgumentException {
        this.runTaskTimer((Object)task, delay, period, unit);
    }

    @Override
    public SuperTask runTaskTimerAsynchronously(Runnable task, long delay, long period, TimeUnit unit) {
        return this.runTaskTimerAsynchronously((Object)task, delay, period, unit);
    }

    @Override
    public void runTaskTimerAsynchronously(Consumer<SuperTask> task, long delay, long period, TimeUnit unit) throws IllegalArgumentException {
        this.runTaskTimerAsynchronously((Object)task, delay, period, unit);
    }

    public SuperTask runTaskTimer(Object runnable, long delay, long period, TimeUnit unit) {
        SimpleScheduler.validate(runnable);
        if (delay < 0L) {
            delay = 0L;
        }
        if (period == 0L) {
            period = 1L;
        } else if (period < -1L) {
            period = -1L;
        }
        return this.handle(new SimpleTask(plugin, runnable, this.nextId(), getTimeInTicks(period, unit)), getTimeInTicks(delay, unit));
    }

    public SuperTask runTaskTimerAsynchronously(Object runnable, long delay, long period, TimeUnit unit) {
        SimpleScheduler.validate(runnable);
        if (delay < 0L) {
            delay = 0L;
        }
        if (period == 0L) {
            period = 1L;
        } else if (period < -1L) {
            period = -1L;
        }
        return this.handle(new SimpleAsyncTask(this.runners, plugin, runnable, this.nextId(), getTimeInTicks(period, unit)), getTimeInTicks(delay, unit));
    }

    public void tick(int currentTick) {
        this.currentTick = currentTick;
        List<SimpleTask> temp = this.temp;
        this.parsePending();

        while (this.isReady(currentTick)) {
            SimpleTask task = this.pending.remove();
            if (task.getPeriod() < -1L) {
                if (task.isSync()) {
                    this.runners.remove(task.getTaskId(), task);
                }

                this.parsePending();
            } else {
                if (task.isSync()) {
                    this.currentTask = task;

                    try {
                        //task.timings.startTiming();
                        task.run();
                        //task.timings.stopTiming();
                    } catch (Throwable t) {
                        task.getOwner().getLogger().warning(String.format("Task #%s generated an exception", task.getTaskId()), t);
                        //task.getOwner().getLogger().log(Level.WARNING, String.format("Task #%s for %s generated an exception", task.getTaskId(), task.getOwner().getDescription().getFullName()), var8);
                    } finally {
                        this.currentTask = null;
                    }

                    this.parsePending();
                } else {
                    this.debugTail = this.debugTail.setNext(new SuperAsyncDebugger(currentTick + RECENT_TICKS, task.getOwner(), task.getTaskClass()));
                    this.executor.execute(task);
                }

                long period = task.getPeriod();
                if (period > 0L) {
                    task.setNextRun((long)currentTick + period);
                    temp.add(task);
                } else if (task.isSync()) {
                    this.runners.remove(task.getTaskId());
                }
            }
        }
        this.pending.addAll(temp);
        temp.clear();
        this.debugHead = this.debugHead.getNextHead(currentTick);
    }

    private void addTask(SimpleTask task) {
        AtomicReference<SimpleTask> tail = this.tail;
        SimpleTask tailTask = tail.get();
        while (!tail.compareAndSet(tailTask, task)) {
            tailTask = tail.get();
        }
        tailTask.setNext(task);
    }

    private SimpleTask handle(SimpleTask task, long delay) {
        task.setNextRun(this.currentTick + delay);
        this.addTask(task);
        return task;
    }

    private long getTimeInTicks(long time, TimeUnit unit) {
        if(time < 1L) {
            return time;
        }

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

    private static void validate(Object task) {
        if(task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        if(!(task instanceof Runnable || task instanceof Consumer || task instanceof Callable)) {
            throw new IllegalArgumentException("Task must be Runnable, Consumer, or Callable");
        }
        // TODO fix validating task fun for not enabled plugin check
        /*if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register task while disabled");
        }*/
    }

    private int nextId() {
        return this.ids.incrementAndGet();
    }

    private void parsePending() {
        SimpleTask head = this.head;
        SimpleTask task = head.getNext();

        SimpleTask lastTask;
        for(lastTask = head; task != null; task = task.getNext()) {
            if (task.getTaskId() == -1) {
                task.run();
            } else if (task.getPeriod() >= -1L) {
                this.pending.add(task);
                this.runners.put(task.getTaskId(), task);
            }

            lastTask = task;
        }

        for(task = head; task != lastTask; task = head) {
            head = task.getNext();
            task.setNext(null);
        }

        this.head = lastTask;
    }

    private boolean isReady(int currentTick) {
        return !this.pending.isEmpty() && this.pending.peek().getNextRun() <= currentTick;
    }

    public String toString() {
        int debugTick = this.currentTick;
        StringBuilder string = new StringBuilder("Recent tasks from ").append(debugTick - RECENT_TICKS).append('-').append(debugTick).append('{');
        this.debugHead.debugTo(string);
        return string.append('}').toString();
    }

    @Override
    public void shutdown() {
        this.cancelTasks();
    }
}
