package org.valdi.SuperApiX.common.config;

import java.io.File;

import org.valdi.SuperApiX.ISuperPlugin;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.common.config.types.HoconConfiguration;
import org.valdi.SuperApiX.common.config.types.JsonConfiguration;
import org.valdi.SuperApiX.common.config.types.TomlConfiguration;
import org.valdi.SuperApiX.common.config.types.YamlConfiguration;

public class FilesProvider implements IFilesProvider {
	private final SuperApiBukkit plugin;
	
	public FilesProvider(final SuperApiBukkit plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public IFileStorage createHoconFile(File path, String fileName) {
		return createHoconFile(plugin, path, fileName);
	}
	
	@Override
	public IFileStorage createHoconFile(ISuperPlugin plugin, File path, String fileName) {
		return new HoconConfiguration(plugin, path, fileName);
	}
	
	@Override
	public IFileStorage createJsonFile(File path, String fileName) {
		return createJsonFile(plugin, path, fileName);
	}
	
	@Override
	public IFileStorage createJsonFile(ISuperPlugin plugin, File path, String fileName) {
		return new JsonConfiguration(plugin, path, fileName);
	}
	
	@Override
	public IFileStorage createTomlFile(File path, String fileName) {
		return createTomlFile(plugin, path, fileName);
	}
	
	@Override
	public IFileStorage createTomlFile(ISuperPlugin plugin, File path, String fileName) {
		return new TomlConfiguration(plugin, path, fileName);
	}
	
	@Override
	public IFileStorage createYamlFile(File path, String fileName) {
		return createYamlFile(plugin, path, fileName);
	}
	
	@Override
	public IFileStorage createYamlFile(ISuperPlugin plugin, File path, String fileName) {
		return new YamlConfiguration(plugin, path, fileName);
	}

}
