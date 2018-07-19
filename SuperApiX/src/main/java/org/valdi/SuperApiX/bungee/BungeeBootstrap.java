package org.valdi.SuperApiX.bungee;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

import org.valdi.SuperApiX.ISuperBootstrap;
import org.valdi.SuperApiX.PlatformType;
import org.valdi.SuperApiX.bungee.utils.RedisBungeeUtil;
import org.valdi.SuperApiX.common.dependencies.classloader.PluginClassLoader;
import org.valdi.SuperApiX.common.dependencies.classloader.ReflectionClassLoader;
import org.valdi.SuperApiX.common.logging.JavaPluginLogger;
import org.valdi.SuperApiX.common.logging.PluginLogger;
import org.valdi.SuperApiX.common.scheduler.BungeeSchedulerAdapter;
import org.valdi.SuperApiX.common.scheduler.SchedulerAdapter;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeBootstrap extends Plugin implements ISuperBootstrap {

    /**
     * The plugin logger
     */
    private PluginLogger logger;

    /**
     * A scheduler adapter for the platform
     */
    private final BungeeSchedulerAdapter schedulerAdapter;

    /**
     * The plugin classloader
     */
    private final PluginClassLoader classLoader;

    /**
     * The plugin instance
     */
    private final SuperApiBungee plugin;

    private Metrics metrics;

    /**
     * The time when the plugin was enabled
     */
    private long startTime;

    // provide adapters

	@Override
	public PluginLogger getPluginLogger() {
        if (this.logger == null) {
            throw new IllegalStateException("Logger has not been initialised yet");
        }
        return this.logger;
	}

	@Override
	public SchedulerAdapter getScheduler() {
		return this.schedulerAdapter;
	}

	@Override
	public PluginClassLoader getPluginClassLoader() {
		return this.classLoader;
	}

    // load/enable latches
    private final CountDownLatch loadLatch = new CountDownLatch(1);
    private final CountDownLatch enableLatch = new CountDownLatch(1);

    public BungeeBootstrap() {
        this.schedulerAdapter = new BungeeSchedulerAdapter(this);
        this.classLoader = new ReflectionClassLoader(this);
        this.plugin = new SuperApiBungee(this);
    }
	
	/*
	 * (non-Javadoc)
	 * @see net.md_5.bungee.api.plugin.Plugin#onLoad()
	 */
	@Override
	public void onLoad() {
        this.logger = new JavaPluginLogger(getLogger());
        
        try {
            this.plugin.load();
        } finally {
            this.loadLatch.countDown();
        }
	}

	/*
	 * (non-Javadoc)
	 * @see net.md_5.bungee.api.plugin.Plugin#onEnable()
	 */
	@Override
	public void onEnable() {
        this.startTime = System.currentTimeMillis();
        try {
        	metrics = new Metrics(this);
            // Optional: Add custom charts
            // metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));
        	
            this.plugin.enable();
        } finally {
            this.enableLatch.countDown();
        }
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.md_5.bungee.api.plugin.Plugin#onDisable()
	 */
	@Override
	public void onDisable() {
		this.plugin.disable();
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
    public long getStartupTime() {
        return this.startTime;
    }

    // provide information about the platform

    @Override
    public PlatformType getType() {
        return PlatformType.BUNGEE;
    }

    @Override
    public String getServerBrand() {
        return getProxy().getName();
    }

    @Override
    public String getServerVersion() {
        return getProxy().getVersion();
    }

    @Override
    public String getServerName() {
        return getProxy().getName();
    }

    @Override
    public Path getDataDirectory() {
        return getDataFolder().toPath().toAbsolutePath();
    }

    @Override
    public InputStream getResourceStream(String path) {
        return getResourceAsStream(path);
    }

    @Override
    public Optional<ProxiedPlayer> getPlayer(UUID uuid) {
        return Optional.ofNullable(getProxy().getPlayer(uuid));
    }

    @Override
    public Optional<UUID> lookupUuid(String username) {
        if (getProxy().getPluginManager().getPlugin("RedisBungee") != null) {
            try {
                return RedisBungeeUtil.lookupUuid(username);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<String> lookupUsername(UUID uuid) {
        if (getProxy().getPluginManager().getPlugin("RedisBungee") != null) {
            try {
                return RedisBungeeUtil.lookupUsername(uuid);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        return Optional.empty();
    }

    @Override
    public int getPlayerCount() {
        return getProxy().getOnlineCount();
    }

    @Override
    public Stream<String> getPlayerList() {
        return getProxy().getPlayers().stream().map(ProxiedPlayer::getName);
    }

    @Override
    public Stream<UUID> getOnlinePlayers() {
        return getProxy().getPlayers().stream().map(ProxiedPlayer::getUniqueId);
    }

    @Override
    public boolean isPlayerOnline(UUID uuid) {
    	ProxiedPlayer player = getProxy().getPlayer(uuid);
        return player != null && player.isConnected();
    }
    
    @Override
    public void disablePlugin() {
    	this.onDisable();
    }

}
