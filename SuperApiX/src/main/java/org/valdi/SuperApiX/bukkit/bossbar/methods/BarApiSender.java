package org.valdi.SuperApiX.bukkit.bossbar.methods;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.bossbar.BarSolution;
import org.valdi.SuperApiX.bukkit.bossbar.IBossBar;
import org.valdi.SuperApiX.bukkit.bossbar.IBossBarProvider;

import me.confuser.barapi.BarAPI;

public class BarApiSender extends BarSolution implements IBossBarProvider, Listener {
	private boolean enabled;
	
	public BarApiSender(final SuperApiBukkit plugin) {
		super(plugin);
		
		this.enabled = true;
	}

	@Override
	public IBossBar createBossBar(String message) {
		return new BarApiBossBar(message);
	}

	@Override
	public BarPlugin getType() {
		return BarPlugin.BARAPI;
	}
	
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPluginEnable(PluginEnableEvent e) {
		if(!e.getPlugin().getName().equalsIgnoreCase("BarAPI")) {
			return;
		}
		
		this.enabled = true;
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPluginDisable(PluginDisableEvent e) {
		if(!e.getPlugin().getName().equalsIgnoreCase("BarAPI")) {
			return;
		}
		
		this.enabled = false;
	}

	public static class BarApiBossBar implements IBossBar {
		private String message;	
		
		private double progress;
		private List<Player> players;
		
		public BarApiBossBar(String message) {
			if(message == null) {
				message = "";
			}
			
			this.message = ChatColor.translateAlternateColorCodes('&', message);
			this.progress = 1D;
			this.players = new ArrayList<Player>();
		}
	
		@Override
		public String getTitle() {
			return this.message;
		}
	
		@Override
		public IBossBar setTitle(String message) {
			if(message == null) {
				return this;
			}
			
			this.message = ChatColor.translateAlternateColorCodes('&', message);
			for(Player player : this.getPlayers()) {
				BarAPI.setMessage(player, message, (float) progress);
			}
			
			return this;
		}
	
		@Override
		public Color getColor() {
			return Color.PURPLE;
		}
	
		@Override
		public IBossBar setColor(Color color) {
			return this;
		}
	
		@Override
		public Style getStyle() {
			return Style.SOLID;
		}
	
		@Override
		public IBossBar setStyle(Style style) {
			return this;
		}
	
		@Override
		public double getProgress() {
			return progress;
		}
	
		@Override
		public IBossBar setProgress(double progress) {
			this.progress = progress;
			for(Player player : this.getPlayers()) {
				BarAPI.setMessage(player, message, (float) progress);
			}
			
			return this;
		}
	
		@Override
		public List<Player> getPlayers() {
			return this.players;
		}
	
		@Override
		public IBossBar addPlayer(Player player) {
			BarAPI.setMessage(player, message, (float) progress);
			this.getPlayers().add(player);
			return this;
		}
	
		@Override
		public IBossBar removePlayer(Player player) {
			BarAPI.removeBar(player);
			this.getPlayers().remove(player);
			return this;
		}
	
		@Override
		public boolean hasPlayer(Player player) {
			return this.getPlayers().contains(player);
		}
	
		@Override
		public IBossBar removeAll() {
			for(Player player : this.getPlayers()) {
				BarAPI.removeBar(player);
			}
			
			this.getPlayers().clear();		
			return this;
		}
	
		@Override
		public IBossBar addFlag(Flag flag) {
			return this;
		}
	
		@Override
		public IBossBar removeFlag(Flag flag) {
			return this;
		}
	
		@Override
		public boolean hasFlag(Flag flag) {
			return false;
		}
	
	}

}
