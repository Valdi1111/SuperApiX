package org.valdi.SuperApiX.bukkit.config.serializers;

import org.bukkit.util.Vector;
import org.valdi.SuperApiX.common.config.SerializersRegister;

import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;

public class VectorSerializer implements TypeSerializer<Vector>, SerializersRegister {

	@Override
	public Vector deserialize(TypeToken<?> type, ConfigurationNode node) throws ObjectMappingException {
		double x = node.getNode("x").getDouble();
		double y = node.getNode("y").getDouble();
		double z = node.getNode("z").getDouble();
		
		return new Vector(x, y, z);
	}

	@Override
	public void serialize(TypeToken<?> type, Vector instance, ConfigurationNode node) throws ObjectMappingException {
		node.getNode("x").setValue(instance.getX());
		node.getNode("y").setValue(instance.getY());
		node.getNode("z").setValue(instance.getZ());
	}
	
	@Override
	public void register() {
		TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Vector.class), this);
	}

}
