package org.valdi.SuperApiX.bukkit.plugin;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.valdi.SuperApiX.bukkit.managers.CommandsManager;
import org.valdi.SuperApiX.bukkit.managers.LocalesManager;
import org.valdi.SuperApiX.bukkit.managers.PlayersManager;
import org.valdi.SuperApiX.bukkit.users.Notifier;
import org.valdi.SuperApiX.common.plugin.StoreLoader;

public interface BukkitStoreLoader extends StoreLoader {

    default String getDefaultLanguage() {
        return "en-US";
    }

    LocalesManager getLocalesManager();

    CommandsManager getCommandsManager();

    PlayersManager getPlayersManager();

    Notifier getNotifier();

    Server getServer();

    JavaPlugin getJavaPlugin();

}
