package org.valdi.SuperApiX.common.config.types;

import java.io.File;

import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.valdi.SuperApiX.common.config.ConfigType;
import org.valdi.SuperApiX.common.StoreLoader;

public class HoconConfiguration extends AbstractConfigAdapter {
	
	public HoconConfiguration(StoreLoader loader, File path, String fileName) {
		super(loader, path, fileName);
	}

	@Override
	protected void setManagerFromFile(File file) {
		setManager(YAMLConfigurationLoader.builder()
				.setIndent(2)
				.setFile(file)
				.build());
	}

	@Override
	public ConfigType getType() {
		return ConfigType.HOCON;
	}

}
