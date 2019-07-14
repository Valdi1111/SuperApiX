package org.valdi.SuperApiX.bukkit.nms.v1_13_R1;

import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.events.ActionBarMessageEvent;
import org.valdi.SuperApiX.bukkit.nms.base.AbstractActionBar;
import org.valdi.SuperApiX.bukkit.nms.base.IActionBar;
import org.valdi.SuperApiX.bukkit.utils.Formatting;

import net.minecraft.server.v1_13_R1.ChatComponentText;
import net.minecraft.server.v1_13_R1.ChatMessageType;
import net.minecraft.server.v1_13_R1.PacketPlayOutChat;

public class ActionBar extends AbstractActionBar implements IActionBar {

	public ActionBar(final SuperApiBukkit plugin) {
		super(plugin);
	}

	@Override
	public void sendActionBar(Player player, String message) {
		if(!player.isOnline() || message == null) {
			return;
		}
		
		ActionBarMessageEvent e = new ActionBarMessageEvent(player, new Formatting(message).parsePlaceholders(player));
		if(e.isCancelled()) {
			return;
		}

		ChatComponentText text = new ChatComponentText(e.getMessage());
		PacketPlayOutChat barPacket = new PacketPlayOutChat(text, ChatMessageType.GAME_INFO);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(barPacket);
	}

}
