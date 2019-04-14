package org.valdi.SuperApiX.bukkit.nms;

import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;

public abstract class AbstractTitle extends AbstractNmsProvider implements ITitle {
	
	protected AbstractTitle(final SuperApiBukkit plugin) {
		super(plugin);
	}

	@Override
	public void sendFullTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) throws VersionUnsupportedException {
		sendTitle(player, fadeIn, stay, fadeOut, title, subtitle);
	}

	@Override
	public void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title) throws VersionUnsupportedException {
		sendTitle(player, fadeIn, stay, fadeOut, title, null);
	}

	@Override
	public void sendSubtitle(Player player, int fadeIn, int stay, int fadeOut, String subtitle) throws VersionUnsupportedException {
		sendTitle(player, fadeIn, stay, fadeOut, null, subtitle);
	}

}
