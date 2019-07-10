package org.valdi.SuperApiX.bukkit;

import java.util.Optional;
import java.util.concurrent.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.valdi.SuperApiX.bukkit.advancements.AdvancementProvider;
import org.valdi.SuperApiX.bukkit.bossbar.IBossBarProvider;
import org.valdi.SuperApiX.bukkit.config.serializers.LocationSerializer;
import org.valdi.SuperApiX.bukkit.config.serializers.VectorSerializer;
import org.valdi.SuperApiX.bukkit.config.serializers.WorldUuidSerializer;
import org.valdi.SuperApiX.bukkit.listeners.JoinLeaveListener;
import org.valdi.SuperApiX.bukkit.listeners.PluginListener;
import org.valdi.SuperApiX.bukkit.nms.core.NmsProvider;
import org.valdi.SuperApiX.bukkit.nms.core.VersionManager;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;
import org.valdi.SuperApiX.bukkit.managers.PlayersManager;
import org.valdi.SuperApiX.bukkit.plugin.AbstractBukkitPlugin;
import org.valdi.SuperApiX.bukkit.users.VaultHandler;
import org.valdi.SuperApiX.common.config.advanced.ConfigLoader;
import org.valdi.SuperApiX.common.config.serializers.SetSerializer;
import org.valdi.SuperApiX.common.databases.*;
import org.valdi.SuperApiX.common.dependencies.Dependencies;

public class SuperApiBukkit extends AbstractBukkitPlugin<BukkitBootstrap> {
	private static SuperApiBukkit instance;

	private ServiceProviderManager provider;
	private VersionManager version;
	
	private NmsProvider nmsProvider;
	private AdvancementProvider advProvider;

	private IDataStorage database;

	// Databases
	private PlayersManager playersManager;

	// Settings
	private ConfigLoader<Settings> settings;

	private VaultHandler vault;
    
    public SuperApiBukkit(BukkitBootstrap bootstrap) {
        super(bootstrap);

		instance = this;
    }
	
	@Override
	public void load() {
		super.load();
		
        // load dependencies
		getDependencyManager().loadDependencies(Dependencies.TEXT, Dependencies.CAFFEINE, Dependencies.OKIO, Dependencies.OKHTTP);
        getDependencyManager().loadStorageDependencies();

        // Register common serializers
    	new SetSerializer().register();
    	
    	// Register bukkit only serializers
    	new VectorSerializer().register();
    	new LocationSerializer().register();
    	new WorldUuidSerializer().register();
        
		provider = new ServiceProviderManager(this);
		version = new VersionManager(this);

		nmsProvider = new NmsProvider(this);
		try {
			advProvider = new AdvancementProvider(this);
		} catch (VersionUnsupportedException e) {
			e.printStackTrace();
		}

		provider.registerAll();

		// Load settings
		if (!loadSettings()) {
			// We're aborting the load.
			bootstrap.disablePlugin();
			return;
		}

		// Save settings - ensures admins always have the latest config file
		settings.saveConfig();

		// Initializing debug (if enabled)
		if(this.getSettings().isDebug()) {
			this.getLogger().setDebugStatus(true);
		}

		// Locales manager must be loaded at load state
		this.getLocalesManager().init();
	}
	
	@Override
	public void enable() {
		super.enable();

		if(!setupDatabase()) {
			this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not connect to database... Disabling plugin.");
			bootstrap.disablePlugin();
			return;
		}

		// Start Database managers
		try {
			playersManager = new PlayersManager(this);
		} catch (DatabaseException e) {
			this.getLogger().severe("### Cannot create players table, disabling plugin... ###", e);
			bootstrap.disablePlugin();
			return;
		}

		if(advProvider != null) {
			advProvider.onEnable();
		}

		vault = new VaultHandler();
		vault.canLoad(this);

		/*new SuperRunnable(this) {
			private int i = 0;

			@Override
			public void run() {
				if(i == 10) {
					this.cancel();
				}

				i++;
				getLogger().info("Messaggio ogni 1 secondo.");
			}
		}.runTaskTimerAsynchronously(5L, 1L, TimeUnit.SECONDS);*/

		// Save players data every X minutes
		bootstrap.getScheduler().runTaskTimer(() -> {
			playersManager.saveAll();
		}, getSettings().getDatabaseBackupPeriod(), getSettings().getDatabaseBackupPeriod(), TimeUnit.MINUTES);

		this.getServer().getPluginManager().registerEvents(new JoinLeaveListener(this), bootstrap);
		this.getServer().getPluginManager().registerEvents(new PluginListener(this), bootstrap);
		
		bootstrap.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + bootstrap.getDescription().getName() + " V" + bootstrap.getDescription().getVersion() + " has been enabled... Enjoy :)");
	}
	
	@Override
	public void disable() {
		super.disable();

		if(advProvider != null) {
			advProvider.onDisable();
		}

		// Save data
		if (playersManager != null) {
			playersManager.shutdown();
		}

		// Close database
		if(database != null) {
			try {
				database.close();
			} catch (DatabaseException e) {
				this.getLogger().severe("### Unable close Database connection. ###");
				this.getLogger().severe("Error: " + e.getMessage());
				this.getLogger().debug("Database error", e);
			}
		}
		
		Bukkit.getServicesManager().unregisterAll(bootstrap);
		HandlerList.unregisterAll(bootstrap);

		instance = null;

		bootstrap.getServer().getConsoleSender().sendMessage(ChatColor.RED + bootstrap.getDescription().getName() + " V" + bootstrap.getDescription().getVersion() + " has been disabled... Goodbye :(");
	}

	private boolean setupDatabase() {
		String mysqlIp = this.getSettings().getDatabaseHost();
		int mysqlPort = this.getSettings().getDatabasePort();
		String mysqlDatabase = this.getSettings().getDatabaseName();
		String mysqlOptions = "";
		String mysqlUsername = this.getSettings().getDatabaseUsername();
		String mysqlPassword = this.getSettings().getDatabasePassword();

		try {
			database = DatabasesProvider.builder()
					.setStoreLoader(this)
					.setType(getSettings().getDatabaseType())
					.setFile(this.getDataFolder(), "database.db")
					.setAddress(mysqlIp)
					.setPort(mysqlPort)
					.setOptions(mysqlOptions)
					.setDatabase(mysqlDatabase)
					.setUsername(mysqlUsername)
					.setPassword(mysqlPassword)
					.setPoolSize(15)
					.setPoolName("SuperApiX-Hikari")
					.build();
			return true;
		} catch (DatabaseException e) {
			this.getLogger().debug("Database error", e);
			return false;
		}
	}
	
	public static SuperApiBukkit getInstance() {
		return instance;
	}
	
	public VersionManager getVersionManager() {
		return this.version;
	}
	
	public NmsProvider getNmsProvider() {
		return this.nmsProvider;
	}

	public AdvancementProvider getAdvProvider() {
    	return this.advProvider;
	}
	
	public Optional<IBossBarProvider> getBossBarSender() {
        RegisteredServiceProvider<IBossBarProvider> provider = Bukkit.getServicesManager().getRegistration(IBossBarProvider.class);
        if(provider == null) {
        	this.getLogger().debug("Cannot get bossbar provider...");
        	return Optional.empty();
        }
        
        return Optional.ofNullable(provider.getProvider());
	}

	public IDataStorage getDataStorage() {
		return this.database;
	}

	/**
	 * Returns the player database
	 * @return the player database
	 */
	public PlayersManager getPlayersManager() {
		return playersManager;
	}

	/**
	 * @return the settings
	 */
	public Settings getSettings() {
		return settings.getConfig();
	}

	/**
	 * Loads the settings from the config file.
	 * If it fails, it can shut the plugin down.
	 * @return {@code true} if it loaded successfully.
	 * @since 1.0.0-beta
	 */
	public boolean loadSettings() {
		getLogger().info("Loading Settings from config.yml...");
		// Load settings from config.yml. This will check if there are any issues with it too.
		settings = new ConfigLoader<>(this, Settings.class);
		settings.loadAnnotatedConfig();

		if (settings == null) {
			// Settings did not load correctly. Disable plugin.
			getLogger().severe("Settings did not load correctly - disabling plugin - please check config.yml");
			return false;
		}
		return true;
	}

	public VaultHandler getVault() {
		return vault;
	}
}
