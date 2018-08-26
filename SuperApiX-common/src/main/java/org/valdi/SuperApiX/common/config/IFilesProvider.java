package org.valdi.SuperApiX.common.config;

import java.io.File;

import org.valdi.SuperApiX.common.ISuperPlugin;

public interface IFilesProvider {

	public IFileStorage createHoconFile(File path, String fileName);

	public IFileStorage createHoconFile(ISuperPlugin plugin, File path, String fileName);

	public IFileStorage createJsonFile(File path, String fileName);

	public IFileStorage createJsonFile(ISuperPlugin plugin, File path, String fileName);

	public IFileStorage createTomlFile(File path, String fileName);

	public IFileStorage createTomlFile(ISuperPlugin plugin, File path, String fileName);

	public IFileStorage createYamlFile(File path, String fileName);

	public IFileStorage createYamlFile(ISuperPlugin plugin, File path, String fileName);

}
