package org.valdi.SuperApiX.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.valdi.SuperApiX.bukkit.nms.IChatProvider;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;

public class ApiUtils {
	
	public static void sendActionBar(Player player, String message) {
        RegisteredServiceProvider<IChatProvider> provider = Bukkit.getServicesManager().getRegistration(IChatProvider.class);
        if(provider == null) {
            return;
        }
        
        try {
			provider.getProvider().sendActionBar(player, message);
		} catch (VersionUnsupportedException e) {
        	player.sendMessage(message);
		}
	}
	
	@Deprecated
	public static void sendFullTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) {
        RegisteredServiceProvider<IChatProvider> provider = Bukkit.getServicesManager().getRegistration(IChatProvider.class);
        if(provider == null) {
            return;
        }
        
        try {
			provider.getProvider().sendFullTitle(player, fadeIn, stay, fadeOut, title, subtitle);
		} catch (VersionUnsupportedException ignored) {}
	}
	
	@Deprecated
	public static void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title) {
        RegisteredServiceProvider<IChatProvider> provider = Bukkit.getServicesManager().getRegistration(IChatProvider.class);
        if(provider == null) {
            return;
        }
        
        try {
			provider.getProvider().sendTitle(player, fadeIn, stay, fadeOut, title);
		} catch (VersionUnsupportedException ignored) {}
	}
	
	@Deprecated
	public static void sendSubtitle(Player player, int fadeIn, int stay, int fadeOut, String subtitle) {
        RegisteredServiceProvider<IChatProvider> provider = Bukkit.getServicesManager().getRegistration(IChatProvider.class);
        if(provider == null) {
            return;
        }
        
        try {
			provider.getProvider().sendSubtitle(player, fadeIn, stay, fadeOut, subtitle);
		} catch (VersionUnsupportedException ignored) {}
	}
	
	public static void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) {
        RegisteredServiceProvider<IChatProvider> provider = Bukkit.getServicesManager().getRegistration(IChatProvider.class);
        if(provider == null) {
            return;
        }
        
        try {
			provider.getProvider().sendTitle(player, fadeIn, stay, fadeOut, title, subtitle);
		} catch (VersionUnsupportedException ignored) {}
	}

}
