package org.valdi.SuperApiX;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class SuperApi extends JavaPlugin {
	private static SuperApi plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getDescription().getName() + " V" + getDescription().getVersion() + " has been enabled... Enjoy :)");
	}
	
	@Override
	public void onDisable() {
		plugin = null;
		getServer().getConsoleSender().sendMessage(ChatColor.RED + getDescription().getName() + " V" + getDescription().getVersion() + " has been disabled... Goodbye :(");
	}
	
	public static SuperApi getInstance() {
		return plugin;
	}

}
