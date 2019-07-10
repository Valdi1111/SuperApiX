package org.valdi.SuperApiX.bukkit.nms.v1_8_R2;

import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.events.TitleSendEvent;
import org.valdi.SuperApiX.bukkit.nms.base.AbstractTitle;
import org.valdi.SuperApiX.bukkit.nms.base.ITitle;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;
import org.valdi.SuperApiX.bukkit.utils.Formatting;

import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R2.PacketPlayOutTitle.EnumTitleAction;

public class Title extends AbstractTitle implements ITitle {

	public Title(final SuperApiBukkit plugin) {
		super(plugin);
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

}
