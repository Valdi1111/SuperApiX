package org.valdi.SuperApiX.common.sender;

import java.util.Optional;
import java.util.UUID;

import org.valdi.SuperApiX.ISuperPlugin;
import org.valdi.SuperApiX.common.command.CommandManager;

import net.kyori.text.Component;

/**
 * Wrapper interface to represent a CommandSender/CommandSource within the common command implementations.
 */
public interface Sender {

    /**
     * Gets the platform where the sender is from.
     *
     * @return the plugin
     */
	public ISuperPlugin getPlugin();

    /**
     * Gets the sender's username
     *
     * @return a friendly username for the sender
     */
    public String getName();

    /**
     * Gets the sender's unique id. See {@link CommandManager#CONSOLE_UUID} for the console's UUID representation.
     *
     * @return the sender's uuid
     */
    public UUID getUUID();

    /**
     * Send a message back to the Sender
     *
     * @param message the message to send. Supports '§' for message formatting.
     */
    public void sendMessage(String message);

    /**
     * Send a json message to the Sender.
     *
     * @param message the message to send.
     */
    public void sendMessage(Component message);

    /**
     * Check if the Sender has a permission.
     *
     * @param permission the permission to check for
     * @return true if the sender has the permission
     */
    public boolean hasPermission(String permission);

    /**
     * Gets whether this sender is the console
     *
     * @return if the sender is the console
     */
    default boolean isConsole() {
        return CommandManager.CONSOLE_UUID.equals(getUUID()) || CommandManager.IMPORT_UUID.equals(getUUID());
    }

    /**
     * Gets whether this sender is an import process
     *
     * @return if the sender is an import process
     */
    default boolean isImport() {
        return CommandManager.IMPORT_UUID.equals(getUUID());
    }

    /**
     * Gets whether this sender is still valid & receiving messages.
     *
     * @return if this sender is valid
     */
    default boolean isValid() {
        return true;
    }

    /**
     * Gets the handle object for this sender. (In most cases, the real
     * CommandSender/CommandSource object from the platform)
     *
     * @return the handle
     */
    default Optional<Object> getHandle() {
        return Optional.empty();
    }

}