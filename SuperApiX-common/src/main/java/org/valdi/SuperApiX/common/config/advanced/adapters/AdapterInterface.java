package org.valdi.SuperApiX.common.config.advanced.adapters;

import org.valdi.SuperApiX.common.config.types.nodes.IConfigNode;

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
    S deserialize(IConfigNode node);

    /**
     * Deserialize object
     * @param node - the node to write to
     * @param instance - object to deserialize
     */
    void serialize(IConfigNode node, S instance);
}
