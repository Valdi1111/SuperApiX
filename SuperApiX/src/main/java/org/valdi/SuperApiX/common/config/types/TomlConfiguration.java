package org.valdi.SuperApiX.common.config.types;

import java.io.File;

import org.valdi.SuperApiX.ISuperPlugin;
import org.valdi.SuperApiX.common.config.ConfigType;
import org.valdi.SuperApiX.common.config.IFileStorage;

import ninja.leaping.configurate.toml.TOMLConfigurationLoader;

public class TomlConfiguration extends AbstractConfigAdapter implements IFileStorage {
	
	public TomlConfiguration(ISuperPlugin plugin, File path, String fileName) {
		super(plugin, path, fileName);
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
