package org.valdi.SuperApiX.common.logging;

import java.util.logging.Logger;

public class JavaPluginLogger implements PluginLogger {
    private final Logger logger;

    public JavaPluginLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String s) {
        this.logger.info(s);
    }

    @Override
    public void warning(String s) {
        this.logger.warning(s);
    }

    @Override
    public void severe(String s) {
        this.logger.severe(s);
    }

}
