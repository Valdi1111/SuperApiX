package org.valdi.SuperApiX.bukkit.events.advancements;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.valdi.SuperApiX.bukkit.advancements.managers.IAdvancement;
import org.valdi.SuperApiX.bukkit.advancements.managers.IAdvancementManager;

public abstract class AdvancementEvent extends PlayerEvent {
    public static final HandlerList handlers = new HandlerList();
    private final IAdvancementManager manager;
    private final IAdvancement advancement;

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    protected AdvancementEvent(IAdvancementManager manager, IAdvancement advancement, Player player) {
        super(player);

        this.manager = manager;
        this.advancement = advancement;
    }

    public IAdvancementManager getManager() {
        return this.manager;
    }

    public IAdvancement getAdvancement() {
        return this.advancement;
    }
}

