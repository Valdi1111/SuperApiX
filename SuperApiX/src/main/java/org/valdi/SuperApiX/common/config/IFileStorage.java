package org.valdi.SuperApiX.common.config;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import ninja.leaping.configurate.ConfigurationNode;

public interface IFileStorage {
    
    public File getFile();
    
    public Path getFilePath();

	public void fromParent();

	public void create();

	public void loadOnly();

	public void save();

	public void save(boolean async);

    public ConfigurationNode getRoot();

    public ConfigurationNode getNode(Object[] objects);

    public ConfigurationNode getFixedNode(String path);

	public String getString(String path, String def);

	public int getInt(String path, int def);

	public double getDouble(String path, double def);

	public boolean getBoolean(String path, boolean def);

	public List<String> getStringList(String path, List<String> def);

	public List<String> getKeys(String path, List<String> def);

	public Map<String, String> getMap(String path, Map<String, String> def);

	public ConfigType getType();

}
