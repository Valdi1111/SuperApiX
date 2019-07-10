package org.valdi.SuperApiX.bukkit.plugin;

import org.bukkit.plugin.Plugin;
import org.valdi.SuperApiX.common.plugin.ISuperBootstrap;

public interface ISuperBukkitBootstrap<T extends ISuperBukkitPlugin> extends ISuperBootstrap<T>, Plugin {

    @Override
    T getPlugin();

}
