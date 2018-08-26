package org.valdi.SuperApiX.bukkit.nms.v1_12_R1;

import org.bukkit.entity.EntityType;

import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityTypes;
import net.minecraft.server.v1_12_R1.MinecraftKey;

public class CustomEntities<T extends Entity> {

    private String name;
    private int id;
    private EntityType entityType;
    private Class<T> nmsClass;
    private Class<? extends T> customClass;
    private MinecraftKey key;
    private MinecraftKey oldKey;
	
	public CustomEntities(String name, int id, EntityType entityType, Class<T> nmsClass, Class<? extends T> customClass) {
        this.name = name;
        this.id = id;
        this.entityType = entityType;
        this.nmsClass = nmsClass;
        this.customClass = customClass;
        this.key = new MinecraftKey(name);
        this.oldKey = EntityTypes.b.b(nmsClass);
	}

    public void register() {
        EntityTypes.d.add(key);
        EntityTypes.b.a(id, key, customClass);
    }

    public void unregister() {
        EntityTypes.d.remove(key);
        EntityTypes.b.a(id, oldKey, nmsClass);
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
