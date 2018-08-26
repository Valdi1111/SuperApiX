package org.valdi.SuperApiX.bukkit.nms.v1_7_R3;

import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.nms.AbstractChatSender;
import org.valdi.SuperApiX.bukkit.nms.Exceptions;
import org.valdi.SuperApiX.bukkit.nms.IChatProvider;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;

public class ChatHandler extends AbstractChatSender implements IChatProvider {

	public ChatHandler(final SuperApiBukkit plugin) {
		super(plugin);
	}

	@Override
	public void sendActionBar(Player player, String message) throws VersionUnsupportedException {
		throw new VersionUnsupportedException(Exceptions.NOT_PRESENT.getExplenation());
	}

	@Override
	public void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) throws VersionUnsupportedException {
		throw new VersionUnsupportedException(Exceptions.NOT_PRESENT.getExplenation());
	}

	@Override
	public void sendTabTitle(Player player, String header, String footer) throws VersionUnsupportedException {
		throw new VersionUnsupportedException(Exceptions.NOT_PRESENT.getExplenation());		
	}

}
