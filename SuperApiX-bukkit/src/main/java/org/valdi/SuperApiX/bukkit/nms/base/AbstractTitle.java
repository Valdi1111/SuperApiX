package org.valdi.SuperApiX.bukkit.nms.base;

import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;

public abstract class AbstractTitle extends AbstractNmsProvider implements ITitle {
	
	protected AbstractTitle(final SuperApiBukkit plugin) {
		super(plugin);
	}

	@Override
	public void sendFullTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) {
		sendTitle(player, fadeIn, stay, fadeOut, title, subtitle);
	}

	@Override
	public void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title) {
		sendTitle(player, fadeIn, stay, fadeOut, title, null);
	}

	@Override
	public void sendSubtitle(Player player, int fadeIn, int stay, int fadeOut, String subtitle) {
		sendTitle(player, fadeIn, stay, fadeOut, null, subtitle);
	}

}
