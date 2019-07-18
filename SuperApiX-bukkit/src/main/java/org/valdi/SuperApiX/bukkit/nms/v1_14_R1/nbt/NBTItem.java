package org.valdi.SuperApiX.bukkit.nms.v1_14_R1.nbt;

import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.valdi.SuperApiX.bukkit.nms.base.nbt.INbtItem;
import org.valdi.SuperApiX.bukkit.nms.base.nbt.NbtApiException;

public class NBTItem extends NBTCompound implements INbtItem {
	private ItemStack stack;

	public NBTItem(ItemStack stack) {
		super(null, null);

		if (stack == null) {
			throw new NbtApiException("ItemStack can't be null!");
		}
		this.stack = stack.clone();
	}

	public NBTItem(NBTCompound nbt) {
		super(null, null);

		if(nbt == null) {
			throw new NbtApiException("Unable to initialize null nbt!");
		}
		this.init(nbt.getHandle());
	}

	public NBTItem(NBTTagCompound nbt) {
		super(null, null);

		this.init(nbt);
	}

	protected void init(NBTTagCompound nbt) {
		if(nbt == null) {
			throw new NbtApiException("Unable to initialize null nbt!");
		}
		net.minecraft.server.v1_14_R1.ItemStack nmsStack;
		try {
			nmsStack = net.minecraft.server.v1_14_R1.ItemStack.a(nbt);
		} catch(Exception e) {
			throw new org.valdi.SuperApiX.bukkit.nms.base.nbt.NbtApiException(e);
		}
		this.stack = CraftItemStack.asCraftMirror(nmsStack);
	}

	@Override
	public NBTTagCompound getHandle() {
		net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
		NBTTagCompound stackTag = nmsStack.getTag();
		return stackTag != null ? stackTag : new NBTTagCompound();
	}

	@Override
	protected void setHandle(NBTTagCompound compound) {
		net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
		nmsStack.setTag(compound);
		this.stack = CraftItemStack.asCraftMirror(nmsStack);
	}

	@Override
	public ItemStack getItem() {
		return stack;
	}

	protected void setItem(ItemStack stack) {
		this.stack = stack;
	}

	@Override
	public boolean hasNBTData() {
		return getHandle() != null && !getHandle().isEmpty();
	}

}
