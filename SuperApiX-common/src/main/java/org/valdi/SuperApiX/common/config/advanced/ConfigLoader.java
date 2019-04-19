package org.valdi.SuperApiX.common.config.advanced;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

import org.valdi.SuperApiX.common.config.ConfigType;
import org.valdi.SuperApiX.common.config.IFileStorage;
import org.valdi.SuperApiX.common.config.IFilesProvider;
import org.valdi.SuperApiX.common.config.advanced.adapters.Adapter;

import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.ConfigurationNode;

public class ConfigLoader<T> {
	private final StoreLoader loader;
	private final Class<T> data;
	
	private IFileStorage storage;
	private T instance;

    /**
     * Flag to indicate if this is a config or a pure object database (difference is in comments and annotations)
     */
    protected boolean configFlag;

    /**
     * Constructor
     * @param plugin - plugin
     * @param type - class to store in the database
     * @param databaseConnector - the database credentials, in this case, just the YAML functions
     */
    public ConfigLoader(StoreLoader loader, Class<T> data) {
		this.loader = loader;
		this.data = data;
    }
    
    public void loadAnnotatedConfig() {
    	StoreTo to = data.getAnnotation(StoreTo.class);
    	File toFolder = loader.getDataFolder();
    	if(!to.path().isEmpty()) {
    		toFolder = new File(toFolder, to.path());
    	}
    	
    	loadDynamicalAnnotatedConfig(toFolder, to.filename());
    }
    
    public void loadDynamicalAnnotatedConfig(File path, String name) {    	
    	Optional<IFilesProvider> provider = loader.getFilesProvider();
    	if(provider.isPresent()) {
    		ConfigType type = ConfigType.YAML;
        	if(data.isAnnotationPresent(FileType.class)) {
            	FileType fileType = data.getAnnotation(FileType.class);
        		type = fileType.value();
        	}
        	
    		IFileStorage storage = provider.get().createFile(type, loader, path, name);
    		loadDynamicalAnnotatedConfig(storage);
    	}
    }
    
    public void loadDynamicalAnnotatedConfig(IFileStorage storage) {
    	StoredAt from = data.getAnnotation(StoredAt.class);
    	String path = from.filename();
    	if(!from.path().isEmpty()) {
    		path = from.path() + File.separator + path;
    	}

		storage.fromParent(path);
		loadConfig(storage);
    }
    
    private List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }

    /**
     * Creates a list of <T>s filled with values from the provided YamlConfiguration
     *
     * @param config - YAML config file
     *
     * @return <T> filled with values
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     */
    public void loadConfig(IFileStorage file) {
    	this.storage = file;
    	
        // Create a new instance of the data of type T (which can be any class)
        try {
			this.instance = data.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}

        // Run through all the fields in the object
        for (Field field : getAllFields(new ArrayList<>(), data)) {
        	try {
	            // Check if there is a ConfigEntry annotation on the field
	            ConfigEntry configEntry = field.getAnnotation(ConfigEntry.class);
	            if(configEntry == null) {
	            	continue;
	            }
	            
	            // Gets the getter and setters for this field using the JavaBeans system
	            PropertyDescriptor property = new PropertyDescriptor(field.getName(), data);
	            // Get the write method
	            Method method = property.getWriteMethod();
	
	            // Information about the field
	            String path = field.getName();
	            
	            /*
	             * Field annotation checks
	             */	
	            // If there is a config annotation then do something
	            if (configEntry != null && !configEntry.path().isEmpty()) {
	                path = configEntry.path();
	            }
	            // Some fields need custom handling to serialize or deserialize and the programmer will need to
	            // define them herself. She can add an annotation to do that.
	            Adapter adapter = field.getAnnotation(Adapter.class);
	            if (adapter != null) {
	                // A conversion adapter has been defined
	                // Get the original value to be stored
	                ConfigurationNode value = storage.getFixedNode(path);
	                // Invoke the deserialization on this value
	                method.invoke(instance, adapter.value().getDeclaredConstructor().newInstance().deserialize(value));
	                // We are done here. If a custom adapter was defined, the rest of this method does not need to be run
	                continue;
	            }
	            
	            /*
	             * What follows is general deserialization code
	             */
	            // Look in the YAML Config to see if this field exists (it should)
	//            if (storage.contains(path)) {
	                // Check for null values
                if (storage.get(path) == null) {                	
                    loader.getLogger().severe("Error in file: value not found for parameter!");
                    loader.getLogger().severe(" - file: " + storage.getFilePath().toString());
                    loader.getLogger().severe(" - path: " + path);
//                    method.invoke(instance, (Object) null);
                    continue;
                }
	
	                method.invoke(instance, storage.get(path, TypeToken.of(property.getReadMethod().getGenericReturnType())));
	//            } else {
	//                loader.getLogger().severe("Error in file: value not found for parameter!");
	//                loader.getLogger().severe(" - file: " + storage.getFilePath().toString());
	//                loader.getLogger().severe(" - path: " + path);
	//            }
        	} catch(Exception e) {
        		loader.getLogger().severe("Error on config entry loading... ", e);
        	}
        }
    }

    /**
     * Inserts T into the corresponding database-table
     *
     * @param instance that should be inserted into the database
     */
    public void saveConfig() {
    	if(storage.getRoot().hasMapChildren()) {
    		for(Object key : storage.getRoot().getChildrenMap().keySet()) {
        		storage.getRoot().removeChild(key);
    		}
    	}
    	
    	// Comments for the file
        Map<String, String> yamlComments = new HashMap<>();

        // See if there are any top-level comments
        // See if there are multiple comments
        ConfigComment.Line comments = instance.getClass().getAnnotation(ConfigComment.Line.class);
        if (comments != null) {
            for (ConfigComment comment : comments.value()) {
                setComment(comment, yamlComments, "");
            }
        }
        // Handle single line comments
        ConfigComment comment = instance.getClass().getAnnotation(ConfigComment.class);
        if (comment != null) {
            setComment(comment, yamlComments, "");
        }

        // Run through all the fields in the class that is being stored. EVERY field must have a get and set method
        for (Field field : getAllFields(new ArrayList<>(), data)) {
        	try {
	            // Check if there is an annotation on the field
	            ConfigEntry configEntry = field.getAnnotation(ConfigEntry.class);
	            if(configEntry == null) {
	            	continue;
	            }
	            // Get the property descriptor for this field
	            PropertyDescriptor property = new PropertyDescriptor(field.getName(), data);
	            // Get the read method
	            Method method = property.getReadMethod();
	            // Invoke the read method to get the value. We have no idea what type of value it is.
	            Object value = method.invoke(instance);
	
	            String path = field.getName();	
	
	            // If there is a config path annotation then do something
	            boolean experimental = false;
	            if (configEntry != null && !configEntry.path().isEmpty()) {
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
	            comments = field.getAnnotation(ConfigComment.Line.class);
	            if (comments != null) {
	                for (ConfigComment bodyComment : comments.value()) {
	                    setComment(bodyComment, yamlComments, parent);
	                }
	            }
	            // Handle single line comments
	            comment = field.getAnnotation(ConfigComment.class);
	            if (comment != null) {
	                setComment(comment, yamlComments, parent);
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
	                	adapterNotation.value().getDeclaredConstructor().newInstance().serialize(storage.getFixedNode(path), value);
	                } catch (InstantiationException | IllegalArgumentException | NoSuchMethodException | SecurityException e) {
	                    loader.getLogger().severe("Could not instantiate adapter " + adapterNotation.value().getName() + " " + e.getMessage());
	                }
	                // We are done here
	                continue;
	            }
	
	            // Depending on the value type, it'll need serializing differently
	            // Check if this field is the mandatory UniqueId field. This is used to identify this instantiation of the class
	            if (method.getName().equals("getUniqueId")) {
	                // If the object does not have a unique name assigned to it already, one is created at random
	                String id = (String)value;
	                if (value == null || id.isEmpty()) {
	                    id = data.getSimpleName().toLowerCase();
	                    // Set it in the class so that it will be used next time
	                    property.getWriteMethod().invoke(instance, id);
	                }
	            }
	            
	            storage.set(path, TypeToken.of(property.getWriteMethod().getGenericParameterTypes()[0]), value);
        	} catch(Exception e) {
        		loader.getLogger().severe("Error on config entry saving... ", e);
        	}
        }
        
        storage.save();
        if (yamlComments != null && !yamlComments.isEmpty()) {
            this.commentFile(storage.getFile(), yamlComments);
        }
    }

    /**
     * Adds comments to a YAML file
     * @param file - file
     * @param commentMap - map of comments to apply to file
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
                        for (int i = 0; i < nextLine.indexOf(e.getKey()); i++){
                            commentLine.append(' ');
                        }
                        commentLine.append(e.getValue());
                        nextLine = commentLine.toString();
                        break;
                    }
                }
                newFile.add(nextLine);
            }
            Files.write(commentedFile.toPath(), (Iterable<String>)newFile.stream()::iterator);
            copyFileUsingStream(commentedFile, file);
            Files.delete(commentedFile.toPath());
        } catch (IOException e) {
            loader.getLogger().severe("Could not comment config file " + file.getName() + " " + e.getMessage());
        }
    }

    /**
     * This method is necessary because Windows has problems with Files.copy and file locking.
     * @param source - file
     * @param dest - file
     * @throws IOException - exception
     */
    private void copyFileUsingStream(File source, File dest) throws IOException {
        try (InputStream is = new FileInputStream(source); OutputStream os = new FileOutputStream(dest)) {
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
    
    public T getConfig() {
    	return instance;
    }

}
