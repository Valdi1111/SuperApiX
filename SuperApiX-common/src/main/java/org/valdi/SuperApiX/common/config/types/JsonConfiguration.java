package org.valdi.SuperApiX.common.config.types;

import java.io.File;

import org.valdi.SuperApiX.common.config.ConfigType;
import org.valdi.SuperApiX.common.config.advanced.StoreLoader;

import ninja.leaping.configurate.gson.GsonConfigurationLoader;

public class JsonConfiguration extends AbstractConfigAdapter {
	
	public JsonConfiguration(StoreLoader loader, File path, String fileName) {
		super(loader, path, fileName);
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
