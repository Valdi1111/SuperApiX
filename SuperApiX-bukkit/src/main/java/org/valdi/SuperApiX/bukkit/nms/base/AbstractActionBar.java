package org.valdi.SuperApiX.bukkit.nms.base;

import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;

public abstract class AbstractActionBar extends AbstractNmsProvider implements IActionBar {
	
	protected AbstractActionBar(final SuperApiBukkit plugin) {
		super(plugin);
	}

	@Override
	public void sendActionBar(Player player, String message, int duration) {
		sendActionBar(player, message);

        if (duration >= 0) {
            // Sends empty message at the end of the duration. Allows messages shorter than 3 seconds, ensures precision.
            getPlugin().getScheduler().runTaskLaterAsynchronously(() -> sendActionBar(player, ""), duration * 50 + 20, TimeUnit.MILLISECONDS);
        } else {
			sendActionBar(player, message);
        }

        // Re-sends the messages every 3 seconds so it doesn't go away from the player's screen.
        while (duration > 40) {
            duration -= 40;
            getPlugin().getScheduler().runTaskLaterAsynchronously(() -> sendActionBar(player, message), duration * 50, TimeUnit.MILLISECONDS);
        }
	}

}
