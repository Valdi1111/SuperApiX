package org.valdi.SuperApiX.bukkit.nms.v1_8_R2;

import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.nms.base.AbstractNmsProvider;
import org.valdi.SuperApiX.bukkit.nms.base.IPlayerUtils;

public class PlayerUtils extends AbstractNmsProvider implements IPlayerUtils {

	public PlayerUtils(SuperApiBukkit plugin) {
		super(plugin);
	}

	@Override
	public int getPing(Player player) {
		return ((CraftPlayer) player).getHandle().ping;
	}

}
