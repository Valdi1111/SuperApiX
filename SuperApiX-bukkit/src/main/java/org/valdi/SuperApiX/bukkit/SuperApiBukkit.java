package org.valdi.SuperApiX.bukkit;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.valdi.SuperApiX.common.AbstractPlugin;
import org.valdi.SuperApiX.common.ISuperPlugin;
import org.valdi.SuperApiX.bukkit.bossbar.BossBarManager;
import org.valdi.SuperApiX.bukkit.bossbar.IBossBarProvider;
import org.valdi.SuperApiX.bukkit.config.serializers.LocationSerializer;
import org.valdi.SuperApiX.bukkit.config.serializers.VectorSerializer;
import org.valdi.SuperApiX.bukkit.nms.core.NmsProvider;
import org.valdi.SuperApiX.bukkit.nms.core.VersionManager;
import org.valdi.SuperApiX.common.config.ConfigType;
import org.valdi.SuperApiX.common.config.FilesProvider;
import org.valdi.SuperApiX.common.databases.DatabasesProvider;
import org.valdi.SuperApiX.common.databases.StorageType;
import org.valdi.SuperApiX.common.databases.data.ExceptionHandler;
import org.valdi.SuperApiX.common.dependencies.Dependencies;
import org.valdi.SuperApiX.common.dependencies.Dependency;
import org.valdi.SuperApiX.common.dependencies.DependencyManager;
import org.valdi.SuperApiX.common.logging.PluginLogger;
import org.valdi.SuperApiX.common.scheduler.SchedulerAdapter;

public class SuperApiBukkit extends AbstractPlugin implements ISuperPlugin {
    private final BukkitBootstrap bootstrap;
	private static SuperApiBukkit instance;

    // init during load
    private DependencyManager dependencyManager;

	private ServiceProviderManager provider;
    private ScheduledExecutorService executorService;
	private VersionManager version;
	
	private NmsProvider nmsProvider;
	private DatabasesProvider dbsProvider;
	private FilesProvider filesProvider;
	
	private BossBarManager barManager;
    
    public SuperApiBukkit(BukkitBootstrap bootstrap) {
        this.bootstrap = bootstrap;
		instance = this;
    }
	
	@Override
	public void load() {        
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

    	//new VectorSerializer().register();
    	//new LocationSerializer().register();
        
		provider = new ServiceProviderManager(this);		
        executorService = Executors.newScheduledThreadPool(100);
		version = new VersionManager(this);

		nmsProvider = new NmsProvider(this);
		dbsProvider = new DatabasesProvider();
		filesProvider = new FilesProvider(this);
		barManager = new BossBarManager(this);
		
		provider.registerAll();
	}
	
	@Override
	public void enable() {        
		bootstrap.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + bootstrap.getDescription().getName() + " V" + bootstrap.getDescription().getVersion() + " has been enabled... Enjoy :)");
	}
	
	@Override
	public void disable() {		
		Bukkit.getServicesManager().unregisterAll(bootstrap);
		HandlerList.unregisterAll(bootstrap);

		instance = null;
		
		bootstrap.getServer().getConsoleSender().sendMessage(ChatColor.RED + bootstrap.getDescription().getName() + " V" + bootstrap.getDescription().getVersion() + " has been disabled... Goodbye :(");
	}

	@Override
	public void reload(ExceptionHandler handler) {
		// TODO Auto-generated method stub
		
	}
	
	public static SuperApiBukkit getInstance() {
		return instance;
	}   

	@Override
    public BukkitBootstrap getBootstrap() {
        return bootstrap;
    }

	@Override
	public PluginLogger getLogger() {
		return bootstrap.getPluginLogger();
	}
	
	@Override
	public SchedulerAdapter getScheduler() {
		return bootstrap.getScheduler();
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
	
	public IBossBarProvider getBossBarSender() {
		return this.barManager.getDispatcher();
	}
	
	public NmsProvider getNmsProvider() {
		return this.nmsProvider;
	}
	
	public DatabasesProvider getDatabasesProvider() {
		return this.dbsProvider;
	}
	
	public FilesProvider getFilesProvider() {
		return this.filesProvider;
	}
	
	public DependencyManager getDependencyManager() {
		return this.dependencyManager;
	}

}
