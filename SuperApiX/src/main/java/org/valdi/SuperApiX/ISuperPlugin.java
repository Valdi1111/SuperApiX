package org.valdi.SuperApiX;

import java.util.concurrent.ThreadFactory;

import org.valdi.SuperApiX.common.logging.PluginLogger;

public interface ISuperPlugin {

    /**
     * Gets the plugin logger
     *
     * @return the logger
     */
	public PluginLogger getLogger();

	public ThreadFactory getThreadFactory();
	
	public ISuperBootstrap getBootstrap();

}
