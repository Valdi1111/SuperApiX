package org.valdi.SuperApiX.common.config.types;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.valdi.SuperApiX.common.config.types.nodes.ConfigNode;
import org.valdi.SuperApiX.common.config.types.nodes.IConfigNode;
import org.valdi.SuperApiX.common.config.IFileStorage;
import org.valdi.SuperApiX.common.plugin.StoreLoader;

import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public abstract class AbstractConfigAdapter implements IFileStorage {
    private final StoreLoader loader;
    private final File file;

	private ConfigurationLoader<? extends ConfigurationNode> manager;
    private ConfigNode root;
    
    protected AbstractConfigAdapter(StoreLoader loader, File path, String fileName) {
		this.file = new File(path, fileName);
        this.loader = loader;
    }
    
    @Override
    public File getFile() {
    	return file;
    }
    
    @Override
    public Path getFilePath() {
    	if(getFile() == null) {
    		return null;
		}
    	return this.getFile().toPath();
    }

	@Override
	public void fromParent() {
		fromParent(file.getName());
	}

	@Override
	public void fromParent(String path) {
    	file.getParentFile().mkdirs();
    	InputStream is = loader.getResource(path);

		this.fromStream(is);
	}

	@Override
	public void fromStream(InputStream is) {
	    if (!file.exists()) {
	        try {
                Files.copy(is, file.toPath());
	        } catch (NullPointerException | IOException e) {
	        	loader.getLogger().info("Error on config creating (" + file.getName() + ") > " + e.getMessage());
	        	e.printStackTrace();
				return;
	        }
	    }

	    this.loadOnly();
	}

	@Override
	public void create() {	
	    if (!file.exists()) {
			try {
    	    	file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (IOException e) {
				loader.getLogger().info("Error on config creating (" + file.getName() + ") > " + e.getMessage());
	        	e.printStackTrace();
				return;
			}
	    }
	
	    this.loadOnly();
	}

	@Override
	public void loadStream(InputStream is) {
		File tempFile;
		try {
			tempFile = File.createTempFile("stream2file-" + UUID.randomUUID().toString(), ".tmp");
			tempFile.deleteOnExit();
			Files.copy(is, tempFile.toPath());
		} catch (IOException e) {
			loader.getLogger().info("Error on temp file creating (" + file.getName() + ") > " + e.getMessage());
			e.printStackTrace();
			return;
		}

		this.setManagerFromFile(tempFile);
		this.load();
	}

	@Override
	public void loadOnly() {
		this.setManagerFromFile(this.getFile());
		this.load();
	}

	private void load() {
		try {
			this.root = new ConfigNode(manager.load());
		} catch (IOException e) {
			loader.getLogger().info("Error on config loading (" + file.getName() + ") > " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Overwrite existing manager
	 * @param manager the new configuration manager
	 */
	protected void setManager(ConfigurationLoader<? extends ConfigurationNode> manager) {
		this.manager = manager;
	}

	/**
	 * Setup default Configuration Manager depending on ConfigType
	 * @param file the file to set in the manager
	 */
	protected abstract void setManagerFromFile(File file);

	@Override
	public void save() {
        try {
        	manager.save(this.root.getNativeNode());
		} catch (NullPointerException e) { // Null manager, loadOnly() hasn't been called
			loader.getLogger().severe("You must load a file before saving! (" + file.getName() + ") > " + e.getMessage());
		} catch (IOException e) {
			loader.getLogger().info("Error on config saving (" + file.getName() + ") > " + e.getMessage());
        	e.printStackTrace();
		}
	}

	@Override
	public void save(boolean async) {
		if(!async) {
			save();
			return;
		}
		
		loader.getScheduler().runTaskAsynchronously((Runnable) this::save);
	}

	@Override
	public ConfigNode getParent() {
		return null;
	}

	@Override
	public ConfigurationNode getNativeNode() {
		return root.getNativeNode();
	}

	@Override
	public ConfigurationNode getNativeNode(Object[] objects) {
		return root.getNativeNode(objects);
	}

	@Override
	public ConfigurationNode getFixedNode(String path) {
		return root.getFixedNode(path);
	}

	@Override
	public ConfigNode getNode(String path) {
		return root.getNode(path);
	}

	@Override
	public ConfigNode getRoot() {
		return root;
	}

	@Override
	public Object get(String path) {
        return root.get(path);
	}

	@Override
	public Object get(String path, Object def) {
		return root.get(path, def);
	}

	@Override
	public <T> T get(String path, TypeToken<T> type) {
		return root.get(path, type);
	}

	@Override
	public <T> T get(String path, TypeToken<T> type, T def) {
		return root.get(path, type, def);
	}

	@Override
	public String getString(String path) {
		return root.getString(path);
	}

	@Override
	public String getString(String path, String def) {
		return root.getString(path, def);
	}

	@Override
	public int getInt(String path) {
		return root.getInt(path);
	}

	@Override
	public int getInt(String path, int def) {
		return root.getInt(path, def);
	}

	@Override
	public long getLong(String path) {
		return root.getLong(path);
	}

	@Override
	public long getLong(String path, long def) {
		return root.getLong(path, def);
	}

	@Override
	public float getFloat(String path) {
		return root.getFloat(path);
	}

	@Override
	public float getFloat(String path, float def) {
		return root.getFloat(path, def);
	}

	@Override
	public double getDouble(String path) {
		return root.getDouble(path);
	}

	@Override
	public double getDouble(String path, double def) {
		return root.getDouble(path, def);
	}

	@Override
	public boolean getBoolean(String path) {
		return root.getBoolean(path);
	}

	@Override
	public boolean getBoolean(String path, boolean def) {
		return root.getBoolean(path, def);
	}

	@Override
	public List<?> getList(String path) {
		return root.getList(path);
	}

	@Override
	public List<?> getList(String path, List<?> def) {
		return root.getList(path, def);
	}

	@Override
	public <T> List<T> getList(String path, TypeToken<T> type) {
		return root.getList(path, type);
	}

	@Override
	public <T> List<T> getList(String path, TypeToken<T> type, List<T> def) {
		return root.getList(path, type, def);
	}

	@Override
	public List<String> getStringList(String path) {
		return root.getStringList(path);
	}

	@Override
	public List<String> getStringList(String path, List<String> def) {
		return root.getStringList(path, def);
	}

	@Override
	public List<Integer> getIntList(String path) {
		return root.getIntList(path);
	}

	@Override
	public List<Integer> getIntList(String path, List<Integer> def) {
		return root.getIntList(path, def);
	}

	@Override
	public List<Long> getLongList(String path) {
		return root.getLongList(path);
	}

	@Override
	public List<Long> getLongList(String path, List<Long> def) {
		return root.getLongList(path, def);
	}

	@Override
	public List<Float> getFloatList(String path) {
		return root.getFloatList(path);
	}

	@Override
	public List<Float> getFloatList(String path, List<Float> def) {
		return root.getFloatList(path, def);
	}

	@Override
	public List<Double> getDoubleList(String path) {
		return root.getDoubleList(path);
	}

	@Override
	public List<Double> getDoubleList(String path, List<Double> def) {
		return root.getDoubleList(path, def);
	}

	@Override
	public List<Boolean> getBooleanList(String path) {
		return root.getBooleanList(path);
	}

	@Override
	public List<Boolean> getBooleanList(String path, List<Boolean> def) {
		return root.getBooleanList(path, def);
	}

	@Override
	public void set(String path, Object value) {
		root.set(path, value);
	}

	@Override
	public <T> void set(String path, TypeToken<T> type, T value) {
		root.set(path, type, value);
	}

	@Override
	public String getKey() {
		return root.getKey();
	}

	@Override
	public boolean contains(String path) {
        return root.contains(path);
	}

	@Override
	public boolean isSection(String path) {
        return root.isSection(path);
	}

	@Override
	public List<String> getKeys(String path) {
        return root.getKeys(path);
	}

	@Override
	public Map<String, ? extends ConfigNode> getValues(String path) {
		return root.getValues(path);
	}

	@Override
	public void removeChild(String path, String child) {
		root.removeChild(path, child);
	}

	@Override
	public void clear(String path) {
		root.clear(path);
	}

	@Override
	public IConfigNode mergeValuesFrom(IConfigNode other) {
		return root.mergeValuesFrom(other);
	}
}
