package org.valdi.SuperApiX.bukkit.events.advancements.offline;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.valdi.SuperApiX.bukkit.advancements.managers.IAdvancement;
import org.valdi.SuperApiX.bukkit.advancements.managers.IAdvancementManager;

import java.util.UUID;

public abstract class OfflineAdvancementEvent extends Event {
    public static final HandlerList handlers = new HandlerList();

    private final IAdvancementManager manager;
    private final IAdvancement advancement;
    private final UUID uuid;

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    protected OfflineAdvancementEvent(IAdvancementManager manager, IAdvancement advancement, UUID uuid) {
        this.manager = manager;
        this.advancement = advancement;
        this.uuid = uuid;
    }

    public IAdvancementManager getManager() {
        return this.manager;
    }

    public IAdvancement getAdvancement() {
        return this.advancement;
    }

    public UUID getUUID() {
        return this.uuid;
    }
}

