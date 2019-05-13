package org.valdi.SuperApiX.bukkit.events.advancements.offline;

import java.util.UUID;
import org.valdi.SuperApiX.bukkit.advancements.managers.IAdvancement;
import org.valdi.SuperApiX.bukkit.advancements.managers.IAdvancementManager;

public class OfflineCriteriaRevokeEvent extends OfflineAdvancementEvent {
    private final String[] criteria;

    public OfflineCriteriaRevokeEvent(IAdvancementManager manager, IAdvancement advancement, String[] criteria, UUID uuid) {
        super(manager, advancement, uuid);

        this.criteria = criteria;
    }

    public String[] getCriteria() {
        return this.criteria;
    }

}

