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

import org.valdi.SuperApiX.ISuperPlugin;
import org.valdi.SuperApiX.common.config.IFileStorage;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

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
	    if (!configFile.exists()) {
	        try {
    	    	configFile.getParentFile().mkdirs();
            	InputStream is = plugin.getClass().getClassLoader().getResourceAsStream(configFile.getName());
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
        if (this.root == null) {
            throw new RuntimeException("Config is not loaded.");
        }
        
        return this.root.getNode(objects);
	}

	@Override
    public ConfigurationNode getFixedNode(String path) {
        return this.getNode(path.split("\\."));
    }

	@Override
	public String getString(String path, String def) {
        return getFixedNode(path).getString(def);
	}

	@Override
	public int getInt(String path, int def) {
        return getFixedNode(path).getInt(def);
	}

	@Override
	public float getFloat(String path, float def) {
		return getFixedNode(path).getFloat();
	}

	@Override
	public double getDouble(String path, double def) {
		return getFixedNode(path).getDouble();
	}

	@Override
	public boolean getBoolean(String path, boolean def) {
        return getFixedNode(path).getBoolean(def);
	}

	@Override
	public List<String> getStringList(String path, List<String> def) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return def;
        }

        return node.getList(Object::toString);
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
