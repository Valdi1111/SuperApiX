package org.valdi.SuperApiX.common.dependencies;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.valdi.SuperApiX.common.plugin.ISuperBootstrap;
import org.valdi.SuperApiX.common.plugin.ISuperPlugin;
import org.valdi.SuperApiX.common.dependencies.classloader.IsolatedClassLoader;
import org.valdi.SuperApiX.common.dependencies.relocation.Relocation;
import org.valdi.SuperApiX.common.dependencies.relocation.RelocationHandler;
import org.valdi.SuperApiX.common.utils.MoreFiles;

/**
 * Responsible for loading runtime dependencies.
 */
public class DependencyManager {
    private final ISuperBootstrap plugin;
    private final MessageDigest digest;
    private final DependencyRegistry registry;
    private final Map<Dependency, Path> loaded = new HashMap<>();
    private final Map<ImmutableSet<Dependency>, IsolatedClassLoader> loaders = new HashMap<>();
    private RelocationHandler relocationHandler = null;

    public DependencyManager(ISuperPlugin plugin) {
        this(plugin.getBootstrap());
    }

    public DependencyManager(ISuperBootstrap plugin) {
        this.plugin = plugin;
        try {
            this.digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        this.registry = new DependencyRegistry();
    }

    private synchronized RelocationHandler getRelocationHandler() {
        if (this.relocationHandler == null) {
            this.relocationHandler = new RelocationHandler(this);
        }
        return this.relocationHandler;
    }

    private Path getSaveDirectory() {
        Path saveDirectory = this.plugin.getDataDirectory().resolve("lib");
        try {
            MoreFiles.createDirectoriesIfNotExists(saveDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create lib directory", e);
        }
        return saveDirectory;
    }

    public IsolatedClassLoader obtainClassLoaderWith(Set<Dependency> dependencies) {
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
                            return file.toUri().toURL();
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

    public void loadStorageDependencies() {
        Set<Dependency> dependencies = this.registry.resolveStorageDependencies();
        loadDependencies(dependencies.toArray(new Dependency[0]));
    }

    public void loadDependencies(Dependency... dependencies) {
        Path saveDirectory = getSaveDirectory();

        // create a list of file sources
        List<Source> sources = new ArrayList<>();

        // obtain a file for each of the dependencies
        for (Dependency dependency : dependencies) {
            if (this.loaded.containsKey(dependency)) {
                continue;
            }

            try {
                Path file = downloadDependency(saveDirectory, dependency);
                sources.add(new Source(dependency, file));
            } catch (Throwable e) {
                this.plugin.getPluginLogger().severe("Exception whilst downloading dependency " + dependency.getId());
                e.printStackTrace();
            }
        }

        // apply any remapping rules to the files
        List<Source> remappedJars = new ArrayList<>(sources.size());
        for (Source source : sources) {
            try {
                // apply remap rules
                List<Relocation> relocations = new ArrayList<>(source.getDependency().getRelocations());
                relocations.addAll(this.registry.getLegacyRelocations(source.getDependency()));

                if (relocations.isEmpty()) {
                    remappedJars.add(source);
                    continue;
                }

                Path input = source.getFile();
                Path output = input.getParent().resolve("remapped-" + input.getFileName().toString());

                // if the remapped file exists already, just use that.
                if (Files.exists(output)) {
                    remappedJars.add(new Source(source.getDependency(), output));
                    continue;
                }

                // init the relocation handler
                RelocationHandler relocationHandler = getRelocationHandler();

                // attempt to remap the jar.
                this.plugin.getPluginLogger().info("Attempting to apply relocations to " + input.getFileName().toString() + "...");
                relocationHandler.remap(input, output, relocations);

                remappedJars.add(new Source(source.getDependency(), output));
            } catch (Throwable e) {
                this.plugin.getPluginLogger().severe("Unable to remap the source file '" + source.getDependency().getId() + "'.");
                e.printStackTrace();
            }
        }

        // load each of the jars
        for (Source source : remappedJars) {
            if (!source.getDependency().shoultAutoLoad()) {
                this.loaded.put(source.getDependency(), source.getFile());
                continue;
            }

            try {
                this.plugin.getPluginClassLoader().loadJar(source.getFile());
                this.loaded.put(source.getDependency(), source.getFile());
            } catch (Throwable e) {
                this.plugin.getPluginLogger().severe("Failed to load dependency jar '" + source.getFile().getFileName().toString() + "'.");
                e.printStackTrace();
            }
        }
    }

    private Path downloadDependency(Path saveDirectory, Dependency dependency) throws Exception {
        String fileName = dependency.getId().toLowerCase() + "-" + dependency.getVersion() + ".jar";
        Path file = saveDirectory.resolve(fileName);

        // if the file already exists, don't attempt to re-download it.
        if (Files.exists(file)) {
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

            this.plugin.getPluginLogger().info("Successfully downloaded '" + fileName + "' with matching checksum: " + Base64.getEncoder().encodeToString(hash));

            // if the checksum matches, save the content to disk
            Files.write(file, bytes);
        }

        // ensure the file saved correctly
        if (!Files.exists(file)) {
            throw new IllegalStateException("File not present. - " + file.toString());
        } else {
            return file;
        }
    }

}
