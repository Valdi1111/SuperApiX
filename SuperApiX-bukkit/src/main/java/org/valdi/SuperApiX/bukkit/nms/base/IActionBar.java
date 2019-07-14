package org.valdi.SuperApiX.bukkit.nms.base;

import org.bukkit.entity.Player;

public interface IActionBar {
	
	void sendActionBar(Player player, String message);
	
	void sendActionBar(Player player, String message, int time);

}
