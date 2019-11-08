package org.valdi.SuperApiX.bukkit.nms.v1_14_R1.nbt;

import net.minecraft.server.v1_14_R1.NBTBase;
import net.minecraft.server.v1_14_R1.NBTTagList;
import org.valdi.SuperApiX.bukkit.nms.base.nbt.AbstractNbtList;
import org.valdi.SuperApiX.bukkit.nms.base.nbt.NBTType;
import org.valdi.SuperApiX.bukkit.nms.base.nbt.NbtApiException;

/**
 * Abstract List implementation for ListCompounds
 *
 * @param <T>
 */
public abstract class NBTList<T> extends AbstractNbtList<T> {
	protected NBTCompound parent;
	protected NBTTagList nbtList;

	protected NBTList(NBTCompound parent, String name, NBTType type, NBTTagList nbtList) {
		super(name, type);

		this.parent = parent;
		this.nbtList = nbtList;
	}

	protected void save() {
		parent.setBase(name, nbtList);
	}

	protected abstract NBTBase asTag(T object);

	@Override
	public NBTTagList getHandle() {
		return nbtList;
	}

	@Override
	public boolean add(T element) {
		try {
			nbtList.add(size(), asTag(element));
			save();
			return true;
		} catch (Exception ex) {
			throw new NbtApiException(ex);
		}
	}

	@Override
	public void add(int index, T element) {
		try {
			nbtList.add(index, asTag(element));
			save();
		} catch (Exception ex) {
			throw new NbtApiException(ex);
		}
	}

	@Override
	public T set(int index, T element) {
		try {
			T prev = get(index);
			nbtList.set(index, asTag(element));
			save();
			return prev;
		} catch (Exception ex) {
			throw new NbtApiException(ex);
		}
	}

	@Override
	public T remove(int i) {
		try {
			T old = get(i);
			nbtList.remove(i);
			save();
			return old;
		} catch (Exception ex) {
			throw new NbtApiException(ex);
		}
	}

	@Override
	public int size() {
		try {
			return nbtList.size();
		} catch (Exception ex) {
			throw new NbtApiException(ex);
		}
	}

}
