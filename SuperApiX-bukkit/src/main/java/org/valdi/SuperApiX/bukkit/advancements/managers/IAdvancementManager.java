package org.valdi.SuperApiX.bukkit.advancements.managers;

import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperKey;

import java.util.*;

public interface IAdvancementManager<T extends AbstractAdvancement> {

    /**
     * @return All players that have been added to the manager
     */
    ArrayList<Player> getPlayers();

    /**
     * Adds a player to the manager
     *
     * @param player Player to add
     */
    void addPlayer(Player player);

    /**
     * Removes a player from the manager
     *
     * @param player Player to remove
     */
    void removePlayer(Player player);

    /**
     * Adds advancements or updates one advancement
     *
     * @param advancementsAdded An array of all advancements that should be added<br>If you want to update the display of an advancement, the array must have a length of 1
     */
     void addAdvancement(T... advancementsAdded);

    /**
     * Removes an advancement from the manager
     *
     * @param advancementsRemoved An array of advancements that should be removed
     */
     void removeAdvancement(T... advancementsRemoved);

     default void removeAdvancements() {
         this.getAdvancements().forEach(a -> this.removeAdvancement(a));
     }

    /**
     * Updates/Refreshes the player
     *
     * @param player Player to update
     */
    void update(Player player);

    /**
     * Updates/Refreshes the player
     *
     * @param player Player to update
     * @param tab Tab to update
     */
    void update(Player player, SuperKey tab);

    /**
     * Updates advancement progress for a player
     *
     * @param player Player to update
     * @param advancementsUpdated An array of advancement to update progress
     */
     void updateProgress(Player player, T... advancementsUpdated);

    /**
     * Updates all possibly affected visibilities for all parents and childs
     *
     * @param player Player to update
     * @param from T to check from
     */
     void updateAllPossiblyAffectedVisibilities(Player player, T from);

    /**
     * Updates the visibility
     *
     * @param player Player to update
     * @param advancement T to update
     */
     void updateVisibility(Player player, T advancement);

    /**
     * @return A list of all advancements in the manager
     */
     ArrayList<T> getAdvancements();

    /**
     * @param namespace Namespace to check
     * @return A list of all advancements in the manager with a specified namespace
     */
     ArrayList<T> getAdvancements(String namespace);

    /**
     * @param name Name to check
     * @return An advancement matching the given name or null if it doesn't exist in the AdvancementManager
     */
     T getAdvancement(SuperKey name);

    /**
     * Displays a message to all players in the manager<br>
     * Note that this doesn't grant the advancement
     *
     * @param player Player which has received an advancement
     * @param advancement T Player has received
     */
     void displayMessage(Player player, T advancement);

    /**
     *
     * @return true if advancement messages will be shown by default in this manager<br>false if advancement messages will never be shown in this manager
     */
    boolean isAnnounceAdvancementMessages();

    /**
     * Changes if advancement messages will be shown by default in this manager
     *
     * @param value
     */
    void setAnnounceAdvancementMessages(boolean value);

    /**
     * Makes the AdvancementManager accessible
     *
     * @param name Unique Name, case insensitive
     */
    void makeAccessible(String name);

    /**
     * Resets Accessibility-Status and Name
     *
     */
    void resetAccessible();

    /**
     * Returns the Unique Name if AdvancementManager is accessible
     *
     * @return Name or null if not accessible
     */
    String getName();

    /**
     * Grants an advancement
     *
     * @param player Reciever
     * @param advancement T to grant
     */
     void grantAdvancement(Player player, T advancement);

    /**
     * Grants an advancement, also works with offline players
     *
     * @param uuid Receiver UUID
     * @param advancement T to grant
     */
     void grantAdvancement(UUID uuid, T advancement);

    /**
     * Revokes an advancement
     *
     * @param player Receiver
     * @param advancement T to revoke
     */
     void revokeAdvancement(Player player, T advancement);

    /**
     * Revokes an advancement, also works with offline players
     *
     * @param uuid Receiver UUID
     * @param advancement T to revoke
     */
     void revokeAdvancement(UUID uuid, T advancement);

    /**
     * Grants criteria for an advancement
     *
     * @param player Receiver
     * @param advancement
     * @param criteria Array of criteria to grant
     */
     void grantCriteria(Player player, T advancement, String... criteria);

    /**
     * Grans criteria for an advancement, also works with offline players
     *
     * @param uuid
     * @param advancement
     * @param criteria
     */
     void grantCriteria(UUID uuid, T advancement, String... criteria);

    /**
     * Revokes criteria for an advancement
     *
     * @param player Receiver
     * @param advancement
     * @param criteria Array of criteria to revoke
     */
     void revokeCriteria(Player player, T advancement, String... criteria);

    /**
     * Revokes criteria for an advancement, also works with offline players
     *
     * @param uuid Receiver UUID
     * @param advancement
     * @param criteria Array of criteria to revoke
     */
     void revokeCriteria(UUID uuid, T advancement, String... criteria);

    /**
     *
     * @param player Player to check
     * @return A JSON String representation of the progress for a player
     */
    String getProgressJSON(Player player);

    /**
     *
     * @param player Player to check
     * @param namespace Namespace to check
     * @return A JSON String representation of the progress for a player in a specified namespace
     */
    String getProgressJSON(Player player, String namespace);

    /**
     * Sets the criteria progress for an advancement<br>
     * Might not work as expected when using features for experts<br>
     * Is the only method triggering CriteriaProgressChangeEvent
     *
     * @param player Receiver
     * @param advancement
     * @param progress
     */
     void setCriteriaProgress(Player player, T advancement, int progress);

    /**
     * Sets the criteria progress for an advancement, also works for offline players<br>
     * Might not work as expected when using features for experts<br>
     * Is the only method triggering CriteriaProgressChangeEvent
     *
     * @param uuid Receiver UUID
     * @param advancement
     * @param progress
     */
     void setCriteriaProgress(UUID uuid, T advancement, int progress);

    /**
     *
     * @param player
     * @param advancement
     * @return The criteria progress
     */
     int getCriteriaProgress(Player player, T advancement);

    /**
     *
     * @param uuid
     * @param advancement
     * @return The criteria progress
     */
     int getCriteriaProgress(UUID uuid, T advancement);

    /**
     * Saves the progress
     *
     * @param player Player to check
     * @param namespace Namespace to check
     */
    void saveProgress(Player player, String namespace);

    // Load Progress

    /**
     * Loads the progress
     *
     * @param player Player to check
     * @param namespace Namespace to check
     */
    void loadProgress(Player player, String namespace);

    /**
     * Loads the progress
     *
     * @param player Player to check
     * @param advancementsLoaded Array of advancements to check, all advancements which arent in the same namespace as the first one will be ignored
     */
     void loadProgress(Player player, T... advancementsLoaded);

    /**
     * Loads the progress with a custom JSON String
     *
     * @param player Player to check
     * @param json JSON String to load from
     * @param advancementsLoaded Array of advancements to check
     */
     void loadCustomProgress(Player player, String json, T... advancementsLoaded);

    /**
     * Loads the progress with a custom JSON String
     *
     * @param player Player to check
     * @param json JSON String to load from
     */
    void loadCustomProgress(Player player, String json);

    /**
     * Loads the progress with a custom JSON String
     *
     * @param player Player to check
     * @param json JSON String to load from
     * @param namespace Namespace to check
     */
    void loadCustomProgress(Player player, String json, String namespace);

    /**
     *
     * @param uuid Player UUID to check
     * @return A JSON String representation of the progress for a player
     */
    String getProgressJSON(UUID uuid);

    /**
     *
     * @param uuid Player UUID to check
     * @param namespace Namespace to check
     * @return A JSON String representation of the progress for a player in a specified namespace
     */
    String getProgressJSON(UUID uuid, String namespace);

    /**
     * Saves the progress
     *
     * @param uuid Player UUID to check
     * @param namespace Namespace to check
     */
    void saveProgress(UUID uuid, String namespace);

    //Load Progress

    /**
     * Loads the progress<br>
     * <b>Recommended to only load progress for online players!</b>
     *
     * @param uuid Player UUID to check
     * @param namespace Namespace to check
     */
    @Deprecated
    void loadProgress(UUID uuid, String namespace);

    /**
     * Loads the progress<br>
     * <b>Recommended to only load progress for online players!</b>
     *
     * @param uuid Player UUID to check
     * @param advancementsLoaded Array of advancements to check, all advancements which arent in the same namespace as the first one will be ignored
     */
    @Deprecated
     void loadProgress(UUID uuid, T... advancementsLoaded);

    /**
     * Loads the progress with a custom JSON String<br>
     * <b>Recommended to only load progress for online players!</b>
     *
     * @param uuid Player UUID to check
     * @param json JSON String to load from
     * @param advancementsLoaded Array of advancements to check
     */
    @Deprecated
     void loadCustomProgress(UUID uuid, String json, T... advancementsLoaded);

    /**
     * Loads the progress with a custom JSON String<br>
     * <b>Recommended to only load progress for online players!</b>
     *
     * @param uuid Player UUID to check
     * @param json JSON String to load from
     */
    @Deprecated
    void loadCustomProgress(UUID uuid, String json);

    /**
     * Loads the progress with a custom JSON String<br>
     * <b>Recommended to only load progress for online players!</b>
     *
     * @param uuid Player UUID to check
     * @param json JSON String to load from
     * @param namespace Namespace to check
     */
    @Deprecated
    void loadCustomProgress(UUID uuid, String json, String namespace);

    //Unload Progress

    /**
     * Unloads the progress for all advancements in the manager<br>
     * <b>Does not work for Online Players!</b>
     *
     * @param uuid Affected Player UUID
     */
    void unloadProgress(UUID uuid);

    /**
     * Unloads the progress for all advancements in the manager with a specified namespace<br>
     * <b>Does not work for Online Players!</b>
     *
     * @param uuid Affected Player UUID
     * @param namespace Specific Namespace
     */
    void unloadProgress(UUID uuid, String namespace);

    /**
     * Unloads the progress for the given advancements<br>
     * <b>Does not work for Online Players!</b>
     *
     * @param uuid Affected Player UUID
     * @param advancements Specific Advancement
     */
     void unloadProgress(UUID uuid, T... advancements);

}
