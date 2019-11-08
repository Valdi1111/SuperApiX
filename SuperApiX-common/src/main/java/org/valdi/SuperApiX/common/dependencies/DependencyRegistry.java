package org.valdi.SuperApiX.common.dependencies;

import java.util.*;

import org.valdi.SuperApiX.common.config.types.ConfigType;
import org.valdi.SuperApiX.common.databases.StorageType;
import org.valdi.SuperApiX.common.dependencies.relocation.Relocation;
import org.valdi.SuperApiX.common.dependencies.relocation.RelocationHandler;

import com.google.gson.JsonElement;

public class DependencyRegistry {

    private static final Map<StorageType, List<Dependency>> STORAGE_DEPENDENCIES = new HashMap<StorageType, List<Dependency>>() {
        {
            this.put(StorageType.MONGODB, Arrays.asList(Dependencies.MONGODB_DRIVER));
            this.put(StorageType.MARIADB, Arrays.asList(Dependencies.MARIADB_DRIVER, Dependencies.SLF4J_API, Dependencies.SLF4J_SIMPLE, Dependencies.HIKARI));
            this.put(StorageType.MYSQL, Arrays.asList(Dependencies.MYSQL_DRIVER, Dependencies.SLF4J_API, Dependencies.SLF4J_SIMPLE, Dependencies.HIKARI));
            this.put(StorageType.POSTGRESQL, Arrays.asList(Dependencies.POSTGRESQL_DRIVER, Dependencies.SLF4J_API, Dependencies.SLF4J_SIMPLE, Dependencies.HIKARI));
            this.put(StorageType.SQLITE, Arrays.asList(Dependencies.SQLITE_DRIVER, Dependencies.SLF4J_API, Dependencies.SLF4J_SIMPLE, Dependencies.HIKARI));
            this.put(StorageType.H2, Arrays.asList(Dependencies.H2_DRIVER, Dependencies.SLF4J_API, Dependencies.SLF4J_SIMPLE, Dependencies.HIKARI));
            this.put(StorageType.CUSTOM, Collections.emptyList());
        }
    };

    private static final Map<ConfigType, List<Dependency>> CONFIG_DEPENDENCIES = new HashMap<ConfigType, List<Dependency>>() {
        {
            this.put(ConfigType.YAML, Arrays.asList(Dependencies.CONFIGURATE_CORE, Dependencies.CONFIGURATE_YAML));
            this.put(ConfigType.JSON, Arrays.asList(Dependencies.CONFIGURATE_CORE, Dependencies.CONFIGURATE_GSON));
            this.put(ConfigType.HOCON, Arrays.asList(Dependencies.HOCON_CONFIG, Dependencies.CONFIGURATE_CORE, Dependencies.CONFIGURATE_HOCON));
            this.put(ConfigType.TOML, Arrays.asList(Dependencies.TOML4J, Dependencies.CONFIGURATE_CORE, Dependencies.CONFIGURATE_TOML));
            this.put(ConfigType.CUSTOM, Collections.emptyList());
        }
    };

    public Dependency[] resolveStorageDependencies() {
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

        return dependencies.toArray(new Dependency[0]);
    }

    // support for legacy (bukkit 1.7.10)
    public List<Relocation> getLegacyRelocations(Dependency dependency) {
        if (Arrays.asList(RelocationHandler.DEPENDENCIES).contains(dependency)) {
            return Collections.emptyList();
        }

        if (JsonElement.class.getName().startsWith("org.valdi")) {
            return Arrays.asList(
                    Relocation.of("guava", "com{}google{}common"),
                    Relocation.of("gson", "com{}google{}gson")
            );
        }
        return Collections.emptyList();
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
