package org.valdi.SuperApiX.sponge;

import org.slf4j.Logger;
import org.valdi.SuperApiX.common.logging.PluginLogger;

public class Slf4jPluginLogger implements PluginLogger {
    private final Logger logger;

    public Slf4jPluginLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void debug(String msg) {
        this.logger.debug(msg);
    }

    @Override
    public void info(String msg) {
        this.logger.info(msg);
    }

    @Override
    public void warning(String msg) {
        this.logger.warn(msg);
    }

    @Override
    public void severe(String msg) {
        this.logger.error(msg);
    }

    @Override
    public void debug(String msg, Throwable thrown) {
        this.logger.debug(msg, thrown);
    }

    @Override
    public void info(String msg, Throwable thrown) {
        this.logger.info(msg, thrown);
    }

    @Override
    public void warning(String msg, Throwable thrown) {
        this.logger.warn(msg, thrown);
    }

    @Override
    public void severe(String msg, Throwable thrown) {
        this.logger.error(msg, thrown);
    }

}
