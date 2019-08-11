package org.valdi.SuperApiX.bukkit.managers;

import org.valdi.SuperApiX.bukkit.Constants;
import org.valdi.SuperApiX.bukkit.plugin.BukkitStoreLoader;
import org.valdi.SuperApiX.bukkit.users.User;
import org.valdi.SuperApiX.bukkit.users.locale.SpaceLocale;
import org.valdi.SuperApiX.bukkit.utils.FileLister;
import org.valdi.SuperApiX.common.plugin.StoreLoader;
import org.valdi.SuperApiX.common.config.IFileStorage;
import org.valdi.SuperApiX.common.config.IFilesProvider;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;

/**
 * @author tastybento, Poslovitch
 */
public class LocalesManager {
    private Map<Locale, SpaceLocale> languages = new HashMap<>();

    private BukkitStoreLoader plugin;
    private File pluginFolder;
    private String subFolder;

    public LocalesManager(BukkitStoreLoader plugin) {
        this(plugin, plugin.getDataFolder(), null);
    }

    public LocalesManager(BukkitStoreLoader plugin, File pluginFolder, String subFolder) {
        this.plugin = plugin;
        this.pluginFolder = pluginFolder;
        this.subFolder = subFolder;
    }

    public void init() {
        this.copyDefaultLocales(plugin);
        this.loadLocalesFromFile();
    }

    public void disable() {
        languages.clear();
    }

    /**
     * Reloads all the language files from the filesystem
     */
    public void reload() {
        languages.clear();
        this.init();
    }

    /**
     * Gets the translated String corresponding to the reference from the locale file for this user.
     * @param user the User
     * @param reference a reference that can be found in a locale file
     * @return the translated String from the User's locale or from the server's locale or from the en-US locale, or null.
     */
    public String get(User user, String reference) {
        // Make sure the user is not null
        if (user != null) {
        	SpaceLocale locale = languages.get(user.getLocale());
            if (locale != null && locale.contains(reference)) {
                return locale.get(reference);
            } else {
            	plugin.getLogger().debug("Cannot find reference in player's locale.");
            	plugin.getLogger().debug(" - path: " + reference);
            	plugin.getLogger().debug(" - locale: " + user.getLocale().toLanguageTag());
            }
        }
        // No translation could be gotten from the player's locale, trying more generic solutions
        return get(reference);
    }
    
    /**
     * Gets the translated String corresponding to the reference from the server's or the en-US locale file.
     * @param reference a reference that can be found in a locale file
     * @return the translated String from the server's locale or from the en-US locale, or null.
     */
    public String get(String reference) {
        // Get the translation from the default server's locale
    	Locale defaultLocale = Locale.forLanguageTag(plugin.getDefaultLanguage());
    	if(defaultLocale != null) {
	        if (languages.get(defaultLocale).contains(reference)) {
	            return languages.get(defaultLocale).get(reference);
	        }
	    	plugin.getLogger().debug("Cannot find reference in default locale.");
	    	plugin.getLogger().debug(" - path: " + reference);
	    	plugin.getLogger().debug(" - locale: " + plugin.getDefaultLanguage());
    	} else {
	    	plugin.getLogger().severe("ERROR: Default locale is null!!!");
	    	plugin.getLogger().severe(" - locale: " + plugin.getDefaultLanguage());
    	}
    	
        // Get the translation from the en-US locale
    	Locale enUsLocale = Locale.forLanguageTag("en-US");
    	if(enUsLocale != null) {
	        if (languages.get(enUsLocale).contains(reference)) {
	            return languages.get(enUsLocale).get(reference);
	        }
	    	plugin.getLogger().warning("Cannot find reference in en-US locale.");
	    	plugin.getLogger().warning(" - path: " + reference);
	    	plugin.getLogger().warning(" - locale: en-US");
    	} else {
	    	plugin.getLogger().severe("ERROR: en-US locale is null!!!");
	    	plugin.getLogger().severe(" - valid locales loaded: ");
	    	for(Map.Entry<Locale, SpaceLocale> entry : languages.entrySet()) {
	        	plugin.getLogger().severe("     " + entry.getKey() + " -> " + entry.getValue());
	    	}
    	}
        return null;
    }

    public File getLocalesFolder(boolean create) {
        File localesFolder = new File(pluginFolder, Constants.LOCALES_FOLDER);
        if(subFolder != null && !subFolder.isEmpty()) {
            localesFolder = new File(localesFolder, subFolder);
        }

        if(localesFolder.exists() && !localesFolder.isDirectory()) {
            throw new RuntimeException("Locales folder already exists, but it isn't a file!!");
        }

        // If there is no locale folder, create it
        if (!localesFolder.exists() && create) {
            localesFolder.mkdirs();
        }
        
        return localesFolder;
    }

    /**
     * Copies all the locale files from the plugin jar to the filesystem.
     * Only done if the locale folder does not already exist.
     */
    public void copyDefaultLocales(StoreLoader loader) {
        try {
            List<String> locales = FileLister.listJar(loader, Constants.LOCALES_FOLDER, Constants.LOCALES_EXTENSION);
            if(locales.isEmpty()) {
                return;
            }

            // Get the folder
            File localeFolder = this.getLocalesFolder(true);

            // Run through the files and store the locales
            // Fill with the locale files from the jar
            // If it does exist, then new files will NOT be written!
            for (String name : locales) {
                // Get the last part of the name
                int lastIndex = name.lastIndexOf('/');
                File outFile = new File(localeFolder, name.substring(Math.max(lastIndex, 0), name.length()));
                InputStream initialStream = plugin.getResource(name);
                if (!outFile.exists()) {
                    Files.copy(initialStream, outFile.toPath());
                }
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Could not copy locale files from jar " + e.getMessage());
        }
    }

    /**
     * Loads all the locales available in the locale folder given. Used for loading all locales from plugin
     */
    public void loadLocalesFromFile() {
        // Filter for files of length 9 and ending with .yml
        int extSize = Constants.LOCALES_EXTENSION.length();
        FilenameFilter ymlFilter = (dir, name) -> name.toLowerCase(Locale.ENGLISH).endsWith(Constants.LOCALES_EXTENSION) && name.length() == (5 + extSize);

        // Get the folder
        File localeFolder = this.getLocalesFolder(false);
        if(!localeFolder.exists()) {
            return;
        }

        // Run through the files and store the locales
        File[] files = localeFolder.listFiles(ymlFilter);
        if(files == null) {
            return;
        }

        for (File language : files) {
            String localeRaw = language.getName().substring(0, language.getName().length() - extSize);
            plugin.getLogger().debug("Loading file " + language.getName() + " to locale " + localeRaw + " from folder " + language.getParentFile());
            Locale locale = Locale.forLanguageTag(localeRaw);

            try {
                Optional<IFilesProvider> provider = plugin.getFilesProvider();
                if(!provider.isPresent()) {
                    return;
                }

                IFileStorage localeFile = provider.get().createYamlFile(plugin, language.getParentFile(), language.getName());
                localeFile.loadOnly();

                if (languages.containsKey(locale)) {
                    // Merge into current language
                    languages.get(locale).merge(localeFile);
                } else {
                    // New language
                    languages.put(locale, new SpaceLocale(locale, localeFile));
                }
            } catch (Exception e) {
                plugin.getLogger().severe("Could not load locale file!", e);
                plugin.getLogger().severe("The file has likely an invalid file format or has been made unreadable during the process.");
                plugin.getLogger().severe(" - file: " + language.getPath());
                plugin.getLogger().severe(" - locale: " + localeRaw);
                plugin.getLogger().severe(" - message: " + e.getMessage());
                plugin.getLogger().severe(" - cause: " + e.getCause());
            }
        }
    }

    /**
     * Gets a list of all the locales loaded
     * @param sort - if true, the locales will be sorted by language tag
     * @return list of locales
     */
    public List<Locale> getAvailableLocales(boolean sort) {
        if (sort) {
            List<Locale> locales = new LinkedList<>(languages.keySet());

            locales.sort((locale1, locale2) -> {
                if (locale1.toLanguageTag().equals(plugin.getDefaultLanguage())) return -2;
                else if (locale1.toLanguageTag().startsWith("en")) return -1;
                else if (locale1.toLanguageTag().equals(locale2.toLanguageTag())) return 0;
                else return 1;
            });

            return locales;
        } else {
            return new ArrayList<>(languages.keySet());
        }
    }

    /**
     * @return raw map of system locales to BentoBox locales
     */
    public Map<Locale, SpaceLocale> getLanguages() {
        return this.languages;
    }
    
}
