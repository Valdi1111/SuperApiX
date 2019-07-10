package org.valdi.SuperApiX.bukkit.users;

import org.bukkit.OfflinePlayer;

public interface OfflineUser extends OfflinePlayer {

    /**
     * @return the offline player
     * @since 1.0.0-beta
     */
    OfflinePlayer getOfflinePlayer();

    /**
     * Gets a {@link User} object that this represents, if there is one
     * <p>
     * If the user is online, this will return that user. Otherwise,
     * it will return null.
     *
     * @return Online user
     */
    User getUser();

}
