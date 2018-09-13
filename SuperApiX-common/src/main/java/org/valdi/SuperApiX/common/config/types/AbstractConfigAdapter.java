package org.valdi.SuperApiX.common.config.types;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.valdi.SuperApiX.common.ISuperPlugin;
import org.valdi.SuperApiX.common.config.IFileStorage;

import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public abstract class AbstractConfigAdapter implements IFileStorage {
    private final ISuperPlugin plugin;
    private final File configFile;

    private ConfigurationNode root;
    private ConfigurationLoader<? extends ConfigurationNode> manager;
    
    protected AbstractConfigAdapter(ISuperPlugin plugin, File path, String fileName) {
		this.configFile = new File(path, fileName);
        this.plugin = plugin;
    }
    
    @Override
    public File getFile() {
    	return configFile;
    }
    
    @Override
    public Path getFilePath() {
    	return this.getFile().toPath();
    }

	@Override
	public void fromParent() {
		fromParent(plugin.getClass().getClassLoader());
	}

	@Override
	public void fromParent(ClassLoader loader) {
    	configFile.getParentFile().mkdirs();
    	InputStream is = loader.getResourceAsStream(configFile.getName());
		fromStream(is);
	}

	@Override
	public void fromStream(InputStream is) {		
	    if (!configFile.exists()) {
	        try {
                Files.copy(is, configFile.toPath());
	        } catch (IOException e) {
	        	plugin.getLogger().info("Error on config creating (" + configFile.getName() + ") > " + e.getMessage());
	        	e.printStackTrace();
	        }
	    }

	    this.loadOnly();
	}

	@Override
	public void create() {	
	    if (!configFile.exists()) {
			try {
    	    	configFile.getParentFile().mkdirs();
				configFile.createNewFile();
			} catch (IOException e) {
				plugin.getLogger().info("Error on config creating (" + configFile.getName() + ") > " + e.getMessage());
	        	e.printStackTrace();
			}
	    }
	
	    this.loadOnly();
	}
	
	protected void setManager(ConfigurationLoader<? extends ConfigurationNode> manager) {
        this.manager = manager;
	}

	@Override
	public void loadOnly() {
        try {
            this.root = manager.load();
        } catch (IOException e) {
        	plugin.getLogger().info("Error on config loading (" + configFile.getName() + ") > " + e.getMessage());
        	e.printStackTrace();
        }
	}

	@Override
	public void save() {
        try {
        	manager.save(this.root);
		} catch (IOException e) {
			plugin.getLogger().info("Error on config saving (" + configFile.getName() + ") > " + e.getMessage());
        	e.printStackTrace();
		}
	}

	@Override
	public void save(boolean async) {
		if(async) {
			plugin.getBootstrap().getScheduler().executeAsync(() -> save());
		} else {
			save();
		}
	}

	@Override
    public ConfigurationNode getRoot() {
        if (this.root == null) {
            throw new RuntimeException("Config is not loaded.");
        }
        
        return this.root;
    }

	@Override
    public ConfigurationNode getNode(Object[] objects) {
        return this.getRoot().getNode(objects);
	}

	@Override
    public ConfigurationNode getFixedNode(String path) {
		if(path.equals("")) {
			return this.getRoot();
		}
		
        return this.getNode(path.split("\\."));
    }

	@Override
	public Object get(String path) {
        return getFixedNode(path).getValue();
	}

	@Override
	public Object get(String path, Object def) {
        return getFixedNode(path).getValue(def);
	}

	@Override
	public <T> T get(String path, TypeToken<T> type) {
        try {
			return getFixedNode(path).getValue(type);
		} catch (ObjectMappingException e) {
			return null;
		}
	}

	@Override
	public <T> T get(String path, TypeToken<T> type, T def) {
        try {
			return getFixedNode(path).getValue(type, def);
		} catch (ObjectMappingException e) {
			return def;
		}
	}

	@Override
	public String getString(String path) {
        return getFixedNode(path).getString();
	}

	@Override
	public String getString(String path, String def) {
        return getFixedNode(path).getString(def);
	}

	@Override
	public int getInt(String path) {
        return getFixedNode(path).getInt();
	}

	@Override
	public int getInt(String path, int def) {
        return getFixedNode(path).getInt(def);
	}

	@Override
	public long getLong(String path) {
        return getFixedNode(path).getLong();
	}

	@Override
	public long getLong(String path, long def) {
        return getFixedNode(path).getLong(def);
	}

	@Override
	public float getFloat(String path) {
		return getFixedNode(path).getFloat();
	}

	@Override
	public float getFloat(String path, float def) {
		return getFixedNode(path).getFloat(def);
	}

	@Override
	public double getDouble(String path) {
		return getFixedNode(path).getDouble();
	}

	@Override
	public double getDouble(String path, double def) {
		return getFixedNode(path).getDouble(def);
	}

	@Override
	public boolean getBoolean(String path) {
        return getFixedNode(path).getBoolean();
	}

	@Override
	public boolean getBoolean(String path, boolean def) {
        return getFixedNode(path).getBoolean(def);
	}

	@Override
	public <T> List<T> getList(String path, TypeToken<T> type) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return null;
        }

        try {
			return node.getList(type);
		} catch (ObjectMappingException e) {
			return null;
		}
	}

	@Override
	public <T> List<T> getList(String path, TypeToken<T> type, List<T> def) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return def;
        }

        try {
			return node.getList(type, def);
		} catch (ObjectMappingException e) {
			return def;
		}
	}

	@Override
	public List<?> getList(String path) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return null;
        }

		return (List<?>) node.getValue();
	}

	@Override
	public List<?> getList(String path, List<?> def) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return def;
        }

		return (List<?>) node.getValue(def);
	}

	@Override
	public List<String> getStringList(String path) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return null;
        }

		return (List<String>) node.getValue();
	}

	@Override
	public List<String> getStringList(String path, List<String> def) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return def;
        }

		return (List<String>) node.getValue(def);
	}

	@Override
	public List<Integer> getIntList(String path) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return null;
        }

		return (List<Integer>) node.getValue();
	}

	@Override
	public List<Integer> getIntList(String path, List<Integer> def) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return def;
        }

		return (List<Integer>) node.getValue(def);
	}

	@Override
	public List<Long> getLongList(String path) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return null;
        }

		return (List<Long>) node.getValue();
	}

	@Override
	public List<Long> getLongList(String path, List<Long> def) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return def;
        }

		return (List<Long>) node.getValue(def);
	}

	@Override
	public List<Float> getFloatList(String path) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return null;
        }

		return (List<Float>) node.getValue();
	}

	@Override
	public List<Float> getFloatList(String path, List<Float> def) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return def;
        }

		return (List<Float>) node.getValue(def);
	}

	@Override
	public List<Double> getDoubleList(String path) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return null;
        }

		return (List<Double>) node.getValue();
	}

	@Override
	public List<Double> getDoubleList(String path, List<Double> def) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return def;
        }

		return (List<Double>) node.getValue(def);
	}

	@Override
	public List<Boolean> getBooleanList(String path) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return null;
        }

		return (List<Boolean>) node.getValue();
	}

	@Override
	public List<Boolean> getBooleanList(String path, List<Boolean> def) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return def;
        }

		return (List<Boolean>) node.getValue(def);
	}

	@Override
	public void set(String path, Object value) {
        getFixedNode(path).setValue(value);
	}

	@Override
	public void setString(String path, String value) {
        getFixedNode(path).setValue(value);
	}

	@Override
	public void setInt(String path, int value) {
        getFixedNode(path).setValue(value);
	}

	@Override
	public void setLong(String path, long value) {
        getFixedNode(path).setValue(value);
	}

	@Override
	public void setFloat(String path, float value) {
        getFixedNode(path).setValue(value);
	}

	@Override
	public void setDouble(String path, double value) {
        getFixedNode(path).setValue(value);
	}

	@Override
	public void setBoolean(String path, boolean value) {
        getFixedNode(path).setValue(value);
	}

	@Override
	public void setStringList(String path, List<String> value) {
        getFixedNode(path).setValue(value);
	}

	@Override
	public boolean contains(String path) {
        return getFixedNode(path).getValue() != null;
	}

	@Override
	public boolean isSection(String path) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return false;
        }

        return node.getChildrenMap() != null && !node.getChildrenMap().isEmpty() && node.getChildrenMap().keySet() != null && !node.getChildrenMap().keySet().isEmpty();
	}

	@Override
	public List<String> getKeys(String path) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return null;
        }

        return node.getChildrenMap().keySet().stream().map(Object::toString).collect(Collectors.toList());
	}

	@Override
	public List<String> getKeys(String path, List<String> def) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return def;
        }

        return node.getChildrenMap().keySet().stream().map(Object::toString).collect(Collectors.toList());
	}

	@Override @SuppressWarnings("unchecked")
	public Map<String, String> getMap(String path, Map<String, String> def) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return def;
        }

        Map<String, Object> m = (Map<String, Object>) node.getValue(Collections.emptyMap());
        return m.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, v -> v.getValue().toString()));
	}

}
