package org.valdi.SuperApiX.bukkit.nms.base;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.valdi.SuperApiX.bukkit.nms.base.nbt.*;

import java.io.File;
import java.io.IOException;

public interface INbtUtils {

    INbtContainer getNbtContainer();

    INbtContainer getNbtContainer(Object tag);

    INbtContainer getNbtContainer(String rawTag);

    INbtFile getNbtFile(File file) throws IOException;

    INbtItem getNbtItem(ItemStack item);

    INbtItem getNbtItem(INbtContainer tag);

    INbtItem getNbtItem(Object tag);

    INbtTileEntity getNbtTileEntity(BlockState tile);

    INbtEntity getNbtEntity(Entity entity);
}
