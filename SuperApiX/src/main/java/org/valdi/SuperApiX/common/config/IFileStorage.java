package org.valdi.SuperApiX.common.config;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import com.google.common.reflect.TypeToken;

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

	public Object get(String path);

	public Object get(String path, Object def);

	public <T> T get(String path, TypeToken<T> type);

	public <T> T get(String path, TypeToken<T> type, T def);

	public String getString(String path);

	public String getString(String path, String def);

	public int getInt(String path);

	public int getInt(String path, int def);

	public long getLong(String path);

	public long getLong(String path, long def);

	public float getFloat(String path);

	public float getFloat(String path, float def);

	public double getDouble(String path);

	public double getDouble(String path, double def);

	public boolean getBoolean(String path);

	public boolean getBoolean(String path, boolean def);

	public <T> List<T> getList(String path, TypeToken<T> type);

	public <T> List<T> getList(String path, TypeToken<T> type, List<T> def);

	public List<?> getList(String path);

	public List<?> getList(String path, List<?> def);

	public List<String> getStringList(String path);

	public List<String> getStringList(String path, List<String> def);
	
	public List<Integer> getIntList(String path);
	
	public List<Integer> getIntList(String path, List<Integer> def);
	
	public List<Long> getLongList(String path);
	
	public List<Long> getLongList(String path, List<Long> def);
	
	public List<Float> getFloatList(String path);
	
	public List<Float> getFloatList(String path, List<Float> def);
	
	public List<Double> getDoubleList(String path);
	
	public List<Double> getDoubleList(String path, List<Double> def);
	
	public List<Boolean> getBooleanList(String path);
	
	public List<Boolean> getBooleanList(String path, List<Boolean> def);

	public void setObject(String path, Object value);

	public void setString(String path, String value);

	public void setInt(String path, int value);

	public void setLong(String path, long value);

	public void setFloat(String path, float value);

	public void setDouble(String path, double value);

	public void setBoolean(String path, boolean value);

	public void setStringList(String path, List<String> value);

	public boolean isSection(String path);

	public List<String> getKeys(String path);

	public List<String> getKeys(String path, List<String> def);

	public Map<String, String> getMap(String path, Map<String, String> def);

	public ConfigType getType();

}
