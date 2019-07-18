package org.valdi.SuperApiX.bukkit.nms.base.nbt;

import java.util.Set;

public interface IListElement {

    /**
     * String getter
     *
     * @param key
     * @return Saved value or NMS fallback
     */
    String getString(String key);

    /**
     * int getter
     *
     * @param value
     * @return Saved value or NMS fallback
     */
    int getInteger(String value);

    /**
     * double getter
     *
     * @param key
     * @return Saved value or NMS fallback
     */
    double getDouble(String key);

    /**
     * String setter
     *
     * @param key
     * @param value
     */
    void setString(String key, String value);

    /**
     * int setter
     *
     * @param key
     * @param value
     */
    void setInteger(String key, int value);

    /**
     * double setter
     *
     * @param key
     * @param value
     */
    void setDouble(String key, double value);

    /**
     * Checks for a given key
     *
     * @param key
     * @return true if the Compound has this key, else false
     */
    boolean hasKey(String key);

    /**
     * @return Set containing all keys
     */
    Set<String> getKeys();

    /**
     * Removes a key from the Compound
     *
     * @param key
     */
    void remove(String key);
}
