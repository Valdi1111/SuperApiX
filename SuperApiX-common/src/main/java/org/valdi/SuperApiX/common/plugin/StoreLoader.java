package org.valdi.SuperApiX.common.plugin;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import org.valdi.SuperApiX.common.config.IFilesProvider;
import org.valdi.SuperApiX.common.databases.IDatabasesProvider;
import org.valdi.SuperApiX.common.dependencies.DependencyManager;
import org.valdi.SuperApiX.common.logging.SuperLogger;
import org.valdi.SuperApiX.common.scheduler.SuperScheduler;

public interface StoreLoader {

	/**
	 * Gets the plugin's name.
	 * @return the name
	 */
	String getName();

	/**
	 * Gets the plugin's version.
	 * @return the version
	 */
	String getVersion();

	/**
	 * Gets the plugin's authors.
	 * @return the authors
	 */
	List<String> getAuthors();

    /**
     * Gets the plugin's logger.
     * @return the logger
     */
	SuperLogger getLogger();

	/**
	 * Get an active instance of the SuperScheduler.
	 * @return the scheduler adapter
	 */
	SuperScheduler getScheduler();

	/**
	 * Get the plugin's data folder.
	 * @return the folder
	 */
	File getDataFolder();

	/**
	 * Get the plugin jar file.
	 * @return the jar file
	 */
	File getJarFile();

	/**
	 * Get the plugin's jar ClassLoader.
	 * @return the loader
	 */
	ClassLoader getJarLoader();

	/**
	 * Get a resource from inside the plugin's jar at the given path.
	 * @param path the resource path
	 * @return an InputStream for the resource
	 */
	InputStream getResource(String path);

	/**
	 * Get a thread factory associated to this StoreLoader.
	 * @return the thread factory
	 */
	ThreadFactory getThreadFactory();

	ScheduledExecutorService getExecutorService();

	/**
	 * Get an active instance of the DependencyManager.
	 * @return the dependency manager
	 */
	DependencyManager getDependencyManager();

	/**
	 * Get an active instance of the IDatabasesProvider.
	 * @return the databases provider
	 */
	Optional<IDatabasesProvider> getDatabasesProvider();

	/**
	 * Get an active instance of the IFilesProvider.
	 * @return the files provider
	 */
	Optional<IFilesProvider> getFilesProvider();

}
