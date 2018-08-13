package org.valdi.SuperApiX.bukkit.config.serializers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.valdi.SuperApiX.common.config.SerializersRegister;

import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;

public class LocationSerializer implements TypeSerializer<Location>, SerializersRegister {

	@Override
	public Location deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
		String world = value.getNode("world").getString();
		double x = value.getNode("x").getDouble();
		double y = value.getNode("y").getDouble();
		double z = value.getNode("z").getDouble();
		float yaw = value.getNode("yaw").getFloat();
		float pitch = value.getNode("pitch").getFloat();
		
		return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
	}

	@Override
	public void serialize(TypeToken<?> type, Location obj, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("world").setValue(obj.getWorld().getName());
        value.getNode("x").setValue(obj.getX());
        value.getNode("y").setValue(obj.getY());
        value.getNode("z").setValue(obj.getZ());
        value.getNode("yaw").setValue(obj.getYaw());
        value.getNode("pitch").setValue(obj.getPitch());
	}
	
	@Override
	public void register() {
		TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Location.class), this);
	}

}
