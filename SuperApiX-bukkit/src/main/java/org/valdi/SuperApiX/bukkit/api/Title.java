package org.valdi.SuperApiX.bukkit.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.valdi.SuperApiX.bukkit.nms.base.ITitle;

public class Title {
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private int fadeIn = 0;
		private int stay = 0;
		private int fadeOut = 0;
		private String title;
		private String subtitle;
		
		public Builder fadeIn(int fadeIn) {
			this.fadeIn = fadeIn;
			return this;
		}
		
		public Builder stay(int stay) {
			this.stay = stay;
			return this;
		}
		
		public Builder fadeOut(int fadeOut) {
			this.fadeOut = fadeOut;
			return this;
		}
		
		public Builder title(String title) {
			this.title = title;
			return this;
		}
		
		public Builder subtitle(String subtitle) {
			this.subtitle = subtitle;
			return this;
		}

		public boolean send(Player player) {
	        RegisteredServiceProvider<ITitle> provider = Bukkit.getServicesManager().getRegistration(ITitle.class);
	        if(provider == null) {
	            return false;
	        }

			provider.getProvider().sendTitle(player, fadeIn, stay, fadeOut, title, subtitle);
			return true;
		}
	}

}
