package org.valdi.SuperApiX.bukkit;

import java.util.Optional;

import org.bukkit.plugin.ServicePriority;
import org.valdi.SuperApiX.bukkit.bossbar.BossBarManager;
import org.valdi.SuperApiX.bukkit.bossbar.IBossBarProvider;
import org.valdi.SuperApiX.bukkit.nms.IActionBar;
import org.valdi.SuperApiX.bukkit.nms.IPlayerUtils;
import org.valdi.SuperApiX.bukkit.nms.ISignEditor;
import org.valdi.SuperApiX.bukkit.nms.ITabList;
import org.valdi.SuperApiX.bukkit.nms.ITitle;
import org.valdi.SuperApiX.bukkit.nms.IWorldBorder;
import org.valdi.SuperApiX.bukkit.nms.IWorldManager;
import org.valdi.SuperApiX.common.config.FilesProvider;
import org.valdi.SuperApiX.common.config.IFilesProvider;
import org.valdi.SuperApiX.common.databases.DatabasesProvider;
import org.valdi.SuperApiX.common.databases.IDatabasesProvider;

public class ServiceProviderManager {
	private final SuperApiBukkit plugin;
	
	public ServiceProviderManager(final SuperApiBukkit plugin) {
		this.plugin = plugin;
	}
	
	public void registerAll() {
		// BossBar Utils
		plugin.getBootstrap().getServer().getServicesManager().register(IBossBarProvider.class, new BossBarManager(plugin).getDispatcher(), plugin.getBootstrap(), ServicePriority.Normal);
		
		// Databases Utils
		plugin.getBootstrap().getServer().getServicesManager().register(IDatabasesProvider.class, new DatabasesProvider(), plugin.getBootstrap(), ServicePriority.Normal);
		
		// Files Utils
		plugin.getBootstrap().getServer().getServicesManager().register(IFilesProvider.class, new FilesProvider(plugin), plugin.getBootstrap(), ServicePriority.Normal);

		/* NMS */
		
		// ActionBar Utils
		Optional<IActionBar> actionbarProvider = plugin.getNmsProvider().getActionBar();
		if(actionbarProvider.isPresent()) {
			plugin.getBootstrap().getServer().getServicesManager().register(IActionBar.class, actionbarProvider.get(), plugin.getBootstrap(), ServicePriority.Normal);
		}
		
		// Title Utils
		Optional<ITitle> titleProvider = plugin.getNmsProvider().getTitle();
		if(titleProvider.isPresent()) {
			plugin.getBootstrap().getServer().getServicesManager().register(ITitle.class, titleProvider.get(), plugin.getBootstrap(), ServicePriority.Normal);
		}
		
		// Tab Utils
		Optional<ITabList> tablistProvider = plugin.getNmsProvider().getTabList();
		if(tablistProvider.isPresent()) {
			plugin.getBootstrap().getServer().getServicesManager().register(ITabList.class, tablistProvider.get(), plugin.getBootstrap(), ServicePriority.Normal);
		}
		
		// Player Utils
		Optional<IPlayerUtils> playerProvider = plugin.getNmsProvider().getPlayerUtils();
		if(playerProvider.isPresent()) {
			plugin.getBootstrap().getServer().getServicesManager().register(IPlayerUtils.class, playerProvider.get(), plugin.getBootstrap(), ServicePriority.Normal);
		}
		
		// Sign Utils
		Optional<ISignEditor> signProvider = plugin.getNmsProvider().getSignEditor();
		if(signProvider.isPresent()) {
			plugin.getBootstrap().getServer().getServicesManager().register(ISignEditor.class, signProvider.get(), plugin.getBootstrap(), ServicePriority.Normal);
		}
		
		// WorldBorder Utils
		Optional<IWorldBorder> borderProvider = plugin.getNmsProvider().getWorldBorder();
		if(borderProvider.isPresent()) {
			plugin.getBootstrap().getServer().getServicesManager().register(IWorldBorder.class, borderProvider.get(), plugin.getBootstrap(), ServicePriority.Normal);
		}
		
		// WorldManager Utils
		Optional<IWorldManager> worldProvider = plugin.getNmsProvider().getWorldManager();
		if(borderProvider.isPresent()) {
			plugin.getBootstrap().getServer().getServicesManager().register(IWorldManager.class, worldProvider.get(), plugin.getBootstrap(), ServicePriority.Normal);
		}
	}

}
