package org.valdi.SuperApiX.common.databases;

import java.util.List;

import com.google.common.collect.ImmutableList;

public enum StorageType {

    // Remote databases
    MYSQL("MySQL", "mysql"),
    MARIADB("MariaDB", "mariadb"),
    POSTGRESQL("PostgreSQL", "postgresql"),
    MONGODB("MongoDB", "mongodb"),

    // Local databases
    SQLITE("SQLite", "sqlite"),
    H2("H2", "h2"),

    // Custom
    CUSTOM("Custom", "custom");

    private final String name;
    private final List<String> identifiers;

    private StorageType(String name, String... identifiers) {
        this.name = name;
        this.identifiers = ImmutableList.copyOf(identifiers);
    }

    /**
     * Get a StorageType from his identifier
     * @param name the identifier
     * @return the corresponding StorageType
     */
    public static StorageType parse(String name) {
        for (StorageType type : values()) {
            for (String id : type.getIdentifiers()) {
                if (id.equalsIgnoreCase(name)) {
                    return type;
                }
            }
        }
        return null;
    }

    /**
     * Get the name/id for this StorageType
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the identifiers for this StorageType
     * @return identifiers
     */
    public List<String> getIdentifiers() {
        return this.identifiers;
    }
	
}
