package org.valdi.SuperApiX.common.plugin;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.*;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.valdi.SuperApiX.common.dependencies.DependencyManager;
import org.valdi.SuperApiX.common.logging.SuperLogger;
import org.valdi.SuperApiX.common.scheduler.SuperScheduler;

public abstract class AbstractPlugin<T extends ISuperBootstrap> implements ISuperPlugin<T> {

    // init during load
    private DependencyManager dependencyManager;

	private ScheduledExecutorService executorService;

	/**
	 * The bootstrap instance
	 */
	protected final T bootstrap;

	public AbstractPlugin(final T bootstrap) {
		this.bootstrap = bootstrap;
	}

	@Override
	public void load() {
        this.dependencyManager = new DependencyManager(this);
		this.executorService = Executors.newScheduledThreadPool(15, new ThreadFactoryBuilder().setNameFormat(getName() + " Thread - %d").build());
	}

	@Override
	public void enable() {

	}

	@Override
	public void disable() {
		
	}

	@Override
	public void reload() {
		
	}

	@Override
	public T getBootstrap() {
		return bootstrap;
	}

	@Override
	public String getName() {
		return getBootstrap().getName();
	}

	@Override
	public String getVersion() {
		return getBootstrap().getVersion();
	}

	@Override
	public List<String> getAuthors() {
		return getBootstrap().getAuthors();
	}

	@Override
	public DependencyManager getDependencyManager() {
		return this.dependencyManager;
	}

	@Override
	public SuperLogger getLogger() {
		return getBootstrap().getPluginLogger();
	}
	
	@Override
	public SuperScheduler getScheduler() {
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

	@Override
	public ThreadFactory getThreadFactory() {
		ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) this.executorService;
		return threadPoolExecutor.getThreadFactory();
	}

	@Override
	public ScheduledExecutorService getExecutorService() {
		return executorService;
	}
}
