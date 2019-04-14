package org.valdi.SuperApiX.sponge;

import org.valdi.SuperApiX.common.config.FilesProvider;
import org.valdi.SuperApiX.common.config.IFilesProvider;
import org.valdi.SuperApiX.common.databases.DatabasesProvider;
import org.valdi.SuperApiX.common.databases.IDatabasesProvider;

public class ServiceProviderManager {
	private final SuperApiSponge plugin;
	
	public ServiceProviderManager(final SuperApiSponge plugin) {
		this.plugin = plugin;
	}
	
	public void registerAll() {		
		// Databases Utils
		plugin.getBootstrap().getGame().getServiceManager().setProvider(plugin.getBootstrap(), IDatabasesProvider.class, new DatabasesProvider());
		
		// Files Utils
		plugin.getBootstrap().getGame().getServiceManager().setProvider(plugin.getBootstrap(), IFilesProvider.class, new FilesProvider(plugin));
	}

}
