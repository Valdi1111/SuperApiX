package org.valdi.SuperApiX.bukkit.nms.v1_7_R3;

import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.nms.AbstractNmsProvider;
import org.valdi.SuperApiX.bukkit.nms.NmsExceptions;
import org.valdi.SuperApiX.bukkit.nms.ITabList;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;

public class TabList extends AbstractNmsProvider implements ITabList {

	public TabList(final SuperApiBukkit plugin) {
		super(plugin);
	}

	@Override
	public void sendTabTitle(Player player, String header, String footer) throws VersionUnsupportedException {
		throw new VersionUnsupportedException(NmsExceptions.NOT_PRESENT.getExplenation());		
	}

}
