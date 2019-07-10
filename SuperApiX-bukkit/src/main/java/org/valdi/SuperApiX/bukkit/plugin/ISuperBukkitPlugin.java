package org.valdi.SuperApiX.bukkit.plugin;

import org.valdi.SuperApiX.bukkit.managers.CommandsManager;
import org.valdi.SuperApiX.bukkit.managers.LocalesManager;
import org.valdi.SuperApiX.bukkit.managers.PlayersManager;
import org.valdi.SuperApiX.bukkit.users.Notifier;
import org.valdi.SuperApiX.common.plugin.ISuperPlugin;

public interface ISuperBukkitPlugin<T extends ISuperBukkitBootstrap> extends ISuperPlugin<T> {

    @Override
    T getBootstrap();

    default String getDefaultLanguage() {
        return "en-US";
    }

    LocalesManager getLocalesManager();

    CommandsManager getCommandsManager();

    PlayersManager getPlayersManager();

    Notifier getNotifier();

}
