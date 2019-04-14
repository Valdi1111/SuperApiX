package org.valdi.SuperApiX.bukkit.nms.v1_12_R1;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.nms.AbstractWorldBorder;
import org.valdi.SuperApiX.bukkit.nms.IWorldBorder;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;

import net.minecraft.server.v1_12_R1.PacketPlayOutWorldBorder;

public class WorldBorder extends AbstractWorldBorder implements IWorldBorder {
	
	public WorldBorder(final SuperApiBukkit plugin) {
		super(plugin);
	}

	@Override
	public void sendBorder(Player player, double radius, Location loc) throws VersionUnsupportedException {
		net.minecraft.server.v1_12_R1.WorldBorder worldBorder = new net.minecraft.server.v1_12_R1.WorldBorder();

		worldBorder.setCenter(loc.getX(), loc.getZ());
		worldBorder.setSize(radius);
	    worldBorder.world = ((CraftWorld) player.getWorld()).getHandle();

        PacketPlayOutWorldBorder sizePacket = new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_SIZE);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(sizePacket);
        
        PacketPlayOutWorldBorder centerPacket = new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_CENTER);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(centerPacket);
	}

}
