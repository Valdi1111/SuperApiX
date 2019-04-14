package org.valdi.SuperApiX.common.config;

import java.io.File;

import org.valdi.SuperApiX.common.ISuperPlugin;
import org.valdi.SuperApiX.common.config.advanced.StoreLoader;
import org.valdi.SuperApiX.common.config.types.HoconConfiguration;
import org.valdi.SuperApiX.common.config.types.JsonConfiguration;
import org.valdi.SuperApiX.common.config.types.TomlConfiguration;
import org.valdi.SuperApiX.common.config.types.YamlConfiguration;

public class FilesProvider implements IFilesProvider {
	private final ISuperPlugin plugin;
	
	public FilesProvider(final ISuperPlugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public IFileStorage createHoconFile(File path, String fileName) {
		return createHoconFile(plugin, path, fileName);
	}
	
	@Override
	public IFileStorage createHoconFile(StoreLoader loader, File path, String fileName) {
		return new HoconConfiguration(loader, path, fileName);
	}
	
	@Override
	public IFileStorage createJsonFile(File path, String fileName) {
		return createJsonFile(plugin, path, fileName);
	}
	
	@Override
	public IFileStorage createJsonFile(StoreLoader loader, File path, String fileName) {
		return new JsonConfiguration(loader, path, fileName);
	}
	
	@Override
	public IFileStorage createTomlFile(File path, String fileName) {
		return createTomlFile(plugin, path, fileName);
	}
	
	@Override
	public IFileStorage createTomlFile(StoreLoader loader, File path, String fileName) {
		return new TomlConfiguration(loader, path, fileName);
	}
	
	@Override
	public IFileStorage createYamlFile(File path, String fileName) {
		return createYamlFile(plugin, path, fileName);
	}
	
	@Override
	public IFileStorage createYamlFile(StoreLoader loader, File path, String fileName) {
		return new YamlConfiguration(loader, path, fileName);
	}

}
