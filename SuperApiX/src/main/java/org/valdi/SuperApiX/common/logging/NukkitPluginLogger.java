package org.valdi.SuperApiX.common.logging;

import cn.nukkit.utils.Logger;

public class NukkitPluginLogger implements PluginLogger {
    private final Logger logger;

    public NukkitPluginLogger(cn.nukkit.plugin.PluginLogger logger) {
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
        this.logger.error(s);
    }

}
