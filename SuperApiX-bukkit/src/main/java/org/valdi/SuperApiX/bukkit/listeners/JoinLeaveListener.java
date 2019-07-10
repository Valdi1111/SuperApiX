package org.valdi.SuperApiX.bukkit.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.users.SimpleUser;
import org.valdi.SuperApiX.bukkit.users.User;

import java.util.UUID;

public class JoinLeaveListener implements Listener {

    private final SuperApiBukkit plugin;

    public JoinLeaveListener(SuperApiBukkit plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        User user = SimpleUser.getInstance(event.getPlayer());
        if (user.getUniqueId() == null) {
            return;
        }
        UUID playerUUID = user.getUniqueId();
        // Load player
        plugin.getPlayersManager().addPlayer(playerUUID);
        plugin.getPlayersManager().setPlayerName(user);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        plugin.getPlayersManager().save(event.getPlayer().getUniqueId());
        SimpleUser.removePlayer(event.getPlayer());
    }

}
