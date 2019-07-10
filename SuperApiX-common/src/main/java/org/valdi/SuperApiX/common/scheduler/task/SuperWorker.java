package org.valdi.SuperApiX.common.scheduler.task;

import org.valdi.SuperApiX.common.plugin.StoreLoader;

public interface SuperWorker {

    int getTaskId();

    StoreLoader getOwner();

    Thread getThread();

}
