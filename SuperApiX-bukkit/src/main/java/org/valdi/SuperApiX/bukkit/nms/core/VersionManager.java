package org.valdi.SuperApiX.bukkit.nms.core;

import org.bukkit.Bukkit;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;

public class VersionManager {
    private final SuperApiBukkit plugin;
    private final String version;
    
    private boolean spigotSupported = true;
    private boolean nmsSupported = true;

    public VersionManager(final SuperApiBukkit plugin) {
        this.plugin = plugin;
        this.version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        
        if(this.getSpigotVersion() == SpigotVersion.INCOMPATIBLE) {
            this.spigotSupported = false;
        }
        
        if(this.getNmsVersion() == NmsVersion.INCOMPATIBLE) {
            this.nmsSupported = false;
        }
    }
    
    public SuperApiBukkit getPlugin() {
    	return this.plugin;
    }
    
    public String getVersionRaw() {
    	return this.version;
    }
    
    public NmsVersion getNmsVersion() {
    	try {
    		return NmsVersion.valueOf(this.version);
    	} catch(Exception e) {
    		return NmsVersion.INCOMPATIBLE;
    	}
    }
    
    public SpigotVersion getSpigotVersion() {
    	try {
            Class.forName("net.md_5.bungee.api.chat.ComponentBuilder");
        	return SpigotVersion.valueOf("v1");
    	} catch(Exception e) {
    		return SpigotVersion.INCOMPATIBLE;
    	}
    }
    
    public boolean isNmsSupported() {
    	return this.nmsSupported;
    }
    
    public boolean isSpigotSupported() {
    	return this.spigotSupported;
    }
    
    /**
     * Supported net.minecraft.server versions
     */
    public static enum NmsVersion {
    	INCOMPATIBLE,
    	v1_7_R1,
    	v1_7_R2,
    	v1_7_R3,
    	v1_7_R4,
    	v1_8_R1,
    	v1_8_R2,
    	v1_8_R3,
    	v1_9_R1,
    	v1_9_R2,
    	v1_10_R1,
    	v1_11_R1,
    	v1_12_R1,
    	v1_13_R1,
    	v1_13_R2,
    	v1_14_R1;
    	
    	public String getRaw() {
    		return this.name();
    	}
    }
    
    public static enum SpigotVersion {
    	INCOMPATIBLE,
    	v1;
    	
    	public String getRaw() {
    		return this.name();
    	}
    }
    
    public boolean newSounds() {
    	return !version.startsWith("v1_8_") && !version.startsWith("v1_7_");
    }
}
