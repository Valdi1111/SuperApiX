package org.valdi.SuperApiX.common.plugin;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

import org.valdi.SuperApiX.common.PlatformType;
import org.valdi.SuperApiX.common.dependencies.DependencyManager;
import org.valdi.SuperApiX.common.dependencies.classloader.PluginClassLoader;
import org.valdi.SuperApiX.common.logging.SuperLogger;
import org.valdi.SuperApiX.common.scheduler.SuperScheduler;

public interface ISuperBootstrap<T extends ISuperPlugin> {

    /**
     * Get the plugin's instance.
     * @return the plugin
     */
    T getPlugin();

    /**
     * Gets the plugin's name.
     * @return the name
     */
    String getName();

    /**
     * Gets the plugin's version.
     * @return the version
     */
    String getVersion();

    /**
     * Gets the plugin's authors.
     * @return the authors
     */
    List<String> getAuthors();

    /**
     * Gets the plugin logger.
     *
     * @return the logger
     */
	SuperLogger getPluginLogger();
	
    /**
     * Gets an adapter for the platforms scheduler.
     *
     * @return the scheduler
     */
    SuperScheduler getScheduler();

    /**
     * Gets the plugins main data storage directory.
     *
     * <p>Bukkit: /root/plugins/plugin-name</p>
     * <p>Bungee: /root/plugins/plugin-name</p>
     * <p>Sponge: /root/plugin-name/</p>
     *
     * @return the platforms data folder
     */
    File getDataFolder();

    /**
     * Get the plugin jar file.
     * @return the jar file
     */
    File getJarFile();

    /**
     * Get the plugin's jar ClassLoader.
     * @return the loader
     */
    ClassLoader getJarLoader();

    /**
     * Gets a {@link PluginClassLoader} for this instance.
     * @return a classloader
     */
    PluginClassLoader getPluginClassLoader();

    /**
     * Gets a bundled resource file from the jar.
     * @param path the path of the file
     * @return the file as an input stream
     */
    InputStream getResourceStream(String path);

    /**
     * Saves the raw contents of any resource embedded with an addon's .jar
     * file assuming it can be found using {@link #getResource(String)}.
     * <p>
     * The resource is saved into the addon's data folder using the same
     * hierarchy as the .jar file (subdirectories are preserved).
     *
     * @param resourcePath the embedded resource path to look for within the
     *     addon's .jar file. (No preceding slash).
     * @param replace if true, the embedded resource will overwrite the
     *     contents of an existing file.
     * @throws IllegalArgumentException if the resource path is null, empty,
     *     or points to a nonexistent resource.
     */
    void saveResource(String resourcePath, boolean replace);

    /**
     * Get an active instance of the DependencyManager.
     * @return the dependency manager
     */
    DependencyManager getDependencyManager();

    /**
     * Returns a countdown latch which {@link CountDownLatch#countDown() counts down}
     * after the plugin has loaded.
     * @return a loading latch
     */
    CountDownLatch getLoadLatch();

    /**
     * Returns a countdown latch which {@link CountDownLatch#countDown() counts down}
     * after the plugin has enabled.
     * @return an enable latch
     */
    CountDownLatch getEnableLatch();

    /**
     * Gets the time when the plugin first started in millis.
     * @return the enable time
     */
    long getStartupTime();

    /**
     * Gets the platform type this instance of LuckPerms is running on.
     * @return the platform type
     */
    PlatformType getType();

    /**
     * Gets the name or "brand" of the running platform.
     * @return the server brand
     */
    String getServerBrand();

    /**
     * Gets the version of the running platform.
     * @return the server version
     */
    String getServerVersion();

    /**
     * Gets the name associated with this server.
     * @return the server name
     */
    default String getServerName() {
        return null;
    }

    /**
     * Gets a player object linked to this User. The returned object must be the same type
     * as the instance used in the platforms ContextManager.
     * @param uuid the users unique id
     * @return a player object, or null, if one couldn't be found.
     */
    Optional<?> getPlayer(UUID uuid);

    /**
     * Lookup a uuid from a username, using the servers internal uuid cache.
     * @param username the username to lookup
     * @return an optional uuid, if found
     */
    Optional<UUID> lookupUuid(String username);

    /**
     * Lookup a username from a uuid, using the servers internal uuid cache.
     * @param uuid the uuid to lookup
     * @return an optional username, if found
     */
    Optional<String> lookupUsername(UUID uuid);

    /**
     * Gets the number of users online on the platform.
     * @return the number of users
     */
    int getPlayerCount();

    /**
     * Gets the usernames of the users online on the platform.
     * @return a {@link List} of usernames
     */
    Stream<String> getPlayerList();

    /**
     * Gets the UUIDs of the users online on the platform.
     * @return a {@link Set} of UUIDs
     */
    Stream<UUID> getOnlinePlayers();

    /**
     * Checks if a user is online.
     * @param uuid the users external uuid
     * @return true if the user is online
     */
    boolean isPlayerOnline(UUID uuid);

    void disablePlugin();

}
