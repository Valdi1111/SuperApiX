package org.valdi.SuperApiX.common.config.advanced.adapters;

import ninja.leaping.configurate.ConfigurationNode;

/**
 * Convert from to S or to V
 * @author david
 *
 * @param <S>
 * @param <V>
 */
public interface AdapterInterface<S> {

    /**
     * Serialize object
     * @param object - object to serialize
     * @return serialized object
     */
    S deserialize(ConfigurationNode node);

    /**
     * Deserialize object
     * @param object - object to deserialize
     * @return deserialized object
     */
    void serialize(ConfigurationNode node, S instance);
}
