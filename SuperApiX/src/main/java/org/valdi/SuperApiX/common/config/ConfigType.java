package org.valdi.SuperApiX.common.config;

import java.util.List;

import com.google.common.collect.ImmutableList;

public enum ConfigType {

    // Config file based
    YAML("YAML", "yaml", "yml"),
    JSON("JSON", "json", "flatfile"),
    HOCON("HOCON", "hocon"),
    TOML("TOML", "toml"),
    /*YAML_COMBINED("YAML Combined", "yaml-combined"),
    JSON_COMBINED("JSON Combined", "json-combined"),
    HOCON_COMBINED("HOCON Combined", "hocon-combined"),
    TOML_COMBINED("TOML Combined", "toml-combined"),*/

    // Custom
    CUSTOM("Custom", "custom");

    private final String name;
    private final List<String> identifiers;

    private ConfigType(String name, String... identifiers) {
        this.name = name;
        this.identifiers = ImmutableList.copyOf(identifiers);
    }

    public static ConfigType parse(String name) {
        for (ConfigType t : values()) {
            for (String id : t.getIdentifiers()) {
                if (id.equalsIgnoreCase(name)) {
                    return t;
                }
            }
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getIdentifiers() {
        return this.identifiers;
    }

}
