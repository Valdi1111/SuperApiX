package org.valdi.SuperApiX.bukkit.nms.v1_7_R3;

import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.nms.base.AbstractNmsProvider;
import org.valdi.SuperApiX.bukkit.nms.base.IPlayerUtils;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;

public class PlayerUtils extends AbstractNmsProvider implements IPlayerUtils {

	public PlayerUtils(SuperApiBukkit plugin) {
		super(plugin);
	}

	@Override
	public int getPing(Player player) throws VersionUnsupportedException {
		return ((CraftPlayer) player).getHandle().ping;
	}

}
