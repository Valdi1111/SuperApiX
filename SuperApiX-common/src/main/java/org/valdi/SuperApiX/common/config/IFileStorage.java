package org.valdi.SuperApiX.common.config;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.ConfigurationNode;

public interface IFileStorage {
    
    File getFile();
    
    Path getFilePath();

	void fromParent();

	void fromParent(String path);

	void fromStream(InputStream is);

	void create();

	void loadOnly();

	void save();

	void save(boolean async);

    ConfigurationNode getRoot();

    ConfigurationNode getNode(Object[] objects);

    ConfigurationNode getFixedNode(String path);
    
    // Get methods

	Object get(String path);

	Object get(String path, Object def);

	<T> T get(String path, TypeToken<T> type);

	<T> T get(String path, TypeToken<T> type, T def);

	String getString(String path);

	String getString(String path, String def);

	int getInt(String path);

	int getInt(String path, int def);

	long getLong(String path);

	long getLong(String path, long def);

	float getFloat(String path);

	float getFloat(String path, float def);

	double getDouble(String path);

	double getDouble(String path, double def);

	boolean getBoolean(String path);

	boolean getBoolean(String path, boolean def);
	
	// Get list methods

	<T> List<T> getList(String path, TypeToken<T> type);

	<T> List<T> getList(String path, TypeToken<T> type, List<T> def);

	List<?> getList(String path);

	List<?> getList(String path, List<?> def);

	List<String> getStringList(String path);

	List<String> getStringList(String path, List<String> def);
	
	List<Integer> getIntList(String path);
	
	List<Integer> getIntList(String path, List<Integer> def);
	
	List<Long> getLongList(String path);
	
	List<Long> getLongList(String path, List<Long> def);
	
	List<Float> getFloatList(String path);
	
	List<Float> getFloatList(String path, List<Float> def);
	
	List<Double> getDoubleList(String path);
	
	List<Double> getDoubleList(String path, List<Double> def);
	
	List<Boolean> getBooleanList(String path);
	
	List<Boolean> getBooleanList(String path, List<Boolean> def);
	
	// Set methods

	void set(String path, Object value);

	<T> void set(String path, TypeToken<T> type, Object value);

	boolean contains(String path);

	boolean isSection(String path);

	List<? extends ConfigurationNode> getKeys(String path);

	Map<String, String> getMap(String path, Map<String, String> def);

	ConfigType getType();

}
