package org.valdi.SuperApiX.bukkit.nms.base;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;

public interface ISignEditor {

    void openSignEditor(Player player, Sign sign) throws VersionUnsupportedException;

}
