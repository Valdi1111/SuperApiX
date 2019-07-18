package org.valdi.SuperApiX.bukkit.nms.base.nbt;

import org.bukkit.block.BlockState;

/**
 * NBT class to access vanilla tags from TileEntities. TileEntities don't
 * support custom tags. Use the NBTInjector for custom tags. Changes will be
 * instantly applied to the Tile, use the merge method to do many things at
 * once.
 */
public interface INbtTileEntity extends INbtCompound {

    BlockState getTileEntity();

}
