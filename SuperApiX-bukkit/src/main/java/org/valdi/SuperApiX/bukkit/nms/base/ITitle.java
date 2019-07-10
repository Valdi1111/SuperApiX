package org.valdi.SuperApiX.bukkit.nms.base;

import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;

public interface ITitle {

	@Deprecated
	public void sendFullTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) throws VersionUnsupportedException;

    /**
     * Sends the specified player a title with the specified params.
     *
     * @param player   The player to send the title to.
     * @param fadeIn   How long it takes the title to fade in, in seconds.
     * @param stay     How long the title is to be visible on screen, in seconds.
     * @param fadeOut  How long the title is to fade out, in seconds.
     * @param title    The message for the title.
     * @deprecated Use {@link sendTitle} instead
     */
	@Deprecated
	void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title) throws VersionUnsupportedException;

    /**
     * Sends the specified player a subtitle with the specified params.
     *
     * @param player   The player to send the subtitle to.
     * @param fadeIn   How long it takes the subtitle to fade in, in seconds.
     * @param stay     How long the subtitle is to be visible on screen, in seconds.
     * @param fadeOut  How long the subtitle is to fade out, in seconds.
     * @param subtitle The message for the subtitle.
     * @deprecated Use {@link sendTitle} instead
     */
	@Deprecated
	void sendSubtitle(Player player, int fadeIn, int stay, int fadeOut, String subtitle) throws VersionUnsupportedException;

    /**
     * Sends the specified player a title with a subtitle with the specified params.
     *
     * @param player   The player to send the title to.
     * @param fadeIn   How long it takes the title to fade in, in seconds.
     * @param stay     How long the title is to be visible on screen, in seconds.
     * @param fadeOut  How long the title is to fade out, in seconds.
     * @param title    The message for the title.
     * @param subtitle The message for the subtitle.
     */
	void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) throws VersionUnsupportedException;

}
