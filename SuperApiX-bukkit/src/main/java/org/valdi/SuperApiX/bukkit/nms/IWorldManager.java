package org.valdi.SuperApiX.bukkit.nms;

import org.bukkit.World;

public interface IWorldManager {
	
	World createWorld(WorldBuilder creator);

}
