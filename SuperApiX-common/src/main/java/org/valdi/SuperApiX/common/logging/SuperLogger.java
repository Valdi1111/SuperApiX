package org.valdi.SuperApiX.common.logging;

public interface SuperLogger {

    void setDebugStatus(boolean active);

    boolean isDebugEnabled();

    void debug(String msg);

    void info(String msg);

    void warning(String msg);

    void severe(String msg);

    void debug(String msg, Throwable thrown);

    void info(String msg, Throwable thrown);

    void warning(String msg, Throwable thrown);

    void severe(String msg, Throwable thrown);

}
