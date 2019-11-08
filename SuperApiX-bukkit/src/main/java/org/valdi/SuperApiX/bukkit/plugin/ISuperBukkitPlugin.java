package org.valdi.SuperApiX.bukkit.plugin;

import org.valdi.SuperApiX.common.plugin.ISuperPlugin;

public interface ISuperBukkitPlugin<T extends ISuperBukkitBootstrap> extends ISuperPlugin<T>, BukkitStoreLoader {

    @Override
    T getBootstrap();

}
