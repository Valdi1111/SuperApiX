package org.valdi.SuperApiX.common.logging;

import org.apache.logging.log4j.Logger;

public class CompatibilityLogger extends AbstractPluginLogger {
    private final Logger logger;

    public CompatibilityLogger(Logger logger) {
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
        this.logger.warn(msg);
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
        this.logger.warn(msg, thrown);
    }

    @Override
    public void severe(String msg, Throwable thrown) {
        this.logger.error(msg, thrown);
    }

}
