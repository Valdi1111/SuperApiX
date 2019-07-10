package org.valdi.SuperApiX.bukkit.users;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class SimpleOfflineUser implements OfflineUser {
    private final UUID uniqueId;

    protected SimpleOfflineUser(OfflinePlayer offlinePlayer) {
        this(offlinePlayer.getUniqueId());
    }

    protected SimpleOfflineUser(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public static OfflineUser getInstance(UUID uniqueId) {
        if(uniqueId == null) {
            return null;
        }
        return new SimpleOfflineUser(uniqueId);
    }

    public static OfflineUser getInstance(OfflinePlayer offlinePlayer) {
        if(offlinePlayer == null) {
            return null;
        }
        return getInstance(offlinePlayer.getUniqueId());
    }

    @Override
    public boolean isOnline() {
        return getOfflinePlayer().isOnline();
    }

    @Override
    public String getName() {
        String name = getOfflinePlayer().getName();
        if(name == null) {
            name =  SuperApiBukkit.getInstance().getPlayersManager().getName(uniqueId);
        }

        return name;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public boolean isBanned() {
        return getOfflinePlayer().isBanned();
    }

    @Override
    public boolean isWhitelisted() {
        return getOfflinePlayer().isWhitelisted();
    }

    @Override
    public void setWhitelisted(boolean value) {
        getOfflinePlayer().setWhitelisted(value);
    }

    @Override
    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uniqueId);
    }

    @Override
    public Player getPlayer() {
        return getOfflinePlayer().getPlayer();
    }

    @Override
    public long getFirstPlayed() {
        return getOfflinePlayer().getFirstPlayed();
    }

    @Override
    public long getLastPlayed() {
        return getOfflinePlayer().getLastPlayed();
    }

    @Override
    public boolean hasPlayedBefore() {
        return getOfflinePlayer().hasPlayedBefore();
    }

    @Override
    public Location getBedSpawnLocation() {
        return getOfflinePlayer().getBedSpawnLocation();
    }

    @Override
    public User getUser() {
        return SimpleUser.getInstance(uniqueId);
    }

    @Override
    public boolean isOp() {
        return getOfflinePlayer().isOp();
    }

    @Override
    public void setOp(boolean value) {
        getOfflinePlayer().setOp(value);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("UUID", uniqueId.toString());
        return result;
    }

    public static SimpleOfflineUser deserialize(Map<String, Object> args) {
        return new SimpleOfflineUser(UUID.fromString((String)args.get("UUID")));
    }
}
