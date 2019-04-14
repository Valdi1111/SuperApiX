package org.valdi.SuperApiX.bukkit.nms.v1_7_R4;

import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.nms.AbstractActionBar;
import org.valdi.SuperApiX.bukkit.nms.IActionBar;
import org.valdi.SuperApiX.bukkit.nms.NmsExceptions;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;

public class ActionBar extends AbstractActionBar implements IActionBar {

	public ActionBar(final SuperApiBukkit plugin) {
		super(plugin);
	}

	@Override
	public void sendActionBar(Player player, String message) throws VersionUnsupportedException {
		throw new VersionUnsupportedException(NmsExceptions.NOT_PRESENT.getExplenation());
	}

}
