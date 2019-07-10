package org.valdi.SuperApiX.bukkit.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.valdi.SuperApiX.common.logging.AbstractPluginLogger;

public class BukkitPluginLogger extends AbstractPluginLogger {
    private final Logger logger;

    public BukkitPluginLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void debug(String msg) {
    	if(!this.isDebugEnabled()) {
    		return;
    	}
    	
        this.logger.info("[DEBUG] " + msg);
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
        this.logger.severe(msg);
    }

    @Override
    public void debug(String msg, Throwable thrown) {
    	if(!this.isDebugEnabled()) {
    		return;
    	}
    	
        this.logger.log(Level.INFO, "[DEBUG] " + msg, thrown);
    }

    @Override
    public void info(String msg, Throwable thrown) {
        this.logger.log(Level.INFO, msg, thrown);
    }

    @Override
    public void warning(String msg, Throwable thrown) {
        this.logger.log(Level.WARNING, msg, thrown);
    }

    @Override
    public void severe(String msg, Throwable thrown) {
        this.logger.log(Level.SEVERE, msg, thrown);
    }

}
