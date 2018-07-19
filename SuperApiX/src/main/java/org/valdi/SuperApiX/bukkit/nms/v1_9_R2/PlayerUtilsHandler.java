package org.valdi.SuperApiX.bukkit.nms.v1_9_R2;

import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.nms.AbstractNmsProvider;
import org.valdi.SuperApiX.bukkit.nms.IPlayerUtils;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;

public class PlayerUtilsHandler extends AbstractNmsProvider implements IPlayerUtils {

	public PlayerUtilsHandler(SuperApiBukkit plugin) {
		super(plugin);
	}

	@Override
	public int getPing(Player player) throws VersionUnsupportedException {
		return ((CraftPlayer) player).getHandle().ping;
	}

}
