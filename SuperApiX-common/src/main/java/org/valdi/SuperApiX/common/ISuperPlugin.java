package org.valdi.SuperApiX.common;

import java.util.concurrent.ThreadFactory;

import org.valdi.SuperApiX.common.logging.PluginLogger;
import org.valdi.SuperApiX.common.scheduler.SchedulerAdapter;

public interface ISuperPlugin {
	
	public ISuperBootstrap getBootstrap();

    /**
     * Gets the plugin logger
     *
     * @return the logger
     */
	public PluginLogger getLogger();
	
	public SchedulerAdapter getScheduler();

	public ThreadFactory getThreadFactory();

}
