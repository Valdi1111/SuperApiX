package org.valdi.SuperApiX.bukkit.nms;

import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;

public interface IPlayerUtils {

    int getPing(Player player) throws VersionUnsupportedException;

}
