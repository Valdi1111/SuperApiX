package org.valdi.SuperApiX.bukkit.nms.v1_7_R4;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.nms.AbstractWorldBorderSender;
import org.valdi.SuperApiX.bukkit.nms.Exceptions;
import org.valdi.SuperApiX.bukkit.nms.IWorldBorderProvider;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;

public class WorldBorderHandler extends AbstractWorldBorderSender implements IWorldBorderProvider {
	
	public WorldBorderHandler(final SuperApiBukkit plugin) {
		super(plugin);
	}

	@Override
	public void sendBorder(Player player, double radius, Location loc) throws VersionUnsupportedException {
		throw new VersionUnsupportedException(Exceptions.NOT_PRESENT.getExplenation());
	}

}
