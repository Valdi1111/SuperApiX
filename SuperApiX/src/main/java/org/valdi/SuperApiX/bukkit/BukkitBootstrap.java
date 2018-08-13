package org.valdi.SuperApiX.bukkit;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.valdi.SuperApiX.ISuperBootstrap;
import org.valdi.SuperApiX.PlatformType;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;
import org.valdi.SuperApiX.common.annotation.plugin.Description;
import org.valdi.SuperApiX.common.annotation.plugin.LoadOrder;
import org.valdi.SuperApiX.common.annotation.plugin.LoadOrder.Phase;
import org.valdi.SuperApiX.common.annotation.plugin.BukkitPlugin;
import org.valdi.SuperApiX.common.annotation.plugin.author.Author;
import org.valdi.SuperApiX.common.dependencies.classloader.PluginClassLoader;
import org.valdi.SuperApiX.common.dependencies.classloader.ReflectionClassLoader;
import org.valdi.SuperApiX.common.logging.JavaPluginLogger;
import org.valdi.SuperApiX.common.logging.PluginLogger;
import org.valdi.SuperApiX.common.scheduler.BukkitSchedulerAdapter;

@BukkitPlugin(name = PluginDetails.NAME, version = PluginDetails.VERSION)
@Description(PluginDetails.DESCRIPTION)
@Author(PluginDetails.AUTHOR)
@LoadOrder(Phase.STARTUP)
public class BukkitBootstrap extends JavaPlugin implements ISuperBootstrap {

    /**
     * The plugin logger
     */
    private final PluginLogger logger;

    /**
     * A scheduler adapter for the platform
     */
    private final BukkitSchedulerAdapter schedulerAdapter;

    /**
     * The plugin classloader
     */
    private final PluginClassLoader classLoader;

    /**
     * The plugin instance
     */
    private final SuperApiBukkit plugin;
    
    // Metrics
    private Metrics metrics;

    /**
     * The time when the plugin was enabled
     */
    private long startTime;

    // load/enable latches
    private final CountDownLatch loadLatch = new CountDownLatch(1);
    private final CountDownLatch enableLatch = new CountDownLatch(1);

    // if the plugin has been loaded on an incompatible version
    private boolean incompatibleVersion = false;

    public BukkitBootstrap() {
        this.logger = new JavaPluginLogger(getLogger());
        this.schedulerAdapter = new BukkitSchedulerAdapter(this);
        this.classLoader = new ReflectionClassLoader(this);
        this.plugin = new SuperApiBukkit(this);
    }

    // provide adapters

    @Override
    public PluginLogger getPluginLogger() {
        return this.logger;
    }

    @Override
    public BukkitSchedulerAdapter getScheduler() {
        return this.schedulerAdapter;
    }

    @Override
    public PluginClassLoader getPluginClassLoader() {
        return this.classLoader;
    }
	
	public File getPluginFile() {
		return super.getFile();
	}

    // lifecycle

	/*
	 * (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onLoad()
	 */
    @Override
    public void onLoad() {
        if (checkIncompatibleVersion()) {
            this.incompatibleVersion = true;
            return;
        }
        
        try {
            this.plugin.load();
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
        if (this.incompatibleVersion) {
            getLogger().severe("----------------------------------------------------------------------");
            getLogger().severe("Your server version is not compatible with this build of ASkyBlock Plugin. :(");
            getLogger().severe("");
            getLogger().severe("Try updating it...");
            getLogger().severe("----------------------------------------------------------------------");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    	
        this.startTime = System.currentTimeMillis();
        try {
            this.plugin.enable();
            
            // Metrics
            metrics = new Metrics(this);
            getLogger().info("Metrics loaded."); 	
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
        if (this.incompatibleVersion) {
            return;
        }

        this.plugin.disable();
    	metrics = null;
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
        return getResource(path);
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

	private boolean checkIncompatibleVersion() {
		return false;
	}
	
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	if(label.equalsIgnoreCase("prova")) {
	        if(args.length != 2) {
	        	return false;
	        }
	        
	        try {
				plugin.getNmsProvider().getChatUtils().sendTabTitle((Player) sender, args[0], args[1]);
			} catch (VersionUnsupportedException ignored) {}
	        sender.sendMessage("asd");
    	}
    	return true;
    }

}
