package org.valdi.SuperApiX.bukkit.nms.base;

import org.bukkit.inventory.ItemStack;

public interface IItemUtils {

    String toBase64(ItemStack item);

    ItemStack fromBase64(String data);

    String toBase64List(ItemStack[] items);

    ItemStack[] fromBase64List(String items);

}
