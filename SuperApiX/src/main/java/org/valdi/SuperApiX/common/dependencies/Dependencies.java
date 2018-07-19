package org.valdi.SuperApiX.common.dependencies;

import java.util.HashSet;
import java.util.Set;

import org.valdi.SuperApiX.common.dependencies.relocation.Relocation;
import org.valdi.SuperApiX.common.dependencies.relocation.RelocationHelper;

public class Dependencies {
	private static final Set<Dependency> legacyDependencies = new HashSet<Dependency>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add(ASM);
			add(ASM_COMMONS);
			add(JAR_RELOCATOR);
			add(TEXT);
			add(CAFFEINE);
			add(OKIO);
			add(OKHTTP);
			add(MARIADB_DRIVER);
			add(MYSQL_DRIVER);
			add(POSTGRESQL_DRIVER);
			add(H2_DRIVER);
			add(SQLITE_DRIVER);
			add(HIKARI);
			add(SLF4J_SIMPLE);
			add(SLF4J_API);
			add(MONGODB_DRIVER);
			add(JEDIS);
			add(COMMONS_POOL_2);
			add(CONFIGURATE_CORE);
			add(CONFIGURATE_GSON);
			add(CONFIGURATE_YAML);
			add(CONFIGURATE_HOCON);
			add(HOCON_CONFIG);
			add(CONFIGURATE_TOML);
			add(TOML4J);
		}
	};

    public static final Dependency ASM = new Dependency(
    		"ASM",
            "org.ow2.asm",
            "asm",
            "6.2",
            "kXvaiIvFQxhzJdX7wQNCB+7RUldO943xc0ygruQLf8g=",
            false
    );
    public static final Dependency ASM_COMMONS = new Dependency(
    		"ASM_COMMONS",
            "org.ow2.asm",
            "asm-commons",
            "6.2",
            "FVRZE9sGyYeqQE8CjjNQHZ8n+M7WEvc3J+NUesTeh4w=",
            false
    );
    public static final Dependency JAR_RELOCATOR = new Dependency(
    		"JAR_RELOCATOR",
            "me.lucko",
            "jar-relocator",
            "1.3",
            "mmz3ltQbS8xXGA2scM0ZH6raISlt4nukjCiU2l9Jxfs=",
            false
    );
    public static final Dependency TEXT = new Dependency(
    		"TEXT",
            "net{}kyori",
            "text",
            "1.11-1.4.0",
            "drQpwf+oI1+DPrn0iCvEtoID+xXR3dpZK5ySaBrUiok=",
            true,
            Relocation.of("text", "net{}kyori{}text")
    );
    public static final Dependency CAFFEINE = new Dependency(
    		"CAFFEINE",
            "com{}github{}ben-manes{}caffeine",
            "caffeine",
            "2.6.2",
            "53pEV3NfB1FY29Ahx2YXl91IVpX8Ttkt/d401HFNl1A=",
            true,
            Relocation.of("caffeine", "com{}github{}benmanes{}caffeine")
    );
    public static final Dependency OKIO = new Dependency(
    		"OKIO",
            "com{}squareup{}" + RelocationHelper.OKIO_STRING,
            RelocationHelper.OKIO_STRING,
            "1.14.1",
            "InCF6E8zEsc1QxiVJF3nwKe29qUK30KayCVqFQoR7ck=",
            true,
            Relocation.of(RelocationHelper.OKIO_STRING, RelocationHelper.OKIO_STRING)
    );
    public static final Dependency OKHTTP = new Dependency(
    		"OKHTTP",
            "com{}squareup{}" + RelocationHelper.OKHTTP3_STRING,
            "okhttp",
            "3.10.0",
            "Sso+VSr7HOtH+JVmhfYpWiduSfoD+QZvi2voO+xW+2Y=",
            true,
            Relocation.allOf(
                    Relocation.of(RelocationHelper.OKHTTP3_STRING, RelocationHelper.OKHTTP3_STRING),
                    Relocation.of(RelocationHelper.OKIO_STRING, RelocationHelper.OKIO_STRING)
            )
    );
    public static final Dependency MARIADB_DRIVER = new Dependency(
    		"MARIADB_DRIVER",
            "org{}mariadb{}jdbc",
            "mariadb-java-client",
            "2.2.5",
            "kFfgzoMFrFKirAFh/DgobV7vAu9NhdnhZLHD4/PCddI=",
            true,
            Relocation.of("mariadb", "org{}mariadb{}jdbc")
    );
    public static final Dependency MYSQL_DRIVER = new Dependency(
    		"MYSQL_DRIVER",
            "mysql",
            "mysql-connector-java",
            "5.1.46",
            "MSIIl2HmQD8C6Kge1KLWWi4QKXNGUboA8uqS2SD/ex4=",
            true,
            Relocation.of("mysql", "com{}mysql")
    );
    public static final Dependency POSTGRESQL_DRIVER = new Dependency(
    		"POSTGRESQL_DRIVER",
            "org{}postgresql",
            "postgresql",
            "9.4.1212",
            "DLKhWL4xrPIY4KThjI89usaKO8NIBkaHc/xECUsMNl0=",
            true,
            Relocation.of("postgresql", "org{}postgresql")
    );
    public static final Dependency H2_DRIVER = new Dependency(
    		"H2_DRIVER",
            "com.h2database",
            "h2",
            "1.4.197",
            "N/UhbhSvJ3KTDf+bhzQ1PwqA6Juj8z4GVEHeZTfF6EI=",
            false
            // we don't apply relocations to h2 - it gets loaded via
            // an isolated classloader
    );
    public static final Dependency SQLITE_DRIVER = new Dependency(
    		"SQLITE_DRIVER",
            "org.xerial",
            "sqlite-jdbc",
            "3.21.0",
            "bglRaH4Y+vQFZV7TfOdsVLO3rJpauJ+IwjuRULAb45Y=",
            false
            // we don't apply relocations to sqlite - it gets loaded via
            // an isolated classloader
    );
    public static final Dependency HIKARI = new Dependency(
    		"HIKARI",
            "com{}zaxxer",
            "HikariCP",
            "3.2.0",
            "sAjeaLvYWBH0tujwhg0JZsastPLnX6vUbsIJRWnL7+s=",
            true,
            Relocation.of("hikari", "com{}zaxxer{}hikari")
    );
    public static final Dependency SLF4J_SIMPLE = new Dependency(
    		"SLF4J_SIMPLE",
            "org.slf4j",
            "slf4j-simple",
            "1.7.25",
            "CWbob/+lvlLT2ee4ndZ02YoD7tCkVPuvfBvZSTvZ2HQ=",
            true
    );
    public static final Dependency SLF4J_API = new Dependency(
    		"SLF4J_API",
            "org.slf4j",
            "slf4j-api",
            "1.7.25",
            "GMSgCV1cHaa4F1kudnuyPSndL1YK1033X/OWHb3iW3k=",
            true
    );
    public static final Dependency MONGODB_DRIVER = new Dependency(
    		"MONGODB_DRIVER",
            "org.mongodb",
            "mongo-java-driver",
            "3.7.1",
            "yllBCqAZwWCNUoMPR0JWilqhVA46+9F47wIcnYOcoy4=",
            true,
            Relocation.allOf(
                    Relocation.of("mongodb", "com{}mongodb"),
                    Relocation.of("bson", "org{}bson")
            )
    );
    public static final Dependency JEDIS = new Dependency(
    		"JEDIS",
            "redis.clients",
            "jedis",
            "2.9.0",
            "HqqWy45QVeTVF0Z/DzsrPLvGKn2dHotqI8YX7GDThvo=",
            true,
            Relocation.allOf(
                    Relocation.of("jedis", "redis{}clients{}jedis"),
                    Relocation.of("jedisutil", "redis{}clients{}util"),
                    Relocation.of("commonspool2", "org{}apache{}commons{}pool2")
            )
    );
    public static final Dependency COMMONS_POOL_2 = new Dependency(
    		"COMMONS_POOL_2",
            "org.apache.commons",
            "commons-pool2",
            "2.5.0",
            "IRhwQ8eZcdnISLlxhA3oQdoOXE3zDJI6lpFBMfue+Wk=",
            true,
            Relocation.of("commonspool2", "org{}apache{}commons{}pool2")
    );
    public static final Dependency CONFIGURATE_CORE = new Dependency(
    		"CONFIGURATE_CORE",
            "me{}lucko{}configurate",
            "configurate-core",
            "3.5",
            "J+1WnX1g5gr4ne8qA7DuBadLDOsZnOZjwHbdRmVgF6c=",
            true,
            Relocation.of("configurate", "ninja{}leaping{}configurate")
    );
    public static final Dependency CONFIGURATE_GSON = new Dependency(
    		"CONFIGURATE_GSON",
            "me{}lucko{}configurate",
            "configurate-gson",
            "3.5",
            "Q3wp3xpqy41bJW3yUhbHOzm+NUkT4bUUBI2/AQLaa3c=",
            true,
            Relocation.of("configurate", "ninja{}leaping{}configurate")
    );
    public static final Dependency CONFIGURATE_YAML = new Dependency(
    		"CONFIGURATE_YAML",
            "me{}lucko{}configurate",
            "configurate-yaml",
            "3.5",
            "Dxr1o3EPbpOOmwraqu+cors8O/nKwJnhS5EiPkTb3fc=",
            true,
            Relocation.of("configurate", "ninja{}leaping{}configurate")
    );
    public static final Dependency CONFIGURATE_HOCON = new Dependency(
    		"CONFIGURATE_HOCON",
            "me{}lucko{}configurate",
            "configurate-hocon",
            "3.5",
            "sOym1KPmQylGSfk90ZFqobuvoZfEWb7XMmMBwbHuxFw=",
            true,
            Relocation.allOf(
                    Relocation.of("configurate", "ninja{}leaping{}configurate"),
                    Relocation.of("hocon", "com{}typesafe{}config")
            )
    );
    public static final Dependency HOCON_CONFIG = new Dependency(
    		"HOCON_CONFIG",
            "com{}typesafe",
            "config",
            "1.3.3",
            "tfHWBx8VSNBb6C9Z+QOcfTeheHvY48Z34x7ida9KRiE=",
            true,
            Relocation.of("hocon", "com{}typesafe{}config")
    );
    public static final Dependency CONFIGURATE_TOML = new Dependency(
    		"CONFIGURATE_TOML",
            "me{}lucko{}configurate",
            "configurate-toml",
            "3.5",
            "U8p0XSTaNT/uebvLpO/vb6AhVGQDYiZsauSGB9zolPU=",
            true,
            Relocation.allOf(
                    Relocation.of("configurate", "ninja{}leaping{}configurate"),
                    Relocation.of("toml4j", "com{}moandjiezana{}toml")
            )
    );
    public static final Dependency TOML4J = new Dependency(
    		"TOML4J",
            "com{}moandjiezana{}toml",
            "toml4j",
            "0.7.2",
            "9UdeY+fonl22IiNImux6Vr0wNUN3IHehfCy1TBnKOiA=",
            true,
            Relocation.of("toml4j", "com{}moandjiezana{}toml")
    );
    
    public static Set<Dependency> values() {
    	return legacyDependencies;
    }

}
