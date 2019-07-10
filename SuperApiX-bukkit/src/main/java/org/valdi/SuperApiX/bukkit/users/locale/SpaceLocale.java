package org.valdi.SuperApiX.bukkit.users.locale;

import org.valdi.SuperApiX.bukkit.Constants;
import org.valdi.SuperApiX.common.config.IFileStorage;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * @author Poslovitch, tastybento
 */
public class SpaceLocale {    
    private Locale locale;
    private IFileStorage localeFile;
    private List<String> authors;

    public SpaceLocale(Locale locale, IFileStorage file) {
        this.locale = locale;
        this.localeFile = file;

        // Load authors from the configuration
        this.authors = new LinkedList<>();
        this.updateAuthors(file);
    }

    /**
     * Get text from the yml file for this locale
     * @param reference - the YAML node where the text is
     * @return Text for this locale reference or the reference if nothing has been found
     */
    public String get(String reference) {
        return localeFile.getString(reference, reference); // return reference in case nothing has been found
    }

    /**
     * Returns the locale language
     * @return the locale language
     */
    public String getLanguage() {
        if(locale == null) {
            return Constants.UNKNOWN;
        }

        return locale.getDisplayLanguage();
    }

    /**
     * Returns the locale country
     * @return the locale country
     */
    public String getCountry() {
        if(locale == null) {
            return Constants.UNKNOWN;
        }

        return locale.getDisplayCountry();
    }

    /**
     * Returns the locale language tag (e.g: en-GB)
     * @return the locale language tag
     */
    public String toLanguageTag() {
        if(locale == null) {
            return Constants.UNKNOWN;
        }
        return locale.toLanguageTag();
    }

    public List<String> getAuthors() {
        return authors;
    }

    /**
     * Merges a language YAML file to this locale
     * @param file the IFileStorage of the language file
     */
    public void merge(IFileStorage file) {
    	localeFile.getRoot().mergeValuesFrom(file.getRoot());
        updateAuthors(file);
    }

    public boolean contains(String reference) {
        return localeFile.contains(reference);
    }

    private void updateAuthors(IFileStorage file) {
        List<String> list = file.getStringList("meta.authors");
        if (list == null || list.isEmpty()) {
        	return;
        }
        
        for (String author : list) {
            if (!authors.contains(author)) {
                authors.add(author);
            }
        }
    }

}
