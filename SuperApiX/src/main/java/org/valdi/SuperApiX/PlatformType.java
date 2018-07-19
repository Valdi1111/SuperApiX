package org.valdi.SuperApiX;

import javax.annotation.Nonnull;

/**
 * Represents a type of platform which EliteVanish can run on.
 */
public enum PlatformType {

    BUKKIT("Bukkit"),
    BUNGEE("Bungee"),
    SPONGE("Sponge"),
    NUKKIT("Nukkit");

    private final String friendlyName;

    private PlatformType(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    /**
     * Gets a readable name for the platform type.
     *
     * @return a readable name
     */
    @Nonnull
    public String getFriendlyName() {
        return this.friendlyName;
    }

}
