package org.valdi.SuperApiX.bukkit.nms.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.nms.base.*;
import org.valdi.SuperApiX.bukkit.versions.MinecraftVersion;

public class NmsProvider {
	public static final String PACKAGE = "org.valdi.SuperApiX.bukkit.nms.%1$s.%2$s";

	private final SuperApiBukkit plugin;
	
	private List<NmsComponent> components;
	
	public NmsProvider(final SuperApiBukkit plugin) {
		this.plugin = plugin;
		
		this.components = new ArrayList<>();
		this.components.add(new NmsComponent<>("ACTIONBAR", "ActionBar", IActionBar.class));
		this.components.add(new NmsComponent<>("TITLE", "Title", ITitle.class));
		this.components.add(new NmsComponent<>("TABLIST", "TabList", ITabList.class));
		this.components.add(new NmsComponent<>("PLAYER_UTILS", "PlayerUtils", IPlayerUtils.class));
		this.components.add(new NmsComponent<>("ITEM_UTILS", "ItemUtils", IItemUtils.class));
		this.components.add(new NmsComponent<>("GENERAL_UTILS", "GeneralUtils", IGeneralUtils.class));
		this.components.add(new NmsComponent<>("SIGN_EDITOR", "SignEditor", ISignEditor.class));
		this.components.add(new NmsComponent<>("WORLD_MANAGER", "WorldManager", IWorldManager.class));
		//this.components.add(new NmsComponent<>("CUSTOM_ENTITIES", "CustomEntity", ICustomEntity.class));
		
		this.setupProviders();
	}
	
	public void setupProviders() {
		MinecraftVersion version = plugin.getBootstrap().getServerCompatibility().getMinecraftVersion(plugin.getServer());
		if(version == MinecraftVersion.UNKNOWN) {
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
		return this.getComponent(IActionBar.class);
	}
	
	public Optional<ITitle> getTitle() {
		return this.getComponent(ITitle.class);
	}
	
	public Optional<ITabList> getTabList() {
		return this.getComponent(ITabList.class);
	}
	
	public Optional<IPlayerUtils> getPlayerUtils() {
		return this.getComponent(IPlayerUtils.class);
	}

	public Optional<IItemUtils> getItemUtils() {
		return this.getComponent(IItemUtils.class);
	}

	public Optional<IGeneralUtils> getGeneralUtils() {
		return this.getComponent(IGeneralUtils.class);
	}
	
	public Optional<ISignEditor> getSignEditor() {
		return this.getComponent(ISignEditor.class);
	}
	
	public Optional<IWorldManager> getWorldManager() {
		return this.getComponent(IWorldManager.class);
	}
	
	public <T> Optional<T> getComponent(Class<T> clazz) {
		Optional<NmsComponent> opt = components.stream().filter(c -> c.getSuper().equals(clazz)).findFirst();
		if(!opt.isPresent()) {
			return Optional.empty();
		}
		
		NmsComponent<T> component = (NmsComponent<T>) opt.get();
		return component.getInstance();
	}

	public Optional<?> getComponent(String id) {
		Optional<NmsComponent> opt = components.stream().filter(c -> c.getId().equals(id)).findFirst();
		if(!opt.isPresent()) {
			return Optional.empty();
		}

		return opt.get().getInstance();
	}
}
