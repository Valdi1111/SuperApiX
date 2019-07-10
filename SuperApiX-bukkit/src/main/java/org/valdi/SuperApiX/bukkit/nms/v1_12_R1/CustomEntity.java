package org.valdi.SuperApiX.bukkit.nms.v1_12_R1;

import org.bukkit.entity.EntityType;
import org.valdi.SuperApiX.bukkit.nms.base.AbstractCustomEntity;

import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityTypes;
import net.minecraft.server.v1_12_R1.MinecraftKey;

public class CustomEntity<T extends Entity> extends AbstractCustomEntity<T, MinecraftKey> {

	public CustomEntity(String name, String oldName, int id, EntityType entityType, Class<T> nmsClass,
			Class<? extends T> customClass) {
		super(name, oldName, id, entityType, nmsClass, customClass);
		
        this.key = new MinecraftKey(name);
        this.oldKey = EntityTypes.b.b(nmsClass);
	}

	@Override
    public void register() {
        EntityTypes.d.add(key);
        EntityTypes.b.a(id, key, customClass);
    }

	@Override
    public void unregister() {
        EntityTypes.d.remove(key);
        EntityTypes.b.a(id, oldKey, nmsClass);
    }

}
