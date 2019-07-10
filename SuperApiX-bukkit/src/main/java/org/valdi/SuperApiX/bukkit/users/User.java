package org.valdi.SuperApiX.bukkit.users;

import java.util.*;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.valdi.SuperApiX.bukkit.plugin.ISuperBukkitPlugin;

/**
 * Combines {@link Player}, {@link OfflinePlayer} and {@link CommandSender} to provide convenience methods related to
 * localization and generic interactions.
 * <br/>
 * Therefore, a User could usually be a Player, an OfflinePlayer or the server's console.
 * Preliminary checks should be performed before trying to run methods that relies on a specific implementation.
 * <br/><br/>
 * It is good practice to use the User instance whenever possible instead of Player or CommandSender.
 *
 * @author tastybento
 */
public interface User extends OfflineUser {

    Set<PermissionAttachmentInfo> getEffectivePermissions();

    PlayerInventory getInventory();

    Location getLocation();

    /**
     * @return true if this User is a player, false if not, e.g., console
     */
    boolean isPlayer();

    /**
     * @return true if this user is an OfflinePlayer, false if not, e.g., console
     * @since 1.0.0-beta
     */
    boolean isOfflinePlayer();

    CommandSender getSender();

    /**
     * @param permission - permission string
     * @return true if permission is empty or if the player has that permission
     */
    boolean hasPermission(String permission);

    /**
     * Checks if User is Op
     * @return true if User is Op
     */
    boolean isOp();

    /**
     * Get the maximum value of a numerical permission setting
     * @param permissionPrefix the start of the perm, e.g., superapix.maxhomes
     * @param defaultValue the default value; the result may be higher or lower than this
     * @return max value
     */
    int getPermissionValue(String permissionPrefix, int defaultValue);

    /**
     * Gets a translation of this reference for this User.
     * @param reference - reference found in a locale file
     * @param variables - variables to insert into translated string. Variables go in pairs, for example
     *                  "[name]", "tastybento"
     * @return Translated string with colors converted, or the reference if nothing has been found
     */
    String getTranslation(ISuperBukkitPlugin plugin, String reference, String... variables);

    /**
     * Gets a translation of this reference for this User.
     * @param reference - reference found in a locale file
     * @param variables - variables to insert into translated string. Variables go in pairs, for example
     *                  "[name]", "tastybento"
     * @return Translated string with colors converted, or a blank String if nothing has been found
     */
    String getTranslationOrNothing(ISuperBukkitPlugin plugin, String reference, String... variables);

    void sendMessage(BaseComponent... components);

    /**
     * Send a message to sender if message is not empty.
     * @param reference - language file reference
     * @param variables - CharSequence target, replacement pairs
     */
    void sendMessage(ISuperBukkitPlugin plugin, String reference, String... variables);

    /**
     * Sends a message to sender without any modification (colors, multi-lines, placeholders).
     * @param message - the message to send
     */
    void sendRawMessage(String message);

    /**
     * Sends a message to sender without any modification (colors, multi-lines, placeholders).
     * @param message - the message to send
     */
    void sendRawBar(String message);

    /**
     * Sends a message to sender if message is not empty and if the same wasn't sent within the previous {@link Notifier#NOTIFICATION_DELAY} seconds.
     * @param reference - language file reference
     * @param variables - CharSequence target, replacement pairs
     *
     * @see Notifier
     */
    void notify(ISuperBukkitPlugin plugin, String reference, String... variables);

    /**
     * Sends an actionBar to sender if message is not empty and if the same wasn't sent within the previous {@link Notifier#NOTIFICATION_DELAY} seconds.
     * @param reference - language file reference
     * @param variables - CharSequence target, replacement pairs
     *
     * @see Notifier
     */
    void notifyBar(ISuperBukkitPlugin plugin, String reference, String... variables);

    void notify(ISuperBukkitPlugin plugin, boolean bar, String reference, String... variables);

    void notify(ISuperBukkitPlugin plugin, Runnable task, boolean bar, String reference, String... variables);

    /**
     * Sets the User's game mode
     * @param mode - GameMode
     */
    void setGameMode(GameMode mode);

    /**
     * Teleports User to this location. If the User is in a vehicle, they will exit first.
     * @param location - the location
     */
    void teleport(Location location);

    /**
     * Gets the current world this entity resides in
     * @return World
     */
    World getWorld();

    /**
     * Closes the User's inventory
     */
    void closeInventory();

    /**
     * Get the User's locale
     * @return Locale
     */
    Locale getLocale();

    /**
     * Forces an update of the User's complete inventory.
     * Deprecated, but there is no current alternative.
     */
    void updateInventory();

    /**
     * Performs a command as the player
     * @param cmd - command to execute
     * @return true if the command was successful, otherwise false
     */
    boolean performCommand(String cmd);

    /**
     * Spawn particles to the player.
     * They are only displayed if they are within the server's view distance.
     * @param particle Particle to display.
     * @param dustOptions Particle.DustOptions for the particle to display.
     *                    Cannot be null when particle is {@link Particle#REDSTONE}.
     * @param x X coordinate of the particle to display.
     * @param y Y coordinate of the particle to display.
     * @param z Z coordinate of the particle to display.
     */
    void spawnParticle(Particle particle, Object dustOptions, double x, double y, double z);

    /**
     * Spawn particles to the player.
     * They are only displayed if they are within the server's view distance.
     * @param particle Particle to display.
     * @param dustOptions Particle.DustOptions for the particle to display.
     *                    Cannot be null when particle is {@link Particle#REDSTONE}.
     * @param x X coordinate of the particle to display.
     * @param y Y coordinate of the particle to display.
     * @param z Z coordinate of the particle to display.
     */
    void spawnParticle(Particle particle, Object dustOptions, int x, int y, int z);

    String getPrefix();

    String getSuffix();

    String getGroup();

    List<String> getGroups();

    boolean inGroups(String group);

}
