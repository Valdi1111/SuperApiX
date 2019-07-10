package org.valdi.SuperApiX.bukkit.users;

import org.valdi.SuperApiX.bukkit.storage.CommonJsonTable;
import org.valdi.SuperApiX.common.plugin.StoreLoader;
import org.valdi.SuperApiX.common.databases.DatabaseException;
import org.valdi.SuperApiX.common.databases.IDataStorage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerStorage<T extends PlayerDataObject<T>> {
    private StoreLoader loader;

    private CommonJsonTable<T> table;
    private Map<UUID, T> cache;
    private Class<T> clazz;

    public PlayerStorage(StoreLoader loader, Class<T> clazz, IDataStorage database) throws DatabaseException {
        this.loader = loader;

        this.table = new CommonJsonTable<>(loader, clazz, database);
        table.createTable();
        this.cache = new HashMap<>();
        this.clazz = clazz;
    }

    public StoreLoader getStoreLoader() {
        return loader;
    }

    public CommonJsonTable<T> getTable() {
        return table;
    }

    public Map<UUID, T> getCache() {
        return cache;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public void load() {
        cache.clear();
        table.loadObjects().forEach(p -> cache.put(p.getPlayerUUID(), p));
    }

    public void save(UUID playerUUID, boolean async) {
        if (cache.containsKey(playerUUID)) {
            if(async) {
                table.saveObject(cache.get(playerUUID), null);
            } else {
                table.saveObject(cache.get(playerUUID));
            }
            cache.remove(playerUUID);
        }
    }

    public void saveAll() {
        Collections.unmodifiableCollection(cache.values()).forEach(p -> table.saveObject(p));
    }

    public void addPlayer(UUID playerUUID) {
        try {
            if (!cache.containsKey(playerUUID)) {
                T player;
                // If the player is in the database, load it, otherwise create a new player
                if (table.objectExists(playerUUID.toString())) {
                    player = table.loadObject(playerUUID.toString());
                    if (player == null) {
                        player = clazz.newInstance().newInstance(playerUUID);
                        // Corrupted database entry
                        loader.getLogger().severe("Corrupted player database entry for " + playerUUID + " - unrecoverable. Recreated.");
                        player.setUniqueId(playerUUID.toString());
                    }
                } else {
                    player = clazz.newInstance().newInstance(playerUUID);
                }
                cache.put(playerUUID, player);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
