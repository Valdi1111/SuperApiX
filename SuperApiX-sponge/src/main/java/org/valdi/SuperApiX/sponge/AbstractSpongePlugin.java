package org.valdi.SuperApiX.sponge;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.ProviderRegistration;
import org.valdi.SuperApiX.common.AbstractPlugin;
import org.valdi.SuperApiX.common.config.IFilesProvider;
import org.valdi.SuperApiX.common.databases.IDatabasesProvider;

public abstract class AbstractSpongePlugin extends AbstractPlugin {
	
	@Override
	public Optional<IDatabasesProvider> getDatabasesProvider() {
        Optional<ProviderRegistration<IDatabasesProvider>> provider = Sponge.getServiceManager().getRegistration(IDatabasesProvider.class);
        if(!provider.isPresent()) {
        	this.getLogger().debug("Cannot get file provider...");
        	return Optional.empty();
        }
        
        return Optional.ofNullable(provider.get().getProvider());
	}

	@Override
	public Optional<IFilesProvider> getFilesProvider() {
		Optional<ProviderRegistration<IFilesProvider>> provider = Sponge.getServiceManager().getRegistration(IFilesProvider.class);
        if(!provider.isPresent()) {
        	this.getLogger().debug("Cannot get file provider...");
        	return Optional.empty();
        }
        
        return Optional.ofNullable(provider.get().getProvider());
	}

}
