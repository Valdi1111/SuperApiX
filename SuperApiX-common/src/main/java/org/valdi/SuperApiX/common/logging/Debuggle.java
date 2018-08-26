package org.valdi.SuperApiX.common.logging;

import org.valdi.SuperApiX.common.ISuperPlugin;

public final class Debuggle {
    private final PluginLogger logger;
    private static Debuggle instance = null;

    public static void log(String message) {
        if (Debuggle.instance != null) {
            Debuggle.instance.logger.info("[DEBUG] " + message);
        }
    }

    public static void initialize(ISuperPlugin plugin) {
        Debuggle.instance = new Debuggle(plugin);
    }

    public static void close() {
        Debuggle.instance = null;
    }

    private Debuggle(ISuperPlugin plugin) {
        this.logger = plugin.getLogger();
        this.logger.info("Debug enabled. Can be disabled in config.yml");
    }
}