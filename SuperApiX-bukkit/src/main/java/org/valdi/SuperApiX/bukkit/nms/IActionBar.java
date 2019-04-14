package org.valdi.SuperApiX.bukkit.nms;

import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;

public interface IActionBar {
	
	public void sendActionBar(Player player, String message) throws VersionUnsupportedException;
	
	public void sendActionBar(Player player, String message, int time) throws VersionUnsupportedException;

}
