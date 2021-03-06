package org.valdi.SuperApiX.bukkit.nms.v1_8_R1;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.events.ActionBarMessageEvent;
import org.valdi.SuperApiX.bukkit.nms.base.AbstractActionBar;
import org.valdi.SuperApiX.bukkit.utils.Formatting;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;

public class ActionBar extends AbstractActionBar {

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

		IChatBaseComponent text = ChatSerializer.a("{\"text\": \"" + e.getMessage() + "\"}");
		PacketPlayOutChat barPacket = new PacketPlayOutChat(text, (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(barPacket);
	}

}
