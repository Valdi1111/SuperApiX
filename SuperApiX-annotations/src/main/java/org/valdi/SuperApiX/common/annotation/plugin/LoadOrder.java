package org.valdi.SuperApiX.common.annotation.plugin;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  Part of the plugin annotations framework.
 *  <p>
 *  Represents the optional load order of the plugin.
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LoadOrder {
    /**
     * Explicitly state when the plugin should be loaded.
     * If not defined, will default to {@link PluginLoadOrder#POSTWORLD}.
     * See {@link PluginLoadOrder}
     */
	Phase value() default Phase.POSTWORLD;
    
    public static enum Phase {
    	STARTUP,
    	POSTWORLD;
    }
}
