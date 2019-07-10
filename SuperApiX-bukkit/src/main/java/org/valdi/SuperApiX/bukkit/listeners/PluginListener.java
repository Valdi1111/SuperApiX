package org.valdi.SuperApiX.bukkit.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.common.plugin.StoreLoader;

public class PluginListener implements Listener {
    private final SuperApiBukkit plugin;

    public PluginListener(SuperApiBukkit plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent e) {
        Plugin pl = e.getPlugin();
        if(!(pl instanceof StoreLoader)) {
            return;
        }

        StoreLoader loader = (StoreLoader) pl;
        plugin.getLogger().info("Plugin " + loader.getName() + " version " + loader.getVersion() + " hooked into SuperApiX!");
        pl.getLogger().info("[Api] Started custom scheduler...");
    }

}
