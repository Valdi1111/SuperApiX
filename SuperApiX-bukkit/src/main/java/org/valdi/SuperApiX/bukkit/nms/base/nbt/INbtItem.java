package org.valdi.SuperApiX.bukkit.nms.base.nbt;

import org.bukkit.inventory.ItemStack;

/**
 * NBT class to access vanilla/custom tags on ItemStacks. This class doesn't
 * autosave to the Itemstack, use getItem to get the changed ItemStack.
 */
public interface INbtItem extends INbtCompound {

    /**
     * @return The modified ItemStack
     */
    ItemStack getItem();

    /**
     * This may return true even when the NBT is empty.
     *
     * @return Does the ItemStack have a NBTCompound.
     */
    boolean hasNBTData();

}
