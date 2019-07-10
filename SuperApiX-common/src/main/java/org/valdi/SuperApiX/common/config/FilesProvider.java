package org.valdi.SuperApiX.common.config;

import java.io.File;

import org.valdi.SuperApiX.common.plugin.StoreLoader;
import org.valdi.SuperApiX.common.config.types.HoconConfiguration;
import org.valdi.SuperApiX.common.config.types.JsonConfiguration;
import org.valdi.SuperApiX.common.config.types.TomlConfiguration;
import org.valdi.SuperApiX.common.config.types.YamlConfiguration;

public class FilesProvider implements IFilesProvider {
	
	@Override
	public IFileStorage createHoconFile(StoreLoader loader, File path, String fileName) {
		return new HoconConfiguration(loader, path, fileName);
	}
	
	@Override
	public IFileStorage createJsonFile(StoreLoader loader, File path, String fileName) {
		return new JsonConfiguration(loader, path, fileName);
	}
	
	@Override
	public IFileStorage createTomlFile(StoreLoader loader, File path, String fileName) {
		return new TomlConfiguration(loader, path, fileName);
	}
	
	@Override
	public IFileStorage createYamlFile(StoreLoader loader, File path, String fileName) {
		return new YamlConfiguration(loader, path, fileName);
	}

}
