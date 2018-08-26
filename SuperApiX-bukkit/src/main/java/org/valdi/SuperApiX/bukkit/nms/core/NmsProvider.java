package org.valdi.SuperApiX.bukkit.nms.core;

import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.nms.IChatProvider;
import org.valdi.SuperApiX.bukkit.nms.IWorldBorderProvider;
import org.valdi.SuperApiX.bukkit.nms.core.VersionManager.NmsVersion;

public class NmsProvider {
	private final SuperApiBukkit plugin;

	private IWorldBorderProvider worldBorder;
	private IChatProvider chatUtils;
	
	public NmsProvider(final SuperApiBukkit plugin) {
		this.plugin = plugin;
		
		this.setupProviders();
	}
	
	public void setupProviders() {
		try {
			NmsVersion version = plugin.getVersionManager().getNmsVersion();
			if(version == NmsVersion.INCOMPATIBLE) {
				plugin.getLogger().severe("Cannot provide support for this nms version... Using bukkit handler, some methods won't work.");
			}
			
			this.worldBorder = (IWorldBorderProvider) Class.forName("org.valdi.SuperApiX.bukkit.nms." + version.getRaw() + ".WorldBorderHandler").getConstructor(SuperApiBukkit.class).newInstance(plugin);
			this.chatUtils = (IChatProvider) Class.forName("org.valdi.SuperApiX.bukkit.nms." + version.getRaw() + ".ChatHandler").getConstructor(SuperApiBukkit.class).newInstance(plugin);
		} catch(Exception e) {
			e.printStackTrace();
			plugin.getLogger().severe("Error initializing providers classes...");
			plugin.getLogger().severe("Try updating SuperApiX!");
		}
	}
	
	public IWorldBorderProvider getWorldBorderUtils() {
		return this.worldBorder;
	}
	
	public IChatProvider getChatUtils() {
		return this.chatUtils;
	}
	
	public NmsVersion getVersion() {
		return plugin.getVersionManager().getNmsVersion();
	}

}
