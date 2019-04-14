package org.valdi.SuperApiX.bukkit.config.serializers;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.valdi.SuperApiX.common.config.SerializersRegister;

import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;

public class WorldUuidSerializer implements TypeSerializer<World>, SerializersRegister {

	@Override
	public World deserialize(TypeToken<?> type, ConfigurationNode node) throws ObjectMappingException {
		UUID world = node.getValue(TypeToken.of(UUID.class));
		return Bukkit.getWorld(world);
	}

	@Override
	public void serialize(TypeToken<?> type, World instance, ConfigurationNode node) throws ObjectMappingException {
		node.setValue(TypeToken.of(UUID.class), instance.getUID());
	}
	
	@Override
	public void register() {
		TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(World.class), this);
	}

}
