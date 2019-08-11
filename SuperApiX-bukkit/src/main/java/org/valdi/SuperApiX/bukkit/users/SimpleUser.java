package org.valdi.SuperApiX.bukkit.users;

import net.md_5.bungee.api.chat.BaseComponent;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.valdi.SuperApiX.bukkit.plugin.BukkitStoreLoader;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.api.ActionBar;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
    protected static Map<UUID, SimpleUser> users = new ConcurrentHashMap<>();

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
        if (sender == null) {
            return null;
        }
        if(sender instanceof Player) {
            return getInstance((Player) sender);
        }
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
     * Gets an instance of User from an OfflinePlayer
     * @param offlinePlayer offline Player
     * @return user
     * @since 1.0.0-beta
     */
    public static User getInstance(OfflinePlayer offlinePlayer) {
        if (offlinePlayer == null) {
            return null;
        }
        if(offlinePlayer.isOnline()) {
            return getInstance(offlinePlayer.getPlayer());
        }
        if (users.containsKey(offlinePlayer.getUniqueId())) {
            return users.get(offlinePlayer.getUniqueId());
        }
        return new SimpleUser(offlinePlayer);
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
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        if(offlinePlayer.isOnline()) {
            return getInstance(offlinePlayer.getPlayer());
        }
        return getInstance(offlinePlayer);
    }

    /**
     * Removes this player from the User cache
     * @param player - the player
     */
    public static void remove(Player player) {
        if (player != null) {
            remove(player.getUniqueId());
        }
    }

    /**
     * Removes this player from the User cache
     * @param playerUUID - the player's uuid
     */
    public static void remove(UUID playerUUID) {
        if (playerUUID != null) {
            users.remove(playerUUID);
        }
    }

    private final UUID playerUUID;

    protected SimpleUser(CommandSender sender) {
        this.playerUUID = null;
    }

    protected SimpleUser(OfflinePlayer offlinePlayer) {
        this.playerUUID = offlinePlayer.getUniqueId();
    }

    protected SimpleUser(Player player) {
        this.playerUUID = player.getUniqueId();
        users.put(playerUUID, this);
    }

    @Override
    public boolean isConsole() {
        return playerUUID == null;
    }

    @Override
    public CommandSender getSender() {
        if(isConsole()) {
            return Bukkit.getServer().getConsoleSender();
        }
        if(isPlayer()) {
            return getPlayer();
        }
        return null;
    }

    @Override
    public boolean isOfflinePlayer() {
        return playerUUID != null;
    }

    @NotNull
    @Override
    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(playerUUID);
    }

    /**
     * @return true if this User is a player, false if not, e.g., console
     */
    @Override
    public boolean isPlayer() {
        return isOfflinePlayer() && getOfflinePlayer().isOnline();
    }

    /**
     * @return the player
     */
    @NotNull
    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(playerUUID);
    }

    @Override
    public User getUser() {
        return this;
    }

    @Override
    public UUID getUniqueId() {
        return playerUUID;
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        CommandSender sender = getSender();
        if(sender != null) {
            return sender.getEffectivePermissions();
        }
        return null;
    }

    @Override
    public PlayerInventory getInventory() {
        return isPlayer() ? getPlayer().getInventory() : null;
    }

    @Override
    public Location getLocation() {
        return isPlayer() ? getPlayer().getLocation() : null;
    }

    @Override
    public String getName() {
        if(isConsole()) {
            return getSender().getName();
        }
        String name = getOfflinePlayer().getName();
        if(name == null) {
            name =  SuperApiBukkit.getInstance().getPlayersManager().getName(playerUUID);
        }

        return name;
    }

    @Override
    public long getFirstPlayed() {
        return isOfflinePlayer() ? getOfflinePlayer().getFirstPlayed() : 0L;
    }

    @Override
    public long getLastPlayed() {
        return isOfflinePlayer() ? getOfflinePlayer().getLastPlayed() : 0L;
    }

    @Override
    public boolean hasPlayedBefore() {
        return isOfflinePlayer() && getOfflinePlayer().hasPlayedBefore();
    }

    @Override
    public Location getBedSpawnLocation() {
        return isOfflinePlayer() ? getOfflinePlayer().getBedSpawnLocation() : null;
    }

    @Override
    public boolean isBanned() {
        return isOfflinePlayer() && getOfflinePlayer().isBanned();
    }

    @Override
    public boolean isWhitelisted() {
        return isOfflinePlayer() && getOfflinePlayer().isWhitelisted();
    }

    @Override
    public void setWhitelisted(boolean value) {
        if(!isOfflinePlayer()) {
            return;
        }
        getOfflinePlayer().setWhitelisted(value);
    }

    /**
     * @param permission - permission string
     * @return true if permission is empty or if the player has that permission
     */
    @Override
    public boolean hasPermission(String permission) {
        CommandSender sender = getSender();
        if(sender == null) {
            return false;
        }
        return permission == null || permission.isEmpty() || isOp() || sender.hasPermission(permission);
    }

    @Override
    public boolean isOnline() {
        return isConsole() || getOfflinePlayer().isOnline();
    }

    /**
     * Checks if User is Op
     * @return true if User is Op
     */
    @Override
    public boolean isOp() {
        CommandSender sender = getSender();
        if(sender == null) {
            return getOfflinePlayer().isOp();
        }
        return sender.isOp();
    }

    @Override
    public void setOp(boolean value) {
        CommandSender sender = getSender();
        if(sender == null) {
            getOfflinePlayer().setOp(value);
            return;
        }
        sender.setOp(value);
    }

    /**
     * Get the maximum value of a numerical permission setting
     * @param permissionPrefix the start of the perm, e.g., superapix.maxhomes
     * @param defaultValue the default value; the result may be higher or lower than this
     * @return max value
     */
    @Override
    public int getPermissionValue(String permissionPrefix, int defaultValue) {
        CommandSender sender = getSender();
        if(sender == null) {
            return 0;
        }

        int value = defaultValue;

        // If there is a dot at the end of the permissionPrefix, remove it
        if (permissionPrefix.endsWith(".")) {
            permissionPrefix = permissionPrefix.substring(0, permissionPrefix.length()-1);
        }
        final String permPrefix = permissionPrefix + ".";

        List<String> permissions = sender.getEffectivePermissions().stream()
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
                        SuperApiBukkit.getInstance().getLogger().severe("Sender " + sender.getName() + " has permission: '" + permission + "' <-- the last part MUST be a number! Ignoring...");
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
    public String getTranslation(BukkitStoreLoader plugin, String reference, String... variables) {
        if(plugin == null) {
            plugin = SuperApiBukkit.getInstance();
        }

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
    public String getTranslationOrNothing(BukkitStoreLoader plugin, String reference, String... variables) {
        if(plugin == null) {
            plugin = SuperApiBukkit.getInstance();
        }

        String translation = getTranslation(plugin, reference, variables);
        return translation.equals(reference) ? "" : translation;
    }

    @Override
    public void sendMessage(BaseComponent... components) {
        CommandSender sender = getSender();
        if(sender == null) {
            return;
        }
        sender.spigot().sendMessage(components);
    }

    /**
     * Send a message to sender if message is not empty.
     * @param reference - language file reference
     * @param variables - CharSequence target, replacement pairs
     */
    @Override
    public void sendMessage(BukkitStoreLoader plugin, String reference, String... variables) {
        CommandSender sender = getSender();
        if(sender == null) {
            return;
        }

        if(plugin == null) {
            plugin = SuperApiBukkit.getInstance();
        }

        String message = getTranslation(plugin, reference, variables);
        if (!ChatColor.stripColor(message).trim().isEmpty()) {
            sender.sendMessage(message);
        }
    }

    /**
     * Sends a message to sender without any modification (colors, multi-lines, placeholders).
     * @param message - the message to send
     */
    @Override
    public void sendRawMessage(String message) {
        CommandSender sender = getSender();
        if(sender == null) {
            return;
        }

        sender.sendMessage(message);
    }

    /**
     * Sends a message to sender without any modification (colors, multi-lines, placeholders).
     * @param message - the message to send
     */
    @Override
    public void sendRawBar(String message) {
        if(!isPlayer()) {
            this.sendRawMessage(message);
            return;
        }

        ActionBar.builder().message(message).send(getPlayer());
    }

    /**
     * Sends a message to sender if message is not empty and if the same wasn't sent within the previous {@link Notifier#NOTIFICATION_DELAY} seconds.
     * @param reference - language file reference
     * @param variables - CharSequence target, replacement pairs
     *
     * @see Notifier
     */
    @Override
    public void notify(BukkitStoreLoader plugin, String reference, String... variables) {
        if(plugin == null) {
            plugin = SuperApiBukkit.getInstance();
        }

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
    public void notifyBar(BukkitStoreLoader plugin, String reference, String... variables) {
        if(plugin == null) {
            plugin = SuperApiBukkit.getInstance();
        }

        this.notify(plugin, true, reference, variables);
    }

    @Override
    public void notify(BukkitStoreLoader plugin, boolean bar, String reference, String... variables) {
        if(plugin == null) {
            plugin = SuperApiBukkit.getInstance();
        }

        this.notify(plugin, null, bar, reference, variables);
    }

    @Override
    public void notify(BukkitStoreLoader plugin, Runnable task, boolean bar, String reference, String... variables) {
        if(plugin == null) {
            plugin = SuperApiBukkit.getInstance();
        }

        String message = getTranslation(plugin, reference, variables);
        if (!ChatColor.stripColor(message).trim().isEmpty()) {
            plugin.getNotifier().notify(this, message, task, bar);
        }
    }

    /**
     * Sets the User's game mode
     * @param mode - GameMode
     */
    @Override
    public void setGameMode(GameMode mode) {
        if (!isPlayer()) {
            return;
        }

        getPlayer().setGameMode(mode);
    }

    /**
     * Teleports User to this location. If the User is in a vehicle, they will exit first.
     * @param location - the location
     */
    @Override
    public void teleport(Location location) {
        if (!isPlayer()) {
            return;
        }

        getPlayer().teleport(location);
    }

    /**
     * Gets the current world this entity resides in
     * @return World
     */
    @Override
    public World getWorld() {
        return isPlayer() ? getPlayer().getWorld() : null;
    }

    /**
     * Closes the User's inventory
     */
    @Override
    public void closeInventory() {
        if (!isPlayer()) {
            return;
        }

        getPlayer().closeInventory();
    }

    /**
     * Get the User's locale
     * @return Locale
     */
    @Override
    public Locale getLocale() {
        String locale = SuperApiBukkit.getInstance().getPlayersManager().getLocale(playerUUID);
        if (isPlayer() && locale != null && !locale.isEmpty()) {
            return Locale.forLanguageTag(locale);
        }
        return Locale.forLanguageTag(SuperApiBukkit.getInstance().getSettings().getDefaultLanguage());
    }

    /**
     * Forces an update of the User's complete inventory.
     * Deprecated, but there is no current alternative.
     */
    @Override
    public void updateInventory() {
        if (!isPlayer()) {
            return;
        }

        getPlayer().updateInventory();
    }

    /**
     * Performs a command as the player
     * @param cmd - command to execute
     * @return true if the command was successful, otherwise false
     */
    @Override
    public boolean performCommand(String cmd) {
        if (!isPlayer()) {
            return false;
        }

        return getPlayer().performCommand(cmd);
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
        if (isPlayer() && getPlayer().getLocation().toVector().distanceSquared(new Vector(x,y,z)) < (Bukkit.getServer().getViewDistance()*256*Bukkit.getServer().getViewDistance())) {
            if (particle.equals(Particle.REDSTONE)) {
                getPlayer().spawnParticle(particle, x, y, z, 1, 0, 0, 0, 1, dustOptions);
            } else {
                getPlayer().spawnParticle(particle, x, y, z, 1);
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
        return SuperApiBukkit.getInstance().getVault().getPrefix(this);
    }

    @Override
    public String getSuffix() {
        return SuperApiBukkit.getInstance().getVault().getSuffix(this);
    }

    @Override
    public String getGroup() {
        return SuperApiBukkit.getInstance().getVault().getGroup(this);
    }

    @Override
    public List<String> getGroups() {
        return SuperApiBukkit.getInstance().getVault().getGroups(this);
    }

    @Override
    public boolean inGroups(String group) {
        return SuperApiBukkit.getInstance().getVault().inGroup(this, group);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("UUID", playerUUID != null ? playerUUID.toString() : null);
        return result;
    }
}
