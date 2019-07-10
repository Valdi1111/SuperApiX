package org.valdi.SuperApiX.common.dependencies;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.ByteStreams;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.valdi.SuperApiX.common.dependencies.classloader.PluginClassLoader;
import org.valdi.SuperApiX.common.logging.SuperLogger;
import org.valdi.SuperApiX.common.plugin.ISuperBootstrap;
import org.valdi.SuperApiX.common.dependencies.classloader.IsolatedClassLoader;
import org.valdi.SuperApiX.common.dependencies.relocation.Relocation;
import org.valdi.SuperApiX.common.dependencies.relocation.RelocationHandler;

/**
 * Responsible for loading runtime dependencies.
 */
public class DependencyManager {
    private final ISuperBootstrap bootstrap;
    private final MessageDigest digest;
    private final DependencyRegistry registry;
    private final Map<Dependency, File> loaded = new HashMap<>();
    private final Map<ImmutableSet<Dependency>, IsolatedClassLoader> loaders = new HashMap<>();
    private RelocationHandler relocator = null;

    private static DependencyManager instance;

    public static DependencyManager getInstance() {
        return instance;
    }

    public static DependencyManager init(ISuperBootstrap bootstrap) {
        if(instance != null) {
            throw new RuntimeException("DependencyManager cannot be redefined!");
        }

        instance = new DependencyManager(bootstrap);
        return getInstance();
    }

    private DependencyManager(ISuperBootstrap bootstrap) {
        this.bootstrap = bootstrap;
        try {
            this.digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        this.registry = new DependencyRegistry();
    }

    private synchronized RelocationHandler getRelocationHandler() {
        if (this.relocator == null) {
            this.relocator = new RelocationHandler(this);
        }
        return this.relocator;
    }

    public IsolatedClassLoader obtainClassLoaderWith(Dependency... dependencies) {
        ImmutableSet<Dependency> set = ImmutableSet.copyOf(dependencies);

        for (Dependency dependency : dependencies) {
            if (!this.loaded.containsKey(dependency)) {
                throw new IllegalStateException("Dependency " + dependency + " is not loaded.");
            }
        }

        synchronized (this.loaders) {
            IsolatedClassLoader classLoader = this.loaders.get(set);
            if (classLoader != null) {
                return classLoader;
            }

            URL[] urls = set.stream()
                    .map(this.loaded::get)
                    .map(file -> {
                        try {
                            return file.toURI().toURL();
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toArray(URL[]::new);

            classLoader = new IsolatedClassLoader(urls);
            this.loaders.put(set, classLoader);
            return classLoader;
        }
    }

    private File getSaveDirectory(File saveFolder, boolean createSub) {
        if(saveFolder == null) {
            saveFolder = bootstrap.getDataFolder();
            createSub = true;
        }

        if(createSub) {
            saveFolder = new File(saveFolder, "libs");
        }

        if(!saveFolder.exists()) {
            saveFolder.mkdirs();
        }
        return saveFolder;
    }

    public void loadStorageDependencies() {
        this.loadDependencies(registry.resolveStorageDependencies());
    }

    public void loadDependencies(Dependency... dependencies) {
        this.loadDependencies(null, null, null, true, dependencies);
    }

    public void loadDependencies(SuperLogger logger, PluginClassLoader classLoader, File folder, boolean createSub, Dependency... dependencies) {
        if(logger == null) {
            logger = bootstrap.getPluginLogger();
        }
        if(classLoader == null) {
            classLoader = bootstrap.getPluginClassLoader();
        }

        File saveFolder = getSaveDirectory(folder, createSub);

        // create a list of file sources
        List<Source> sources = new ArrayList<>();

        // obtain a file for each of the dependencies
        for (Dependency dependency : dependencies) {
            if (loaded.containsKey(dependency)) {
                continue;
            }

            try {
                File file = downloadDependency(logger, saveFolder, dependency);
                sources.add(new Source(dependency, file));
            } catch (Throwable e) {
                logger.severe("Exception whilst downloading dependency " + dependency.getId());
                e.printStackTrace();
            }
        }

        // apply any remapping rules to the files
        List<Source> remappedJars = new ArrayList<>(sources.size());
        for (Source source : sources) {
            try {
                // apply remap rules
                List<Relocation> relocations = new ArrayList<>(source.getDependency().getRelocations());
                relocations.addAll(registry.getLegacyRelocations(source.getDependency()));

                if (relocations.isEmpty()) {
                    remappedJars.add(source);
                    continue;
                }

                File input = source.getFile();
                File output = new File(input.getParent(), "remapped-" + input.getName());

                // if the remapped file exists already, just use that.
                if (output.exists()) {
                    remappedJars.add(new Source(source.getDependency(), output));
                    continue;
                }

                // init the relocation handler
                RelocationHandler relocationHandler = getRelocationHandler();

                // attempt to remap the jar.
                logger.info("Attempting to apply relocations to " + input.getName() + "...");
                relocationHandler.remap(input, output, relocations);

                remappedJars.add(new Source(source.getDependency(), output));
            } catch (Throwable e) {
                logger.severe("Unable to remap the source file '" + source.getDependency().getId() + "'.");
                e.printStackTrace();
            }
        }

        // load each of the jars
        for (Source source : remappedJars) {
            if (!source.getDependency().shoultAutoLoad()) {
                loaded.put(source.getDependency(), source.getFile());
                continue;
            }

            try {
                classLoader.loadJar(source.getFile());
                loaded.put(source.getDependency(), source.getFile());
            } catch (Throwable e) {
                logger.severe("Failed to load dependency jar '" + source.getFile().getName() + "'.");
                e.printStackTrace();
            }
        }
    }

    private File downloadDependency(SuperLogger logger, File saveFolder, Dependency dependency) throws Exception {
        String fileName = dependency.getId().toLowerCase() + "-" + dependency.getVersion() + ".jar";
        File file = new File(saveFolder, fileName);

        // if the file already exists, don't attempt to re-download it.
        if (file.exists()) {
            return file;
        }

        URL url = new URL(dependency.getUrl());
        try (InputStream in = url.openStream()) {

            // download the jar content
            byte[] bytes = ByteStreams.toByteArray(in);
            if (bytes.length == 0) {
                throw new RuntimeException("Empty stream");
            }


            // compute a hash for the downloaded file
            byte[] hash = this.digest.digest(bytes);

            // ensure the hash matches the expected checksum
            if (!Arrays.equals(hash, dependency.getChecksum())) {
                throw new RuntimeException("Downloaded file had an invalid hash. " +
                        "Expected: " + Base64.getEncoder().encodeToString(dependency.getChecksum()) + " " +
                        "Actual: " + Base64.getEncoder().encodeToString(hash));
            }

            logger.info("Successfully downloaded '" + fileName + "' with matching checksum: " + Base64.getEncoder().encodeToString(hash));

            // if the checksum matches, save the content to disk
            Files.write(file.toPath(), bytes);
        }

        // ensure the file saved correctly
        if (!file.exists()) {
            throw new IllegalStateException("File not present. - " + file.toString());
        }

        return file;
    }

}
