package org.valdi.SuperApiX.bukkit.nms.base.nbt;

/**
 * Enum of all NBT Types Minecraft contains.
 */
public enum NBTType {
	END(0),
    BYTE(1),
	SHORT(2),
	INT(3),
	LONG(4),
	FLOAT(5),
	DOUBLE(6),
	BYTE_ARRAY(7),
	INT_ARRAY(11),
	STRING(8),
	LIST(9),
	COMPOUND(10);

	private final int id;

	NBTType(int id) {
		this.id = id;
	}

	/**
	 * @return id used by Minecraft internally
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id internal Minecraft id
	 * @return enum representing the id, NBTType.END for invalid ids
	 */
	public static NBTType valueOf(int id) {
		for (NBTType t : values())
			if (t.getId() == id)
				return t;
		return NBTType.END;
	}

}
