package org.valdi.SuperApiX.common.annotation.plugin;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


/**
 *  Represents the required elements needed to register a Bukkit plugin.
 *  This <i>must</i> be placed in the main class of your plugin
 *  (i.e. the class that extends {@link org.bukkit.plugin.java.JavaPlugin}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BukkitPlugin {
    /**
     * The name of this plugin
     */
    String name();

    /**
     * This version of this plugin
     */
    String version();

    Target api() default Target.DEFAULT;

    String DEFAULT_VERSION = "v0.0";

    /**
     * Specifies the target api-version for this plugin.
     *
     * All pre-1.13 plugins must use {@link #DEFAULT}.
     */
    public static enum Target {
        /**
         * This target version specifies that the plugin was made for pre-1.13 Spigot versions.
         */
        DEFAULT( null ),

        /**
         * This target version specifies that the plugin was made with 1.13+ versions in mind.
         */
        v1_13( "1.13", DEFAULT );


        private final String version;
        private final Collection<Target> conflictsWith = Sets.newLinkedHashSet();

        private Target(String version, Target... conflictsWith) {
            this.version = version;
            this.conflictsWith.addAll( Lists.newArrayList( conflictsWith ) );
        }


        public String getVersion() {
            return version;
        }

        public boolean conflictsWith(Target target) {
            return this.conflictsWith.contains( target );
        }
    }
}
