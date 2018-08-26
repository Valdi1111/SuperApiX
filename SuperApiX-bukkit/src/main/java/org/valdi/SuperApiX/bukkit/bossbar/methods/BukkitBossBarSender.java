package org.valdi.SuperApiX.bukkit.bossbar.methods;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.bossbar.BarSolution;
import org.valdi.SuperApiX.bukkit.bossbar.IBossBar;
import org.valdi.SuperApiX.bukkit.bossbar.IBossBarProvider;

public class BukkitBossBarSender extends BarSolution implements IBossBarProvider {
	
	public BukkitBossBarSender(final SuperApiBukkit plugin) {
		super(plugin);
	}

	@Override
	public IBossBar createBossBar(String message) {
		return new BukkitBossBar(message);
	}

	@Override
	public BarPlugin getType() {
		return BarPlugin.BUKKIT;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}

	public static class BukkitBossBar implements IBossBar {
		private BossBar bossbar;
		
		public BukkitBossBar(String message) {
			if(message == null) {
				message = "";
			}
			
			message = ChatColor.translateAlternateColorCodes('&', message);
			bossbar = Bukkit.createBossBar(message, BarColor.PURPLE, BarStyle.SOLID);
		}
	
		@Override
		public String getTitle() {
			return bossbar.getTitle();
		}
	
		@Override
		public IBossBar setTitle(String message) {
			if(message == null) {
				return this;
			}
			
			bossbar.setTitle(ChatColor.translateAlternateColorCodes('&', message));
			return this;
		}
	
		@Override
		public Color getColor() {
			return Color.valueOf(bossbar.getColor().name());
		}
	
		@Override
		public IBossBar setColor(Color color) {
			bossbar.setColor(BarColor.valueOf(color.name()));
			return this;
		}
	
		@Override
		public Style getStyle() {
			return Style.valueOf(bossbar.getStyle().name());
		}
	
		@Override
		public IBossBar setStyle(Style style) {
			bossbar.setStyle(BarStyle.valueOf(style.name()));
			return this;
		}
	
		@Override
		public IBossBar setProgress(double progress) {
			bossbar.setProgress(progress);
			return this;
		}
	
		@Override
		public double getProgress() {
			return bossbar.getProgress();
		}
	
		@Override
		public List<Player> getPlayers() {
			return bossbar.getPlayers();
		}
	
		@Override
		public IBossBar addPlayer(Player player) {
			bossbar.addPlayer(player);
			return this;
		}
	
		@Override
		public IBossBar removePlayer(Player player) {
			bossbar.removePlayer(player);
			return this;
		}
	
		@Override
		public boolean hasPlayer(Player player) {
			return bossbar.getPlayers().contains(player);
		}
	
		@Override
		public IBossBar removeAll() {
			bossbar.removeAll();
			return this;
		}
	
		@Override
		public IBossBar addFlag(Flag flag) {
			bossbar.addFlag(BarFlag.valueOf(flag.name()));
			return this;
		}
	
		@Override
		public IBossBar removeFlag(Flag flag) {
			bossbar.removeFlag(BarFlag.valueOf(flag.name()));
			return this;
		}
		
		@Override
		public boolean hasFlag(Flag flag) {
			return bossbar.hasFlag(BarFlag.valueOf(flag.name()));
		}
	
	}
	
}
