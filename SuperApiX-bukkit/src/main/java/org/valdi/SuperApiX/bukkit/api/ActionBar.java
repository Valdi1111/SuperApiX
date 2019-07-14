package org.valdi.SuperApiX.bukkit.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.valdi.SuperApiX.bukkit.nms.base.IActionBar;

public class ActionBar {
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private String message;
		private int time = -1;
		
		public Builder time(int time) {
			this.time = time;
			return this;
		}
		
		public Builder message(String message) {
			this.message = message;
			return this;
		}

		public boolean send(Player player) {
	        RegisteredServiceProvider<IActionBar> provider = Bukkit.getServicesManager().getRegistration(IActionBar.class);
	        if(provider == null) {
	            return false;
	        }

			provider.getProvider().sendActionBar(player, message, time);
			return true;
		}
	}

}
