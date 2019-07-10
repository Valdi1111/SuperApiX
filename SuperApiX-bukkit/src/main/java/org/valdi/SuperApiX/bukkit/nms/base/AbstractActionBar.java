package org.valdi.SuperApiX.bukkit.nms.base;

import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;

public abstract class AbstractActionBar extends AbstractNmsProvider implements IActionBar {
	
	protected AbstractActionBar(final SuperApiBukkit plugin) {
		super(plugin);
	}

	@Override
	public void sendActionBar(Player player, String message, int duration) throws VersionUnsupportedException {
		sendActionBar(player, message);

        if (duration >= 0) {
            // Sends empty message at the end of the duration. Allows messages shorter than 3 seconds, ensures precision.
            getPlugin().getBootstrap().getScheduler().runTaskLaterAsynchronously(() -> {
				try {
					sendActionBar(player, "");
				} catch (VersionUnsupportedException ignored) {}
			}, duration * 50 + 20, TimeUnit.MILLISECONDS);
        } else {
			sendActionBar(player, message);
        }

        // Re-sends the messages every 3 seconds so it doesn't go away from the player's screen.
        while (duration > 40) {
            duration -= 40;
            getPlugin().getBootstrap().getScheduler().runTaskLaterAsynchronously(() -> {
				try {
					sendActionBar(player, message);
				} catch (VersionUnsupportedException ignored) {}
			}, duration * 50, TimeUnit.MILLISECONDS);
        }
	}

}
