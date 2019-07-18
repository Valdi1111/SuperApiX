package org.valdi.SuperApiX.bukkit.nms.v1_14_R1.nbt;

import net.minecraft.server.v1_14_R1.NBTBase;
import net.minecraft.server.v1_14_R1.NBTTagInt;
import net.minecraft.server.v1_14_R1.NBTTagList;
import org.valdi.SuperApiX.bukkit.nms.base.nbt.NBTType;
import org.valdi.SuperApiX.bukkit.nms.nbt.NbtApiException;

/**
 * Integer implementation for NBTLists
 */
public class NBTIntegerList extends NBTList<Integer> {

	protected NBTIntegerList(NBTCompound parent, String name, NBTTagList list) {
		super(parent, name, NBTType.INT, list);
	}

	@Override
	public Integer get(int index) {
		try {
			return Integer.valueOf(nbtList.get(index).toString());
		} catch (NumberFormatException nf) {
			return 0;
		} catch (Exception ex) {
			throw new NbtApiException(ex);
		}
	}

	@Override
	protected NBTBase asTag(Integer value) {
		return new NBTTagInt(value);
	}

}
