package org.valdi.SuperApiX.bukkit.nms.v1_14_R1.nbt;

import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.valdi.SuperApiX.bukkit.nms.base.nbt.INbtEntity;
import org.valdi.SuperApiX.bukkit.nms.base.nbt.NbtApiException;

public class NBTEntity extends NBTCompound implements INbtEntity {
	private final Entity entity;

	public NBTEntity(Entity entity) {
		super(null, null);

		if (entity == null) {
			throw new NbtApiException("Entity can't be null!");
		}
		this.entity = entity;
	}

	@Override
	public NBTTagCompound getHandle() {
		CraftEntity cEntity = (CraftEntity) entity;
		net.minecraft.server.v1_14_R1.Entity nmsEntity = cEntity.getHandle();
		NBTTagCompound tag = nmsEntity.save(new NBTTagCompound());
		return tag != null ? tag : new NBTTagCompound();
	}

	@Override
	protected void setHandle(NBTTagCompound compound) {
		CraftEntity cEntity = (CraftEntity) entity;
		net.minecraft.server.v1_14_R1.Entity nmsEntity = cEntity.getHandle();
		nmsEntity.f(compound);
	}

	@Override
	public Entity getEntity() {
		return entity;
	}
}
