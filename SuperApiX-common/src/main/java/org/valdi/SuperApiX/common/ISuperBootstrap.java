package org.valdi.SuperApiX.common;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

import org.valdi.SuperApiX.common.dependencies.classloader.PluginClassLoader;
import org.valdi.SuperApiX.common.logging.PluginLogger;
import org.valdi.SuperApiX.common.scheduler.SchedulerAdapter;

public interface ISuperBootstrap {

    public File getDataFolder();

    /**
     * Gets the plugin logger
     *
     * @return the logger
     */
	public PluginLogger getPluginLogger();
	
    /**
     * Gets an adapter for the platforms scheduler
     *
     * @return the scheduler
     */
    public SchedulerAdapter getScheduler();

    /**
     * Gets a {@link PluginClassLoader} for this instance
     *
     * @return a classloader
     */
    public PluginClassLoader getPluginClassLoader();
    
    public File getJarFile();

	public ClassLoader getJarLoader();

    /**
     * Returns a countdown latch which {@link CountDownLatch#countDown() counts down}
     * after the plugin has loaded.
     *
     * @return a loading latch
     */
    public CountDownLatch getLoadLatch();

    /**
     * Returns a countdown latch which {@link CountDownLatch#countDown() counts down}
     * after the plugin has enabled.
     *
     * @return an enable latch
     */
    public CountDownLatch getEnableLatch();

    /**
     * Gets a string of the plugin's version
     *
     * @return the version of the plugin
     */
    public String getVersion();

    /**
     * Gets the time when the plugin first started in millis.
     *
     * @return the enable time
     */
    public long getStartupTime();

    /**
     * Gets the platform type this instance of LuckPerms is running on.
     *
     * @return the platform type
     */
    public PlatformType getType();

    /**
     * Gets the name or "brand" of the running platform
     *
     * @return the server brand
     */
    public String getServerBrand();

    /**
     * Gets the version of the running platform
     *
     * @return the server version
     */
    public String getServerVersion();

    /**
     * Gets the name associated with this server
     *
     * @return the server name
     */
    public default String getServerName() {
        return null;
    }

    /**
     * Gets the plugins main data storage directory
     *
     * <p>Bukkit: /root/plugins/LuckPerms</p>
     * <p>Bungee: /root/plugins/LuckPerms</p>
     * <p>Sponge: /root/luckperms/</p>
     *
     * @return the platforms data folder
     */
    public Path getDataDirectory();

    /**
     * Gets the plugins configuration directory
     *
     * @return the config directory
     */
    public default Path getConfigDirectory() {
        return getDataDirectory();
    }

    /**
     * Gets a bundled resource file from the jar
     *
     * @param path the path of the file
     * @return the file as an input stream
     */
    public InputStream getResourceStream(String path);

    /**
     * Gets a player object linked to this User. The returned object must be the same type
     * as the instance used in the platforms ContextManager
     *
     * @param uuid the users unique id
     * @return a player object, or null, if one couldn't be found.
     */
    public Optional<?> getPlayer(UUID uuid);

    /**
     * Lookup a uuid from a username, using the servers internal uuid cache.
     *
     * @param username the username to lookup
     * @return an optional uuid, if found
     */
    public Optional<UUID> lookupUuid(String username);

    /**
     * Lookup a username from a uuid, using the servers internal uuid cache.
     *
     * @param uuid the uuid to lookup
     * @return an optional username, if found
     */
    public Optional<String> lookupUsername(UUID uuid);

    /**
     * Gets the number of users online on the platform
     *
     * @return the number of users
     */
    public int getPlayerCount();

    /**
     * Gets the usernames of the users online on the platform
     *
     * @return a {@link List} of usernames
     */
    public Stream<String> getPlayerList();

    /**
     * Gets the UUIDs of the users online on the platform
     *
     * @return a {@link Set} of UUIDs
     */
    public Stream<UUID> getOnlinePlayers();

    /**
     * Checks if a user is online
     *
     * @param uuid the users external uuid
     * @return true if the user is online
     */
    public boolean isPlayerOnline(UUID uuid);

    public void disablePlugin();

}
