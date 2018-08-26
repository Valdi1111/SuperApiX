package org.valdi.SuperApiX.sponge;

import org.valdi.SuperApiX.common.config.IFilesProvider;
import org.valdi.SuperApiX.common.databases.IDatabasesProvider;

public class ServiceProviderManager {
	private final SuperApiSponge plugin;
	
	public ServiceProviderManager(final SuperApiSponge plugin) {
		this.plugin = plugin;
	}
	
	public void registerAll() {
		// BossBar Utils
		//plugin.getBootstrap().getServer().getServicesManager().register(IBossBarProvider.class, plugin.getBossBarSender(), plugin.getBootstrap(), ServicePriority.Normal);

		// Chat & Tab Utils
		//plugin.getBootstrap().getServer().getServicesManager().register(IChatProvider.class, plugin.getNmsProvider().getChatUtils(), plugin.getBootstrap(), ServicePriority.Normal);
		
		// WorldBorder Utils
		//plugin.getBootstrap().getServer().getServicesManager().register(IWorldBorderProvider.class, plugin.getNmsProvider().getWorldBorderUtils(), plugin.getBootstrap(), ServicePriority.Normal);
		
		// Databases Utils
		plugin.getBootstrap().getGame().getServiceManager().setProvider(plugin.getBootstrap(), IDatabasesProvider.class, plugin.getDatabasesProvider());
		
		// Files Utils
		plugin.getBootstrap().getGame().getServiceManager().setProvider(plugin.getBootstrap(), IFilesProvider.class, plugin.getFilesProvider());
	}

}
