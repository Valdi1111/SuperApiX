package org.valdi.SuperApiX.bukkit.nms.v1_14_R1.nbt;

import net.minecraft.server.v1_14_R1.BlockPosition;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.TileEntity;
import net.minecraft.server.v1_14_R1.World;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.valdi.SuperApiX.bukkit.nms.base.nbt.INbtTileEntity;
import org.valdi.SuperApiX.bukkit.nms.base.nbt.NbtApiException;

public class NBTTileEntity extends NBTCompound implements INbtTileEntity {
	private final BlockState tile;

	public NBTTileEntity(BlockState tile) {
		super(null, null);

		if (tile == null) {
			throw new NbtApiException("TileEntity can't be null!");
		}
		this.tile = tile;
	}

	@Override
	public NBTTagCompound getHandle() {
		BlockPosition pos = new BlockPosition(tile.getX(), tile.getY(), tile.getZ());
		CraftWorld world = (CraftWorld) tile.getWorld();
		World nmsWorld = world.getHandle();
		TileEntity tileEntity = nmsWorld.getTileEntity(pos);
		NBTTagCompound tag = tileEntity.save(new NBTTagCompound());
		return tag != null ? tag : new NBTTagCompound();
	}

	@Override
	protected void setHandle(NBTTagCompound compound) {
		BlockPosition pos = new BlockPosition(tile.getX(), tile.getY(), tile.getZ());
		CraftWorld world = (CraftWorld) tile.getWorld();
		World nmsWorld = world.getHandle();
		TileEntity tileEntity = nmsWorld.getTileEntity(pos);
		tileEntity.load(compound);
	}

	@Override
	public BlockState getTileEntity() {
		return tile;
	}
}
