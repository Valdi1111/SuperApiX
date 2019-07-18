package org.valdi.SuperApiX.bukkit.nms.v1_14_R1.nbt;

import net.minecraft.server.v1_14_R1.NBTBase;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.NBTTagList;
import org.apache.commons.lang.NotImplementedException;
import org.valdi.SuperApiX.bukkit.nms.base.nbt.IListElement;
import org.valdi.SuperApiX.bukkit.nms.base.nbt.NBTType;
import org.valdi.SuperApiX.bukkit.nms.base.nbt.NbtApiException;

/**
 * {@link NBTListCompound} implementation for NBTLists
 */
public class NBTCompoundList extends NBTList<IListElement> {

	protected NBTCompoundList(NBTCompound parent, String name, NBTTagList list) {
		super(parent, name, NBTType.COMPOUND, list);
	}

	/**
	 * Adds a new Compound to the end of the List and returns it.
	 * 
	 * @return The added {@link NBTListCompound}
	 */
	public IListElement addCompound() {
		try {
			NBTTagCompound compound = new NBTTagCompound();
			nbtList.add(size(), compound);
			return new NBTListCompound(this, compound);
		} catch (Exception ex) {
			throw new NbtApiException(ex);
		}
	}

	/**
	 * Adds a new Compound to the end of the List.
	 * 
	 * @param empty This has to be null!
	 * @return True, if compound was added
	 */
	@Override
	public boolean add(IListElement empty) {
		if (empty != null) {
			throw new NotImplementedException("You need to pass null! ListCompounds from other lists won't work.");
		}
		try {
			nbtList.add(0, new NBTTagCompound());
			return true;
		} catch (Exception ex) {
			throw new NbtApiException(ex);
		}
	}

	@Override
	public void add(int index, IListElement empty) {
		if (empty != null) {
			throw new NotImplementedException("You need to pass null! ListCompounds from other lists won't work.");
		}
		try {
			nbtList.add(index, new NBTTagCompound());
		} catch (Exception ex) {
			throw new NbtApiException(ex);
		}
	}

	@Override
	public NBTListCompound get(int index) {
		try {
			NBTTagCompound base = nbtList.getCompound(index);
			return new NBTListCompound(this, base);
		} catch (Exception ex) {
			throw new NbtApiException(ex);
		}
	}

	@Override
	public NBTListCompound set(int index, IListElement element) {
		throw new NotImplementedException("This method doesn't work in the ListCompound context.");
	}

	@Override
	protected NBTBase asTag(IListElement object) {
		return null;
	}

}
