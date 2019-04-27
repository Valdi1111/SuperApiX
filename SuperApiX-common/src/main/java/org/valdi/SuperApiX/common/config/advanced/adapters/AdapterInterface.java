package org.valdi.SuperApiX.common.config.advanced.adapters;

import ninja.leaping.configurate.ConfigurationNode;

/**
 * Serialize S to ConfigurationNode
 * @author david
 *
 * @param <S> the type to serialize
 */
public interface AdapterInterface<S> {

    /**
     * Serialize object
     * @param node - the node to read from
     * @return instance of the serialized object
     */
    S deserialize(ConfigurationNode node);

    /**
     * Deserialize object
     * @param node - the node to write to
     * @param instance - object to deserialize
     */
    void serialize(ConfigurationNode node, S instance);
}
