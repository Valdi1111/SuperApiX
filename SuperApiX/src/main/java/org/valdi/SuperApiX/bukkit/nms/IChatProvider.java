package org.valdi.SuperApiX.bukkit.nms;

import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;

public interface IChatProvider {
	
	public void sendActionBar(Player player, String message) throws VersionUnsupportedException;
	
	public void sendActionBar(Player player, String message, int time) throws VersionUnsupportedException;

	@Deprecated
	public void sendFullTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) throws VersionUnsupportedException;

	@Deprecated
	public void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title) throws VersionUnsupportedException;

	@Deprecated
	public void sendSubtitle(Player player, int fadeIn, int stay, int fadeOut, String subtitle) throws VersionUnsupportedException;

	public void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) throws VersionUnsupportedException;

	public void sendTabTitle(Player player, String header, String footer) throws VersionUnsupportedException;

}
