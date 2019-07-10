package org.valdi.SuperApiX.common.dependencies.classloader;

import java.io.File;
import java.net.URL;

/**
 * Represents the plugins classloader
 */
public interface PluginClassLoader {

    void loadJar(URL url);

    void loadJar(File file);

}
