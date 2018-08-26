package org.valdi.SuperApiX.bukkit.bossbar;

import org.valdi.SuperApiX.bukkit.BukkitBootstrap;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;

public abstract class BarSolution implements IBossBarProvider {
	private final SuperApiBukkit plugin;
	
	protected BarSolution(final SuperApiBukkit plugin) {
		this.plugin = plugin;
	}
	
	protected SuperApiBukkit getPlugin() {
		return this.plugin;
	}
	
	protected BukkitBootstrap getBootstrap() {
		return this.plugin.getBootstrap();
	}
	
	public static enum BarPlugin {
		BUKKIT,
		BOSSBARAPI,
		BARAPI;
	}

}
