package org.valdi.SuperApiX.bukkit;

import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.api.ActionBar;
import org.valdi.SuperApiX.bukkit.api.Title;

@Deprecated
public class ApiUtils {

	@Deprecated
	public static void sendActionBar(Player player, String message) {
        ActionBar.builder().message(message).send(player);
	}
	
	@Deprecated
	public static void sendFullTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) {
		Title.builder().fadeIn(fadeIn).stay(stay).fadeOut(fadeOut).title(title).subtitle(subtitle).send(player);
	}
	
	@Deprecated
	public static void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title) {
		Title.builder().fadeIn(fadeIn).stay(stay).fadeOut(fadeOut).title(title).send(player);
	}
	
	@Deprecated
	public static void sendSubtitle(Player player, int fadeIn, int stay, int fadeOut, String subtitle) {
		Title.builder().fadeIn(fadeIn).stay(stay).fadeOut(fadeOut).subtitle(subtitle).send(player);
	}

	@Deprecated	
	public static void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) {
		Title.builder().fadeIn(fadeIn).stay(stay).fadeOut(fadeOut).title(title).subtitle(subtitle).send(player);
	}

}
