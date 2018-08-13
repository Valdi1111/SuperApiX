package org.valdi.SuperApiX.bukkit.nms.v1_13_R1;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.EntityType;

import net.minecraft.server.v1_13_R1.EntityInsentient;
import net.minecraft.server.v1_13_R1.EntityTypes;
import net.minecraft.server.v1_13_R1.EntityTypes.a;
import net.minecraft.server.v1_13_R1.World;

public class CustomEntities<T extends EntityInsentient> {

    private String name;
    private int id;
    private EntityType entityType;
    private Class<T> nmsClass;
    private Class<? extends T> customClass;
    private String key;
	
	public CustomEntities(String name, int id, EntityType entityType, Class<T> nmsClass, Class<? extends T> customClass) {
        this.name = name;
        this.id = id;
        this.entityType = entityType;
        this.nmsClass = nmsClass;
        this.customClass = customClass;
        this.key = name;
	}

    public void register() {
    	EntityTypes.a(key, a.a(customClass, world -> {
			try {
				return customClass.getConstructor(World.class).newInstance(world);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}));
    }

    public void unregister() {
    	//EntityTypes.REGISTRY;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return id;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public Class<?> getCustomClass() {
        return customClass;
    }

}
