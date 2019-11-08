package org.valdi.SuperApiX.bukkit.nms.v1_14_R1.nbt;

import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.valdi.SuperApiX.bukkit.nms.base.nbt.IListElement;
import org.valdi.SuperApiX.bukkit.nms.base.nbt.NbtApiException;

import java.util.Set;

/**
 * Cut down version of the {@link NBTCompound} for inside
 * {@link NBTCompoundList} This Compound implementation is missing the ability
 * for further subCompounds and Lists. This class probably will change in the
 * future
 */
public class NBTListCompound implements IListElement {
	private NBTList<IListElement> parent;
	private NBTTagCompound tag;

	protected NBTListCompound(NBTList<IListElement> parent, NBTTagCompound tag) {
		this.parent = parent;
		this.tag = tag;
	}

	@Override
	public String getString(String key) {
		try {
			return tag.getString(key);
		} catch (Exception ex) {
			throw new NbtApiException(ex);
		}
	}

	@Override
	public int getInteger(String value) {
		try {
			return tag.getInt(value);
		} catch (Exception ex) {
			throw new NbtApiException(ex);
		}
	}

	@Override
	public double getDouble(String key) {
		try {
			return tag.getDouble(key);
		} catch (Exception ex) {
			throw new NbtApiException(ex);
		}
	}

	@Override
	public void setString(String key, String value) {
		if (value == null) {
			remove(key);
			return;
		}
		try {
			tag.setString(key, value);
			parent.save();
		} catch (Exception ex) {
			throw new NbtApiException(ex);
		}
	}

	@Override
	public void setInteger(String key, int value) {
		try {
			tag.setInt(key, value);
			parent.save();
		} catch (Exception ex) {
			throw new NbtApiException(ex);
		}
	}

	@Override
	public void setDouble(String key, double value) {
		try {
			tag.setDouble(key, value);
			parent.save();
		} catch (Exception ex) {
			throw new NbtApiException(ex);
		}
	}

	@Override
	public boolean hasKey(String key) {
		try {
			return tag.hasKey(key);
		} catch (Exception ex) {
			throw new NbtApiException(ex);
		}
	}

	@Override
	public Set<String> getKeys() {
		try {
			return tag.getKeys();
		} catch (Exception ex) {
			throw new NbtApiException(ex);
		}
	}

	@Override
	public void remove(String key) {
		try {
			tag.remove(key);
		} catch (Exception ex) {
			throw new NbtApiException(ex);
		}
	}

}
