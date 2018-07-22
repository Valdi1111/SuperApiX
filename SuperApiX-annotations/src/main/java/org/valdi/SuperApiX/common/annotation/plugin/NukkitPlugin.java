package org.valdi.SuperApiX.common.annotation.plugin;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 *  Represents the required elements needed to register a Bukkit plugin.
 *  This <i>must</i> be placed in the main class of your plugin
 *  (i.e. the class that extends {@link org.bukkit.plugin.java.JavaPlugin}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NukkitPlugin {    
    /**
     * The name of this plugin
     */
    String name();

    /**
     * This version of this plugin
     */
    String version();
    
    /**
     * The Nukkit API versions that the plugin supports.
     */
    String[] api();

    String DEFAULT_VERSION = "v0.0";
    String[] DEFAULT_API_VERSION = new String[] {"v1.0"};
}
