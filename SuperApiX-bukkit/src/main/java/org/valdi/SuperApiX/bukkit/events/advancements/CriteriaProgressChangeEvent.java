package org.valdi.SuperApiX.bukkit.events.advancements;

import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.advancements.managers.IAdvancement;
import org.valdi.SuperApiX.bukkit.advancements.managers.IAdvancementManager;

public class CriteriaProgressChangeEvent extends AdvancementEvent {
    private final int progressBefore;
    private int progress;

    public CriteriaProgressChangeEvent(IAdvancementManager manager, IAdvancement advancement, Player player, int progressBefore, int progress) {
        super(manager, advancement, player);

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

