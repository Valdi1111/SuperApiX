package org.valdi.SuperApiX.bukkit.events.advancements.offline;

import java.util.UUID;
import org.valdi.SuperApiX.bukkit.advancements.managers.IAdvancement;
import org.valdi.SuperApiX.bukkit.advancements.managers.IAdvancementManager;

public class OfflineAdvancementGrantEvent extends OfflineAdvancementEvent {

    public OfflineAdvancementGrantEvent(IAdvancementManager manager, IAdvancement advancement, UUID uuid) {
        super(manager, advancement, uuid);
    }

}

