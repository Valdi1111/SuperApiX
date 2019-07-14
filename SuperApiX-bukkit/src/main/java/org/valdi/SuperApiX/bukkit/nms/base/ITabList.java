package org.valdi.SuperApiX.bukkit.nms.base;

import org.bukkit.entity.Player;

public interface ITabList {

    /**
     * Sends a new tab list to the player with the specified params.
     *
     * @param player  The player to send the tab list to.
     * @param header  The header for the tablist.
     * @param footer  The footer for the tablist.
     */
	void sendTabTitle(Player player, String header, String footer);

}
