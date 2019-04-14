package org.valdi.SuperApiX.bukkit.nms.v1_7_R4;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.nms.AbstractWorldBorder;
import org.valdi.SuperApiX.bukkit.nms.NmsExceptions;
import org.valdi.SuperApiX.bukkit.nms.IWorldBorder;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;

public class WorldBorder extends AbstractWorldBorder implements IWorldBorder {
	
	public WorldBorder(final SuperApiBukkit plugin) {
		super(plugin);
	}

	@Override
	public void sendBorder(Player player, double radius, Location loc) throws VersionUnsupportedException {
		throw new VersionUnsupportedException(NmsExceptions.NOT_PRESENT.getExplenation());
	}

}
