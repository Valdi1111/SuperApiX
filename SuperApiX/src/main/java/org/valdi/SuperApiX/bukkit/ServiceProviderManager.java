package org.valdi.SuperApiX.bukkit;

import org.bukkit.plugin.ServicePriority;
import org.valdi.SuperApiX.bukkit.bossbar.IBossBarProvider;
import org.valdi.SuperApiX.bukkit.nms.IChatProvider;
import org.valdi.SuperApiX.bukkit.nms.IWorldBorderProvider;
import org.valdi.SuperApiX.common.config.IFilesProvider;
import org.valdi.SuperApiX.common.databases.IDatabasesProvider;

public class ServiceProviderManager {
	private final SuperApiBukkit plugin;
	
	public ServiceProviderManager(final SuperApiBukkit plugin) {
		this.plugin = plugin;
	}
	
	public void registerAll() {
		// BossBar Utils
		plugin.getBootstrap().getServer().getServicesManager().register(IBossBarProvider.class, plugin.getBossBarSender(), plugin.getBootstrap(), ServicePriority.Normal);

		// Chat & Tab Utils
		plugin.getBootstrap().getServer().getServicesManager().register(IChatProvider.class, plugin.getNmsProvider().getChatUtils(), plugin.getBootstrap(), ServicePriority.Normal);
		
		// WorldBorder Utils
		plugin.getBootstrap().getServer().getServicesManager().register(IWorldBorderProvider.class, plugin.getNmsProvider().getWorldBorderUtils(), plugin.getBootstrap(), ServicePriority.Normal);
		
		// Databases Utils
		plugin.getBootstrap().getServer().getServicesManager().register(IDatabasesProvider.class, plugin.getDatabasesProvider(), plugin.getBootstrap(), ServicePriority.Normal);
		
		// Files Utils
		plugin.getBootstrap().getServer().getServicesManager().register(IFilesProvider.class, plugin.getFilesProvider(), plugin.getBootstrap(), ServicePriority.Normal);
	}

}
