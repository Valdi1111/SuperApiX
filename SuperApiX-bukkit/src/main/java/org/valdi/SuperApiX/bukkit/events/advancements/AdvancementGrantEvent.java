package org.valdi.SuperApiX.bukkit.events.advancements;

import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.advancements.managers.IAdvancement;
import org.valdi.SuperApiX.bukkit.advancements.managers.IAdvancementManager;

public class AdvancementGrantEvent extends AdvancementEvent {
    private boolean displayMessage;

    public AdvancementGrantEvent(IAdvancementManager manager, IAdvancement advancement, Player player, boolean displayMessage) {
        super(manager, advancement, player);

        this.displayMessage = displayMessage;
    }

    public boolean isDisplayMessage() {
        return this.displayMessage;
    }

    public void setDisplayMessage(boolean displayMessage) {
        this.displayMessage = displayMessage;
    }
}

