package org.valdi.SuperApiX.common.config.types;

import java.io.File;

import org.valdi.SuperApiX.common.config.ConfigType;
import org.valdi.SuperApiX.common.config.advanced.StoreLoader;

import ninja.leaping.configurate.toml.TOMLConfigurationLoader;

public class TomlConfiguration extends AbstractConfigAdapter {
	
	public TomlConfiguration(StoreLoader loader, File path, String fileName) {
		super(loader, path, fileName);
	}
	
	@Override
	public void loadOnly() {
		this.setManager(TOMLConfigurationLoader.builder().setKeyIndent(2).setTableIndent(2).setFile(this.getFile()).build());
		super.loadOnly();
	}

	@Override
	public ConfigType getType() {
		return ConfigType.TOML;
	}

}
