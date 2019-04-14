package org.valdi.SuperApiX.bungee;

import java.util.Optional;

import org.valdi.SuperApiX.common.AbstractPlugin;
import org.valdi.SuperApiX.common.config.FilesProvider;
import org.valdi.SuperApiX.common.config.IFilesProvider;
import org.valdi.SuperApiX.common.databases.DatabasesProvider;
import org.valdi.SuperApiX.common.databases.IDatabasesProvider;

public abstract class AbstractBungeePlugin extends AbstractPlugin {
	private final IFilesProvider fileProvider;
	private final IDatabasesProvider dbsProvider;
	
	protected AbstractBungeePlugin() {
		fileProvider = new FilesProvider(this);
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

}
