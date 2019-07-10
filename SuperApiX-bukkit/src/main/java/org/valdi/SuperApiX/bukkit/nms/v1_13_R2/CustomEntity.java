package org.valdi.SuperApiX.bukkit.nms.v1_13_R2;

import java.util.Map;

import org.bukkit.entity.EntityType;
import org.valdi.SuperApiX.bukkit.nms.base.AbstractCustomEntity;

import com.mojang.datafixers.types.Type;

import net.minecraft.server.v1_13_R2.DataConverterRegistry;
import net.minecraft.server.v1_13_R2.DataConverterTypes;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.MinecraftKey;
import net.minecraft.server.v1_13_R2.World;

public class CustomEntity<T extends Entity> extends AbstractCustomEntity<T, MinecraftKey> {

	public CustomEntity(String name, String oldName, int id, EntityType entityType, Class<T> nmsClass,
			Class<? extends T> customClass) {
		super(name, oldName, id, entityType, nmsClass, customClass);
		
        this.key = new MinecraftKey(name);
        this.oldKey = new MinecraftKey(oldName);
	}

    public void register() {
        // get the server's datatypes (also referred to as "data fixers" by some)
        // I still don't know where 15190 came from exactly, when a few of us
        // put our heads together that's the number someone else came up with
        Map<Object, Type<?>> dataTypes = (Map<Object, Type<?>>) DataConverterRegistry.a().getSchema(15190).findChoiceType(DataConverterTypes.n).types();
        // inject the new custom entity (this registers the
        // name/id with the server so you can use it in things
        // like the vanilla /summon command)
        dataTypes.put(key.toString(), dataTypes.get(oldKey.toString()));
        // create and return an EntityTypes for the custom entity
        // store this somewhere so you can reference it later (like for spawning)
        EntityTypes.a(name, EntityTypes.a.a(customClass, (world) -> {
			try {
				return customClass.getConstructor(World.class).newInstance(world);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
        }));
    }

    public void unregister() {
    	;
    }

}
