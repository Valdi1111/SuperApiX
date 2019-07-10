package org.valdi.SuperApiX.bukkit.nms.v1_7_R4;

import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.nms.base.AbstractTitle;
import org.valdi.SuperApiX.bukkit.nms.NmsExceptions;
import org.valdi.SuperApiX.bukkit.nms.base.ITitle;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;

public class Title extends AbstractTitle implements ITitle {

	public Title(final SuperApiBukkit plugin) {
		super(plugin);
	}

	@Override
	public void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) throws VersionUnsupportedException {
		throw new VersionUnsupportedException(NmsExceptions.NOT_PRESENT.getExplenation());		
	}

}
