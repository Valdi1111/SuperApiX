package org.valdi.SuperApiX.bukkit.plugin;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.valdi.SuperApiX.bukkit.logging.BukkitPluginLogger;
import org.valdi.SuperApiX.bukkit.versions.*;
import org.valdi.SuperApiX.common.PlatformType;
import org.valdi.SuperApiX.common.dependencies.classloader.PluginClassLoader;
import org.valdi.SuperApiX.common.dependencies.classloader.ReflectionClassLoader;
import org.valdi.SuperApiX.common.logging.SuperLogger;
import org.valdi.SuperApiX.common.scheduler.SimpleScheduler;

public abstract class AbstractBukkitBootstrap<T extends ISuperBukkitPlugin> extends JavaPlugin implements ISuperBukkitBootstrap<T> {
    /**
     * The plugin instance
     */
    protected T plugin;

    /**
     * The plugin logger
     */
    private final SuperLogger logger;

    /**
     * A scheduler adapter for the platform
     */
    private SimpleScheduler schedulerAdapter;

    /**
     * The plugin classloader
     */
    private final PluginClassLoader classLoader;

    /**
     * The time when the plugin was enabled
     */
    private long startTime;

    // load/enable latches
    private final CountDownLatch loadLatch = new CountDownLatch(1);
    private final CountDownLatch enableLatch = new CountDownLatch(1);

    private final ServerCompatibility compatibility;
    private final Map<ServerSoftware, Compatibility> compatibleSoftwares;
    private final Map<ServerVersion, Compatibility> compatibleVersions;

    private BukkitTask task;

    protected AbstractBukkitBootstrap() {
        this.logger = new BukkitPluginLogger(getLogger());
        this.classLoader = new ReflectionClassLoader(this);
        
        this.compatibility = new ServerCompatibility();
        this.compatibleSoftwares = new HashMap<>();
        this.compatibleVersions = new HashMap<>();
    }

    // provide adapters

    @Override
    public SuperLogger getPluginLogger() {
        return this.logger;
    }

    @Override
    public SimpleScheduler getScheduler() {
        if (this.schedulerAdapter == null) {
            throw new IllegalStateException("Scheduler has not been initialised yet");
        }
        return this.schedulerAdapter;
    }

    @Override
    public PluginClassLoader getPluginClassLoader() {
        return this.classLoader;
    }
	
    @Override
	public File getJarFile() {
		return super.getFile();
	}
	
    @Override
	public ClassLoader getJarLoader() {
		return super.getClassLoader();
	}

    // lifecycle

	/*
	 * (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onLoad()
	 */
    @Override
    public void onLoad() {
        this.schedulerAdapter = new SimpleScheduler(plugin);
        if (checkIncompatibleVersion()) {
            return;
        }
        
        try {
            this.getPlugin().load();
        } finally {
            this.loadLatch.countDown();
        }
    }

	/*
	 * (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
	 */
    @Override
    public void onEnable() {
        if (!isCompatible()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    	
        this.startTime = System.currentTimeMillis();
        try {
            task = this.getServer().getScheduler().runTaskTimer(this, new Runnable() {
                int alteredTicks = 0;

                @Override
                public void run() {
                    ++alteredTicks;
                    schedulerAdapter.tick(alteredTicks);

                    //getLogger().info("Ticking scheduler... Ticks: " + alteredTicks);
                }
            }, 0L, 1L);

            this.getPlugin().enable();
        } finally {
            this.enableLatch.countDown();
        }
    }

	/*
	 * (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onDisable()
	 */
    @Override
    public void onDisable() {
        if (!isCompatible()) {
            return;
        }

        this.getPlugin().disable();
        try {
            task.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        schedulerAdapter.shutdown();
    }

    @Override
    public T getPlugin() {
        return plugin;
    }

    @Override
    public CountDownLatch getEnableLatch() {
        return this.enableLatch;
    }

    @Override
    public CountDownLatch getLoadLatch() {
        return this.loadLatch;
    }

    // provide information about the plugin

    @Override
    public String getVersion() {
        return getDescription().getVersion();
    }

    @Override
    public List<String> getAuthors() {
        return getDescription().getAuthors();
    }

    @Override
    public long getStartupTime() {
        return this.startTime;
    }

    // provide information about the platform

    @Override
    public PlatformType getType() {
        return PlatformType.BUKKIT;
    }

    @Override
    public String getServerBrand() {
        return getServer().getName();
    }

    @Override
    public String getServerVersion() {
        return getServer().getVersion() + " - " + getServer().getBukkitVersion();
    }

    @Override
    public String getServerName() {
        return getServer().getServerName();
    }

    @Override
    public Path getDataDirectory() {
        return getDataFolder().toPath().toAbsolutePath();
    }

    @Override
    public InputStream getResourceStream(String path) {
        return super.getResource(path);
    }

    @Override
    public Optional<Player> getPlayer(UUID uuid) {
        return Optional.ofNullable(getServer().getPlayer(uuid));
    }

    @Override
    public Optional<UUID> lookupUuid(String username) {
        // noinspection deprecation
        return Optional.ofNullable(getServer().getOfflinePlayer(username)).map(OfflinePlayer::getUniqueId);
    }

    @Override
    public Optional<String> lookupUsername(UUID uuid) {
        return Optional.ofNullable(getServer().getOfflinePlayer(uuid)).map(OfflinePlayer::getName);
    }

    @Override
    public int getPlayerCount() {
        return getServer().getOnlinePlayers().size();
    }

    @Override
    public Stream<String> getPlayerList() {
        return getServer().getOnlinePlayers().stream().map(Player::getName);
    }

    @Override
    public Stream<UUID> getOnlinePlayers() {
        return getServer().getOnlinePlayers().stream().map(Player::getUniqueId);
    }

    @Override
    public boolean isPlayerOnline(UUID uuid) {
        Player player = getServer().getPlayer(uuid);
        return player != null && player.isOnline();
    }
    
    @Override
    public void disablePlugin() {
    	this.getPluginLoader().disablePlugin(this);
    }

    public ServerCompatibility getServerCompatibility() {
    	return compatibility;
    }
    
    /**
     * 
     * @return {@code true} if the plugin is compatible
     */
    protected boolean isCompatible() {
    	return getServerCompatibility().getCompatibility().isCanLaunch();
    }

    /**
     * Check if the plugin is compatible with this server version.
     * @return {@code true} if the plugin is incompatible and could not be load
     */
	protected boolean checkIncompatibleVersion() {
		getServerCompatibility().checkCompatibility(this);
		return !isCompatible();
	}
	
	public Compatibility getSoftwareCompatibility(ServerSoftware software) {
		if(software == null) {
			return Compatibility.INCOMPATIBLE;
		}
		
		return compatibleSoftwares.containsKey(software) ? compatibleSoftwares.get(software) : Compatibility.COMPATIBLE;
	}
	
	protected void registerSoftwareCompatibility(Compatibility compatibility, ServerSoftware... softwares) {
		Arrays.asList(softwares).stream().forEach(s -> compatibleSoftwares.put(s, compatibility));
	}
	
	public Compatibility getVersionCompatibility(ServerVersion version) {
		if(version == null) {
			return Compatibility.INCOMPATIBLE;
		}

		return compatibleVersions.containsKey(version) ? compatibleVersions.get(version) : Compatibility.COMPATIBLE;
	}
	
	protected void registerVersionCompatibility(Compatibility compatibility, ServerVersion... versions) {
		Arrays.asList(versions).stream().forEach(v -> compatibleVersions.put(v, compatibility));
	}

}
