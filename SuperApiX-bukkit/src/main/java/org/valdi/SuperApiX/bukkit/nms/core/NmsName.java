package org.valdi.SuperApiX.bukkit.nms.core;

import org.valdi.SuperApiX.bukkit.nms.IActionBar;
import org.valdi.SuperApiX.bukkit.nms.ICustomEntity;
import org.valdi.SuperApiX.bukkit.nms.IPlayerUtils;
import org.valdi.SuperApiX.bukkit.nms.ISignEditor;
import org.valdi.SuperApiX.bukkit.nms.ITabList;
import org.valdi.SuperApiX.bukkit.nms.ITitle;
import org.valdi.SuperApiX.bukkit.nms.IWorldBorder;
import org.valdi.SuperApiX.bukkit.nms.IWorldManager;

public class NmsName<T> {	
	public static final NmsName<IActionBar> ACTIONBAR = new NmsName<>("actionbar");
	public static final NmsName<ITitle> TITLE = new NmsName<>("title");
	public static final NmsName<ITabList> TABLIST = new NmsName<>("tablist");
	public static final NmsName<IPlayerUtils> PLAYER_UTILS = new NmsName<>("player_utils");
	public static final NmsName<ISignEditor> SIGN_EDITOR = new NmsName<>("sign_editor");
	public static final NmsName<IWorldBorder> WORLD_BORDER = new NmsName<>("world_border");
	public static final NmsName<IWorldManager> WORLD_MANAGER = new NmsName<>("world_manager");
	public static final NmsName<ICustomEntity> CUSTOM_ENTITIES = new NmsName<>("custom_entity");
	
	public String name;
	
	public NmsName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof NmsName)) {
			return false;
		}
		
		NmsName it = (NmsName) o;
		return it.getName().equals(getName());
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public String toString() {
		return name;
	}

}
