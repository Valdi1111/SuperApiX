package org.valdi.SuperApiX.bukkit;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.valdi.SuperApiX.bukkit.bossbar.IBossBarProvider;
import org.valdi.SuperApiX.bukkit.config.serializers.LocationSerializer;
import org.valdi.SuperApiX.bukkit.config.serializers.VectorSerializer;
import org.valdi.SuperApiX.bukkit.config.serializers.WorldUuidSerializer;
import org.valdi.SuperApiX.bukkit.nms.core.NmsProvider;
import org.valdi.SuperApiX.bukkit.nms.core.VersionManager;
import org.valdi.SuperApiX.common.config.ConfigType;
import org.valdi.SuperApiX.common.config.serializers.SetSerializer;
import org.valdi.SuperApiX.common.databases.StorageType;
import org.valdi.SuperApiX.common.dependencies.Dependencies;
import org.valdi.SuperApiX.common.dependencies.Dependency;

public class SuperApiBukkit extends AbstractBukkitPlugin {
    private final BukkitBootstrap bootstrap;
	private static SuperApiBukkit instance;

	private ServiceProviderManager provider;
    private ScheduledExecutorService executorService;
	private VersionManager version;
	
	private NmsProvider nmsProvider;
    
    public SuperApiBukkit(BukkitBootstrap bootstrap) {
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

        // Register common serializers
    	new SetSerializer().register();
    	
    	// Register bukkit only serializers
    	new VectorSerializer().register();
    	new LocationSerializer().register();
    	new WorldUuidSerializer().register();
        
		provider = new ServiceProviderManager(this);		
        executorService = Executors.newScheduledThreadPool(100);
		version = new VersionManager(this);

		nmsProvider = new NmsProvider(this);
		
		provider.registerAll();
	}
	
	@Override
	public void enable() {
		super.enable();
		
		bootstrap.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + bootstrap.getDescription().getName() + " V" + bootstrap.getDescription().getVersion() + " has been enabled... Enjoy :)");
	}
	
	@Override
	public void disable() {
		super.disable();
		
		Bukkit.getServicesManager().unregisterAll(bootstrap);
		HandlerList.unregisterAll(bootstrap);

		instance = null;
		
		bootstrap.getServer().getConsoleSender().sendMessage(ChatColor.RED + bootstrap.getDescription().getName() + " V" + bootstrap.getDescription().getVersion() + " has been disabled... Goodbye :(");
	}
	
	public static SuperApiBukkit getInstance() {
		return instance;
	}   

	@Override
    public BukkitBootstrap getBootstrap() {
        return bootstrap;
    }

	@Override
	public ThreadFactory getThreadFactory() {
        ScheduledExecutorService scheduledExecutorService = this.executorService;
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) scheduledExecutorService;
        ThreadFactory threadFactory = threadPoolExecutor.getThreadFactory();
        return threadFactory;
	}
	
	public VersionManager getVersionManager() {
		return this.version;
	}
	
	public NmsProvider getNmsProvider() {
		return this.nmsProvider;
	}
	
	public Optional<IBossBarProvider> getBossBarSender() {
        RegisteredServiceProvider<IBossBarProvider> provider = Bukkit.getServicesManager().getRegistration(IBossBarProvider.class);
        if(provider == null) {
        	this.getLogger().debug("Cannot get file provider...");
        	return Optional.empty();
        }
        
        return Optional.ofNullable(provider.getProvider());
	}

}
