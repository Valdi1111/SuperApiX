package org.valdi.SuperApiX.bukkit.users;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;

import java.util.Arrays;
import java.util.List;

public class VaultHandler {
    protected static Permission perms = null;
    protected static Chat chat = null;

    private boolean setupProviders() throws ClassNotFoundException {
        Class.forName("net.milkbowl.vault.permission.Permission");
        Class.forName("net.milkbowl.vault.chat.Chat");

        RegisteredServiceProvider<Permission> permsProvider = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        perms = permsProvider.getProvider();

        RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        chat = chatProvider.getProvider();

        return perms != null && chat != null;
    }

    public String getGroup(final User user) {
        return perms.getPrimaryGroup(user.getPlayer());
    }

    public List<String> getGroups(final User user) {
        return Arrays.asList(perms.getPlayerGroups(user.getPlayer()));
    }

    public boolean inGroup(final User user, final String group) {
        return perms.playerInGroup(user.getPlayer(), group);
    }

    public String getPrefix(final User user) {
        String playerPrefix = chat.getPlayerPrefix(user.getPlayer());
        if (playerPrefix == null) {
            String playerGroup = perms.getPrimaryGroup(user.getPlayer());
            return chat.getGroupPrefix(user.getWorld().getName(), playerGroup);
        } else {
            return playerPrefix;
        }
    }

    public String getSuffix(final User user) {
        String playerSuffix = chat.getPlayerSuffix(user.getPlayer());
        if (playerSuffix == null) {
            String playerGroup = perms.getPrimaryGroup(user.getPlayer());
            return chat.getGroupSuffix(user.getWorld().getName(), playerGroup);
        } else {
            return playerSuffix;
        }
    }

    public boolean canLoad(SuperApiBukkit plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        try {
            return setupProviders();
        } catch (Throwable t) {
            return false;
        }
    }

}
