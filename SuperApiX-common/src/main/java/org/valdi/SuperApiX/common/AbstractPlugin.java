package org.valdi.SuperApiX.common;

import java.io.File;
import java.io.InputStream;

import org.valdi.SuperApiX.common.databases.data.ExceptionHandler;
import org.valdi.SuperApiX.common.dependencies.DependencyManager;
import org.valdi.SuperApiX.common.logging.PluginLogger;
import org.valdi.SuperApiX.common.scheduler.SchedulerAdapter;

public abstract class AbstractPlugin implements ISuperPlugin {

    // init during load
    private DependencyManager dependencyManager;
	
	public void load() {
        this.dependencyManager = new DependencyManager(this);
	}
	
	public void enable() {
		
	}
	
	public void disable() {
		
	}
	
	public void reload(ExceptionHandler handler) {
		
	}
	
	@Override
	public DependencyManager getDependencyManager() {
		return this.dependencyManager;
	}

	@Override
	public PluginLogger getLogger() {
		return getBootstrap().getPluginLogger();
	}
	
	@Override
	public SchedulerAdapter getScheduler() {
		return getBootstrap().getScheduler();
	}

	@Override
	public File getDataFolder() {
		return getBootstrap().getDataFolder();
	}

	@Override
	public File getJarFile() {
		return getBootstrap().getJarFile();
	}

	@Override
	public ClassLoader getJarLoader() {
		return getBootstrap().getJarLoader();
	}

	@Override
	public InputStream getResource(String path) {
		return getBootstrap().getResourceStream(path);
	}

}
