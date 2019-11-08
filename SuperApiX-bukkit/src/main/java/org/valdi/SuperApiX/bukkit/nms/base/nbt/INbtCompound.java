package org.valdi.SuperApiX.bukkit.nms.base.nbt;

import java.io.Serializable;
import java.util.Set;

public interface INbtCompound {

    /**
     * @return The Compound name
     */
    String getName();

    /**
     * @return The NMS Compound behind this Object
     */
    Object getHandle();

    /**
     * @return The parent Compound
     */
    INbtCompound getParent();

    /**
     * Getter
     *
     * @param key
     * @return The stored value or NMS fallback
     */
    String getString(String key);

    /**
     * Getter
     *
     * @param key
     * @return The stored value or NMS fallback
     */
    Integer getInteger(String key);

    /**
     * Getter
     *
     * @param key
     * @return The stored value or NMS fallback
     */
    Double getDouble(String key);

    /**
     * Getter
     *
     * @param key
     * @return The stored value or NMS fallback
     */
    Byte getByte(String key);

    /**
     * Getter
     *
     * @param key
     * @return The stored value or NMS fallback
     */
    Short getShort(String key);

    /**
     * Getter
     *
     * @param key
     * @return The stored value or NMS fallback
     */
    Long getLong(String key);

    /**
     * Getter
     *
     * @param key
     * @return The stored value or NMS fallback
     */
    Float getFloat(String key);

    /**
     * Getter
     *
     * @param key
     * @return The stored value or NMS fallback
     */
    Boolean getBoolean(String key);

    /**
     * Getter
     *
     * @param key
     * @return The stored value or NMS fallback
     */
    byte[] getByteArray(String key);

    /**
     * Getter
     *
     * @param key
     * @return The stored value or NMS fallback
     */
    int[] getIntArray(String key);

    /**
     * Uses Gson to retrieve a stored Object
     *
     * @param key
     * @param type Class of the Object
     * @return The created Object or null if empty
     */
    <T> T getObject(String key, Class<T> type);

    /**
     * Setter
     *
     * @param key
     * @param value
     */
    void setString(String key, String value);

    /**
     * Setter
     *
     * @param key
     * @param value
     */
    void setInteger(String key, Integer value);

    /**
     * Setter
     *
     * @param key
     * @param value
     */
    void setDouble(String key, Double value);

    /**
     * Setter
     *
     * @param key
     * @param value
     */
    void setByte(String key, Byte value);

    /**
     * Setter
     *
     * @param key
     * @param value
     */
    void setShort(String key, Short value);

    /**
     * Setter
     *
     * @param key
     * @param value
     */
    void setLong(String key, Long value);

    /**
     * Setter
     *
     * @param key
     * @param value
     */
    void setFloat(String key, Float value);

    /**
     * Setter
     *
     * @param key
     * @param value
     */
    void setBoolean(String key, Boolean value);

    /**
     * Setter
     *
     * @param key
     * @param value
     */
    void setByteArray(String key, byte[] value);

    /**
     * Setter
     *
     * @param key
     * @param value
     */
    void setIntArray(String key, int[] value);

    /**
     * Uses Gson to store an {@link Serializable} Object
     *
     * @param key
     * @param value
     */
    void setObject(String key, Object value);

    /**
     * @param key
     * @return True if the key is set
     */
    Boolean hasKey(String key);

    /**
     * @param key Deletes the given Key
     */
    void removeKey(String key);

    /**
     * @return Set of all stored Keys
     */
    Set<String> getKeys();

    /**
     * Merges all data from comp into this compound. This is done in one action, so
     * it also works with Tiles/Entities
     *
     * @param comp
     */
    <T extends INbtCompound> void mergeCompound(T comp);

    /**
     * Creates a subCompound
     *
     * @param key Key to use
     * @return The subCompound Object
     */
    INbtCompound addCompound(String key);

    /**
     * @param key
     * @return The Compound instance or null
     */
    INbtCompound getCompound(String key);

    /**
     * @param key
     * @return The retrieved String List
     */
    INbtList<String> getStringList(String key);

    /**
     * @param key
     * @return The retrieved Integer List
     */
    INbtList<Integer> getIntegerList(String key);

    /**
     * @param key
     * @return The retrieved Compound List
     */
    INbtList<IListElement> getCompoundList(String key);

    /**
     * @param key
     * @return The type of the given stored key or null
     */
    NBTType getType(String key);

    @Override
    String toString();

    /**
     * @param key
     * @return A string representation of the given key
     */
    String toString(String key);

    /**
     * @return A json valid nbt string for this Compound
     */
    String asNBTString();

}
