package org.valdi.SuperApiX.bukkit.events.advancements;

import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.advancements.managers.IAdvancement;
import org.valdi.SuperApiX.bukkit.advancements.managers.IAdvancementManager;

public class AdvancementRevokeEvent extends AdvancementEvent {

    public AdvancementRevokeEvent(IAdvancementManager manager, IAdvancement advancement, Player player) {
        super(manager, advancement, player);
    }

}

