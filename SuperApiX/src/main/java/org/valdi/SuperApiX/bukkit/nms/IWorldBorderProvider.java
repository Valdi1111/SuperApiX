package org.valdi.SuperApiX.bukkit.nms;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;

public interface IWorldBorderProvider {

	public void sendBorder(Player player, double radius, Location loc) throws VersionUnsupportedException;

}
