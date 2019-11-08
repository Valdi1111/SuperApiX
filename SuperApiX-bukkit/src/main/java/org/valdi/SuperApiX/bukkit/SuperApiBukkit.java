package org.valdi.SuperApiX.bukkit;

import java.util.Optional;
import java.util.concurrent.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.valdi.SuperApiX.bukkit.bossbar.IBossBarProvider;
import org.valdi.SuperApiX.bukkit.config.serializers.LocationSerializer;
import org.valdi.SuperApiX.bukkit.config.serializers.VectorSerializer;
import org.valdi.SuperApiX.bukkit.config.serializers.WorldUuidSerializer;
import org.valdi.SuperApiX.bukkit.listeners.JoinLeaveListener;
import org.valdi.SuperApiX.bukkit.listeners.PluginListener;
import org.valdi.SuperApiX.bukkit.managers.NmsManager;
import org.valdi.SuperApiX.bukkit.managers.PlayersManager;
import org.valdi.SuperApiX.bukkit.managers.ServiceProviderManager;
import org.valdi.SuperApiX.bukkit.nms.nbt.utils.MinecraftVersion;
import org.valdi.SuperApiX.bukkit.plugin.AbstractBukkitPlugin;
import org.valdi.SuperApiX.bukkit.users.VaultHandler;
import org.valdi.SuperApiX.common.config.advanced.ConfigLoader;
import org.valdi.SuperApiX.common.config.serializers.SetSerializer;
import org.valdi.SuperApiX.common.databases.*;

public class SuperApiBukkit extends AbstractBukkitPlugin<BukkitBootstrap> {
	private static SuperApiBukkit instance;

	// Databases
	private IDataStorage database;
	private PlayersManager playersManager;

	// Settings
	private ConfigLoader<Settings> settings;

	private ServiceProviderManager providerManager;
	private NmsManager nmsManager;
	private VaultHandler vault;
    
    public SuperApiBukkit(BukkitBootstrap bootstrap) {
        super(bootstrap);

		instance = this;
    }
	
	@Override
	public void load() {
		super.load();

        // Register common serializers
    	new SetSerializer().register();
    	
    	// Register bukkit only serializers
    	new VectorSerializer().register();
    	new LocationSerializer().register();
    	new WorldUuidSerializer().register();

		// Initialize NBT support
		MinecraftVersion.getVersion();

		providerManager = new ServiceProviderManager(this);
		nmsManager = new NmsManager(this);
		providerManager.registerAll();

		// Load settings
		if (!loadSettings()) {
			// We're aborting the load.
			this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not load config... Disabling plugin.");
			bootstrap.disablePlugin();
			return;
		}

		// Save settings - ensures admins always have the latest config file
		settings.save();

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

		// Create database
		if(!setupDatabase()) {
			// We're aborting the load.
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

		vault = new VaultHandler();
		vault.canLoad(this);

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

		// Save data
		if (playersManager != null) {
			playersManager.shutdown();
		}

		// Close database
		if(database != null) {
			try {
				database.close();
			} catch (DatabaseException e) {
				this.getLogger().severe("### Unable close Database connection. ###", e);
			}
		}
		
		Bukkit.getServicesManager().unregisterAll(bootstrap);
		HandlerList.unregisterAll(bootstrap);

		instance = null;

		bootstrap.getServer().getConsoleSender().sendMessage(ChatColor.RED + bootstrap.getDescription().getName() + " V" + bootstrap.getDescription().getVersion() + " has been disabled... Goodbye :(");
	}

	/**
	 * Loads the settings from the config file.
	 * If it fails, it can shut the plugin down.
	 * @return {@code true} if it loaded successfully.
	 */
	private boolean loadSettings() {
		getLogger().info("Loading Settings from config.yml...");
		// Load settings from config.yml. This will check if there are any issues with it too.
		settings = new ConfigLoader<>(this, Settings.class);
		settings.loadAnnotated();

		if (settings == null) {
			// Settings did not load correctly. Disable plugin.
			getLogger().severe("### Settings did not load correctly - please check config.yml");
			return false;
		}
		return true;
	}

	private boolean setupDatabase() {
		String ip = this.getSettings().getDatabaseHost();
		int port = this.getSettings().getDatabasePort();
		String options = this.getSettings().getOptions();
		String dbName = this.getSettings().getDatabaseName();
		String username = this.getSettings().getDatabaseUsername();
		String password = this.getSettings().getDatabasePassword();
		int poolSize = this.getSettings().getDatabasePoolSize();
		String poolName = this.getSettings().getDatabasePoolName();

		try {
			getLogger().info("Creating database and connecting to it...");
			database = DatabasesProvider.builder()
					.setStoreLoader(this)
					.setType(getSettings().getDatabaseType())
					.setFile(this.getDataFolder(), dbName)
					.setAddress(ip)
					.setPort(port)
					.setOptions(options)
					.setDatabase(dbName)
					.setUsername(username)
					.setPassword(password)
					.setPoolSize(poolSize)
					.setPoolName(poolName)
					.build();
			return true;
		} catch (DatabaseException e) {
			this.getLogger().debug("### Cannot create database ###", e);
			return false;
		}
	}
	
	public static SuperApiBukkit getInstance() {
		return instance;
	}
	
	public NmsManager getNmsManager() {
		return this.nmsManager;
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
	 * Returns the players database
	 * @return the players database
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

	public VaultHandler getVault() {
		return vault;
	}
}
