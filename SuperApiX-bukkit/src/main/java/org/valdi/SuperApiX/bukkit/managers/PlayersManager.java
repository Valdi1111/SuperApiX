package org.valdi.SuperApiX.bukkit.managers;

import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.storage.CommonJsonTable;
import org.valdi.SuperApiX.bukkit.storage.objects.Names;
import org.valdi.SuperApiX.bukkit.storage.objects.Players;
import org.valdi.SuperApiX.bukkit.users.PlayerDataObject;
import org.valdi.SuperApiX.bukkit.users.PlayerStorage;
import org.valdi.SuperApiX.bukkit.users.SimpleUser;
import org.valdi.SuperApiX.bukkit.users.User;
import org.valdi.SuperApiX.common.plugin.StoreLoader;
import org.valdi.SuperApiX.common.databases.DatabaseException;
import org.valdi.SuperApiX.common.databases.IDataStorage;

import java.util.*;

public class PlayersManager {
    private SuperApiBukkit plugin;

    //private final CommonJsonTable<Players> players;
    private final CommonJsonTable<Names> names;
    //private final Map<UUID, Players> playerCache;

    private final Map<StoreLoader, PlayerStorage<? extends PlayerDataObject>> storages;

    private final Set<UUID> inTeleport;

    /**
     * Provides a memory cache of online player information
     * This is the one-stop-shop of player info
     * If the player is not cached, then a request is made to Players to obtain it
     *
     * @param plugin - the plugin object
     * @throws DatabaseException
     */
    public PlayersManager(SuperApiBukkit plugin) throws DatabaseException {
        this.plugin = plugin;
        // Set up the database handler to store and retrieve Players classes
        //this.players = new CommonJsonTable<>(plugin, Players.class);
        // Set up the names database
        this.names = new CommonJsonTable<>(plugin, Names.class, plugin.getDataStorage());
        names.createTable();
        //this.playerCache = new HashMap<>();

        this.storages = new HashMap<>();
        this.registerStore(plugin, Players.class, plugin.getDataStorage());

        this.inTeleport = new HashSet<>();
    }

    public <T extends PlayerDataObject<T>> void registerStore(StoreLoader loader, Class<T> clazz, IDataStorage storage) throws DatabaseException {
        storages.put(loader, new PlayerStorage<>(loader, clazz, storage));
    }

    public void registerStore(StoreLoader loader, PlayerStorage<? extends PlayerDataObject> manager) {
        storages.put(loader, manager);
    }

    public void unregisterStore(StoreLoader loader) {
        storages.remove(loader);
    }

    public <T extends PlayerDataObject<T>> PlayerStorage<T> getStorage(StoreLoader loader, Class<T> clazz) {
        PlayerStorage storage = storages.get(loader);
        if(storage.getClazz().getName().equals(clazz.getName())) {
            return storage;
        }
        return null;
    }

    /**
     * Load all players - not normally used as to load all players into memory will be wasteful
     */
    public void load() {
        //playerCache.clear();
        inTeleport.clear();
        //players.loadObjects().forEach(p -> playerCache.put(p.getPlayerUUID(), p));
        storages.values().forEach(PlayerStorage::load);
    }

    /**
     * Saves the player to the database async
     * @param playerUUID - the player's UUID
     */
    public void save(UUID playerUUID) {
        save(playerUUID, true);
    }

    /**
     * Saves the player to the database
     * @param playerUUID - the player's UUID
     * @param async - async
     */
    public void save(UUID playerUUID, boolean async) {
        //if (playerCache.containsKey(playerUUID)) {
            //players.saveObject(playerCache.get(playerUUID), async);
            //playerCache.remove(playerUUID);
            storages.forEach((l, s) -> s.save(playerUUID, async));
        //}
    }

    /**
     * Save all players
     */
    public void saveAll() {
        //Collections.unmodifiableCollection(playerCache.values()).forEach(p -> players.saveObject(p, false));
        storages.values().forEach(PlayerStorage::saveAll);
    }

    public void shutdown() {
        saveAll();
        //playerCache.clear();
        //players.disable();
        storages.forEach((l, s) -> s.getCache().clear());
        //storages.forEach((l, s) -> s.getTable().disable());
    }

    protected PlayerStorage<Players> getPlayersStorage() {
        return getStorage(plugin, Players.class);
    }

    protected CommonJsonTable<Players> getPlayersTable() {
        return getPlayersStorage().getTable();
    }

    protected Map<UUID, Players> getPlayersCache() {
        return getPlayersStorage().getCache();
    }

    /**
     * Get player by UUID. Adds player to cache if not in there already
     * @param uuid of player
     * @return player object or null if it does not exist
     */
    public Players getPlayer(UUID uuid) {
        if (!getPlayersCache().containsKey(uuid)) {
            addPlayer(uuid);
        }
        return getPlayersCache().get(uuid);
    }

    /**
     * Returns an <strong>unmodifiable collection</strong> of all the players that are <strong>currently in the cache</strong>.
     * @return unmodifiable collection containing every player in the cache.
     * @since 1.0.0-beta
     */
    public Collection<Players> getPlayers() {
        return Collections.unmodifiableCollection(getPlayersCache().values());
    }

    /*
     * Cache control methods
     */

    /**
     * Adds a player to the cache. If the UUID does not exist, a new player is made
     * @param playerUUID - the player's UUID
     */
    public void addPlayer(UUID playerUUID) {
        if (playerUUID == null) {
            return;
        }
        /*if (!playerCache.containsKey(playerUUID)) {
        	Players player;
            // If the player is in the database, load it, otherwise create a new player
            if (players.objectExists(playerUUID.toString())) {
                player = players.loadObject(playerUUID.toString());
                if (player == null) {
                    player = new Players().newInstance(playerUUID);
                    // Corrupted database entry
                    plugin.getLogger().severe("Corrupted player database entry for " + playerUUID + " - unrecoverable. Recreated.");
                    player.setUniqueId(playerUUID.toString());
                }
            } else {
                player = new Players().newInstance(playerUUID);
            }
            playerCache.put(playerUUID, player);*/
            storages.forEach((l, s) -> s.addPlayer(playerUUID));
        //}
    }

    /**
     * Checks if the player is known or not
     *
     * @param uniqueID - unique ID
     * @return true if player is known, otherwise false
     */
    public boolean isKnown(UUID uniqueID) {
        return uniqueID != null && (getPlayersCache().containsKey(uniqueID) || getPlayersTable().objectExists(uniqueID.toString()));
    }

    /**
     * Attempts to return a UUID for a given player's name.
     * @param name - name of player
     * @return UUID of player or null if unknown
     */
    public UUID getUUID(String name) {
        // See if this is a UUID
        // example: 5988eecd-1dcd-4080-a843-785b62419abb
        if (name.length() == 36 && name.contains("-")) {
            try {
                return UUID.fromString(name);
            } catch (Exception ignored) {
            	// Not used
            }
        }
        // Look in the name cache, then the data base and then give up
        return getPlayersCache().values().stream()
                .filter(p -> p.getPlayerName().equalsIgnoreCase(name)).findFirst()
                .map(p -> UUID.fromString(p.getUniqueId()))
                .orElse(names.objectExists(name) ? names.loadObject(name).getUuid() : null);
    }

    /**
     * Sets the player's name and updates the name>UUID database
     * @param user - the User
     */
    public void setPlayerName(User user) {
        addPlayer(user.getUniqueId());
        getPlayersCache().get(user.getUniqueId()).setPlayerName(user.getName());
        // Add to names database
        names.saveObject(new Names(user.getName(), user.getUniqueId()), null);
    }

    /**
     * Obtains the name of the player from their UUID
     * Player must have logged into the game before
     * @param playerUUID - the player's UUID
     * @return String - playerName, empty string if UUID is null
     */
    public String getName(UUID playerUUID) {
        if (playerUUID == null) {
            return "";
        }
        addPlayer(playerUUID);
        return getPlayersCache().get(playerUUID).getPlayerName();
    }

    /**
     * Returns the locale for this player. If missing, will return nothing
     * @param playerUUID - the player's UUID
     * @return name of the locale this player uses
     */
    public String getLocale(UUID playerUUID) {
        addPlayer(playerUUID);
        if (playerUUID == null) {
            return "";
        }
        return getPlayersCache().get(playerUUID).getLocale();
    }

    /**
     * Sets the locale this player wants to use
     * @param playerUUID - the player's UUID
     * @param localeName - locale name, e.g., en-US
     */
    public void setLocale(UUID playerUUID, String localeName) {
        addPlayer(playerUUID);
        getPlayersCache().get(playerUUID).setLocale(localeName);
    }

    /**
     * Sets if a player is mid-teleport or not
     * @param uniqueId - unique ID
     */
    public void setInTeleport(UUID uniqueId) {
        inTeleport.add(uniqueId);
    }

    /**
     * Removes player from in-teleport
     * @param uniqueId - unique ID
     */
    public void removeInTeleport(UUID uniqueId) {
        inTeleport.remove(uniqueId);
    }

    /**
     * @param uniqueId - unique ID
     * @return true if a player is mid-teleport
     */
    public boolean isInTeleport(UUID uniqueId) {
        return inTeleport.contains(uniqueId);
    }

    /**
     * Tries to get the user from his name
     * @param name - name
     * @return user - user
     */
    public User getUser(String name) {
        return getUser(getUUID(name));
    }

    /**
     * Tries to get the user from his UUID
     * @param uuid - UUID
     * @return user - user
     */
    public User getUser(UUID uuid) {
        return SimpleUser.getInstance(uuid);
    }

}
