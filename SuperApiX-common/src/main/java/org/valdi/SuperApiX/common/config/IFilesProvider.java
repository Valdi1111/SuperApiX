package org.valdi.SuperApiX.common.config;

import java.io.File;

import org.valdi.SuperApiX.common.StoreLoader;

public interface IFilesProvider {

	/**
	 * Create Hocon file configuration
	 * @param loader the store loader
	 * @param path the file path
	 * @param fileName the file name
	 * @return a file storage instance for this file config
	 */
	IFileStorage createHoconFile(StoreLoader loader, File path, String fileName);

	/**
	 * Create Json file configuration
	 * @param loader the store loader
	 * @param path the file path
	 * @param fileName the file name
	 * @return a file storage instance for this file config
	 */
	IFileStorage createJsonFile(StoreLoader loader, File path, String fileName);

	/**
	 * Create Toml file configuration
	 * @param loader the store loader
	 * @param path the file path
	 * @param fileName the file name
	 * @return a file storage instance for this file config
	 */
	IFileStorage createTomlFile(StoreLoader loader, File path, String fileName);

	/**
	 * Create Yaml file configuration
	 * @param loader the store loader
	 * @param path the file path
	 * @param fileName the file name
	 * @return a file storage instance for this file config
	 */
	IFileStorage createYamlFile(StoreLoader loader, File path, String fileName);

	/**
	 * Create a file configuration of the given type
	 * @param type the config type
	 * @param loader the store loader
	 * @param path the file path
	 * @param fileName the file name
	 * @return a file storage instance for this file config
	 */
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
