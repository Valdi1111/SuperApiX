package org.valdi.SuperApiX.bukkit.nms.v1_8_R1;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.nms.AbstractWorldBorderSender;
import org.valdi.SuperApiX.bukkit.nms.IWorldBorderProvider;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;

import net.minecraft.server.v1_8_R1.EnumWorldBorderAction;
import net.minecraft.server.v1_8_R1.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_8_R1.WorldBorder;

public class WorldBorderHandler extends AbstractWorldBorderSender implements IWorldBorderProvider {
	
	public WorldBorderHandler(final SuperApiBukkit plugin) {
		super(plugin);
	}

	@Override
	public void sendBorder(Player player, double radius, Location loc) throws VersionUnsupportedException {
		WorldBorder worldBorder = new WorldBorder();

		worldBorder.c(loc.getX(), loc.getZ());
		worldBorder.a(radius);
	    worldBorder.world = ((CraftWorld) player.getWorld()).getHandle();

        PacketPlayOutWorldBorder sizePacket = new PacketPlayOutWorldBorder(worldBorder, EnumWorldBorderAction.SET_SIZE);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(sizePacket);
        
        PacketPlayOutWorldBorder centerPacket = new PacketPlayOutWorldBorder(worldBorder, EnumWorldBorderAction.SET_CENTER);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(centerPacket);
	}

}