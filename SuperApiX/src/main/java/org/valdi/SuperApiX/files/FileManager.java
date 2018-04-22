package org.valdi.SuperApiX.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.valdi.SuperApiX.SuperApi;

public class FileManager {
	
	private File configFile;
	private FileConfiguration configConf;
	private File path;
	private String name;
	
	public FileManager(File path, String name) {
		this.path = path;
		this.name = name;
	}
	
	public void loadOnly() {
	
		configFile = new File(path, name);	
	    configConf = new YamlConfiguration();
	    
	    try {
	    	configConf.load(configFile);
	    } catch (IOException | InvalidConfigurationException e) {
	        SuperApi.getInstance().getLogger().info("Error on file loading (" + configFile.getName() + ")");
	    }
	}
	
	public void create() {
	
		configFile = new File(path, name);
	
	    if (!configFile.exists()) {
	    	try {
				configFile.createNewFile();
			} catch (IOException e) {
				SuperApi.getInstance().getLogger().info("Error on file creating (" + configFile.getName() + ") > " + e.getMessage());
			}
	    }
	
	    configConf = new YamlConfiguration();
	    
	    try {
	    	configConf.load(configFile);
	    } catch (IOException | InvalidConfigurationException e) {
	    	SuperApi.getInstance().getLogger().info("Error on file loading (" + configFile.getName() + ") > " + e.getMessage());
	    }
	}
	
	public void createFromParent(JavaPlugin plugin) {
	
		configFile = new File(path, name);
	
	    if (!configFile.exists()) {
	    	configFile.getParentFile().mkdirs();
	    	plugin.saveResource(name, false);
	    }
	
	    configConf = new YamlConfiguration();
	    
	    try {
	    	configConf.load(configFile);
	    } catch (IOException | InvalidConfigurationException e) {
	    	SuperApi.getInstance().getLogger().info("Error on config loading (" + configFile.getName() + ") > " + e.getMessage());
	    }
	}
	
	public FileConfiguration getConfig() {
		return configConf;
	}
	
	public File getFile() {
		return configFile;
	}
	
	public void saveFile() {
        try {
        	configConf.save(configFile);
        } catch (IOException e) {
        	SuperApi.getInstance().getLogger().info("Error on config saving (" + configFile.getName() + ") > " + e.getMessage());
        }
	}

}
