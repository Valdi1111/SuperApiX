package org.valdi.SuperApiX.bukkit.nms.v1_10_R1;

import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.events.TabTitleSendEvent;
import org.valdi.SuperApiX.bukkit.nms.base.AbstractNmsProvider;
import org.valdi.SuperApiX.bukkit.nms.base.ITabList;
import org.valdi.SuperApiX.bukkit.utils.Formatting;

import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_10_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_10_R1.PacketDataSerializer;

import java.io.IOException;

public class TabList extends AbstractNmsProvider implements ITabList {

	public TabList(final SuperApiBukkit plugin) {
		super(plugin);
	}

	@Override
	public void sendTabTitle(Player player, String header, String footer) {
		if(!player.isOnline() || (header == null && footer == null)) {
			return;
		}
		
		TabTitleSendEvent e = new TabTitleSendEvent(player, new Formatting(header).parsePlaceholders(player), new Formatting(footer).parsePlaceholders(player));
		if(e.isCancelled()) {
			return;
		}

		try {
			IChatBaseComponent headerText = ChatSerializer.a("{\"text\": \"" + e.getHeader() + "\"}");
			IChatBaseComponent footerText = ChatSerializer.a("{\"text\": \"" + e.getFooter() + "\"}");
			PacketDataSerializer dataSerialized = new PacketDataSerializer(Unpooled.buffer());
			dataSerialized.a(headerText);
			dataSerialized.a(footerText);
			
			PacketPlayOutPlayerListHeaderFooter tabPacket = new PacketPlayOutPlayerListHeaderFooter();
			tabPacket.a(dataSerialized);
			
	        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(tabPacket);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
