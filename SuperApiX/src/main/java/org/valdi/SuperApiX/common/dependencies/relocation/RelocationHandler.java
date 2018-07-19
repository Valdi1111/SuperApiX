package org.valdi.SuperApiX.common.dependencies.relocation;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.valdi.SuperApiX.common.dependencies.Dependencies;
import org.valdi.SuperApiX.common.dependencies.Dependency;
import org.valdi.SuperApiX.common.dependencies.DependencyManager;
import org.valdi.SuperApiX.common.dependencies.classloader.IsolatedClassLoader;

/**
 * Handles class runtime relocation of packages in downloaded dependencies
 */
public class RelocationHandler {
    public static final Set<Dependency> DEPENDENCIES = new HashSet<Dependency>() {
		private static final long serialVersionUID = -4770074707911058062L;

		{
    		add(Dependencies.ASM);
    		add(Dependencies.ASM_COMMONS);
    		add(Dependencies.JAR_RELOCATOR);
    	}
    };
    private static final String JAR_RELOCATOR_CLASS = "me.lucko.jarrelocator.JarRelocator";
    private static final String JAR_RELOCATOR_RUN_METHOD = "run";

    private final Constructor<?> jarRelocatorConstructor;
    private final Method jarRelocatorRunMethod;

    public RelocationHandler(DependencyManager dependencyManager) {
        try {
            // download the required dependencies for remapping
            dependencyManager.loadDependencies(DEPENDENCIES);
            // get a classloader containing the required dependencies as sources
            IsolatedClassLoader classLoader = dependencyManager.obtainClassLoaderWith(DEPENDENCIES);

            // load the relocator class
            Class<?> jarRelocatorClass = classLoader.loadClass(JAR_RELOCATOR_CLASS);

            // prepare the the reflected constructor & method instances
            this.jarRelocatorConstructor = jarRelocatorClass.getDeclaredConstructor(File.class, File.class, Map.class);
            this.jarRelocatorConstructor.setAccessible(true);

            this.jarRelocatorRunMethod = jarRelocatorClass.getDeclaredMethod(JAR_RELOCATOR_RUN_METHOD);
            this.jarRelocatorRunMethod.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void remap(Path input, Path output, List<Relocation> relocations) throws Exception {
        Map<String, String> mappings = new HashMap<>();
        for (Relocation relocation : relocations) {
            mappings.put(relocation.getPattern(), relocation.getRelocatedPattern());
        }

        // create and invoke a new relocator
        Object relocator = this.jarRelocatorConstructor.newInstance(input.toFile(), output.toFile(), mappings);
        this.jarRelocatorRunMethod.invoke(relocator);
    }

}
