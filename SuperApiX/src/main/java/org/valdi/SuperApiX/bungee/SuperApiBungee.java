package org.valdi.SuperApiX.bungee;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import org.valdi.SuperApiX.AbstractPlugin;
import org.valdi.SuperApiX.ISuperPlugin;
import org.valdi.SuperApiX.common.config.ConfigType;
import org.valdi.SuperApiX.common.databases.StorageType;
import org.valdi.SuperApiX.common.databases.data.ExceptionHandler;
import org.valdi.SuperApiX.common.dependencies.Dependencies;
import org.valdi.SuperApiX.common.dependencies.Dependency;
import org.valdi.SuperApiX.common.dependencies.DependencyManager;
import org.valdi.SuperApiX.common.logging.PluginLogger;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class SuperApiBungee extends AbstractPlugin implements ISuperPlugin {
	private final BungeeBootstrap bootstrap;
	private SuperApiBungee instance;

    // init during load
    private DependencyManager dependencyManager;

    private ScheduledExecutorService executorService;
    
    public SuperApiBungee(BungeeBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }
	
	@Override
	public void load() {
		instance = this;
        
        // load dependencies
        this.dependencyManager = new DependencyManager(this);
        this.dependencyManager.loadDependencies(new HashSet<Dependency>() {
			private static final long serialVersionUID = -4725363509634307896L;

			{
        		add(Dependencies.TEXT);
        		add(Dependencies.CAFFEINE);
        		add(Dependencies.OKIO);
        		add(Dependencies.OKHTTP);
        	}
        });
        this.dependencyManager.loadStorageDependencies(EnumSet.of(ConfigType.YAML, ConfigType.HOCON, ConfigType.JSON, ConfigType.TOML), 
        		EnumSet.of(StorageType.SQLITE, StorageType.H2, StorageType.MYSQL, StorageType.POSTGRESQL, StorageType.MARIADB, StorageType.MONGODB));
		
        executorService = Executors.newScheduledThreadPool(100);
	}

	@Override
	public void enable() {
		bootstrap.getProxy().getConsole().sendMessage(new TextComponent(ChatColor.GREEN + bootstrap.getDescription().getName() + " V" + bootstrap.getDescription().getVersion() + " has been enabled... Enjoy :)"));
	}

	@Override
	public void disable() {
		instance = null;

		bootstrap.getProxy().getConsole().sendMessage(new TextComponent(ChatColor.RED + bootstrap.getDescription().getName() + " V" + bootstrap.getDescription().getVersion() + " has been disabled... Goodbye :("));
	}

	@Override
	public void reload(ExceptionHandler handler) {
		// TODO Auto-generated method stub

	}

	public SuperApiBungee getInstance() {
		return instance;
	}

	@Override
	public BungeeBootstrap getBootstrap() {
		return bootstrap;
	}

	@Override
	public PluginLogger getLogger() {
		return bootstrap.getPluginLogger();
	}

	@Override
	public ThreadFactory getThreadFactory() {
        ScheduledExecutorService scheduledExecutorService = this.executorService;
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) scheduledExecutorService;
        ThreadFactory threadFactory = threadPoolExecutor.getThreadFactory();
        return threadFactory;
	}
	
	public DependencyManager getDependencyManager() {
		return this.dependencyManager;
	}

}
