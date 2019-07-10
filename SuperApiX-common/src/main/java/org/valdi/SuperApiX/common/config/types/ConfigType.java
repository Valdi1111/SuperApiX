package org.valdi.SuperApiX.common.config.types;

import java.util.List;

import com.google.common.collect.ImmutableList;

public enum ConfigType {

    // Config file based
    YAML("YAML", "yaml", "yml"),
    JSON("JSON", "json", "gson", "flatfile"),
    HOCON("HOCON", "hocon"),
    TOML("TOML", "toml"),

    // Custom
    CUSTOM("Custom", "custom");

    private final String name;
    private final List<String> identifiers;

    private ConfigType(String name, String... identifiers) {
        this.name = name;
        this.identifiers = ImmutableList.copyOf(identifiers);
    }

    /**
     * Get a ConfigType from his identifier
     * @param name the identifier
     * @return the corresponding ConfigType
     */
    public static ConfigType parse(String name) {
        for (ConfigType type : values()) {
            for (String id : type.getIdentifiers()) {
                if (id.equalsIgnoreCase(name)) {
                    return type;
                }
            }
        }
        return null;
    }

    /**
     * Get the name/id for this ConfigType
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the identifiers for this ConfigType
     * @return identifiers
     */
    public List<String> getIdentifiers() {
        return this.identifiers;
    }

}
