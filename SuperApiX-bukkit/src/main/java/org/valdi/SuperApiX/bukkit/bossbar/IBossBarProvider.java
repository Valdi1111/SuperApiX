package org.valdi.SuperApiX.bukkit.bossbar;

import org.valdi.SuperApiX.bukkit.bossbar.BarSolution.BarPlugin;

public interface IBossBarProvider {
	
	public IBossBar createBossBar(String message);

	public BarPlugin getType();
	
	public boolean isEnabled();

}
