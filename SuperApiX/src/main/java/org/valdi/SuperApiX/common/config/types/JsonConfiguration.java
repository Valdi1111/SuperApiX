package org.valdi.SuperApiX.common.config.types;

import java.io.File;

import org.valdi.SuperApiX.ISuperPlugin;
import org.valdi.SuperApiX.common.config.ConfigType;
import org.valdi.SuperApiX.common.config.IFileStorage;

import ninja.leaping.configurate.gson.GsonConfigurationLoader;

public class JsonConfiguration extends AbstractConfigAdapter implements IFileStorage {
	
	public JsonConfiguration(ISuperPlugin plugin, File path, String fileName) {
		super(plugin, path, fileName);
	}
	
	@Override
	public void loadOnly() {
		this.setManager(GsonConfigurationLoader.builder().setIndent(2).setFile(this.getFile()).build());
		super.loadOnly();
	}

	@Override
	public ConfigType getType() {
		return ConfigType.JSON;
	}

}
