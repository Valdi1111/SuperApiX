package org.valdi.SuperApiX.bukkit.nms.v1_8_R3;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.events.TabTitleSendEvent;
import org.valdi.SuperApiX.bukkit.nms.AbstractNmsProvider;
import org.valdi.SuperApiX.bukkit.nms.ITabList;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;
import org.valdi.SuperApiX.bukkit.utils.Formatting;

import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;

public class TabList extends AbstractNmsProvider implements ITabList {

	public TabList(final SuperApiBukkit plugin) {
		super(plugin);
	}

	@Override
	public void sendTabTitle(Player player, String header, String footer) throws VersionUnsupportedException {
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
		} catch (Exception ex) {
			throw new VersionUnsupportedException(ex);
		}
	}

}
