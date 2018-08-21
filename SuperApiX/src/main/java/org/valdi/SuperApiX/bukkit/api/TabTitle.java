package org.valdi.SuperApiX.bukkit.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.valdi.SuperApiX.bukkit.nms.IChatProvider;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;

public class TabTitle {
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private String header;
		private String footer;
		
		public Builder header(String header) {
			this.header = header;
			return this;
		}
		
		public Builder footer(String footer) {
			this.footer = footer;
			return this;
		}

		public boolean send(Player player) {
	        RegisteredServiceProvider<IChatProvider> provider = Bukkit.getServicesManager().getRegistration(IChatProvider.class);
	        if(provider == null) {
	            return false;
	        }
	        
	        try {
				provider.getProvider().sendTabTitle(player, header, footer);
				return true;
			} catch (VersionUnsupportedException ignored) {
				return false;
			}
		}
	}

}