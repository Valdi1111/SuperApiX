package org.valdi.SuperApiX.common.logging;

public interface PluginLogger {

    public void debug(String msg);

    public void info(String msg);
    
    public void warning(String msg);
    
    public void severe(String msg);

    public void debug(String msg, Throwable thrown);

    public void info(String msg, Throwable thrown);

    public void warning(String msg, Throwable thrown);

    public void severe(String msg, Throwable thrown);

}
