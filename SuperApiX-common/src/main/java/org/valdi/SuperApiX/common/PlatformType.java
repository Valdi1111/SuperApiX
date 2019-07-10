package org.valdi.SuperApiX.common;

/**
 * Represents a type of platform which SuperApiX can run on.
 */
public enum PlatformType {

    BUKKIT("Bukkit"),
    BUNGEE("Bungee"),
    SPONGE("Sponge"),
    NUKKIT("Nukkit");

    private final String friendlyName;

    PlatformType(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    /**
     * Gets a readable name for the platform type.
     *
     * @return a readable name
     */
    public String getFriendlyName() {
        return this.friendlyName;
    }

}
