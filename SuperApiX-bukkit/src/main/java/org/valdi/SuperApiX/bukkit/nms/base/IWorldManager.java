package org.valdi.SuperApiX.bukkit.nms.base;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.nms.WorldBuilder;

public interface IWorldManager {

    void sendBorder(Player player, double radius, Location loc);

    World createWorld(WorldBuilder creator);

}
