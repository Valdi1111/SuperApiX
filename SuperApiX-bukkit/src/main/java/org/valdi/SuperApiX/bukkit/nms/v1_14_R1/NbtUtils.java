package org.valdi.SuperApiX.bukkit.nms.v1_14_R1;

import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.valdi.SuperApiX.bukkit.nms.base.INbtUtils;
import org.valdi.SuperApiX.bukkit.nms.base.nbt.*;
import org.valdi.SuperApiX.bukkit.nms.v1_14_R1.nbt.*;

import java.io.File;
import java.io.IOException;

public class NbtUtils implements INbtUtils {

    @Override
    public INbtContainer getNbtContainer() {
        return new NBTContainer();
    }

    @Override
    public INbtContainer getNbtContainer(Object tag) {
        return new NBTContainer((NBTTagCompound) tag);
    }

    @Override
    public INbtContainer getNbtContainer(String rawTag) {
        return new NBTContainer(rawTag);
    }

    @Override
    public INbtFile getNbtFile(File file) throws IOException {
        return new NBTFile(file);
    }

    @Override
    public INbtItem getNbtItem(ItemStack item) {
        return new NBTItem(item);
    }

    @Override
    public INbtItem getNbtItem(INbtContainer tag) {
        return new NBTItem((NBTCompound) tag);
    }

    @Override
    public INbtItem getNbtItem(Object tag) {
        return new NBTItem((NBTTagCompound) tag);
    }

    @Override
    public INbtTileEntity getNbtTileEntity(BlockState tile) {
        return new NBTTileEntity(tile);
    }

    @Override
    public INbtEntity getNbtEntity(Entity entity) {
        return new NBTEntity(entity);
    }

}
