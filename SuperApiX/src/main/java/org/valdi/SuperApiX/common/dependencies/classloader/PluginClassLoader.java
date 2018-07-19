package org.valdi.SuperApiX.common.dependencies.classloader;

import java.net.URL;
import java.nio.file.Path;

/**
 * Represents the plugins classloader
 */
public interface PluginClassLoader {

    void loadJar(URL url);

    void loadJar(Path file);

}
