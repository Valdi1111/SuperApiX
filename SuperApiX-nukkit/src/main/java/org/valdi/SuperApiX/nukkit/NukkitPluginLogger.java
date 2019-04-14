package org.valdi.SuperApiX.nukkit;

import org.valdi.SuperApiX.common.logging.AbstractPluginLogger;

import cn.nukkit.utils.Logger;

public class NukkitPluginLogger extends AbstractPluginLogger {
    private final Logger logger;

    public NukkitPluginLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void debug(String msg) {
    	if(!this.isDebugEnabled()) {
    		return;
    	}
    	
        this.logger.debug(msg);
    }

    @Override
    public void info(String msg) {
        this.logger.info(msg);
    }

    @Override
    public void warning(String msg) {
        this.logger.warning(msg);
    }

    @Override
    public void severe(String msg) {
        this.logger.error(msg);
    }

    @Override
    public void debug(String msg, Throwable thrown) {
    	if(!this.isDebugEnabled()) {
    		return;
    	}
    	
        this.logger.debug(msg, thrown);
    }

    @Override
    public void info(String msg, Throwable thrown) {
        this.logger.info(msg, thrown);
    }

    @Override
    public void warning(String msg, Throwable thrown) {
        this.logger.warning(msg, thrown);
    }

    @Override
    public void severe(String msg, Throwable thrown) {
        this.logger.error(msg, thrown);
    }

}
