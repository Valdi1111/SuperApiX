package org.valdi.SuperApiX.bukkit.users;

import net.md_5.bungee.api.chat.BaseComponent;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.util.Vector;
import org.valdi.SuperApiX.bukkit.plugin.ISuperBukkitPlugin;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.api.ActionBar;

import java.util.*;
import java.util.stream.Collectors;

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
public class SimpleUser implements User {
    protected static Map<UUID, SimpleUser> users = new HashMap<>();

    /**
     * Clears all Users from the User list
     */
    public static void clearUsers() {
        users.clear();
    }

    /**
     * Gets an instance of User from a CommandSender
     * @param sender - command sender, e.g. console
     * @return User - User
     */
    public static User getInstance(CommandSender sender) {
        if (sender instanceof Player) {
            return getInstance((Player)sender);
        }
        // Console
        return new SimpleUser(sender);
    }

    /**
     * Gets an instance of User from a Player object
     * @param player - the player
     * @return User - User
     */
    public static User getInstance(Player player) {
        if (player == null) {
            return null;
        }
        if (users.containsKey(player.getUniqueId())) {
            return users.get(player.getUniqueId());
        }
        return new SimpleUser(player);
    }

    /**
     * Gets an instance of User from a UUID
     * @param uuid - UUID
     * @return User - User
     */
    public static User getInstance(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        if (users.containsKey(uuid)) {
            return users.get(uuid);
        }
        // Return player, or null if they are not online
        return new SimpleUser(uuid);
    }

    /**
     * Gets an instance of User from an OfflinePlayer
     * @param offlinePlayer offline Player
     * @return user
     * @since 1.0.0-beta
     */
    public static User getInstance(OfflinePlayer offlinePlayer) {
        if (offlinePlayer == null) {
            return null;
        }
        return getInstance(offlinePlayer.getUniqueId());
    }

    /**
     * Removes this player from the User cache
     * @param player - the player
     */
    public static void removePlayer(Player player) {
        if (player != null) {
            removePlayer(player.getUniqueId());
        }
    }

    /**
     * Removes this player from the User cache
     * @param playerUUID - the player's uuid
     */
    public static void removePlayer(UUID playerUUID) {
        if (playerUUID != null) {
            users.remove(playerUUID);
        }
    }

    /**
     * Removes this player from the User cache
     * @param offlinePlayer - the player
     */
    public static void removePlayer(OfflinePlayer offlinePlayer) {
        if (offlinePlayer != null) {
            removePlayer(offlinePlayer.getUniqueId());
        }
    }


    //


    private static SuperApiBukkit plugin = SuperApiBukkit.getInstance();

    private Player player;
    private OfflinePlayer offlinePlayer;
    private final UUID playerUUID;
    private final CommandSender sender;

    protected SimpleUser(CommandSender sender) {
        this.player = null;
        this.playerUUID = null;
        this.sender = sender;
    }

    protected SimpleUser(Player player) {
        if(player == null) {
            throw new IllegalArgumentException();
        }

        this.playerUUID = player.getUniqueId();
        this.player = player;
        this.offlinePlayer = player;
        this.sender = player;
        users.put(playerUUID, this);
    }

    protected SimpleUser(UUID playerUUID) {
        this(Bukkit.getPlayer(playerUUID));
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return sender.getEffectivePermissions();
    }

    @Override
    public PlayerInventory getInventory() {
        return player != null ? player.getInventory() : null;
    }

    @Override
    public Location getLocation() {
        return player != null ? player.getLocation() : null;
    }

    @Override
    public String getName() {
        String name = getOfflinePlayer().getName();
        if(name == null) {
            name =  SuperApiBukkit.getInstance().getPlayersManager().getName(playerUUID);
        }

        return name;
    }

    /**
     * @return the player
     */
    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public long getFirstPlayed() {
        return player.getFirstPlayed();
    }

    @Override
    public long getLastPlayed() {
        return player.getLastPlayed();
    }

    @Override
    public boolean hasPlayedBefore() {
        return player.hasPlayedBefore();
    }

    @Override
    public Location getBedSpawnLocation() {
        return player.getBedSpawnLocation();
    }

    @Override
    public User getUser() {
        return this;
    }

    /**
     * @return true if this User is a player, false if not, e.g., console
     */
    @Override
    public boolean isPlayer() {
        return player != null;
    }

    /**
     * @return the offline player
     * @since 1.0.0-beta
     */
    @Override
    public OfflinePlayer getOfflinePlayer() {
        return offlinePlayer;
    }

    /**
     * @return true if this user is an OfflinePlayer, false if not, e.g., console
     * @since 1.0.0-beta
     */
    @Override
    public boolean isOfflinePlayer() {
        return offlinePlayer != null;
    }

    @Override
    public CommandSender getSender() {
        return sender;
    }

    @Override
    public UUID getUniqueId() {
        return playerUUID;
    }

    @Override
    public boolean isBanned() {
        if (playerUUID != null && offlinePlayer != null) {
            return offlinePlayer.isBanned();
        }
        return false;
    }

    @Override
    public boolean isWhitelisted() {
        if (playerUUID != null && offlinePlayer != null) {
            return offlinePlayer.isWhitelisted();
        }
        return false;
    }

    @Override
    public void setWhitelisted(boolean value) {
        if (playerUUID != null && offlinePlayer != null) {
            offlinePlayer.setWhitelisted(value);
        }
    }

    /**
     * @param permission - permission string
     * @return true if permission is empty or if the player has that permission
     */
    @Override
    public boolean hasPermission(String permission) {
        return permission.isEmpty() || isOp() || sender.hasPermission(permission);
    }

    @Override
    public boolean isOnline() {
        return offlinePlayer != null && offlinePlayer.isOnline();
    }

    /**
     * Checks if User is Op
     * @return true if User is Op
     */
    @Override
    public boolean isOp() {
        if (sender != null) {
            return sender.isOp();
        }
        if (playerUUID != null && offlinePlayer != null) {
            return offlinePlayer.isOp();
        }
        return false;
    }

    @Override
    public void setOp(boolean value) {
        if (sender != null) {
            sender.setOp(value);
            return;
        }
        if (playerUUID != null && offlinePlayer != null) {
            offlinePlayer.setOp(value);
        }
    }

    /**
     * Get the maximum value of a numerical permission setting
     * @param permissionPrefix the start of the perm, e.g., superapix.maxhomes
     * @param defaultValue the default value; the result may be higher or lower than this
     * @return max value
     */
    @Override
    public int getPermissionValue(String permissionPrefix, int defaultValue) {
        int value = defaultValue;

        // If there is a dot at the end of the permissionPrefix, remove it
        if (permissionPrefix.endsWith(".")) {
            permissionPrefix = permissionPrefix.substring(0, permissionPrefix.length()-1);
        }
        final String permPrefix = permissionPrefix + ".";

        List<String> permissions = player.getEffectivePermissions().stream()
                .map(PermissionAttachmentInfo::getPermission)
                .filter(permission -> permission.startsWith(permPrefix))
                .collect(Collectors.toList());

        for (String permission : permissions) {
            if (permission.contains(permPrefix + "*")) {
                // 'Star' permission
                return value;
            } else {
                String[] spl = permission.split(permPrefix);
                if (spl.length > 1) {
                    if (!NumberUtils.isNumber(spl[1])) {
                        plugin.getLogger().severe("Player " + player.getName() + " has permission: '" + permission + "' <-- the last part MUST be a number! Ignoring...");
                    } else {
                        int v = Integer.parseInt(spl[1]);
                        if (v < 0) {
                            return v;
                        }
                        value = Math.max(value, v);
                    }
                }
            }
            // Do some sanity checking
            if (value < 1) {
                value = 1;
            }
        }
        return value;
    }

    /**
     * Gets a translation of this reference for this User.
     * @param reference - reference found in a locale file
     * @param variables - variables to insert into translated string. Variables go in pairs, for example
     *                  "[name]", "tastybento"
     * @return Translated string with colors converted, or the reference if nothing has been found
     */
    @Override
    public String getTranslation(ISuperBukkitPlugin plugin, String reference, String... variables) {
        // Get translation
        String translation = plugin.getLocalesManager().get(this, reference);

        // If no translation has been found, return the reference for debug purposes.
        if (translation == null) {
            plugin.getLogger().severe("Locale not found for " + this.toString());
            return reference;
        }

        // Then replace variables
        if (variables.length > 1) {
            for (int i = 0; i < variables.length; i += 2) {
                translation = translation.replace(variables[i], variables[i+1]);
            }
        }

        // Replace placeholders
        //translation = PlaceholderHandler.replacePlaceholders(this, translation);

        return ChatColor.translateAlternateColorCodes('&', translation);
    }

    /**
     * Gets a translation of this reference for this User.
     * @param reference - reference found in a locale file
     * @param variables - variables to insert into translated string. Variables go in pairs, for example
     *                  "[name]", "tastybento"
     * @return Translated string with colors converted, or a blank String if nothing has been found
     */
    @Override
    public String getTranslationOrNothing(ISuperBukkitPlugin plugin, String reference, String... variables) {
        String translation = getTranslation(plugin, reference, variables);
        return translation.equals(reference) ? "" : translation;
    }

    @Override
    public void sendMessage(BaseComponent... components) {
        if (sender != null) {
            sender.spigot().sendMessage(components);
        }
    }

    /**
     * Send a message to sender if message is not empty.
     * @param reference - language file reference
     * @param variables - CharSequence target, replacement pairs
     */
    @Override
    public void sendMessage(ISuperBukkitPlugin plugin, String reference, String... variables) {
        String message = getTranslation(plugin, reference, variables);
        if (!ChatColor.stripColor(message).trim().isEmpty() && sender != null) {
            sender.sendMessage(message);
        }
    }

    /**
     * Sends a message to sender without any modification (colors, multi-lines, placeholders).
     * @param message - the message to send
     */
    @Override
    public void sendRawMessage(String message) {
        if (sender != null) {
            sender.sendMessage(message);
        }
    }

    /**
     * Sends a message to sender without any modification (colors, multi-lines, placeholders).
     * @param message - the message to send
     */
    @Override
    public void sendRawBar(String message) {
        if (sender != null) {
            if(!(sender instanceof Player)) {
                this.sendRawMessage(message);
                return;
            }

            ActionBar.builder().message(message).send((Player) sender);
        }
    }

    /**
     * Sends a message to sender if message is not empty and if the same wasn't sent within the previous {@link Notifier#NOTIFICATION_DELAY} seconds.
     * @param reference - language file reference
     * @param variables - CharSequence target, replacement pairs
     *
     * @see Notifier
     */
    @Override
    public void notify(ISuperBukkitPlugin plugin, String reference, String... variables) {
        this.notify(plugin, false, reference, variables);
    }

    /**
     * Sends an actionBar to sender if message is not empty and if the same wasn't sent within the previous {@link Notifier#NOTIFICATION_DELAY} seconds.
     * @param reference - language file reference
     * @param variables - CharSequence target, replacement pairs
     *
     * @see Notifier
     */
    @Override
    public void notifyBar(ISuperBukkitPlugin plugin, String reference, String... variables) {
        this.notify(plugin, true, reference, variables);
    }

    @Override
    public void notify(ISuperBukkitPlugin plugin, boolean bar, String reference, String... variables) {
        this.notify(plugin, null, bar, reference, variables);
    }

    @Override
    public void notify(ISuperBukkitPlugin plugin, Runnable task, boolean bar, String reference, String... variables) {
        String message = getTranslation(plugin, reference, variables);
        if (!ChatColor.stripColor(message).trim().isEmpty() && sender != null) {
            plugin.getNotifier().notify(this, message, task, bar);
        }
    }

    /**
     * Sets the User's game mode
     * @param mode - GameMode
     */
    @Override
    public void setGameMode(GameMode mode) {
        if (playerUUID != null && player != null) {
            player.setGameMode(mode);
        }
    }

    /**
     * Teleports User to this location. If the User is in a vehicle, they will exit first.
     * @param location - the location
     */
    @Override
    public void teleport(Location location) {
        if (playerUUID != null && player != null) {
            player.teleport(location);
        }
    }

    /**
     * Gets the current world this entity resides in
     * @return World
     */
    @Override
    public World getWorld() {
        if (playerUUID != null && player != null) {
            return player.getWorld();
        }
        return null;
    }

    /**
     * Closes the User's inventory
     */
    @Override
    public void closeInventory() {
        if (playerUUID != null && player != null) {
            player.closeInventory();
        }
    }

    /**
     * Get the User's locale
     * @return Locale
     */
    @Override
    public Locale getLocale() {
        if (sender instanceof Player && !plugin.getPlayersManager().getLocale(playerUUID).isEmpty()) {
            return Locale.forLanguageTag(plugin.getPlayersManager().getLocale(playerUUID));
        }
        return Locale.forLanguageTag(plugin.getSettings().getDefaultLanguage());
    }

    /**
     * Forces an update of the User's complete inventory.
     * Deprecated, but there is no current alternative.
     */
    @Override
    public void updateInventory() {
        if (playerUUID != null && player != null) {
            player.updateInventory();
        }
    }

    /**
     * Performs a command as the player
     * @param cmd - command to execute
     * @return true if the command was successful, otherwise false
     */
    @Override
    public boolean performCommand(String cmd) {
        if (playerUUID != null && player != null) {
            return player.performCommand(cmd);
        }
        return false;
    }

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
    @Override
    public void spawnParticle(Particle particle, Object dustOptions, double x, double y, double z) {
        if (particle.equals(Particle.REDSTONE) && dustOptions == null) {
            // Security check that will avoid later unexpected exceptions.
            throw new IllegalArgumentException("A non-null Particle.DustOptions must be provided when using Particle.REDSTONE as particle.");
        }
        // Check if this particle is beyond the viewing distance of the server
        if (playerUUID != null && player != null && player.getLocation().toVector().distanceSquared(new Vector(x,y,z)) < (Bukkit.getServer().getViewDistance()*256*Bukkit.getServer().getViewDistance())) {
            if (particle.equals(Particle.REDSTONE)) {
                player.spawnParticle(particle, x, y, z, 1, 0, 0, 0, 1, dustOptions);
            } else {
                player.spawnParticle(particle, x, y, z, 1);
            }
        }
    }

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
    @Override
    public void spawnParticle(Particle particle, Object dustOptions, int x, int y, int z) {
        spawnParticle(particle, dustOptions, (double) x, (double) y, (double) z);
    }

    @Override
    public String getPrefix() {
        return plugin.getVault().getPrefix(this);
    }

    @Override
    public String getSuffix() {
        return plugin.getVault().getSuffix(this);
    }

    @Override
    public String getGroup() {
        return plugin.getVault().getGroup(this);
    }

    @Override
    public List<String> getGroups() {
        return plugin.getVault().getGroups(this);
    }

    @Override
    public boolean inGroups(String group) {
        return plugin.getVault().inGroup(this, group);
    }

    @Override
    public String toString() {
        return "User[name=" + this.getName() + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((playerUUID == null) ? 0 : playerUUID.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SimpleUser)) {
            return false;
        }
        SimpleUser other = (SimpleUser) obj;
        if (playerUUID == null) {
            return other.playerUUID == null;
        } else return playerUUID.equals(other.playerUUID);
    }

    @Override
    public Map<String, Object> serialize() {
        return null;
    }
}
