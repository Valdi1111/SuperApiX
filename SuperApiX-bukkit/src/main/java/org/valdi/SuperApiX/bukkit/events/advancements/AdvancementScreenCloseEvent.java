package org.valdi.SuperApiX.bukkit.events.advancements;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class AdvancementScreenCloseEvent extends PlayerEvent {
    public static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public AdvancementScreenCloseEvent(Player player) {
        super(player);
    }

    public String getInformationString() {
        return "tab_action=close;player=" + this.player.getName();
    }
}

