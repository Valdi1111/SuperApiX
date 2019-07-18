package org.valdi.SuperApiX.bukkit.nms.v1_14_R1.nbt;

import net.minecraft.server.v1_14_R1.MojangsonParser;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.valdi.SuperApiX.bukkit.nms.base.nbt.INbtContainer;
import org.valdi.SuperApiX.bukkit.nms.nbt.NbtApiException;

public class NBTContainer extends NBTCompound implements INbtContainer {
	private NBTTagCompound nbt;

	public NBTContainer() {
		super(null, null);

		this.nbt = new NBTTagCompound();
	}

	public NBTContainer(NBTTagCompound nbt) {
		super(null, null);

		if(nbt == null) {
			throw new NbtApiException("Unable to initialize null nbt!");
		}
		this.nbt = nbt;
	}

	public NBTContainer(String nbtString) {
		super(null, null);

		try {
			this.nbt = MojangsonParser.parse(nbtString);
		} catch (Exception e) {
			throw new NbtApiException("Unable to parse Malformed Json!", e);
		}
	}

	@Override
	public NBTTagCompound getHandle() {
		return nbt;
	}

	@Override
	protected void setHandle(NBTTagCompound tag) {
		this.nbt = tag;
	}

}
