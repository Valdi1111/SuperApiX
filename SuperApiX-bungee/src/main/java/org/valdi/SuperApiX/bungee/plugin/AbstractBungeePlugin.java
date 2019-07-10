package org.valdi.SuperApiX.bungee.plugin;

import java.util.Optional;

import net.md_5.bungee.api.ProxyServer;
import org.valdi.SuperApiX.common.plugin.AbstractPlugin;
import org.valdi.SuperApiX.common.config.FilesProvider;
import org.valdi.SuperApiX.common.config.IFilesProvider;
import org.valdi.SuperApiX.common.databases.DatabasesProvider;
import org.valdi.SuperApiX.common.databases.IDatabasesProvider;

public abstract class AbstractBungeePlugin<T extends AbstractBungeeBootstrap> extends AbstractPlugin<T> {
	private final IFilesProvider fileProvider;
	private final IDatabasesProvider dbsProvider;
	
	protected AbstractBungeePlugin(T bootstrap) {
		super(bootstrap);

		fileProvider = new FilesProvider();
		dbsProvider = new DatabasesProvider();
	}
	
	@Override
	public Optional<IDatabasesProvider> getDatabasesProvider() {
        return Optional.ofNullable(dbsProvider);
	}

	@Override
	public Optional<IFilesProvider> getFilesProvider() {
        return Optional.ofNullable(fileProvider);
	}

	public ProxyServer getProxy() {
		return bootstrap.getProxy();
	}

}
