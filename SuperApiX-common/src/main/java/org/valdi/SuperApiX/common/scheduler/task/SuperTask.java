package org.valdi.SuperApiX.common.scheduler.task;

import org.valdi.SuperApiX.common.plugin.StoreLoader;

public interface SuperTask {

    int getTaskId();

    StoreLoader getOwner();

    boolean isSync();

    boolean isCancelled();

    void cancel();

}
