package org.valdi.SuperApiX.common.config.types;

import java.io.File;

import org.valdi.SuperApiX.common.config.ConfigType;
import org.valdi.SuperApiX.common.config.advanced.StoreLoader;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;

import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;

public class YamlConfiguration extends AbstractConfigAdapter {
	
	public YamlConfiguration(StoreLoader loader, File path, String fileName) {
		super(loader, path, fileName);
	}
	
	@Override
	public void loadOnly() {
		this.setManager(YAMLConfigurationLoader.builder().setIndent(2).setFlowStyle(FlowStyle.BLOCK).setFile(this.getFile()).build());
		super.loadOnly();
	}

	@Override
	public ConfigType getType() {
		return ConfigType.YAML;
	}

}
