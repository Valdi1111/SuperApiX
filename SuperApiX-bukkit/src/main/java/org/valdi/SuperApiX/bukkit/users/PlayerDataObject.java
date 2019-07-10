package org.valdi.SuperApiX.bukkit.users;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.storage.objects.DataObject;

import java.util.UUID;

public interface PlayerDataObject<T> extends DataObject {

    T newInstance(UUID playerUUID);

    /**
     * @return the player's uniqueId
     */
    UUID getPlayerUUID();

    /**
     * Set the uuid for this player object
     * @param value - UUID
     */
    void setPlayerUUID(UUID value);

    /**
     * @return a player's instance or null if they aren't online
     */
    default Player getPlayer() {
        return Bukkit.getPlayer(getPlayerUUID());
    }

}
