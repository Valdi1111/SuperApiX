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
	public Vector deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
		double x = value.getNode("x").getDouble();
		double y = value.getNode("y").getDouble();
		double z = value.getNode("z").getDouble();
		
		return new Vector(x, y, z);
	}

	@Override
	public void serialize(TypeToken<?> type, Vector obj, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("x").setValue(obj.getX());
        value.getNode("y").setValue(obj.getY());
        value.getNode("z").setValue(obj.getZ());
	}
	
	@Override
	public void register() {
		TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Vector.class), this);
	}

}
