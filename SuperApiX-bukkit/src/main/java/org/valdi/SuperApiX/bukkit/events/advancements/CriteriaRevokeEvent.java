package org.valdi.SuperApiX.bukkit.events.advancements;

import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.advancements.managers.IAdvancement;
import org.valdi.SuperApiX.bukkit.advancements.managers.IAdvancementManager;

public class CriteriaRevokeEvent extends AdvancementEvent {
    private final String[] criteria;

    public CriteriaRevokeEvent(IAdvancementManager manager, IAdvancement advancement, String[] criteria, Player player) {
        super(manager, advancement, player);

        this.criteria = criteria;
    }

    public String[] getCriteria() {
        return this.criteria;
    }

}

