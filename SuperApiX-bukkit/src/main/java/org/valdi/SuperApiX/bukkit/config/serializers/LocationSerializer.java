package org.valdi.SuperApiX.bukkit.config.serializers;

import org.bukkit.Location;
import org.bukkit.World;
import org.valdi.SuperApiX.common.config.SerializersRegister;

import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;

public class LocationSerializer implements TypeSerializer<Location>, SerializersRegister {

	@Override
	public Location deserialize(TypeToken<?> type, ConfigurationNode node) throws ObjectMappingException {
		World world = node.getNode("world").getValue(TypeToken.of(World.class));
		double x = node.getNode("x").getDouble();
		double y = node.getNode("y").getDouble();
		double z = node.getNode("z").getDouble();
		float yaw = node.getNode("yaw").getFloat();
		float pitch = node.getNode("pitch").getFloat();
		
		return new Location(world, x, y, z, yaw, pitch);
	}

	@Override
	public void serialize(TypeToken<?> type, Location instance, ConfigurationNode node) throws ObjectMappingException {
		node.getNode("world").setValue(TypeToken.of(World.class), instance.getWorld());
		node.getNode("x").setValue(instance.getX());
		node.getNode("y").setValue(instance.getY());
		node.getNode("z").setValue(instance.getZ());
		node.getNode("yaw").setValue(instance.getYaw());
		node.getNode("pitch").setValue(instance.getPitch());
	}
	
	@Override
	public void register() {
		TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Location.class), this);
	}

}
