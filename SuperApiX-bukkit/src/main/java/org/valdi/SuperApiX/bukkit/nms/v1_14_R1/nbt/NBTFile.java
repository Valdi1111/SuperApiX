package org.valdi.SuperApiX.bukkit.nms.v1_14_R1.nbt;

import net.minecraft.server.v1_14_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.valdi.SuperApiX.bukkit.nms.base.nbt.INbtFile;
import org.valdi.SuperApiX.bukkit.nms.base.nbt.NbtApiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class NBTFile extends NBTCompound implements INbtFile {
	private NBTTagCompound nbt;
	private final File file;

	public NBTFile(File file) throws IOException {
		super(null, null);

		if (file == null) {
			throw new NbtApiException("File can't be null!");
		}
		this.file = file;
		if (file.exists()) {
			FileInputStream is = new FileInputStream(file);
			this.nbt = NBTCompressedStreamTools.a(is);
			return;
		}

		this.nbt = new NBTTagCompound();
		save();
	}

	@Override
	public NBTTagCompound getHandle() {
		return nbt;
	}

	@Override
	protected void setHandle(NBTTagCompound compound) {
		nbt = compound;
	}

	@Override
	public void save() throws IOException {
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			if (!file.createNewFile())
				throw new IOException("Unable to create file at " + file.getAbsolutePath());
		}
		FileOutputStream os = new FileOutputStream(file);
		NBTCompressedStreamTools.a(nbt, os);
	}

	@Override
	public File getFile() {
		return file;
	}

}
