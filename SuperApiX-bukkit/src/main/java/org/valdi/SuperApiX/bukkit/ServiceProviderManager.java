package org.valdi.SuperApiX.bukkit;

import org.bukkit.plugin.ServicePriority;
import org.valdi.SuperApiX.bukkit.advancements.AdvancementProvider;
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

		// BossBar Utils
		boot.getServer().getServicesManager().register(IBossBarProvider.class, new BossBarManager(plugin).getDispatcher(), boot, ServicePriority.Normal);
		
		// Databases Utils
		boot.getServer().getServicesManager().register(IDatabasesProvider.class, new DatabasesProvider(), boot, ServicePriority.Normal);
		
		// Files Utils
		boot.getServer().getServicesManager().register(IFilesProvider.class, new FilesProvider(), boot, ServicePriority.Normal);

		// Nms Utils
		plugin.getNmsProvider().getActionBar().ifPresent(p -> boot.getServer().getServicesManager().register(IActionBar.class, p, boot, ServicePriority.Normal));
		plugin.getNmsProvider().getTitle().ifPresent(p -> boot.getServer().getServicesManager().register(ITitle.class, p, boot, ServicePriority.Normal));
		plugin.getNmsProvider().getTabList().ifPresent(p -> boot.getServer().getServicesManager().register(ITabList.class, p, boot, ServicePriority.Normal));
		plugin.getNmsProvider().getPlayerUtils().ifPresent(p -> boot.getServer().getServicesManager().register(IPlayerUtils.class, p, boot, ServicePriority.Normal));
		plugin.getNmsProvider().getItemUtils().ifPresent(p -> boot.getServer().getServicesManager().register(IItemUtils.class, p, boot, ServicePriority.Normal));
		plugin.getNmsProvider().getGeneralUtils().ifPresent(p -> boot.getServer().getServicesManager().register(IGeneralUtils.class, p, boot, ServicePriority.Normal));
		plugin.getNmsProvider().getSignEditor().ifPresent(p -> boot.getServer().getServicesManager().register(ISignEditor.class, p, boot, ServicePriority.Normal));
		plugin.getNmsProvider().getWorldBorder().ifPresent(p -> boot.getServer().getServicesManager().register(IWorldBorder.class, p, boot, ServicePriority.Normal));
		plugin.getNmsProvider().getWorldManager().ifPresent(p -> boot.getServer().getServicesManager().register(IWorldManager.class, p, boot, ServicePriority.Normal));

		AdvancementProvider advProvider = plugin.getAdvProvider();
		if(advProvider != null) {
			boot.getServer().getServicesManager().register(AdvancementProvider.class, advProvider, boot, ServicePriority.Normal);
		}
	}

}
