package org.valdi.SuperApiX.bukkit.storage.objects;

import com.google.gson.annotations.Expose;
import org.bukkit.Bukkit;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.users.PlayerDataObject;

import java.util.UUID;

/**
 * Tracks the following info on the player
 */
public class Players implements PlayerDataObject<Players> {
    @Expose
    private String uniqueId;
    @Expose
    private String playerName;
    @Expose
    private String locale;

    /**
     * This is required for database storage
     */
    public Players() {}

    /**
     * @param uniqueId - unique ID
     *            Constructor - initializes the state variables
     *
     */
    private Players(UUID uniqueId) {
        this.uniqueId = uniqueId.toString();
        this.locale = getPlugin().getSettings().getDefaultLanguage();
        // Try to get player's name
        this.playerName = Bukkit.getOfflinePlayer(uniqueId).getName();
        if (this.playerName == null) {
            this.playerName = uniqueId.toString();
        }
    }

    @Override
    public Players newInstance(UUID playerUUID) {
        return new Players(playerUUID);
    }

    @Override
    public UUID getPlayerUUID() {
        return UUID.fromString(uniqueId);
    }

    @Override
    public void setPlayerUUID(UUID value) {
        uniqueId = value.toString();
    }

    /**
     * @return the player's name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * @param value the playerName to set
     */
    public void setPlayerName(String value) {
        this.playerName = value;
    }

    /**
     * @return the locale
     */
    public String getLocale() {
        return locale;
    }

    /**
     * @param value the locale to set
     */
    public void setLocale(String value) {
        this.locale = value;
    }

    @Override
    public String getUniqueId() {
        return uniqueId;
    }

    @Override
    public void setUniqueId(String value) {
        this.uniqueId = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Players)) {
            return false;
        }

        Players player = (Players) o;
        return uniqueId.equals(player.getUniqueId());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uniqueId == null) ? 0 : uniqueId.hashCode());
        return result;
    }

}
