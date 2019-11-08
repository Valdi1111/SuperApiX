package org.valdi.SuperApiX.common.dependencies;

import org.valdi.SuperApiX.common.dependencies.classloader.IsolatedClassLoader;
import org.valdi.SuperApiX.common.dependencies.classloader.PluginClassLoader;
import org.valdi.SuperApiX.common.logging.SuperLogger;

import java.io.File;

public interface IDependencyManager {

    IsolatedClassLoader obtainClassLoaderWith(Dependency... dependencies);

    void loadStorageDependencies();

    void loadDependencies(Dependency... dependencies);

    void loadDependencies(SuperLogger logger, PluginClassLoader classLoader, File folder, boolean createSub, Dependency... dependencies);

}
