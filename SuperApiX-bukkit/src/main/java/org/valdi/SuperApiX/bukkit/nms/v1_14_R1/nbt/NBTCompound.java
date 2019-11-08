package org.valdi.SuperApiX.bukkit.nms.v1_14_R1.nbt;

import net.minecraft.server.v1_14_R1.NBTBase;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.valdi.SuperApiX.bukkit.nms.base.nbt.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;

/**
 * Base class representing NMS Compounds. For a standalone implementation check
 * {@link NBTContainer}
 */
public class NBTCompound implements INbtCompound {
	private NBTCompound parent;
	private String name;

	protected NBTCompound(NBTCompound parent, String name) {
		this.parent = parent;
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public NBTTagCompound getHandle() {
		return getParent().getHandle();
	}

	protected void setHandle(NBTTagCompound compound) {
		getParent().setHandle(compound);
	}

	@Override
	public NBTCompound getParent() {
		return parent;
	}

	@Override
	public String getString(String key) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		return nbt.getString(key);
	}

	@Override
	public Integer getInteger(String key) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		return nbt.getInt(key);
	}

	@Override
	public Double getDouble(String key) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		return nbt.getDouble(key);
	}

	@Override
	public Byte getByte(String key) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		return nbt.getByte(key);
	}

	@Override
	public Short getShort(String key) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		return nbt.getShort(key);
	}

	@Override
	public Long getLong(String key) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		return nbt.getLong(key);
	}

	@Override
	public Float getFloat(String key) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		return nbt.getFloat(key);
	}

	@Override
	public Boolean getBoolean(String key) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		return nbt.getBoolean(key);
	}

	@Override
	public byte[] getByteArray(String key) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		return nbt.getByteArray(key);
	}

	@Override
	public int[] getIntArray(String key) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		return nbt.getIntArray(key);
	}

	@Override
	public <T> T getObject(String key, Class<T> type) {
		String json = getString(key);
		return GsonWrapper.deserialize(json, type);
	}

	@Override
	public void setString(String key, String value) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		nbt.setString(key, value);
		setHandle(root);
	}

	@Override
	public void setInteger(String key, Integer value) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		nbt.setInt(key, value);
		setHandle(root);
	}

	@Override
	public void setDouble(String key, Double value) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		nbt.setDouble(key, value);
		setHandle(root);
	}

	@Override
	public void setByte(String key, Byte value) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		nbt.setByte(key, value);
		setHandle(root);
	}

	@Override
	public void setShort(String key, Short value) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		nbt.setShort(key, value);
		setHandle(root);
	}

	@Override
	public void setLong(String key, Long value) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		nbt.setLong(key, value);
		setHandle(root);
	}

	@Override
	public void setFloat(String key, Float value) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		nbt.setFloat(key, value);
		setHandle(root);
	}

	@Override
	public void setBoolean(String key, Boolean value) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		nbt.setBoolean(key, value);
		setHandle(root);
	}

	@Override
	public void setByteArray(String key, byte[] value) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		nbt.setByteArray(key, value);
		setHandle(root);
	}

	@Override
	public void setIntArray(String key, int[] value) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		nbt.setIntArray(key, value);
		setHandle(root);
	}

	@Override
	public void setObject(String key, Object value) {
		String json = GsonWrapper.serialize(value);
		setString(key, json);
	}

	@Override
	public Boolean hasKey(String key) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		return nbt.hasKey(key);
	}

	@Override
	public void removeKey(String key) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		nbt.remove(key);
		setHandle(root);
	}

	@Override
	public Set<String> getKeys() {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		return nbt.getKeys();
	}

	@Override
	public <T extends INbtCompound> void mergeCompound(T comp) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		nbt.a((NBTTagCompound) comp.getHandle());
		setHandle(root);
	}

	protected NBTBase getBase(String key) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		return nbt.get(key);
	}

	protected void setBase(String key, NBTBase value) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		nbt.set(key, value);
		setHandle(root);
	}

	@Override
	public NBTCompound addCompound(String key) {
		if (getType(key) == NBTType.COMPOUND) {
			return getCompound(key);
		}
		this.setBase(key, new NBTTagCompound());
		NBTCompound comp = getCompound(key);
		if(comp == null) {
			throw new NbtApiException("Error while adding Compound, got null!");
		}
		return comp;
	}

	@Override
	public NBTCompound getCompound(String key) {
		if (getType(key) != NBTType.COMPOUND) {
			return null;
		}
		return new NBTCompound(this, key);
	}

	@Override
	public NBTList<String> getStringList(String key) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		return new NBTStringList(this, key, nbt.getList(key, NBTType.STRING.getId()));
	}

	@Override
	public NBTList<Integer> getIntegerList(String key) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		return new NBTIntegerList(this, key, nbt.getList(key, NBTType.INT.getId()));
	}

	@Override
	public NBTList<IListElement> getCompoundList(String key) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		return new NBTCompoundList(this, key, nbt.getList(key, NBTType.COMPOUND.getId()));
	}

	@Override
	public NBTType getType(String key) {
		NBTTagCompound root = getHandle();
		if(root == null) {
			root = new NBTTagCompound();
		}
		NBTTagCompound nbt = getToCompount(root, this);
		byte id = nbt.d(key);
		return NBTType.valueOf(id);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (String key : getKeys()) {
			result.append(toString(key));
		}
		return result.toString();
	}

	@Override
	public String toString(String key) {
		StringBuilder result = new StringBuilder();
		NBTCompound compound = this;
		while (compound.getParent() != null) {
			result.append("   ");
			compound = compound.getParent();
		}
		if (this.getType(key) == NBTType.COMPOUND) {
			return this.getCompound(key).toString();
		} else {
			return result + "-" + key + ": " + getBase(key) + System.lineSeparator();
		}
	}

	@Override
	public String asNBTString() {
		Object handle = getHandle();
		if (handle == null)
			return "{}";
		return handle.toString();
	}

	protected NBTTagCompound getToCompount(NBTTagCompound tag, NBTCompound compound) {
		Deque<String> structure = new ArrayDeque<>();
		while (compound.getParent() != null) {
			structure.add(compound.getName());
			compound = compound.getParent();
		}
		while (!structure.isEmpty()) {
			String target = structure.pollLast();
			tag = getSubNBTTagCompound(tag, target);
			if (tag == null) {
				throw new NbtApiException("Unable to find tag '" + target + "' in " + tag);
			}
		}
		return tag;
	}

	protected NBTTagCompound getSubNBTTagCompound(NBTTagCompound tag, String key) {
		if (tag.hasKey(key)) {
			return tag.getCompound(key);
		} else {
			throw new NbtApiException("Tried getting invalide compound '" + key + "' from '" + tag + "'!");
		}
	}

}
