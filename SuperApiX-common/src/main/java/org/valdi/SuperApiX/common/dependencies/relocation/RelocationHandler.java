package org.valdi.SuperApiX.common.dependencies.relocation;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.valdi.SuperApiX.common.dependencies.Dependencies;
import org.valdi.SuperApiX.common.dependencies.Dependency;
import org.valdi.SuperApiX.common.dependencies.IDependencyManager;
import org.valdi.SuperApiX.common.dependencies.classloader.IsolatedClassLoader;

/**
 * Handles class runtime relocation of packages in downloaded dependencies
 */
public class RelocationHandler {
    public static final Dependency[] DEPENDENCIES = new Dependency[] { Dependencies.ASM, Dependencies.ASM_COMMONS, Dependencies.JAR_RELOCATOR };
    private static final String JAR_RELOCATOR_CLASS = "me.lucko.jarrelocator.JarRelocator";
    private static final String JAR_RELOCATOR_RUN_METHOD = "run";

    private final Constructor<?> constructor;
    private final Method method;

    public RelocationHandler(IDependencyManager dependencyManager) {
        try {
            // download the required dependencies for remapping
            dependencyManager.loadDependencies(DEPENDENCIES);
            // get a classloader containing the required dependencies as sources
            IsolatedClassLoader classLoader = dependencyManager.obtainClassLoaderWith(DEPENDENCIES);

            // load the relocator class
            Class<?> clazz = classLoader.loadClass(JAR_RELOCATOR_CLASS);

            // prepare the the reflected constructor & method instances
            this.constructor = clazz.getDeclaredConstructor(File.class, File.class, Map.class);
            this.constructor.setAccessible(true);

            this.method = clazz.getDeclaredMethod(JAR_RELOCATOR_RUN_METHOD);
            this.method.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void remap(File input, File output, List<Relocation> relocations) throws Exception {
        Map<String, String> mappings = new HashMap<>();
        for (Relocation relocation : relocations) {
            mappings.put(relocation.getPattern(), relocation.getRelocatedPattern());
        }

        // create and invoke a new relocator
        Object relocator = this.constructor.newInstance(input, output, mappings);
        this.method.invoke(relocator);
    }

}
