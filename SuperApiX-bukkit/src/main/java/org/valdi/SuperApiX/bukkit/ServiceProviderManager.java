package org.valdi.SuperApiX.bukkit;

import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.valdi.SuperApiX.bukkit.bossbar.BossBarManager;
import org.valdi.SuperApiX.bukkit.bossbar.IBossBarProvider;
import org.valdi.SuperApiX.bukkit.nms.base.*;
import org.valdi.SuperApiX.common.config.FilesProvider;
import org.valdi.SuperApiX.common.config.IFilesProvider;
import org.valdi.SuperApiX.common.databases.DatabasesProvider;
import org.valdi.SuperApiX.common.databases.IDatabasesProvider;

public class ServiceProviderManager {
	private final SuperApiBukkit plugin;
	
	public ServiceProviderManager(final SuperApiBukkit plugin) {
		this.plugin = plugin;
	}
	
	public void registerAll() {
		BukkitBootstrap boot = plugin.getBootstrap();
		ServicesManager sm = boot.getServer().getServicesManager();

		// Files Utils
		sm.register(IFilesProvider.class, new FilesProvider(), boot, ServicePriority.Normal);

		// Databases Utils
		sm.register(IDatabasesProvider.class, new DatabasesProvider(), boot, ServicePriority.Normal);

		// Nms Utils
		plugin.getNmsProvider().getActionBar().ifPresent(p -> sm.register(IActionBar.class, p, boot, ServicePriority.Normal));
		plugin.getNmsProvider().getTitle().ifPresent(p -> sm.register(ITitle.class, p, boot, ServicePriority.Normal));
		plugin.getNmsProvider().getTabList().ifPresent(p -> sm.register(ITabList.class, p, boot, ServicePriority.Normal));
		plugin.getNmsProvider().getPlayerUtils().ifPresent(p -> sm.register(IPlayerUtils.class, p, boot, ServicePriority.Normal));
		plugin.getNmsProvider().getItemUtils().ifPresent(p -> sm.register(IItemUtils.class, p, boot, ServicePriority.Normal));
		plugin.getNmsProvider().getGeneralUtils().ifPresent(p -> sm.register(IGeneralUtils.class, p, boot, ServicePriority.Normal));
		plugin.getNmsProvider().getSignEditor().ifPresent(p -> sm.register(ISignEditor.class, p, boot, ServicePriority.Normal));
		plugin.getNmsProvider().getWorldManager().ifPresent(p -> sm.register(IWorldManager.class, p, boot, ServicePriority.Normal));

		// BossBar Utils
		sm.register(IBossBarProvider.class, new BossBarManager(plugin).getDispatcher(), boot, ServicePriority.Normal);
	}

}
