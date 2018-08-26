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

    public static StorageType parse(String name) {
        for (StorageType t : values()) {
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
