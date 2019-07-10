package org.valdi.SuperApiX.common.config.advanced;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.valdi.SuperApiX.common.config.types.nodes.IConfigNode;
import org.valdi.SuperApiX.common.plugin.StoreLoader;
import org.valdi.SuperApiX.common.config.types.ConfigType;
import org.valdi.SuperApiX.common.config.IFileStorage;
import org.valdi.SuperApiX.common.config.IFilesProvider;
import org.valdi.SuperApiX.common.config.advanced.adapters.Adapter;

import com.google.common.reflect.TypeToken;
import org.valdi.SuperApiX.common.utils.ReflectionUtils;

public class ConfigLoader<Data> {
    protected final StoreLoader loader;
    protected final Class<Data> data;

    protected IFileStorage storage;
    protected List<Field> dataFields;
    protected Data instance;

    protected AtomicBoolean saving;

    /**
     * Constructor
     * @param loader a loader instance (plugin)
     * @param data class to store
     */
    public ConfigLoader(StoreLoader loader, Class<Data> data) {
        this.loader = loader;
        this.data = data;

        this.dataFields = ReflectionUtils.getAllFields(data);
        this.saving = new AtomicBoolean(false);
    }

    /**
     * Load values from file. The config class must be annotated
     * with {@code FileType} class to change the file type (default is YAML)and
     * with {@code StoreTo} class to create physical file and
     * with {@code StoredAt} class to copy it from jar if doesn't exists.
     * @deprecated use {@code #loadAnnotated()} instead
     */
    @Deprecated
    public void loadAnnotatedConfig() {
        this.loadAnnotated();
    }

    /**
     * Load values from file. The config class must be annotated
     * with {@code FileType} class to change the file type (default is YAML)and
     * with {@code StoreTo} class to create physical file and
     * with {@code StoredAt} class to copy it from jar if doesn't exists.
     */
    public void loadAnnotated() {
        StoreTo to = data.getAnnotation(StoreTo.class);
        File toFolder = loader.getDataFolder();
        if (!to.path().isEmpty()) {
            toFolder = new File(toFolder, to.path());
        }

        loadDynamicalAnnotated(toFolder, to.filename());
    }

    /**
     * Load values from file. The config class must be annotated
     * with {@code StoredAt} class to copy it from jar if doesn't exists.
     * @param folder the physical file folder
     * @param name   the physical file name (including file extension)
     * @deprecated use {@code #loadDinamicalAnnotated(File, String)} instead
     */
    @Deprecated
    public void loadDynamicalAnnotatedConfig(File folder, String name) {
        this.loadDynamicalAnnotated(folder, name);
    }

    /**
     * Load values from file. The config class must be annotated
     * with {@code StoredAt} class to copy it from jar if doesn't exists.
     * @param folder the physical file folder
     * @param name   the physical file name (including file extension)
     */
    public void loadDynamicalAnnotated(File folder, String name) {
        Optional<IFilesProvider> provider = loader.getFilesProvider();
        if (provider.isPresent()) {
            ConfigType type = ConfigType.YAML;
            if (data.isAnnotationPresent(FileType.class)) {
                FileType fileType = data.getAnnotation(FileType.class);
                type = fileType.value();
            }

            IFileStorage storage = provider.get().createFile(type, loader, folder, name);
            loadDynamicalAnnotated(storage);
        }
    }

    /**
     * Load values from file. The config class must be annotated
     * with {@code StoredAt} class to copy it from jar if doesn't exists.
     * @param storage the physical file's IFileStorage
     * @deprecated use {@code #loadDinamicalAnnotated(IFileStorage)} instead
     */
    @Deprecated
    public void loadDynamicalAnnotatedConfig(IFileStorage storage) {
        this.loadDynamicalAnnotated(storage);
    }

    /**
     * Load values from file. The config class must be annotated
     * with {@code StoredAt} class to copy it from jar if doesn't exists.
     * @param storage the physical file's IFileStorage
     */
    public void loadDynamicalAnnotated(IFileStorage storage) {
        StoredAt from = data.getAnnotation(StoredAt.class);
        String path = from.filename();
        if (!from.path().isEmpty()) {
            path = from.path() + File.separator + path;
        }

        storage.fromParent(path);
        load(storage);
    }

    /**
     * Load class fields from file.
     * @param file the config file
     * @deprecated use {@code #load()} instead
     */
    @Deprecated
    public void loadConfig(IFileStorage file) {
        this.load(file);
    }

    /**
     * Load class fields from file.
     * @param file the config file
     */
    public void load(IFileStorage file) {
        this.storage = file;

        // Create a new instance of the data of type T (which can be any class)
        try {
            this.instance = data.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            loader.getLogger().severe("Failed to create a new instance for class " + data.getName(), e);
            return;
        }

        // Run through all the fields in the object
        for (Field field : dataFields) {
            try {
                // Check if there is a ConfigEntry annotation on the field
                ConfigEntry configEntry = field.getAnnotation(ConfigEntry.class);
                if (configEntry == null) {
                    continue; // Ignore unannotated fields
                }

                // Gets the getter and setters for this field using the JavaBeans system
                PropertyDescriptor property = new PropertyDescriptor(field.getName(), data);
                // Get the write method
                Method method = property.getWriteMethod();

                // Information about the field
                String path = field.getName();

                // If there is a config annotation then do something
                if (!configEntry.path().isEmpty()) {
                    path = configEntry.path();
                }

                // Some fields need custom handling to serialize or deserialize and the programmer will need to
                // define them herself. She can add an annotation to do that.
                Adapter adapter = field.getAnnotation(Adapter.class);
                if (adapter != null) {
                    // A conversion adapter has been defined
                    // Get the original value to be stored
                    IConfigNode value = storage.getRoot().getNode(path);
                    // Invoke the deserialization on this value
                    method.invoke(instance, adapter.value().getDeclaredConstructor().newInstance().deserialize(value));
                    // We are done here. If a custom adapter was defined, the rest of this method does not need to be run
                    continue;
                }

                // Look in the Config to see if this field exists (it should)
                if (!storage.contains(path)) {
                    loader.getLogger().debug("Error in file: value not found for parameter!");
                    loader.getLogger().debug(" - file: " + storage.getFilePath().toString());
                    loader.getLogger().debug(" - path: " + path);
                    //method.invoke(instance, (Object) null);
                    continue;
                }

                method.invoke(instance, storage.get(path, TypeToken.of(property.getReadMethod().getGenericReturnType())));
            } catch (Exception e) {
                loader.getLogger().severe("Error on config entry loading... ", e);
            }
        }
    }

    /**
     * Save changes to file.
     * @deprecated use {@code #save()} instead
     */
    @Deprecated
    public void saveConfig() {
        this.save();
    }

    /**
     * Save changes to file.
     */
    public void save() {
        if(saving.getAndSet(true)) {
            loader.getLogger().severe("Skipping config saving for " + storage.getFile().getName() + ", it's already saving!");
            return;
        }

        if (storage.isSection(null)) {
            storage.clear(null);
        }

        // Comments for the file
        Map<String, String> yamlComments = new HashMap<>();

        // See if there are any top-level comments
        ConfigComment[] comments = data.getDeclaredAnnotationsByType(ConfigComment.class);
        for (ConfigComment comment : comments) {
            setComment(comment, yamlComments, "");
        }

        // Run through all the fields in the class that is being stored. EVERY field must have a get and set method
        for (Field field : dataFields) {
            try {
                // Check if there is an annotation on the field
                ConfigEntry configEntry = field.getAnnotation(ConfigEntry.class);
                if (configEntry == null) {
                    continue; // Ignore unannotated fields
                }

                // Get the property descriptor for this field
                PropertyDescriptor property = new PropertyDescriptor(field.getName(), data);
                // Get the read method
                Method method = property.getReadMethod();
                // Invoke the read method to get the value. We have no idea what type of value it is.
                Object value = method.invoke(instance);

                // Information about the field
                String path = field.getName();

                // If there is a config path annotation then do something
                boolean experimental = false;
                if (!configEntry.path().isEmpty()) {
                    path = configEntry.path();
                    experimental = configEntry.experimental();

                    if (configEntry.hidden()) {
                        // If the annotation tells us to not print the config entry, then we won't.
                        continue;
                    }
                }

                // Get path for comments
                String parent = "";
                if (path.contains(".")) {
                    parent = path.substring(0, path.lastIndexOf('.')) + ".";
                }

                // See if there are multiple comments
                comments = field.getDeclaredAnnotationsByType(ConfigComment.class);
                for (ConfigComment bodyComment : comments) {
                    setComment(bodyComment, yamlComments, parent);
                }

                // If the configEntry is experimental, then tell it
                if (experimental) {
                    setComment("/!\\ This feature is experimental and might not work as expected or might not work at all.", yamlComments, parent);
                }

                // Adapter
                Adapter adapterNotation = field.getAnnotation(Adapter.class);
                if (adapterNotation != null) {
                    // A conversion adapter has been defined
                    try {
                        adapterNotation.value().getDeclaredConstructor().newInstance().serialize(storage.getNode(path), value);
                    } catch (InstantiationException | IllegalArgumentException | NoSuchMethodException | SecurityException e) {
                        loader.getLogger().severe("Could not instantiate adapter " + adapterNotation.value().getName(), e);
                    }
                    // We are done here
                    continue;
                }

                // Depending on the value type, it'll need serializing differently
                // Check if this field is the mandatory UniqueId field. This is used to identify this instantiation of the class
                if (method.getName().equals("getUniqueId")) {
                    // If the object does not have a unique name assigned to it already, one is created at random
                    String id = (String) value;
                    if (value == null || id.isEmpty()) {
                        id = data.getSimpleName().toLowerCase();
                        // Set it in the class so that it will be used next time
                        property.getWriteMethod().invoke(instance, id);
                    }
                }

                TypeToken token = TypeToken.of(property.getWriteMethod().getGenericParameterTypes()[0]);
                storage.set(path, token, value);
            } catch (Exception e) {
                loader.getLogger().severe("Error on config entry saving... ", e);
            }
        }

        storage.save();
        if (!yamlComments.isEmpty() && (storage.getType() == ConfigType.YAML || storage.getType() == ConfigType.TOML || storage.getType() == ConfigType.HOCON)) {
            this.commentFile(storage.getFile(), yamlComments);
        }

        saving.compareAndSet(true, false);
    }

    /**
     * Adds comments to the file.
     * @param file file
     * @param commentMap map of comments to apply to file
     */
    private void commentFile(File file, Map<String, String> commentMap) {
        // Run through the file and add in the comments
        File commentedFile = new File(file.getPath() + ".tmp");
        List<String> newFile = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                // See if there are any comments in this line
                for (Entry<String, String> e : commentMap.entrySet()) {
                    if (nextLine.contains(e.getKey())) {
                        // We want the comment to start at the same level as the entry
                        StringBuilder commentLine = new StringBuilder();
                        for (int i = 0; i < nextLine.indexOf(e.getKey()); i++) {
                            commentLine.append(' ');
                        }
                        commentLine.append(e.getValue());
                        nextLine = commentLine.toString();
                        break;
                    }
                }
                newFile.add(nextLine);
            }
            Files.write(commentedFile.toPath(), (Iterable<String>) newFile.stream()::iterator);
            copyFileUsingStream(commentedFile, file);
            Files.delete(commentedFile.toPath());
        } catch (IOException e) {
            loader.getLogger().severe("Could not comment config file " + file.getName(), e);
        }
    }

    /**
     * This method is necessary because Windows has problems with Files.copy and file locking.
     * @param source file
     * @param dest file
     * @throws IOException - exception
     */
    private void copyFileUsingStream(File source, File dest) throws IOException {
        try (InputStream is = new FileInputStream(source);
             OutputStream os = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
    }

    private void setComment(ConfigComment comment, Map<String, String> yamlComments, String parent) {
        setComment(comment.value(), yamlComments, parent);
    }

    private void setComment(String comment, Map<String, String> yamlComments, String parent) {
        String random = "comment-" + UUID.randomUUID().toString();
        // Store placeholder
        storage.set(parent + random, " ");
        // Create comment
        yamlComments.put(random, "# " + comment);
    }

    public StoreLoader getStoreLoader() {
        return loader;
    }

    /**
     * Get the file manager for this config.
     * @return the manager
     */
    public IFileStorage getFileStorage() {
        return storage;
    }

    public Class<Data> getClazz() {
        return data;
    }

    /**
     * Get an active instance of the config class.
     * @return the config
     */
    public Data getConfig() {
        return instance;
    }
}
