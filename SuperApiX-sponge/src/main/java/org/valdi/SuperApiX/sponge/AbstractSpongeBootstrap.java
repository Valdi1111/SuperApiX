package org.valdi.SuperApiX.sponge;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.SpongeExecutorService;
import org.valdi.SuperApiX.common.ISuperBootstrap;
import org.valdi.SuperApiX.common.PlatformType;
import org.valdi.SuperApiX.common.PluginDetails;
import org.valdi.SuperApiX.common.dependencies.classloader.PluginClassLoader;
import org.valdi.SuperApiX.common.dependencies.classloader.ReflectionClassLoader;
import org.valdi.SuperApiX.common.logging.PluginLogger;
import org.valdi.SuperApiX.common.scheduler.SchedulerAdapter;
import org.valdi.SuperApiX.common.utils.MoreFiles;

public abstract class AbstractSpongeBootstrap<T extends AbstractSpongePlugin> implements ISuperBootstrap {
    /**
     * The plugin logger
     */
    private PluginLogger logger;

    /**
     * A scheduler adapter for the platform
     */
    private final SpongeSchedulerAdapter schedulerAdapter;

    /**
     * The plugin classloader
     */
    private final PluginClassLoader classLoader;

    /**
     * The time when the plugin was enabled
     */
    private long startTime;

    /**
     * Reference to the central {@link Game} instance in the API
     */
    private Game game;

    /**
     * Injected configuration directory for the plugin
     */
    private Path configDirectory;

    /**
     * Injected plugin container for the plugin
     */
    private PluginContainer pluginContainer;

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
	
    @Override
	public File getJarFile() {
		return pluginContainer.getSource().get().toFile();
	}
	
    @Override
	public ClassLoader getJarLoader() {
		return this.getClass().getClassLoader();
	}

    /**
     * Reference to the sponge scheduler
     */
    private final Scheduler spongeScheduler;

    // load/enable latches
    private final CountDownLatch loadLatch = new CountDownLatch(1);
    private final CountDownLatch enableLatch = new CountDownLatch(1);
    
    protected AbstractSpongeBootstrap(Logger logger, Game game, PluginContainer plugin, Path dir, SpongeExecutorService syncExecutor, SpongeExecutorService asyncExecutor) {
        this.logger = new Slf4jPluginLogger(logger);
        this.game = game;
        this.configDirectory = dir;
        this.pluginContainer = plugin;
        this.spongeScheduler = Sponge.getScheduler();
        this.schedulerAdapter = new SpongeSchedulerAdapter(this, this.spongeScheduler, syncExecutor, asyncExecutor);
        this.classLoader = new ReflectionClassLoader(this);
    }

    @Listener(order = Order.FIRST)
    public void onLoad(GamePreInitializationEvent event) {
        this.startTime = System.currentTimeMillis();
        try {
        	getPlugin().load();
        } finally {
            this.loadLatch.countDown();
        }
    }

    @Listener(order = Order.LATE)
    public void onEnable(GamePreInitializationEvent event) {
        try {
        	getPlugin().enable();
        } finally {
            this.enableLatch.countDown();
        }
    }

    @Listener
    public void onDisable(GameStoppingServerEvent event) {
    	getPlugin().disable();
    }
	
	@Listener
	public void onReload(GameReloadEvent event) {
		getPlugin().reload((Exception e) -> {
    		throw new RuntimeException("Exeception while reloading plugin > " + e.getMessage());
    	});
	}
    
    protected abstract T getPlugin();

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
    public File getDataFolder() {
        return this.getConfigPath().toFile();
    }

    @Override
    public String getVersion() {
        return PluginDetails.VERSION;
    }

    @Override
    public long getStartupTime() {
        return this.startTime;
    }

    // provide information about the platform

    @Override
    public PlatformType getType() {
        return PlatformType.SPONGE;
    }

    @Override
    public String getServerBrand() {
        return getGame().getPlatform().getContainer(Platform.Component.IMPLEMENTATION).getName();
    }

    @Override
    public String getServerVersion() {
        PluginContainer api = getGame().getPlatform().getContainer(Platform.Component.API);
        PluginContainer impl = getGame().getPlatform().getContainer(Platform.Component.IMPLEMENTATION);
        return api.getName() + ": " + api.getVersion().orElse("null") + " - " + impl.getName() + ": " + impl.getVersion().orElse("null");
    }

    @Override
    public Path getDataDirectory() {
        Path dataDirectory = getGame().getGameDirectory().toAbsolutePath().resolve("superapix");
        try {
            MoreFiles.createDirectoriesIfNotExists(dataDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataDirectory;
    }

    @Override
    public InputStream getResourceStream(String path) {
        return getClass().getClassLoader().getResourceAsStream(path);
    }

    @Override
    public Optional<Player> getPlayer(UUID uuid) {
        if (!getGame().isServerAvailable()) {
            return Optional.empty();
        }

        return getGame().getServer().getPlayer(uuid);
    }

    @Override
    public Optional<UUID> lookupUuid(String username) {
        if (!getGame().isServerAvailable()) {
            return Optional.empty();
        }

        return getGame().getServer().getGameProfileManager().get(username)
                .thenApply(p -> Optional.of(p.getUniqueId()))
                .exceptionally(x -> Optional.empty())
                .join();
    }

    @Override
    public Optional<String> lookupUsername(UUID uuid) {
        if (!getGame().isServerAvailable()) {
            return Optional.empty();
        }

        return getGame().getServer().getGameProfileManager().get(uuid)
                .thenApply(GameProfile::getName)
                .exceptionally(x -> Optional.empty())
                .join();
    }

    @Override
    public int getPlayerCount() {
        return getGame().isServerAvailable() ? getGame().getServer().getOnlinePlayers().size() : 0;
    }

    @Override
    public Stream<String> getPlayerList() {
        return getGame().isServerAvailable() ? getGame().getServer().getOnlinePlayers().stream().map(Player::getName) : Stream.empty();
    }

    @Override
    public Stream<UUID> getOnlinePlayers() {
        return getGame().isServerAvailable() ? getGame().getServer().getOnlinePlayers().stream().map(Player::getUniqueId) : Stream.empty();
    }

    @Override
    public boolean isPlayerOnline(UUID uuid) {
        return getGame().isServerAvailable() ? getGame().getServer().getPlayer(uuid).map(Player::isOnline).orElse(false) : false;
    }

	@Override
	public void disablePlugin() {
		// TODO Auto-generated method stub
		
	}

    // getters for the injected sponge instances

    public Game getGame() {
        return this.game;
    }

    public Path getConfigPath() {
        return this.configDirectory;
    }

    public PluginContainer getPluginContainer() {
        return this.pluginContainer;
    }

    public Scheduler getSpongeScheduler() {
        return this.spongeScheduler;
    }

}
