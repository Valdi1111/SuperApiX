package org.valdi.SuperApiX.bukkit.nms.v1_14_R1;

import net.minecraft.server.v1_14_R1.AdvancementFrameType;
import net.minecraft.server.v1_14_R1.MinecraftKey;
import net.minecraft.server.v1_14_R1.PacketPlayOutAdvancements;
import net.minecraft.server.v1_14_R1.PacketPlayOutSelectAdvancementTab;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.SuperKey;
import org.valdi.SuperApiX.bukkit.advancements.AdvancementFrame;
import org.valdi.SuperApiX.bukkit.nms.AbstractNmsProvider;
import org.valdi.SuperApiX.bukkit.nms.IAdvancementUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class AdvancementUtils extends AbstractNmsProvider implements IAdvancementUtils {

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
