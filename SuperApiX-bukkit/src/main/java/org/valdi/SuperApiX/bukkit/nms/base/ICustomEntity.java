package org.valdi.SuperApiX.bukkit.nms.base;

import org.bukkit.entity.EntityType;

public interface ICustomEntity<T, K> {

    String getName();

    String getSuperName();

    int getId();

    EntityType getEntityType();

    Class<T> getSuperClass();

    Class<? extends T> getCustomClass();
    
    K getSuperKey();
    
    K getCustomKey();

    void register();

    void unregister();

}
