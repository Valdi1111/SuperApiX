package org.valdi.SuperApiX.common.plugin;

public class IllegalPluginAccessException extends RuntimeException {
    private static final long serialVersionUID = 5162710183389028792L;

    public IllegalPluginAccessException(String message) {
        super(message);
    }

}
