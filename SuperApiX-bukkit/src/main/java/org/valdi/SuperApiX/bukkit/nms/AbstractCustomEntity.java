package org.valdi.SuperApiX.bukkit.nms;

import org.bukkit.entity.EntityType;

public abstract class AbstractCustomEntity<T, K> implements ICustomEntity<T, K> {
    protected final String name;
    protected final String oldName;
    protected final int id;
    protected final EntityType entityType;
    protected final Class<T> nmsClass;
    protected final Class<? extends T> customClass;
    protected volatile K key;
    protected volatile K oldKey;
	
	protected AbstractCustomEntity(String name, String oldName, int id, EntityType entityType, Class<T> nmsClass, Class<? extends T> customClass) {
        this.name = name;
        this.oldName = oldName;
        this.id = id;
        this.entityType = entityType;
        this.nmsClass = nmsClass;
        this.customClass = customClass;
	}

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSuperName() {
        return oldName;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public EntityType getEntityType() {
        return entityType;
    }

    @Override
    public Class<T> getSuperClass() {
        return nmsClass;
    }

    @Override
    public Class<? extends T> getCustomClass() {
        return customClass;
    }

    @Override
    public K getSuperKey() {
    	return oldKey;
    }
    
    @Override
    public K getCustomKey() {
    	return key;
    }

}
