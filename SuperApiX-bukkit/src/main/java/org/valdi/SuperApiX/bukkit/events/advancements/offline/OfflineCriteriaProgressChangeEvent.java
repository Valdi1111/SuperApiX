package org.valdi.SuperApiX.bukkit.events.advancements.offline;

import java.util.UUID;
import org.valdi.SuperApiX.bukkit.advancements.managers.IAdvancement;
import org.valdi.SuperApiX.bukkit.advancements.managers.IAdvancementManager;

public class OfflineCriteriaProgressChangeEvent extends OfflineAdvancementEvent {
    private final int progressBefore;
    private int progress;

    public OfflineCriteriaProgressChangeEvent(IAdvancementManager manager, IAdvancement advancement, UUID uuid, int progressBefore, int progress) {
        super(manager, advancement, uuid);

        this.progressBefore = progressBefore;
        this.progress = progress;
    }

    public int getProgressBefore() {
        return this.progressBefore;
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

}

