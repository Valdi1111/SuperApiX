package org.valdi.SuperApiX.common.config.advanced;

import java.io.File;
import java.io.InputStream;
import java.util.Optional;

import org.valdi.SuperApiX.common.config.IFilesProvider;
import org.valdi.SuperApiX.common.databases.IDatabasesProvider;
import org.valdi.SuperApiX.common.dependencies.DependencyManager;
import org.valdi.SuperApiX.common.logging.PluginLogger;
import org.valdi.SuperApiX.common.scheduler.SchedulerAdapter;

public interface StoreLoader {

    /**
     * Gets the plugin logger
     *
     * @return the logger
     */
	PluginLogger getLogger();

	File getDataFolder();
	
	InputStream getResource(String path);
	
	ClassLoader getJarLoader();
	
	File getJarFile();
	
	Optional<IDatabasesProvider> getDatabasesProvider();
	
	Optional<IFilesProvider> getFilesProvider();

	SchedulerAdapter getScheduler();
	
	DependencyManager getDependencyManager();

}
