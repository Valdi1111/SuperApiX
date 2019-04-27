package org.valdi.SuperApiX.common.config.types;

import java.io.File;

import org.valdi.SuperApiX.common.config.ConfigType;
import org.valdi.SuperApiX.common.StoreLoader;

import ninja.leaping.configurate.gson.GsonConfigurationLoader;

public class JsonConfiguration extends AbstractConfigAdapter {
	
	public JsonConfiguration(StoreLoader loader, File path, String fileName) {
		super(loader, path, fileName);
	}

	@Override
	protected void setManagerFromFile(File file) {
		setManager(GsonConfigurationLoader.builder()
				.setIndent(2)
				.setFile(file)
				.build());
	}

	@Override
	public ConfigType getType() {
		return ConfigType.JSON;
	}

}
