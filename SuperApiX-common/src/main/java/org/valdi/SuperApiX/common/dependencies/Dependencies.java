package org.valdi.SuperApiX.common.dependencies;

import org.valdi.SuperApiX.common.dependencies.relocation.RelocationHelper;

public class Dependencies {

	public static final Dependency ASM = Dependency
			.builder("ASM")
			.setGroupId("org.ow2.asm")
			.setArtifactId("asm")
			.setVersion("6.2")
			.setChecksum("kXvaiIvFQxhzJdX7wQNCB+7RUldO943xc0ygruQLf8g=")
			.setAutoLoad(false)
			.build();

	public static final Dependency ASM_COMMONS = Dependency
			.builder("ASM_COMMONS")
			.setGroupId("org.ow2.asm")
			.setArtifactId("asm-commons")
			.setVersion("6.2")
			.setChecksum("FVRZE9sGyYeqQE8CjjNQHZ8n+M7WEvc3J+NUesTeh4w=")
			.setAutoLoad(false)
			.build();

	public static final Dependency JAR_RELOCATOR = Dependency
			.builder("JAR_RELOCATOR")
			.setGroupId("me.lucko")
			.setArtifactId("jar-relocator")
			.setVersion("1.3")
			.setChecksum("mmz3ltQbS8xXGA2scM0ZH6raISlt4nukjCiU2l9Jxfs=")
			.setAutoLoad(false)
			.build();

	public static final Dependency TEXT = Dependency
			.builder("TEXT")
			.setGroupId("net{}kyori")
			.setArtifactId("text")
			.setVersion("1.11-1.4.0")
			.setChecksum("drQpwf+oI1+DPrn0iCvEtoID+xXR3dpZK5ySaBrUiok=")
			.setAutoLoad(true)
			.build();

	public static final Dependency CAFFEINE = Dependency
			.builder("CAFFEINE")
			.setGroupId("com{}github{}ben-manes{}caffeine")
			.setArtifactId("caffeine")
			.setVersion("2.6.2")
			.setChecksum("53pEV3NfB1FY29Ahx2YXl91IVpX8Ttkt/d401HFNl1A=")
			.setAutoLoad(true)
			.build();

	public static final Dependency OKIO = Dependency
			.builder("OKIO")
			.setGroupId("com{}squareup{}" + RelocationHelper.OKIO_STRING)
			.setArtifactId(RelocationHelper.OKIO_STRING)
			.setVersion("1.14.1")
			.setChecksum("InCF6E8zEsc1QxiVJF3nwKe29qUK30KayCVqFQoR7ck=")
			.setAutoLoad(true)
			.build();

	public static final Dependency OKHTTP = Dependency
			.builder("OKHTTP")
			.setGroupId("com{}squareup{}" + RelocationHelper.OKHTTP3_STRING)
			.setArtifactId("okhttp")
			.setVersion("3.10.0")
			.setChecksum("Sso+VSr7HOtH+JVmhfYpWiduSfoD+QZvi2voO+xW+2Y=")
			.setAutoLoad(true)
			.build();

	public static final Dependency JSOUP = Dependency
			.builder("JSOUP")
			.setGroupId("org.jsoup")
			.setArtifactId("jsoup")
			.setVersion("1.11.3")
			.setChecksum("Sso+VSr7HOtH+JVmhfYpWiduSfoD+QZvi2voO+xW+2Y=")
			.setAutoLoad(true)
			.build();

	public static final Dependency MARIADB_DRIVER = Dependency
			.builder("MARIADB_DRIVER")
			.setGroupId("org{}mariadb{}jdbc")
			.setArtifactId("mariadb-java-client")
			.setVersion("2.2.5")
			.setChecksum("kFfgzoMFrFKirAFh/DgobV7vAu9NhdnhZLHD4/PCddI=")
			.setAutoLoad(true)
			.build();

    public static final Dependency MYSQL_DRIVER = Dependency
			.builder("MYSQL_DRIVER")
			.setGroupId("mysql")
			.setArtifactId("mysql-connector-java")
			.setVersion("5.1.46")
			.setChecksum("MSIIl2HmQD8C6Kge1KLWWi4QKXNGUboA8uqS2SD/ex4=")
			.setAutoLoad(true)
			.build();

	public static final Dependency POSTGRESQL_DRIVER = Dependency
			.builder("POSTGRESQL_DRIVER")
			.setGroupId("org{}postgresql")
			.setArtifactId("postgresql")
			.setVersion("9.4.1212")
			.setChecksum("DLKhWL4xrPIY4KThjI89usaKO8NIBkaHc/xECUsMNl0=")
			.setAutoLoad(true)
			.build();

	public static final Dependency H2_DRIVER = Dependency
			.builder("H2_DRIVER")
			.setGroupId("com.h2database")
			.setArtifactId("h2")
			.setVersion("1.4.197")
			.setChecksum("N/UhbhSvJ3KTDf+bhzQ1PwqA6Juj8z4GVEHeZTfF6EI=")
			.setAutoLoad(true)
			.build();
            // we don't apply relocations to h2 - it gets loaded via
            // an isolated classloader

    public static final Dependency SQLITE_DRIVER = Dependency
			.builder("SQLITE_DRIVER")
			.setGroupId("org.xerial")
			.setArtifactId("sqlite-jdbc")
			.setVersion("3.27.2.1")
			.setChecksum("SxCoOLnNH9btS1lJZsfHsNFcLUxcmrqYzSjshgLraS0")
			.setAutoLoad(false)
			.build();
            // we don't apply relocations to sqlite - it gets loaded via
            // an isolated classloader

    public static final Dependency HIKARI = Dependency
			.builder("HIKARI")
			.setGroupId("com{}zaxxer")
			.setArtifactId("HikariCP")
			.setVersion("3.2.0")
			.setChecksum("sAjeaLvYWBH0tujwhg0JZsastPLnX6vUbsIJRWnL7+s=")
			.setAutoLoad(true)
			.build();

	public static final Dependency SLF4J_SIMPLE = Dependency
			.builder("SLF4J_SIMPLE")
			.setGroupId("org.slf4j")
			.setArtifactId("slf4j-simple")
			.setVersion("1.7.25")
			.setChecksum("CWbob/+lvlLT2ee4ndZ02YoD7tCkVPuvfBvZSTvZ2HQ=")
			.setAutoLoad(true)
			.build();

	public static final Dependency SLF4J_API = Dependency
			.builder("SLF4J_API")
			.setGroupId("org.slf4j")
			.setArtifactId("slf4j-api")
			.setVersion("1.7.25")
			.setChecksum("GMSgCV1cHaa4F1kudnuyPSndL1YK1033X/OWHb3iW3k=")
			.setAutoLoad(true)
			.build();

	public static final Dependency MONGODB_DRIVER = Dependency
			.builder("MONGODB_DRIVER")
			.setGroupId("org.mongodb")
			.setArtifactId("mongo-java-driver")
			.setVersion("3.7.1")
			.setChecksum("yllBCqAZwWCNUoMPR0JWilqhVA46+9F47wIcnYOcoy4=")
			.setAutoLoad(true)
			.build();

	public static final Dependency CONFIGURATE_CORE = Dependency
			.builder("CONFIGURATE_CORE")
			.setGroupId("me{}lucko{}configurate")
			.setArtifactId("configurate-core")
			.setVersion("3.5")
			.setChecksum("J+1WnX1g5gr4ne8qA7DuBadLDOsZnOZjwHbdRmVgF6c=")
			.setAutoLoad(true)
			.build();

	public static final Dependency CONFIGURATE_GSON = Dependency
			.builder("CONFIGURATE_GSON")
			.setGroupId("me{}lucko{}configurate")
			.setArtifactId("configurate-gson")
			.setVersion("3.5")
			.setChecksum("Q3wp3xpqy41bJW3yUhbHOzm+NUkT4bUUBI2/AQLaa3c=")
			.setAutoLoad(true)
			.build();

	public static final Dependency CONFIGURATE_YAML = Dependency
			.builder("CONFIGURATE_YAML")
			.setGroupId("me{}lucko{}configurate")
			.setArtifactId("configurate-yaml")
			.setVersion("3.5")
			.setChecksum("Dxr1o3EPbpOOmwraqu+cors8O/nKwJnhS5EiPkTb3fc=")
			.setAutoLoad(true)
			.build();

	public static final Dependency CONFIGURATE_HOCON = Dependency
			.builder("CONFIGURATE_HOCON")
			.setGroupId("me{}lucko{}configurate")
			.setArtifactId("configurate-hocon")
			.setVersion("3.5")
			.setChecksum("sOym1KPmQylGSfk90ZFqobuvoZfEWb7XMmMBwbHuxFw=")
			.setAutoLoad(true)
			.build();

	public static final Dependency HOCON_CONFIG = Dependency
			.builder("HOCON_CONFIG")
			.setGroupId("com{}typesafe")
			.setArtifactId("config")
			.setVersion("1.3.3")
			.setChecksum("tfHWBx8VSNBb6C9Z+QOcfTeheHvY48Z34x7ida9KRiE=")
			.setAutoLoad(true)
			.build();

	public static final Dependency CONFIGURATE_TOML = Dependency
			.builder("CONFIGURATE_TOML")
			.setGroupId("me{}lucko{}configurate")
			.setArtifactId("configurate-toml")
			.setVersion("3.5")
			.setChecksum("U8p0XSTaNT/uebvLpO/vb6AhVGQDYiZsauSGB9zolPU=")
			.setAutoLoad(true)
			.build();

	public static final Dependency TOML4J = Dependency
			.builder("TOML4J")
			.setGroupId("com{}moandjiezana{}toml")
			.setArtifactId("toml4j")
			.setVersion("0.7.2")
			.setChecksum("9UdeY+fonl22IiNImux6Vr0wNUN3IHehfCy1TBnKOiA=")
			.setAutoLoad(true)
			.build();

}
