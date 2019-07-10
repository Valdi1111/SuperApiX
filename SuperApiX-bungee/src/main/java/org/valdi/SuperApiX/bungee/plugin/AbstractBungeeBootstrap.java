package org.valdi.SuperApiX.bungee.plugin;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import net.md_5.bungee.api.scheduler.ScheduledTask;
import org.valdi.SuperApiX.bungee.logging.BungeePluginLogger;
import org.valdi.SuperApiX.bungee.utils.RedisBungeeUtil;
import org.valdi.SuperApiX.common.plugin.ISuperBootstrap;
import org.valdi.SuperApiX.common.PlatformType;
import org.valdi.SuperApiX.common.dependencies.classloader.PluginClassLoader;
import org.valdi.SuperApiX.common.dependencies.classloader.ReflectionClassLoader;
import org.valdi.SuperApiX.common.logging.SuperLogger;
import org.valdi.SuperApiX.common.scheduler.SimpleScheduler;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public abstract class AbstractBungeeBootstrap<T extends AbstractBungeePlugin> extends Plugin implements ISuperBootstrap<T> {
    /**
     * The plugin instance
     */
    protected T plugin;

    /**
     * The plugin logger
     */
    private SuperLogger logger;

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

    private ScheduledTask task;

    // provide adapters

    @Override
    public String getName() {
        return super.getDescription().getName();
    }

    @Override
	public SuperLogger getPluginLogger() {
        if (this.logger == null) {
            throw new IllegalStateException("Logger has not been initialised yet");
        }
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
		return super.getClass().getClassLoader();
	}

    // load/enable latches
    private final CountDownLatch loadLatch = new CountDownLatch(1);
    private final CountDownLatch enableLatch = new CountDownLatch(1);

    protected AbstractBungeeBootstrap() {
        this.classLoader = new ReflectionClassLoader(this);
    }
	
	@Override
	public void onLoad() {
        this.logger = new BungeePluginLogger(getLogger());
        this.schedulerAdapter = new SimpleScheduler(plugin);
        
        try {
        	getPlugin().load();
        } finally {
            this.loadLatch.countDown();
        }
	}

	@Override
	public void onEnable() {
        this.startTime = System.currentTimeMillis();
        try {
            task = this.getProxy().getScheduler().schedule(this, new Runnable() {
                int alteredTicks = 0;

                @Override
                public void run() {
                    ++alteredTicks;
                    schedulerAdapter.tick(alteredTicks);

                    //getLogger().info("Ticking scheduler... Ticks: " + alteredTicks);
                }
            }, 0L, 50L, TimeUnit.MILLISECONDS);

        	getPlugin().enable();
        } finally {
            this.enableLatch.countDown();
        }
	}
	
	@Override
	public void onDisable() {
		getPlugin().disable();
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
