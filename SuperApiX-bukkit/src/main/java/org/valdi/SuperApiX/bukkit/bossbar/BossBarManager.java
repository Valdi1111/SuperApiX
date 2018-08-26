package org.valdi.SuperApiX.bukkit.bossbar;

import org.bukkit.Bukkit;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.bossbar.BarSolution.BarPlugin;
import org.valdi.SuperApiX.bukkit.bossbar.methods.BarApiSender;
import org.valdi.SuperApiX.bukkit.bossbar.methods.BossBarApiSender;
import org.valdi.SuperApiX.bukkit.bossbar.methods.BukkitBossBarSender;

public class BossBarManager {
	private final IBossBarProvider sender;
	
	public BossBarManager(final SuperApiBukkit plugin) {
        Class<?> clazz;
        try {
            clazz = Class.forName("org.bukkit.boss.BossBar");
        } catch (Exception e) {
            //getLogger().info("No BossBar method found.");
            clazz = null;
        }
        
        if (clazz != null) {
        	sender = new BukkitBossBarSender(plugin);
        	return;
        }
        
        if(Bukkit.getPluginManager().isPluginEnabled("BossBarAPI")) {
        	sender = new BossBarApiSender(plugin);
        	return;
        }
        
        if(Bukkit.getPluginManager().isPluginEnabled("BarAPI")) {
        	sender = new BarApiSender(plugin);
        	return;
        }
        
        sender = null;
        if(sender == null) {
        	plugin.getLogger().severe("Cannot load bossbar method, running on < 1.9 version... You must provide BossBarApi or BarApi!");
        }
	}
	
	public boolean isEnabled() {
		return this.sender != null && sender.isEnabled();
	}
	
	public boolean isBukkitCompatible() {
		return this.sender.getType() == BarPlugin.BUKKIT;
	}
	
	public IBossBarProvider getDispatcher() {
		return this.sender;
	}
	
	public BarPlugin getBarSolution() {
		return this.sender.getType();
	}

}
