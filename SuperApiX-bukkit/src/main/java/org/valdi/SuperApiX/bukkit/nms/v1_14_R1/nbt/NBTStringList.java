package org.valdi.SuperApiX.bukkit.nms.v1_14_R1.nbt;

import net.minecraft.server.v1_14_R1.NBTBase;
import net.minecraft.server.v1_14_R1.NBTTagList;
import net.minecraft.server.v1_14_R1.NBTTagString;
import org.valdi.SuperApiX.bukkit.nms.base.nbt.NBTType;
import org.valdi.SuperApiX.bukkit.nms.base.nbt.NbtApiException;

/**
 * String implementation for NBTLists
 */
public class NBTStringList extends NBTList<String> {

	protected NBTStringList(NBTCompound parent, String name, NBTTagList list) {
		super(parent, name, NBTType.STRING, list);
	}

	@Override
	public String get(int index) {
		try {
			return nbtList.getString(index);
		} catch (Exception ex) {
			throw new NbtApiException(ex);
		}
	}

	@Override
	protected NBTBase asTag(String value) {
		return new NBTTagString(value);
	}

}
