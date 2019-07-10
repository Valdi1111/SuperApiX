package org.valdi.SuperApiX.bukkit.nms.v1_10_R1;

import java.lang.reflect.Field;
import java.util.Map;

import org.bukkit.entity.EntityType;
import org.valdi.SuperApiX.bukkit.nms.base.AbstractCustomEntity;

import net.minecraft.server.v1_10_R1.Entity;
import net.minecraft.server.v1_10_R1.EntityTypes;
import net.minecraft.server.v1_10_R1.MinecraftKey;

public class CustomEntity<T extends Entity> extends AbstractCustomEntity<T, MinecraftKey> {

	public CustomEntity(String name, String oldName, int id, EntityType entityType, Class<T> nmsClass,
			Class<? extends T> customClass) {
		super(name, oldName, id, entityType, nmsClass, customClass);
		
        this.key = new MinecraftKey(name);
        this.oldKey = new MinecraftKey(oldName);
	}

	@Override
    public void register() {
	    Class<?>[] args = new Class[3];
	    args[0] = Class.class;
	    args[1] = String.class;
	    args[2] = Integer.TYPE;
	    Class<EntityTypes> entityTypeClass;

	    try {
	        entityTypeClass = EntityTypes.class;
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return;
	    }

	    try {
	        Field c = entityTypeClass.getDeclaredField("c");
	        Field d = entityTypeClass.getDeclaredField("d");
	        Field e = entityTypeClass.getDeclaredField("e");
	        Field f = entityTypeClass.getDeclaredField("f");
	        Field g = entityTypeClass.getDeclaredField("g");

	        c.setAccessible(true);
	        d.setAccessible(true);
	        e.setAccessible(true);
	        f.setAccessible(true);
	        g.setAccessible(true);

	        Map cMap = (Map) c.get(null);
	        Map dMap = (Map) d.get(null);
	        Map eMap = (Map) e.get(null);
	        Map fMap = (Map) f.get(null);
	        Map gMap = (Map) g.get(null);

	        cMap.put(name, customClass);
	        dMap.put(customClass, name);
	        eMap.put(id, customClass);
	        fMap.put(customClass, id);
	        gMap.put(name, id);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
    }

	@Override
    public void unregister() {
		;
    }

}
