package org.valdi.SuperApiX.bukkit.nms;

import org.valdi.SuperApiX.bukkit.SuperApiBukkit;

public abstract class AbstractNmsProvider {
	private final SuperApiBukkit plugin;
	
	protected AbstractNmsProvider(final SuperApiBukkit plugin) {
		this.plugin = plugin;
	}
	
	protected SuperApiBukkit getPlugin() {
		return this.plugin;
	}

}
