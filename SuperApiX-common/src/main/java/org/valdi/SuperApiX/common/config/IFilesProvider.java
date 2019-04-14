package org.valdi.SuperApiX.common.config;

import java.io.File;

import org.valdi.SuperApiX.common.config.advanced.StoreLoader;

public interface IFilesProvider {

	IFileStorage createHoconFile(File path, String fileName);

	IFileStorage createHoconFile(StoreLoader loader, File path, String fileName);

	IFileStorage createJsonFile(File path, String fileName);

	IFileStorage createJsonFile(StoreLoader loader, File path, String fileName);

	IFileStorage createTomlFile(File path, String fileName);

	IFileStorage createTomlFile(StoreLoader loader, File path, String fileName);

	IFileStorage createYamlFile(File path, String fileName);

	IFileStorage createYamlFile(StoreLoader loader, File path, String fileName);

	default IFileStorage createFile(ConfigType type, File path, String fileName) {
		switch(type) {
		case HOCON:
			return this.createHoconFile(path, fileName);
		case JSON:
			return this.createJsonFile(path, fileName);
		case TOML:
			return this.createTomlFile(path, fileName);
		case YAML:
			return this.createYamlFile(path, fileName);
		case CUSTOM:
			break;
		}
		return null;
	}

	default IFileStorage createFile(ConfigType type, StoreLoader loader, File path, String fileName) {
		switch(type) {
		case HOCON:
			return this.createHoconFile(loader, path, fileName);
		case JSON:
			return this.createJsonFile(loader, path, fileName);
		case TOML:
			return this.createTomlFile(loader, path, fileName);
		case YAML:
			return this.createYamlFile(loader, path, fileName);
		case CUSTOM:
			break;
		}
		return null;
	}

}
