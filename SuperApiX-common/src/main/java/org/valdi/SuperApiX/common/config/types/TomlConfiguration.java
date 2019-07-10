package org.valdi.SuperApiX.common.config.types;

import java.io.File;

import org.valdi.SuperApiX.common.plugin.StoreLoader;

import ninja.leaping.configurate.toml.TOMLConfigurationLoader;

public class TomlConfiguration extends AbstractConfigAdapter {
	
	public TomlConfiguration(StoreLoader loader, File path, String fileName) {
		super(loader, path, fileName);
	}

	@Override
	protected void setManagerFromFile(File file) {
		setManager(TOMLConfigurationLoader.builder()
				.setKeyIndent(2)
				.setTableIndent(2)
				.setFile(file)
				.build());
	}

	@Override
	public ConfigType getType() {
		return ConfigType.TOML;
	}

}
