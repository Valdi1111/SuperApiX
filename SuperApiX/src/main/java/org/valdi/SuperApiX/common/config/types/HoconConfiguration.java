package org.valdi.SuperApiX.common.config.types;

import java.io.File;

import org.valdi.SuperApiX.ISuperPlugin;
import org.valdi.SuperApiX.common.config.ConfigType;
import org.valdi.SuperApiX.common.config.IFileStorage;

import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

public class HoconConfiguration extends AbstractConfigAdapter implements IFileStorage {
	
	public HoconConfiguration(ISuperPlugin plugin, File path, String fileName) {
		super(plugin, path, fileName);
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
