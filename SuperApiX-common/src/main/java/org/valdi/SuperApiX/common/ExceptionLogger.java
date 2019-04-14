package org.valdi.SuperApiX.common;

import org.valdi.SuperApiX.common.logging.PluginLogger;

public class ExceptionLogger {
	private final PluginLogger logger;
	private final Exception exception;
	
	public ExceptionLogger(Exception exception, PluginLogger logger) {
		this.logger = logger;
		this.exception = exception;
	}
	
	public void logResume(String resume) {
        logger.severe(resume);
	}
	
	public void logError() {
        logger.severe("Error: " + exception.getMessage());
	}
	
	public void logThrowable() {
		logger.severe(exception.toString());
	}
	
	public void logThrowableAsDebug() {
        logger.debug(exception.toString());
	}

}
