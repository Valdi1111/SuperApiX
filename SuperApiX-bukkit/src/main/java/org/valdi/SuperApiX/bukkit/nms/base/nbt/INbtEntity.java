package org.valdi.SuperApiX.bukkit.nms.base.nbt;

import org.bukkit.entity.Entity;

/**
 * NBT class to access vanilla tags from Entities. Entities don't support custom
 * tags. Use the NBTInjector for custom tags. Changes will be instantly applied
 * to the Entity, use the merge method to do many things at once.
 */
public interface INbtEntity extends INbtCompound {

    Entity getEntity();

}
