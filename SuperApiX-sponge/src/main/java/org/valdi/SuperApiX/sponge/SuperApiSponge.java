package org.valdi.SuperApiX.sponge;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.valdi.SuperApiX.common.PluginDetails;
import org.valdi.SuperApiX.common.config.ConfigType;
import org.valdi.SuperApiX.common.databases.StorageType;
import org.valdi.SuperApiX.common.dependencies.Dependencies;
import org.valdi.SuperApiX.common.dependencies.Dependency;

public class SuperApiSponge extends AbstractSpongePlugin {
	private static SuperApiSponge instance;
    private final SpongeBootstrap bootstrap;

	private ServiceProviderManager provider;
    private ScheduledExecutorService executorService;

    public SuperApiSponge(SpongeBootstrap bootstrap) {
        this.bootstrap = bootstrap;
		instance = this;
    }
    
    public static SuperApiSponge getInstance() {
    	return instance;
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

		provider = new ServiceProviderManager(this);
		
		provider.registerAll();
	}

	@Override
	public void enable() {
		super.enable();
		
		Sponge.getServer().getConsole().sendMessage(Text.builder(PluginDetails.NAME + " V " + PluginDetails.VERSION + " has been enabled... Enjoy :)").color(TextColors.GREEN).build());
	}

	@Override
	public void disable() {
		super.disable();
		
		instance = null;
		Sponge.getEventManager().unregisterPluginListeners(bootstrap);
		Sponge.getServer().getConsole().sendMessage(Text.builder(PluginDetails.NAME + " V " + PluginDetails.VERSION + " has been disabled... Goodbye :(").color(TextColors.RED).build());
	}

	@Override
	public SpongeBootstrap getBootstrap() {
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
