package org.valdi.SuperApiX.bukkit.versions;

public enum Compatibility {
    /**
     * The server software is compatible with the current version of the plugin.
     * There shouldn't be any issues.
     */
    COMPATIBLE(true),

    /**
     * The server software might not be compatible but is supported.
     * Issues might occur.
     */
    SUPPORTED(true),

    /**
     * The server software is not supported, even though the plugin may work fine.
     * Issues are likely and won't receive any support.
     */
    NOT_SUPPORTED(true),

    /**
     * The server software is explicitly not supported and incompatible.
     * The plugin won't run on it: that's pointless to try to run it.
     */
    INCOMPATIBLE(false);

    private boolean canLaunch;

    Compatibility(boolean canLaunch) {
        this.canLaunch = canLaunch;
    }

    public boolean isCanLaunch() {
        return canLaunch;
    }

}
