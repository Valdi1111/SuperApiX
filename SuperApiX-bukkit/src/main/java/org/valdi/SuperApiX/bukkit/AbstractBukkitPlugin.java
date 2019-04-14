package org.valdi.SuperApiX.bukkit;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.valdi.SuperApiX.common.AbstractPlugin;
import org.valdi.SuperApiX.common.config.IFilesProvider;
import org.valdi.SuperApiX.common.databases.IDatabasesProvider;

public abstract class AbstractBukkitPlugin extends AbstractPlugin {
	
	@Override
	public Optional<IDatabasesProvider> getDatabasesProvider() {
        RegisteredServiceProvider<IDatabasesProvider> provider = Bukkit.getServicesManager().getRegistration(IDatabasesProvider.class);
        if(provider == null) {
        	this.getLogger().debug("Cannot get file provider...");
        	return Optional.empty();
        }
        
        return Optional.ofNullable(provider.getProvider());
	}

	@Override
	public Optional<IFilesProvider> getFilesProvider() {
        RegisteredServiceProvider<IFilesProvider> provider = Bukkit.getServicesManager().getRegistration(IFilesProvider.class);
        if(provider == null) {
        	this.getLogger().debug("Cannot get file provider...");
        	return Optional.empty();
        }
        
        return Optional.ofNullable(provider.getProvider());
	}
	
	public Server getServer() {
		return ((Plugin) this.getBootstrap()).getServer();
	}

}
