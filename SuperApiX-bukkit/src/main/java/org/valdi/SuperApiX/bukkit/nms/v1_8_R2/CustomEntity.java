package org.valdi.SuperApiX.bukkit.nms.v1_8_R2;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.EntityType;
import org.valdi.SuperApiX.bukkit.nms.AbstractCustomEntity;

import net.minecraft.server.v1_8_R2.Entity;
import net.minecraft.server.v1_8_R2.EntityTypes;
import net.minecraft.server.v1_8_R2.MinecraftKey;

public class CustomEntity<T extends Entity> extends AbstractCustomEntity<T, MinecraftKey> {

	public CustomEntity(String name, String oldName, int id, EntityType entityType, Class<T> nmsClass,
			Class<? extends T> customClass) {
		super(name, oldName, id, entityType, nmsClass, customClass);
		
        this.key = new MinecraftKey(name);
        this.oldKey = new MinecraftKey(oldName);
	}

	@Override
	public void register() {
        try {     
            List<Map<?, ?>> dataMap = new ArrayList<Map<?, ?>>();
            for (Field f : EntityTypes.class.getDeclaredFields()){
                if (f.getType().getSimpleName().equals(Map.class.getSimpleName())){
                    f.setAccessible(true);
                    dataMap.add((Map<?, ?>) f.get(null));
                }
            }
     
            if (dataMap.get(2).containsKey(id)){
                dataMap.get(0).remove(name);
                dataMap.get(2).remove(id);
            }
     
            Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
            method.setAccessible(true);
            method.invoke(null, customClass, name, id);
     
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	@Override
	public void unregister() {
		;
	}

}
