package org.valdi.SuperApiX.bukkit.versions;

import org.bukkit.Server;
import org.valdi.SuperApiX.bukkit.plugin.AbstractBukkitBootstrap;

/**
 * Checks and ensures the current server software is compatible with BentoBox.
 * @author Poslovitch
 */
public class ServerCompatibility {
    private final AbstractBukkitBootstrap plugin;

    public ServerCompatibility(final AbstractBukkitBootstrap plugin) {
        this.plugin = plugin;
    }

    // ---- CONTENT ----

    private ServerSoftware serverSoftware;
    private ServerVersion serverVersion;
    private MinecraftVersion minecraftVersion;

    private Compatibility result;

    @Deprecated
    public Compatibility checkCompatibility(AbstractBukkitBootstrap ignored) {
        return this.checkCompatibility();
    }

    /**
     * Checks the compatibility with the current server software and returns the {@link Compatibility}.
     * Note this is a one-time calculation: further calls won't change the result.
     * @param plugin AbstractBukkitBootstrap instance to provide.
     * @return the {@link Compatibility}.
     */
    public Compatibility checkCompatibility() {
        if (result == null) {
            // Check the server version first
        	Compatibility version = plugin.getVersionCompatibility(getServerVersion(plugin.getServer()));

            if (version.equals(Compatibility.INCOMPATIBLE)) {
                // 'Version = null' means that it's not listed. And therefore, it's implicitly incompatible.
                result = Compatibility.INCOMPATIBLE;
                return result;
            }

            // Now, check the server software
            Compatibility software = plugin.getSoftwareCompatibility(getServerSoftware(plugin.getServer()));

            if (software.equals(Compatibility.INCOMPATIBLE)) {
                // 'software = null' means that it's not listed. And therefore, it's implicitly incompatible.
                result = Compatibility.INCOMPATIBLE;
                return result;
            }

            if (software.equals(Compatibility.NOT_SUPPORTED) || version.equals(Compatibility.NOT_SUPPORTED)) {
                result = Compatibility.NOT_SUPPORTED;
                return result;
            }

            if (software.equals(Compatibility.SUPPORTED) || version.equals(Compatibility.SUPPORTED)) {
                result = Compatibility.SUPPORTED;
                return result;
            }

            // Nothing's wrong, the server is compatible.
            result = Compatibility.COMPATIBLE;
            return result;
        }

        return result;
    }

    @Deprecated
    public ServerSoftware getServerSoftware(Server ignored) {
        return this.getServerSoftware();
    }

    /**
     * Returns the {@link ServerSoftware} entry corresponding to the current server software, may be UNKNOWN.
     * @param server the {@link Server} instance, must not be null.
     * @return the {@link ServerSoftware} run by this server or UNKNOWN.
     */
    public ServerSoftware getServerSoftware() {
        if(serverSoftware == null) {
            String software = plugin.getServer().getVersion().substring(4).split("-")[0];
            serverSoftware = ServerSoftware.getById(software.toUpperCase()).orElse(ServerSoftware.UNKNOWN);
        }

        return serverSoftware;
    }

    @Deprecated
    public ServerVersion getServerVersion(Server ignored) {
        return this.getServerVersion();
    }

    /**
     * Returns the {@link ServerVersion} entry corresponding to the current server software, may be UNKNOWN.
     * @param server the {@link Server} instance, must not be null.
     * @return the {@link ServerVersion} run by this server or UNKNOWN.
     */
    public ServerVersion getServerVersion() {
        if(serverVersion == null) {
            String version = plugin.getServer().getBukkitVersion().split("-")[0].replace(".", "_");
            serverVersion = ServerVersion.getById("v" + version).orElse(ServerVersion.UNKNOWN);
        }

        return serverVersion;
    }

    @Deprecated
    public MinecraftVersion getMinecraftVersion(Server ignored) {
        return this.getMinecraftVersion();
    }

    /**
     * Returns the {@link MinecraftVersion} entry corresponding to the current server software, may be UNKNOWN.
     * @param server the {@link Server} instance, must not be null.
     * @return the {@link MinecraftVersion} run by this server or UNKNOWN.
     */
    public MinecraftVersion getMinecraftVersion() {
        if(minecraftVersion == null) {
            String version = plugin.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
            minecraftVersion = MinecraftVersion.getById(version).orElse(MinecraftVersion.UNKNOWN);
        }

        return minecraftVersion;
    }
    
    public Compatibility getCompatibility() {
    	return result;
    }

}
