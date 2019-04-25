package org.valdi.SuperApiX.common;

import java.io.File;
import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.ThreadFactory;

import org.valdi.SuperApiX.common.config.IFilesProvider;
import org.valdi.SuperApiX.common.databases.IDatabasesProvider;
import org.valdi.SuperApiX.common.dependencies.DependencyManager;
import org.valdi.SuperApiX.common.logging.PluginLogger;
import org.valdi.SuperApiX.common.scheduler.SchedulerAdapter;

public interface StoreLoader {

    /**
     * Gets the plugin's logger
     * @return the logger
     */
	PluginLogger getLogger();

	/**
	 * Get the plugin's data folder
	 * @return the folder
	 */
	File getDataFolder();

	/**
	 * Get a resource from inside the plugin's jar at the given path
	 * @param path the resource path
	 * @return an InputStream for the resource
	 */
	InputStream getResource(String path);

	/**
	 * Get the plugin's jar ClassLoader
	 * @return the loader
	 */
	ClassLoader getJarLoader();

	/**
	 * Get the plugin jar file
	 * @return the jar file
	 */
	File getJarFile();

	/**
	 * Get an active instance of the IDatabasesProvider
	 * @return the databases provider
	 */
	Optional<IDatabasesProvider> getDatabasesProvider();

	/**
	 * Get an active instance of the IFilesProvider
	 * @return the files provider
	 */
	Optional<IFilesProvider> getFilesProvider();

	/**
	 * Get an active instance of the SchedulerAdapter
	 * @return the scheduler adapter
	 */
	SchedulerAdapter getScheduler();

	/**
	 * Get an active instance of the DependencyManager
	 * @return the dependency manager
	 */
	DependencyManager getDependencyManager();

	/**
	 * Get a thread factory associated to this StoreLoader
	 * @return the thread factory
	 */
	ThreadFactory getThreadFactory();

}
