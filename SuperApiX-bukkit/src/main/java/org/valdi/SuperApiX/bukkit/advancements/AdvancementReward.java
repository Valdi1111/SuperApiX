package org.valdi.SuperApiX.bukkit.advancements;

import org.bukkit.entity.Player;

public abstract class AdvancementReward {

    /**
     * Called when a player complete the advancement
     * @param player - the player
     */
    public abstract void onGrant(Player player);

}

