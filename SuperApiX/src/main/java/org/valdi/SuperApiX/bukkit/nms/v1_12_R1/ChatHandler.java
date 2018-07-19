package org.valdi.SuperApiX.bukkit.nms.v1_12_R1;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.events.ActionBarMessageEvent;
import org.valdi.SuperApiX.bukkit.events.TabTitleSendEvent;
import org.valdi.SuperApiX.bukkit.events.TitleSendEvent;
import org.valdi.SuperApiX.bukkit.nms.AbstractChatSender;
import org.valdi.SuperApiX.bukkit.nms.IChatProvider;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;
import org.valdi.SuperApiX.bukkit.utils.Formatting;

import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_12_R1.ChatComponentText;
import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;

public class ChatHandler extends AbstractChatSender implements IChatProvider {

	public ChatHandler(final SuperApiBukkit plugin) {
		super(plugin);
	}

	@Override
	public void sendActionBar(Player player, String message) throws VersionUnsupportedException {
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

	@Override
	public void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) throws VersionUnsupportedException {
		if(!player.isOnline() || (title == null && subtitle == null)) {
			return;
		}
		
		TitleSendEvent e = new TitleSendEvent(player, fadeIn, stay, fadeOut, new Formatting(title).parsePlaceholders(player), new Formatting(subtitle).parsePlaceholders(player));
		if(e.isCancelled()) {
			return;
		}
		
		PacketPlayOutTitle timesPacket = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, e.getFadeIn(), e.getStay(), e.getFadeOut());
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(timesPacket);
		
		if(title != null) {
			IChatBaseComponent titleText = ChatSerializer.a("{\"text\": \"" + e.getTitle() + "\"}");
			PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.TITLE, titleText);
	        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlePacket);
		}
		
		if(subtitle != null) {
			IChatBaseComponent subtitleText = ChatSerializer.a("{\"text\": \"" + e.getSubtitle() + "\"}");
			PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, subtitleText);
	        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlePacket);
		}
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
