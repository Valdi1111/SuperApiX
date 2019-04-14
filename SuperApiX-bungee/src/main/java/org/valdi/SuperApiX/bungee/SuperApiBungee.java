package org.valdi.SuperApiX.bungee;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import org.valdi.SuperApiX.common.config.ConfigType;
import org.valdi.SuperApiX.common.databases.StorageType;
import org.valdi.SuperApiX.common.dependencies.Dependencies;
import org.valdi.SuperApiX.common.dependencies.Dependency;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class SuperApiBungee extends AbstractBungeePlugin {
	private final BungeeBootstrap bootstrap;
	private SuperApiBungee instance;

    private ScheduledExecutorService executorService;
    
    public SuperApiBungee(BungeeBootstrap bootstrap) {
        this.bootstrap = bootstrap;
        
		instance = this;
    }
	
	@Override
	public void load() {
		super.load();
        
        // load dependencies
        getDependencyManager().loadDependencies(new HashSet<Dependency>() {
			private static final long serialVersionUID = -4725363509634307896L;

			{
        		add(Dependencies.TEXT);
        		add(Dependencies.CAFFEINE);
        		add(Dependencies.OKIO);
        		add(Dependencies.OKHTTP);
        	}
        });
        getDependencyManager().loadStorageDependencies(EnumSet.of(ConfigType.YAML, ConfigType.HOCON, ConfigType.JSON, ConfigType.TOML), 
        		EnumSet.of(StorageType.SQLITE, StorageType.H2, StorageType.MYSQL, StorageType.POSTGRESQL, StorageType.MARIADB, StorageType.MONGODB));
		
        executorService = Executors.newScheduledThreadPool(100);
	}

	@Override
	public void enable() {
		super.enable();
		
		bootstrap.getProxy().getConsole().sendMessage(new TextComponent(ChatColor.GREEN + bootstrap.getDescription().getName() + " V" + bootstrap.getDescription().getVersion() + " has been enabled... Enjoy :)"));
	}

	@Override
	public void disable() {
		super.disable();
		
		instance = null;
		bootstrap.getProxy().getConsole().sendMessage(new TextComponent(ChatColor.RED + bootstrap.getDescription().getName() + " V" + bootstrap.getDescription().getVersion() + " has been disabled... Goodbye :("));
	}

	public SuperApiBungee getInstance() {
		return instance;
	}

	@Override
	public BungeeBootstrap getBootstrap() {
		return bootstrap;
	}

	@Override
	public ThreadFactory getThreadFactory() {
        ScheduledExecutorService scheduledExecutorService = this.executorService;
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) scheduledExecutorService;
        ThreadFactory threadFactory = threadPoolExecutor.getThreadFactory();
        return threadFactory;
	}

}
