package org.valdi.SuperApiX.common.config.types;

import java.io.File;

import org.valdi.SuperApiX.common.plugin.StoreLoader;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;

import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;

public class YamlConfiguration extends AbstractConfigAdapter {
	
	public YamlConfiguration(StoreLoader loader, File path, String fileName) {
		super(loader, path, fileName);
	}

	@Override
	protected void setManagerFromFile(File file) {
		setManager(YAMLConfigurationLoader.builder()
				.setIndent(2)
				.setFlowStyle(FlowStyle.BLOCK)
				.setFile(file)
				.build());
	}

	@Override
	public ConfigType getType() {
		return ConfigType.YAML;
	}

}
