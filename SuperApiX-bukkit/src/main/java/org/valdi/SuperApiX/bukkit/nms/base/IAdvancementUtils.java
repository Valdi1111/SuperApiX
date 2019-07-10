package org.valdi.SuperApiX.bukkit.nms.base;

import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperKey;
import org.valdi.SuperApiX.bukkit.advancements.AdvancementFrame;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;

public interface IAdvancementUtils {

    Object getFrameType(AdvancementFrame type) throws VersionUnsupportedException;

    void changeAdvancementTab(Player player, SuperKey rootAdvancement) throws VersionUnsupportedException;

    void resetAdvancements(Player player) throws VersionUnsupportedException;

}
