package org.valdi.SuperApiX.bukkit.utils;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Formatting {
	private String message;
	
	public Formatting(String message) {
		this.message = message;
	}
	
	public String parsePlaceholders(Player player) {
		if(message == null) {
			return null;
		}
		
		if(message.equals("")) {
			return "";
		}
		
		message = ChatColor.translateAlternateColorCodes('&', message);
		return message;
	}
	
	public String parsePlaceholders(OfflinePlayer player) {
		if(message == null) {
			return null;
		}
		
		if(message.equals("")) {
			return "";
		}

		message = ChatColor.translateAlternateColorCodes('&', message);
		return message;
	}

}
