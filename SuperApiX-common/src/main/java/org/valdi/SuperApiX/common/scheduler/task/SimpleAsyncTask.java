package org.valdi.SuperApiX.common.scheduler.task;

import org.valdi.SuperApiX.common.plugin.StoreLoader;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class SimpleAsyncTask extends SimpleTask {
    private final LinkedList<SuperWorker> workers = new LinkedList<>();
    private final Map<Integer, SimpleTask> runners;

    public SimpleAsyncTask(Map<Integer, SimpleTask> runners, StoreLoader plugin, Object task, int id, long delay) {
        super(plugin, task, id, delay);
        this.runners = runners;
    }

    @Override
    public boolean isSync() {
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        block37 : {
            Thread thread;
            Throwable thrown;
            block35 : {
                thread = Thread.currentThread();
                LinkedList<SuperWorker> linkedList = this.workers;
                synchronized (linkedList) {
                    if (this.getPeriod() == -2L) {
                        return;
                    }
                    this.workers.add(new SuperWorker(){

                        @Override
                        public Thread getThread() {
                            return thread;
                        }

                        @Override
                        public int getTaskId() {
                            return SimpleAsyncTask.this.getTaskId();
                        }

                        @Override
                        public StoreLoader getOwner() {
                            return SimpleAsyncTask.this.getOwner();
                        }
                    });
                }
                thrown = null;
                try {
                    try {
                        super.run();
                        break block35;
                    } catch (Throwable t2) {
                        thrown = t2;
                        this.getOwner().getLogger().warning(String.format("Plugin %s Generated an exception while executing task %s", this.getOwner().getName(), this.getTaskId()), thrown);
                        LinkedList<SuperWorker> linkedList2 = this.workers;
                        synchronized (linkedList2) {
                            try {
                                Iterator<SuperWorker> workers = this.workers.iterator();
                                boolean removed = false;
                                while (workers.hasNext()) {
                                    if (workers.next().getThread() != thread) continue;
                                    workers.remove();
                                    removed = true;
                                    break;
                                }
                                if (!removed) {
                                    throw new IllegalStateException(String.format("Unable to remove worker %s on task %s for %s", thread.getName(), this.getTaskId(), this.getOwner().getName()), thrown);
                                }
                            }
                            finally {
                                if (this.getPeriod() < 0L && this.workers.isEmpty()) {
                                    this.runners.remove(this.getTaskId());
                                }
                            }
                            break block37;
                        }
                    }
                } catch (Throwable throwable) {
                    LinkedList<SuperWorker> linkedList3 = this.workers;
                    synchronized (linkedList3) {
                        try {
                            Iterator<SuperWorker> workers = this.workers.iterator();
                            boolean removed = false;
                            while (workers.hasNext()) {
                                if (workers.next().getThread() != thread) continue;
                                workers.remove();
                                removed = true;
                                break;
                            }
                            if (!removed) {
                                throw new IllegalStateException(String.format("Unable to remove worker %s on task %s for %s", thread.getName(), this.getTaskId(), this.getOwner().getName()), thrown);
                            }
                        } finally {
                            if (this.getPeriod() < 0L && this.workers.isEmpty()) {
                                this.runners.remove(this.getTaskId());
                            }
                        }
                    }
                }
                //throw throwable;
            }
            LinkedList<SuperWorker> linkedList = this.workers;
            synchronized (linkedList) {
                try {
                    Iterator<SuperWorker> workers = this.workers.iterator();
                    boolean removed = false;
                    while (workers.hasNext()) {
                        if (workers.next().getThread() != thread) continue;
                        workers.remove();
                        removed = true;
                        break;
                    }
                    if (!removed) {
                        throw new IllegalStateException(String.format("Unable to remove worker %s on task %s for %s", thread.getName(), this.getTaskId(), this.getOwner().getName()), thrown);
                    }
                } finally {
                    if (this.getPeriod() < 0L && this.workers.isEmpty()) {
                        this.runners.remove(this.getTaskId());
                    }
                }
            }
        }
    }

    public LinkedList<SuperWorker> getWorkers() {
        return this.workers;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean cancel0() {
        LinkedList<SuperWorker> linkedList = this.workers;
        synchronized (linkedList) {
            this.setPeriod(-2L);
            if (this.workers.isEmpty()) {
                this.runners.remove(this.getTaskId());
            }
        }
        return true;
    }
}
