package org.valdi.SuperApiX.common.config.types;

import java.io.File;

import org.valdi.SuperApiX.common.config.ConfigType;
import org.valdi.SuperApiX.common.config.advanced.StoreLoader;

import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

public class HoconConfiguration extends AbstractConfigAdapter {
	
	public HoconConfiguration(StoreLoader loader, File path, String fileName) {
		super(loader, path, fileName);
	}
	
	@Override
	public void loadOnly() {
		this.setManager(HoconConfigurationLoader.builder().setFile(this.getFile()).build());
		super.loadOnly();
	}

	@Override
	public ConfigType getType() {
		return ConfigType.HOCON;
	}

}
