package org.valdi.SuperApiX.bukkit.commands;

import org.bukkit.Bukkit;
import org.valdi.SuperApiX.bukkit.plugin.BukkitStoreLoader;
import org.valdi.SuperApiX.bukkit.users.User;
import org.valdi.SuperApiX.common.scheduler.task.SuperTask;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Space Confirmable Command
 * Adds ability to confirm a command before execution
 * 
 * @author david
 *
 */
public abstract class ConfirmableCommand<T extends BukkitStoreLoader> extends CompositeCommand<T> {
    /**
    * Confirmation tracker
    */
	private static Map<User, Confirmer> toBeConfirmed = new HashMap<>();
	
    /**
    * Top level command
    * @param plugin - plugin creating the command
    * @param label - string for this command
    * @param aliases - aliases
    */
	public ConfirmableCommand(T plugin, String label, String... aliases) {
		super(plugin, label, aliases);
	}
	
    /**
     * Command to register a command from a plugin under a parent command (that could be from another plugin)
    * @param plugin - this command's plugin
    * @param parent - parent command
    * @param aliases - aliases for this command
    */
	public ConfirmableCommand(T plugin, CompositeCommand<? extends BukkitStoreLoader> parent, String label, String... aliases ) {
		super(plugin, parent, label, aliases);
	}

    public ConfirmableCommand(CompositeCommand<T> parent, String label, String... aliases) {
        super(parent, label, aliases);
    }
    
    /**
     * Tells user to confirm command by retyping
     * @param user - user
     * @param confirmed - runnable to be executed if confirmed
     */
    public void askConfirmation(User user, String message, int time, Runnable confirmed) {
        // Check for pending confirmations
        if (toBeConfirmed.containsKey(user)) {
            if (toBeConfirmed.get(user).getTopLabel().equals(getTopLabel()) && toBeConfirmed.get(user).getLabel().equalsIgnoreCase(getLabel())) {
                toBeConfirmed.get(user).getTask().cancel();
                Bukkit.getScheduler().runTask(getPlugin(), toBeConfirmed.get(user).getRunnable());
                toBeConfirmed.remove(user);
                return;
            } else {
                // Player has another outstanding confirmation request that will now be cancelled
                user.sendMessage(null, "commands.confirmation.previous-request-cancelled");
            }
        }
        // Send user the context message if it is not empty
        if (!message.trim().isEmpty()) {
            user.sendRawMessage(message);
        }
        // Tell user that they need to confirm
        user.sendMessage(null, "commands.confirmation.confirm", "[seconds]", String.valueOf(time));
        // Set up a cancellation task
        SuperTask task = plugin.getScheduler().runTaskLater(() -> {
            user.sendMessage(null, "commands.confirmation.request-cancelled");
            toBeConfirmed.remove(user);
        }, time, TimeUnit.SECONDS);

        // Add to the global confirmation map
        toBeConfirmed.put(user, new Confirmer(getTopLabel(), getLabel(), confirmed, task));
    }
    
    /**
    * Tells user to confirm command by retyping it.
    * @param user User to ask confirmation to.
    * @param confirmed Runnable to be executed if successfully confirmed.
    */
    public void askConfirmation(User user, int time, Runnable confirmed) {
    	askConfirmation(user, "", time, confirmed);
    }

    private static class Confirmer {
        private final String topLabel;
        private final String label;
        private final Runnable runnable;
        private final SuperTask task;

        /**
         * @param label - command label
         * @param runnable - runnable to run when confirmed
         * @param task - task ID to cancel when confirmed
         */
        private Confirmer(String topLabel, String label, Runnable runnable, SuperTask task) {
            this.topLabel = topLabel;
            this.label = label;
            this.runnable = runnable;
            this.task = task;
        }
        /**
         * @return the topLabel
         */
        public String getTopLabel() {
            return topLabel;
        }
        /**
         * @return the label
         */
        public String getLabel() {
            return label;
        }
        /**
         * @return the runnable
         */
        public Runnable getRunnable() {
            return runnable;
        }
        /**
         * @return the task
         */
        public SuperTask getTask() {
            return task;
        }

    }

}
