package org.valdi.SuperApiX.bukkit.nms.v1_13_R2;

import net.minecraft.server.v1_13_R2.*;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.SuperKey;
import org.valdi.SuperApiX.bukkit.advancements.AdvancementFrame;
import org.valdi.SuperApiX.bukkit.events.ActionBarMessageEvent;
import org.valdi.SuperApiX.bukkit.nms.base.AbstractActionBar;
import org.valdi.SuperApiX.bukkit.nms.base.AbstractNmsProvider;
import org.valdi.SuperApiX.bukkit.nms.base.IAdvancementUtils;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;
import org.valdi.SuperApiX.bukkit.utils.Formatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ActionBar extends AbstractActionBar {

	public ActionBar(final SuperApiBukkit plugin) {
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

    public static class AdvancementUtils extends AbstractNmsProvider implements IAdvancementUtils {

        public AdvancementUtils(SuperApiBukkit plugin) {
            super(plugin);
        }

        @Override
        public AdvancementFrameType getFrameType(AdvancementFrame type) {
            return AdvancementFrameType.a(type.getId());
        }

        @Override
        public void changeAdvancementTab(Player player, SuperKey rootAdvancement) {
            PacketPlayOutSelectAdvancementTab packet = new PacketPlayOutSelectAdvancementTab(rootAdvancement == null ? null : (MinecraftKey) rootAdvancement.toMinecraftKey());
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
        }

        @Override
        public void resetAdvancements(Player player) {
            PacketPlayOutAdvancements packet = new PacketPlayOutAdvancements(true, new ArrayList<>(), new HashSet<>(), new HashMap<>());
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
        }

    }
}
