package org.valdi.SuperApiX.common.dependencies;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.valdi.SuperApiX.common.config.types.ConfigType;
import org.valdi.SuperApiX.common.databases.StorageType;
import org.valdi.SuperApiX.common.dependencies.relocation.Relocation;
import org.valdi.SuperApiX.common.dependencies.relocation.RelocationHandler;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;

public class DependencyRegistry {

    private static final Map<StorageType, List<Dependency>> STORAGE_DEPENDENCIES = ImmutableMap.<StorageType, List<Dependency>>builder()
            .put(StorageType.MONGODB, ImmutableList.of(Dependencies.MONGODB_DRIVER))
            .put(StorageType.MARIADB, ImmutableList.of(Dependencies.MARIADB_DRIVER, Dependencies.SLF4J_API, Dependencies.SLF4J_SIMPLE, Dependencies.HIKARI))
            .put(StorageType.MYSQL, ImmutableList.of(Dependencies.MYSQL_DRIVER, Dependencies.SLF4J_API, Dependencies.SLF4J_SIMPLE, Dependencies.HIKARI))
            .put(StorageType.POSTGRESQL, ImmutableList.of(Dependencies.POSTGRESQL_DRIVER, Dependencies.SLF4J_API, Dependencies.SLF4J_SIMPLE, Dependencies.HIKARI))
            .put(StorageType.SQLITE, ImmutableList.of(Dependencies.SQLITE_DRIVER))
            .put(StorageType.H2, ImmutableList.of(Dependencies.H2_DRIVER))
            .put(StorageType.CUSTOM, ImmutableList.of())
            .build();

    private static final Map<ConfigType, List<Dependency>> CONFIG_DEPENDENCIES = ImmutableMap.<ConfigType, List<Dependency>>builder()
            .put(ConfigType.YAML, ImmutableList.of(Dependencies.CONFIGURATE_CORE, Dependencies.CONFIGURATE_YAML))
            .put(ConfigType.JSON, ImmutableList.of(Dependencies.CONFIGURATE_CORE, Dependencies.CONFIGURATE_GSON))
            .put(ConfigType.HOCON, ImmutableList.of(Dependencies.HOCON_CONFIG, Dependencies.CONFIGURATE_CORE, Dependencies.CONFIGURATE_HOCON))
            .put(ConfigType.TOML, ImmutableList.of(Dependencies.TOML4J, Dependencies.CONFIGURATE_CORE, Dependencies.CONFIGURATE_TOML))
            .put(ConfigType.CUSTOM, ImmutableList.of())
            .build();

    public Set<Dependency> resolveStorageDependencies() {
        Set<Dependency> dependencies = new LinkedHashSet<>();
        for (StorageType storageType : StorageType.values()) {
            dependencies.addAll(STORAGE_DEPENDENCIES.get(storageType));
        }

        for (ConfigType configType : ConfigType.values()) {
            dependencies.addAll(CONFIG_DEPENDENCIES.get(configType));
        }

        // don't load slf4j if it's already present
        if (slf4jPresent()) {
            dependencies.remove(Dependencies.SLF4J_API);
            dependencies.remove(Dependencies.SLF4J_SIMPLE);
        }

        return dependencies;
    }

    // support for legacy (bukkit 1.7.10)
    public List<Relocation> getLegacyRelocations(Dependency dependency) {
        if (RelocationHandler.DEPENDENCIES.contains(dependency)) {
            return ImmutableList.of();
        }

        if (JsonElement.class.getName().startsWith("org.valdi")) {
            return ImmutableList.of(
                    Relocation.of("guava", "com{}google{}common"),
                    Relocation.of("gson", "com{}google{}gson")
            );
        }
        return ImmutableList.of();
    }

    private static boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static boolean slf4jPresent() {
        return classExists("org.slf4j.Logger") && classExists("org.slf4j.LoggerFactory");
    }

}
