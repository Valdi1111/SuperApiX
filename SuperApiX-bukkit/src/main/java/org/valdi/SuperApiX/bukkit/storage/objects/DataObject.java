package org.valdi.SuperApiX.bukkit.storage.objects;

import org.valdi.SuperApiX.bukkit.SuperApiBukkit;

/**
 * Contains fields that must be in any data object
 * @author tastybento
 *
 */
public interface DataObject {

    default SuperApiBukkit getPlugin() {
        return SuperApiBukkit.getInstance();
    }

    /**
     * @return the uniqueId
     */
    String getUniqueId();

    /**
     * @param value - unique ID the uniqueId to set
     */
    void setUniqueId(String value);

}
