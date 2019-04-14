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

import org.valdi.SuperApiX.common.config.IFileStorage;
import org.valdi.SuperApiX.common.config.advanced.StoreLoader;

import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public abstract class AbstractConfigAdapter implements IFileStorage {
    private final StoreLoader loader;
    private final File configFile;

    private ConfigurationNode root;
    private ConfigurationLoader<? extends ConfigurationNode> manager;
    
    protected AbstractConfigAdapter(StoreLoader loader, File path, String fileName) {
		this.configFile = new File(path, fileName);
        this.loader = loader;
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
		fromParent(configFile.getName());
	}

	@Override
	public void fromParent(String path) {
    	configFile.getParentFile().mkdirs();
    	InputStream is = loader.getResource(path);
		fromStream(is);
	}

	@Override
	public void fromStream(InputStream is) {
	    if (!configFile.exists()) {
	        try {
                Files.copy(is, configFile.toPath());
	        } catch (NullPointerException | IOException e) {
	        	loader.getLogger().info("Error on config creating (" + configFile.getName() + ") > " + e.getMessage());
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
				loader.getLogger().info("Error on config creating (" + configFile.getName() + ") > " + e.getMessage());
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
        	loader.getLogger().info("Error on config loading (" + configFile.getName() + ") > " + e.getMessage());
        	e.printStackTrace();
        }
	}

	@Override
	public void save() {
        try {
        	manager.save(this.root);
		} catch (IOException e) {
			loader.getLogger().info("Error on config saving (" + configFile.getName() + ") > " + e.getMessage());
        	e.printStackTrace();
		}
	}

	@Override
	public void save(boolean async) {
		if(!async) {
			save();
			return;
		}
		
		loader.getScheduler().executeAsync(() -> save());
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
		return this.getList(path, TypeToken.of(String.class));
	}

	@Override
	public List<String> getStringList(String path, List<String> def) {
		return this.getList(path, TypeToken.of(String.class), def);
	}

	@Override
	public List<Integer> getIntList(String path) {
		return this.getList(path, TypeToken.of(Integer.class));
	}

	@Override
	public List<Integer> getIntList(String path, List<Integer> def) {
		return this.getList(path, TypeToken.of(Integer.class), def);
	}

	@Override
	public List<Long> getLongList(String path) {
		return this.getList(path, TypeToken.of(Long.class));
	}

	@Override
	public List<Long> getLongList(String path, List<Long> def) {
		return this.getList(path, TypeToken.of(Long.class), def);
	}

	@Override
	public List<Float> getFloatList(String path) {
		return this.getList(path, TypeToken.of(Float.class));
	}

	@Override
	public List<Float> getFloatList(String path, List<Float> def) {
		return this.getList(path, TypeToken.of(Float.class), def);
	}

	@Override
	public List<Double> getDoubleList(String path) {
		return this.getList(path, TypeToken.of(Double.class));
	}

	@Override
	public List<Double> getDoubleList(String path, List<Double> def) {
		return this.getList(path, TypeToken.of(Double.class), def);
	}

	@Override
	public List<Boolean> getBooleanList(String path) {
		return this.getList(path, TypeToken.of(Boolean.class));
	}

	@Override
	public List<Boolean> getBooleanList(String path, List<Boolean> def) {
		return this.getList(path, TypeToken.of(Boolean.class), def);
	}

	@Override
	public void set(String path, Object value) {
        getFixedNode(path).setValue(value);
	}

	@Override
	public <T> void set(String path, TypeToken<T> type, Object value) {
        try {
			getFixedNode(path).setValue(type, (T)value);
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean contains(String path) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return false;
        }
        
        return node.getValue() != null || node.hasListChildren() || node.hasMapChildren();
	}

	@Override
	public boolean isSection(String path) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return false;
        }

        return node.hasMapChildren();
	}

	@Override
	public List<? extends ConfigurationNode> getKeys(String path) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return null;
        }

        return node.getChildrenList();
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
