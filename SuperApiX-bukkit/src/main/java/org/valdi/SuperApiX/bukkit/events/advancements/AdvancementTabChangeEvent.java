package org.valdi.SuperApiX.bukkit.events.advancements;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.valdi.SuperApiX.bukkit.SuperKey;

public class AdvancementTabChangeEvent extends PlayerEvent implements Cancellable {
    public static final HandlerList handlers = new HandlerList();

    private SuperKey tabAdvancement;
    private boolean cancelled;

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public AdvancementTabChangeEvent(Player player, SuperKey tabAdvancement) {
        super(player);

        this.tabAdvancement = tabAdvancement;
    }

    public SuperKey getTabAdvancement() {
        return this.tabAdvancement;
    }

    public void setTabAdvancement(SuperKey tabAdvancement) {
        this.tabAdvancement = tabAdvancement;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public String getInformationString() {
        return "tab_action=change;player=" + this.player.getName() + ";tab=" + this.tabAdvancement.getNamespace() + ":" + this.tabAdvancement.getKey() + ",cancelled=" + this.cancelled;
    }
}

