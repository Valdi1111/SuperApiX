package org.valdi.SuperApiX.bukkit.nms.v1_13_R1;

import net.minecraft.server.v1_13_R1.PacketPlayOutWorldBorder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.nms.WorldBuilder;
import org.valdi.SuperApiX.bukkit.nms.base.AbstractNmsProvider;
import org.valdi.SuperApiX.bukkit.nms.base.IWorldManager;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;

public class WorldManager extends AbstractNmsProvider implements IWorldManager {

	public WorldManager(SuperApiBukkit plugin) {
		super(plugin);
	}

    @Override
    public void sendBorder(Player player, double radius, Location loc) {
        net.minecraft.server.v1_13_R1.WorldBorder worldBorder = new net.minecraft.server.v1_13_R1.WorldBorder();

        worldBorder.setCenter(loc.getX(), loc.getZ());
        worldBorder.setSize(radius);
        worldBorder.world = ((CraftWorld) player.getWorld()).getHandle();

        PacketPlayOutWorldBorder sizePacket = new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_SIZE);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(sizePacket);

        PacketPlayOutWorldBorder centerPacket = new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_CENTER);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(centerPacket);
    }

    @Override
    public World createWorld(WorldBuilder creator) {
        throw new VersionUnsupportedException("Not implemented Yet");
    }
}
