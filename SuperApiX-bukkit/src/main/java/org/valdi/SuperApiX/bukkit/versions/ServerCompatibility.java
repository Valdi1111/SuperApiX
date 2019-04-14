package org.valdi.SuperApiX.bukkit.versions;

import org.bukkit.Server;
import org.valdi.SuperApiX.bukkit.AbstractBukkitBootstrap;

/**
 * Checks and ensures the current server software is compatible with BentoBox.
 * @author Poslovitch
 */
public class ServerCompatibility {

    public ServerCompatibility() { }

    // ---- CONTENT ----

    private Compatibility result;

    /**
     * Checks the compatibility with the current server software and returns the {@link Compatibility}.
     * Note this is a one-time calculation: further calls won't change the result.
     * @param plugin AbstractBukkitBootstrap instance to provide.
     * @return the {@link Compatibility}.
     */
    public Compatibility checkCompatibility(AbstractBukkitBootstrap plugin) {
        if (result == null) {
            // Check the server version first
        	Compatibility version = plugin.getVersionCompatibility(getServerVersion(plugin.getServer()));

            if (version == null || version.equals(Compatibility.INCOMPATIBLE)) {
                // 'Version = null' means that it's not listed. And therefore, it's implicitly incompatible.
                result = Compatibility.INCOMPATIBLE;
                return result;
            }

            // Now, check the server software
            Compatibility software = plugin.getSoftwareCompatibility(getServerSoftware(plugin.getServer()));

            if (software == null || software.equals(Compatibility.INCOMPATIBLE)) {
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

    /**
     * Returns the {@link ServerSoftware} entry corresponding to the current server software, may be null.
     * @param server the {@link Server} instance, must not be null.
     * @return the {@link ServerSoftware} run by this server or null.
     */
    public ServerSoftware getServerSoftware(Server server) {
        String serverSoftware = server.getVersion().substring(4).split("-")[0];
        try {
            return ServerSoftware.getById(serverSoftware.toUpperCase()).orElse(null);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Returns the {@link ServerVersion} entry corresponding to the current server software, may be null.
     * @param server the {@link Server} instance, must not be null.
     * @return the {@link ServerVersion} run by this server or null.
     */
    public ServerVersion getServerVersion(Server server) {
        String serverVersion = server.getBukkitVersion().split("-")[0].replace(".", "_");
        try {
            return ServerVersion.getById("V" + serverVersion.toUpperCase()).orElse(null);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    public Compatibility getCompatibility() {
    	return result;
    }

}
