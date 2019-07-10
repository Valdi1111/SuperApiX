package org.valdi.SuperApiX.bukkit.nms.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.nms.base.*;
import org.valdi.SuperApiX.bukkit.nms.core.VersionManager.NmsVersion;

public class NmsProvider {
	private final SuperApiBukkit plugin;
	
	private List<NmsComponent<?>> components;

	public static final String PACKAGE = "org.valdi.SuperApiX.bukkit.nms.[version].[classname]";
	public static final String ADVANCEMENTS_PACKAGE = "org.valdi.SuperApiX.bukkit.nms.[version].advancements.[classname]";
	
	public NmsProvider(final SuperApiBukkit plugin) {
		this.plugin = plugin;
		
		this.components = new ArrayList<>();
		this.components.add(new NmsComponent<>(NmsName.ACTIONBAR, "ActionBar", "IActionBar"));
		this.components.add(new NmsComponent<>(NmsName.TITLE, "Title", "ITitle"));
		this.components.add(new NmsComponent<>(NmsName.TABLIST, "TabList", "ITabList"));
		this.components.add(new NmsComponent<>(NmsName.ADVANCEMENT_UTILS, "AdvancementUtils", "IAdvancementUtils"));
		this.components.add(new NmsComponent<>(NmsName.PLAYER_UTILS, "PlayerUtils", "IPlayerUtils"));
		this.components.add(new NmsComponent<>(NmsName.ITEM_UTILS, "ItemUtils", "IItemUtils"));
		this.components.add(new NmsComponent<>(NmsName.GENERAL_UTILS, "GeneralUtils", "IGeneralUtils"));
		this.components.add(new NmsComponent<>(NmsName.SIGN_EDITOR, "SignEditor", "ISignEditor"));
		this.components.add(new NmsComponent<>(NmsName.WORLD_BORDER, "WorldBorder", "IWorldBorder"));
		this.components.add(new NmsComponent<>(NmsName.WORLD_MANAGER, "WorldManager", "IWorldManager"));
//		this.components.add(new NmsComponent<>(NmsName.CUSTOM_ENTITIES, "CustomEntity", "ICustomEntity"));
		
		this.setupProviders();
	}
	
	public void setupProviders() {
		NmsVersion version = plugin.getVersionManager().getNmsVersion();
		if(version == NmsVersion.INCOMPATIBLE) {
			plugin.getLogger().severe("Cannot provide support for this nms version... Using bukkit handler, some methods won't work.");
			return;
		}
		
		for(NmsComponent<?> component : components) {
			try {
				component.setupProvider(plugin, version);
			} catch(Exception e) {
				e.printStackTrace();
				plugin.getLogger().severe("Error initializing " + component.getSuperName() + " provider class...");
				plugin.getLogger().severe("Try updating SuperApiX!");
			}
		}
	}
	
	public Optional<IActionBar> getActionBar() {
		return this.getComponent(NmsName.ACTIONBAR);
	}
	
	public Optional<ITitle> getTitle() {
		return this.getComponent(NmsName.TITLE);
	}
	
	public Optional<ITabList> getTabList() {
		return this.getComponent(NmsName.TABLIST);
	}

	public Optional<IAdvancementUtils> getAdvancementUtils() {
		return this.getComponent(NmsName.ADVANCEMENT_UTILS);
	}
	
	public Optional<IPlayerUtils> getPlayerUtils() {
		return this.getComponent(NmsName.PLAYER_UTILS);
	}

	public Optional<IItemUtils> getItemUtils() {
		return this.getComponent(NmsName.ITEM_UTILS);
	}

	public Optional<IGeneralUtils> getGeneralUtils() {
		return this.getComponent(NmsName.GENERAL_UTILS);
	}
	
	public Optional<ISignEditor> getSignEditor() {
		return this.getComponent(NmsName.SIGN_EDITOR);
	}
	
	public Optional<IWorldBorder> getWorldBorder() {
		return this.getComponent(NmsName.WORLD_BORDER);
	}
	
	public Optional<IWorldManager> getWorldManager() {
		return this.getComponent(NmsName.WORLD_MANAGER);
	}
	
	private <T> Optional<T> getComponent(NmsName<T> type) {
		Optional<NmsComponent<?>> opt = components.stream().filter(c -> c.isType(type)).findFirst();
		if(!opt.isPresent()) {
			return Optional.empty();
		}
		
		NmsComponent<T> component = (NmsComponent<T>) opt.get();
		return component.getInstance();
	}
	
	public NmsVersion getVersion() {
		return plugin.getVersionManager().getNmsVersion();
	}
}
