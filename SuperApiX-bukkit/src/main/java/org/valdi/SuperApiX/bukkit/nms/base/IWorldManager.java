package org.valdi.SuperApiX.bukkit.nms.base;

import org.bukkit.World;
import org.valdi.SuperApiX.bukkit.nms.WorldBuilder;

public interface IWorldManager {
	
	World createWorld(WorldBuilder creator);

}
