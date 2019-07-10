package org.valdi.SuperApiX.common.config;

import org.valdi.SuperApiX.common.config.types.ConfigType;
import org.valdi.SuperApiX.common.config.types.nodes.IConfigNode;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;

public interface IFileStorage extends IConfigNode {

	/**
	 * Get the file
	 * @return the file or null if virtual
	 */
	File getFile();

	/**
	 * Get the file's path
	 * @return the file's path or null if virtual
	 */
	Path getFilePath();

	/**
	 * Load config copying the file from jar if it doesn't exists.
	 * The file will be copied from jar using the same path.
	 */
	void fromParent();

	/**
	 * Load config copying the file from jar if it doesn't exists.
	 * @param path the path of the file stored into the jar
	 */
	void fromParent(String path);

	/**
	 * Load config from a given InputStream
	 * @param is the input stream
	 */
	void fromStream(InputStream is);

	/**
	 * Create an empty file
	 */
	void create();

	/**
	 * Load the root ConfigurationNode from the given InputStream, physical file won't be loaded
	 * @param is the InputStream to load from
	 */
	void loadStream(InputStream is);

	/**
	 * Load the root ConfigurationNode from the file.
	 */
	void loadOnly();

	/**
	 * Save changes to file
	 */
	void save();

	/**
	 * Save changes to file
	 * @param async true if it has to be made async
	 */
	void save(boolean async);

	/**
	 * Get the root configuration node
	 * @return the root node
	 */
	IConfigNode getRoot();

	/**
	 * Get the ConfigType for this config
	 * @return the config type
	 */
	ConfigType getType();

}
