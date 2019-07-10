package org.valdi.SuperApiX.common.config.types.nodes;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;

import java.util.List;
import java.util.Map;

public interface IConfigNode {

    /**
     * Gets the parent of this configuration node or
     * null if the node doesn't have a parent.
     * @return the parent or null
     */
    IConfigNode getParent();

    /**
     * Get the root configuration node
     * @return the root node
     */
    ConfigurationNode getNativeNode();

    /**
     * Get the node at the given path
     * @param objects the path
     * @return the node
     */
    default ConfigurationNode getNativeNode(Object[] objects) {
        return this.getNativeNode().getNode(objects);
    }

    /**
     * Get the native node at the given path (separate each path with a dot)
     * e.g. "messages.commands.noperm"
     * @param path the path
     * @return the node
     */
    default ConfigurationNode getFixedNode(String path) {
        if(path == null || path.equals("")) {
            return this.getNativeNode();
        }

        return this.getNativeNode(path.split("\\."));
    }

    /**
     * Get the node at the given path (separate each path with a dot)
     * e.g. "messages.commands.noperm"
     * @param path the path
     * @return the node
     */
    IConfigNode getNode(String path);

    // Get methods

    /**
     * Gets the requested Object by path.
     * @param path the node path
     * @return requested Object or null if doesn't exists.
     */
    Object get(String path);

    /**
     * Gets the requested Object by path.
     * @param path the node path
     * @param def the default value
     * @return requested Object or default if doesn't exists.
     */
    Object get(String path, Object def);

    /**
     * Gets the requested T by path.
     * @param path the node path
     * @param type the serializer
     * @param <T> a serializer type
     * @return requested T or null if doesn't exists.
     */
    <T> T get(String path, TypeToken<T> type);

    /**
     * Gets the requested T by path.
     * @param path the node path
     * @param type the serializer
     * @param def the default value
     * @param <T> a serializer type
     * @return requested T or default if doesn't exists.
     */
    <T> T get(String path, TypeToken<T> type, T def);

    /**
     * Gets the requested String by path.
     * @param path the node path
     * @return requested String or null if doesn't exists.
     */
    String getString(String path);

    /**
     * Gets the requested String by path.
     * @param path the node path
     * @param def the default value
     * @return requested String or default if doesn't exists.
     */
    String getString(String path, String def);

    /**
     * Gets the requested int by path.
     * @param path the node path
     * @return requested int or null if doesn't exists.
     */
    int getInt(String path);

    /**
     * Gets the requested int by path.
     * @param path the node path
     * @param def the default value
     * @return requested int or default if doesn't exists.
     */
    int getInt(String path, int def);

    /**
     * Gets the requested long by path.
     * @param path the node path
     * @return requested long or null if doesn't exists.
     */
    long getLong(String path);

    /**
     * Gets the requested long by path.
     * @param path the node path
     * @param def the default value
     * @return requested long or default if doesn't exists.
     */
    long getLong(String path, long def);

    /**
     * Gets the requested float by path.
     * @param path the node path
     * @return requested float or null if doesn't exists.
     */
    float getFloat(String path);

    /**
     * Gets the requested float by path.
     * @param path the node path
     * @param def the default value
     * @return requested float or default if doesn't exists.
     */
    float getFloat(String path, float def);

    /**
     * Gets the requested double by path.
     * @param path the node path
     * @return requested double or null if doesn't exists.
     */
    double getDouble(String path);

    /**
     * Gets the requested double by path.
     * @param path the node path
     * @param def the default value
     * @return requested double or default if doesn't exists.
     */
    double getDouble(String path, double def);

    /**
     * Gets the requested boolean by path.
     * @param path the node path
     * @return requested boolean or null if doesn't exists.
     */
    boolean getBoolean(String path);

    /**
     * Gets the requested boolean by path.
     * @param path the node path
     * @param def the default value
     * @return requested boolean or default if doesn't exists.
     */
    boolean getBoolean(String path, boolean def);

    // Get list methods

    /**
     * Gets the requested List of Object by path.
     * @param path the node path
     * @return requested List of Object or null if doesn't exists.
     */
    List<?> getList(String path);

    /**
     * Gets the requested List of Object by path.
     * @param path the node path
     * @param def the default value
     * @return requested List of Object or default if doesn't exists.
     */
    List<?> getList(String path, List<?> def);

    /**
     * Gets the requested List of T by path.
     * @param path the node path
     * @param type the serializer
     * @param <T> a serializer type
     * @return requested List of T or null if doesn't exists.
     */
    <T> List<T> getList(String path, TypeToken<T> type);

    /**
     * Gets the requested List of T by path.
     * @param path the node path
     * @param type the serializer
     * @param def the default value
     * @param <T> a serializer type
     * @return requested List of T or default if doesn't exists.
     */
    <T> List<T> getList(String path, TypeToken<T> type, List<T> def);

    /**
     * Gets the requested List of String by path.
     * @param path the node path
     * @return requested List of String or null if doesn't exists.
     */
    List<String> getStringList(String path);

    /**
     * Gets the requested List of String by path.
     * @param path the node path
     * @param def the default value
     * @return requested List of String or default if doesn't exists.
     */
    List<String> getStringList(String path, List<String> def);

    /**
     * Gets the requested List of int by path.
     * @param path the node path
     * @return requested List of int or null if doesn't exists.
     */
    List<Integer> getIntList(String path);

    /**
     * Gets the requested List of int by path.
     * @param path the node path
     * @param def the default value
     * @return requested List of int or default if doesn't exists.
     */
    List<Integer> getIntList(String path, List<Integer> def);

    /**
     * Gets the requested List of long by path.
     * @param path the node path
     * @return requested List of long or null if doesn't exists.
     */
    List<Long> getLongList(String path);

    /**
     * Gets the requested List of long by path.
     * @param path the node path
     * @param def the default value
     * @return requested List of long or default if doesn't exists.
     */
    List<Long> getLongList(String path, List<Long> def);

    /**
     * Gets the requested List of float by path.
     * @param path the node path
     * @return requested List of float or null if doesn't exists.
     */
    List<Float> getFloatList(String path);

    /**
     * Gets the requested List of float by path.
     * @param path the node path
     * @param def the default value
     * @return requested List of float or default if doesn't exists.
     */
    List<Float> getFloatList(String path, List<Float> def);

    /**
     * Gets the requested List of double by path.
     * @param path the node path
     * @return requested List of double or null if doesn't exists.
     */
    List<Double> getDoubleList(String path);

    /**
     * Gets the requested List of double by path.
     * @param path the node path
     * @param def the default value
     * @return requested List of double or default if doesn't exists.
     */
    List<Double> getDoubleList(String path, List<Double> def);

    /**
     * Gets the requested List of boolean by path.
     * @param path the node path
     * @return requested List of boolean or null if doesn't exists.
     */
    List<Boolean> getBooleanList(String path);

    /**
     * Gets the requested List of boolean by path.
     * @param path the node path
     * @param def the default value
     * @return requested List of boolean or default if doesn't exists.
     */
    List<Boolean> getBooleanList(String path, List<Boolean> def);

    // Set methods

    void set(String path, Object value);

    /**
     * Set a value in config
     * @param path the node path
     * @param type the serializer
     * @param value the value to set
     * @param <T> a serializer type
     */
    <T> void set(String path, TypeToken<T> type, T value);

    // Utilities

    /**
     * Gets the configuration node's name.
     * @return the node's name
     */
    String getKey();

    /**
     * Checks if this ConfigurationNode contains the given path.
     * @param path the path to check
     * @return true if this node contains the requested path.
     */
    boolean contains(String path);

    /**
     * Check if the given path is a section.
     * @param path the node path
     * @return true if has map children, false otherwise
     */
    boolean isSection(String path);

    /**
     * Gets a set containing all keys in this node.
     * @param path the node path
     * @return set of keys contained within this ConfigurationNode
     */
    List<String> getKeys(String path);

    /**
     * Gets a Map containing all keys and their values for this section.
     * @param path the ndoe path
     * @return Map of keys and values of this node.
     */
    Map<String, ? extends IConfigNode> getValues(String path);

    /**
     * Remove a child from a configuration node.
     * @param path the node path
     * @param child the child name
     */
    void removeChild(String path, String child);

    /**
     * Clear a configuration node.
     * @param path the node path
     */
    void clear(String path);

    /**
     * Set all the values from the given node that are not present in this node
     * to their values in the provided node.
     *
     * <p>Map keys will be merged. Lists and scalar values will be replaced.</p>
     *
     * @param other The node to merge values from
     * @return this
     */
    IConfigNode mergeValuesFrom(IConfigNode other);

}
