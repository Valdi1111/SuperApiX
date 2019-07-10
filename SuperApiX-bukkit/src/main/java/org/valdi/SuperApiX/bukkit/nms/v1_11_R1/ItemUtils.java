package org.valdi.SuperApiX.bukkit.nms.v1_11_R1;

import net.minecraft.server.v1_11_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_11_R1.NBTTagCompound;
import net.minecraft.server.v1_11_R1.NBTTagList;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.nms.base.AbstractNmsProvider;
import org.valdi.SuperApiX.bukkit.nms.base.IItemUtils;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.math.BigInteger;

public class ItemUtils extends AbstractNmsProvider implements IItemUtils {

	public ItemUtils(SuperApiBukkit plugin) {
		super(plugin);
	}

	@Override
	public String toBase64(ItemStack item) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutput = new DataOutputStream(outputStream);

		NBTTagList nbtTagListItems = new NBTTagList();
		NBTTagCompound nbtTagCompoundItem = new NBTTagCompound();

		net.minecraft.server.v1_11_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

		nmsItem.save(nbtTagCompoundItem);

		nbtTagListItems.add(nbtTagCompoundItem);

		try {
			NBTCompressedStreamTools.a(nbtTagCompoundItem, (DataOutput) dataOutput);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new BigInteger(1, outputStream.toByteArray()).toString(32);
	}

	@Override
	public ItemStack fromBase64(String data) {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(new BigInteger(data, 32).toByteArray());

		NBTTagCompound nbtTagCompoundRoot = null;
		try {
			nbtTagCompoundRoot = NBTCompressedStreamTools.a(new DataInputStream(inputStream));
		} catch (IOException e) {
			e.printStackTrace();
		}

		net.minecraft.server.v1_11_R1.ItemStack nmsItem = new net.minecraft.server.v1_11_R1.ItemStack(nbtTagCompoundRoot);
		ItemStack item = CraftItemStack.asBukkitCopy(nmsItem);

		return item;
	}

	@Override
	public String toBase64List(ItemStack[] items) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		BukkitObjectOutputStream dataOutput;
		try {
			dataOutput = new BukkitObjectOutputStream(outputStream);

			// Content Size
			// Contents
			dataOutput.writeInt(items.length);

			int index = 0;
			for (ItemStack is : items) {
				if (is != null && is.getType() != Material.AIR) {
					dataOutput.writeObject(toBase64(is));
				} else {
					dataOutput.writeObject(null);
				}
				dataOutput.writeInt(index);
				index++;
			}
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save item stacks.", e);
		}
	}

	@Override
	public ItemStack[] fromBase64List(String items) {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(items));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

			int size = dataInput.readInt();

			ItemStack[] list = new ItemStack[size];
			// Read the serialized inventory
			for (int i = 0; i < size; i++) {
				Object utf = dataInput.readObject();
				int slot = dataInput.readInt();
				if (utf == null) { // yey²?

				} else {
					list[slot] = fromBase64((String) utf);
				}
			}

			dataInput.close();
			return list;
		} catch (Exception e) {
			throw new IllegalStateException("Unable to load item stacks.", e);
		}
	}

}
