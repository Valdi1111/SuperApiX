package org.valdi.SuperApiX.bukkit.versions;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;

/**
 * Provides a list of server versions.
 * Any version that is not listed here is implicitly considered as "INCOMPATIBLE".
 */
public class ServerVersion {
    public static final ServerVersion UNKNOWN = new ServerVersion("UNKNOWN", null);

    public static final ServerVersion v1_8 = new ServerVersion("v1_8", "1.8");
    public static final ServerVersion v1_8_1 = new ServerVersion("v1_8_1", "1.8.1");
    public static final ServerVersion v1_8_2 = new ServerVersion("v1_8_2", "1.8.2");
    public static final ServerVersion v1_8_3 = new ServerVersion("v1_8_3", "1.8.3");
    public static final ServerVersion v1_8_4 = new ServerVersion("v1_8_4", "1.8.4");
    public static final ServerVersion v1_8_5 = new ServerVersion("v1_8_5", "1.8.5");
    public static final ServerVersion v1_8_6 = new ServerVersion("v1_8_6", "1.8.6");
    public static final ServerVersion v1_8_7 = new ServerVersion("v1_8_7", "1.8.7");
    public static final ServerVersion v1_8_8 = new ServerVersion("v1_8_8", "1.8.8");
    public static final ServerVersion v1_8_9 = new ServerVersion("v1_8_9", "1.8.9");

    public static final ServerVersion v1_9 = new ServerVersion("v1_9", "1.9");
    public static final ServerVersion v1_9_1 = new ServerVersion("v1_9_1", "1.9.1");
    public static final ServerVersion v1_9_2 = new ServerVersion("v1_9_2", "1.9.2");
    public static final ServerVersion v1_9_3 = new ServerVersion("v1_9_3", "1.9.3");
    public static final ServerVersion v1_9_4 = new ServerVersion("v1_9_4", "1.9.4");

    public static final ServerVersion v1_10 = new ServerVersion("v1_10", "1.10");
    public static final ServerVersion v1_10_1 = new ServerVersion("v1_10_1", "1.10.1");
    public static final ServerVersion v1_10_2 = new ServerVersion("v1_10_2", "1.10.2");

    public static final ServerVersion v1_11 = new ServerVersion("v1_11", "1.11");
    public static final ServerVersion v1_11_1 = new ServerVersion("v1_11_1", "1.11.1");
    public static final ServerVersion v1_11_2 = new ServerVersion("v1_11_2", "1.11.2");

    public static final ServerVersion v1_12 = new ServerVersion("v1_12", "1.12");
    public static final ServerVersion v1_12_1 = new ServerVersion("v1_12_1", "1.12.1");
    public static final ServerVersion v1_12_2 = new ServerVersion("v1_12_2", "1.12.2");

    public static final ServerVersion v1_13 = new ServerVersion("v1_13", "1.13");
    public static final ServerVersion v1_13_1 = new ServerVersion("v1_13_1", "1.13.1");
    public static final ServerVersion v1_13_2 = new ServerVersion("v1_13_2", "1.13.2");

    public static final ServerVersion v1_14 = new ServerVersion("v1_14", "1.14");
    public static final ServerVersion v1_14_1 = new ServerVersion("v1_14_1", "1.14.1");
    public static final ServerVersion v1_14_2 = new ServerVersion("v1_14_2", "1.14.2");
    public static final ServerVersion v1_14_3 = new ServerVersion("v1_14_3", "1.14.3");
    public static final ServerVersion v1_14_4 = new ServerVersion("v1_14_4", "1.14.4");
	
	public static Optional<ServerVersion> getById(String id) {
		return values().stream().filter(s -> s.getId().equals(id)).findFirst();
	}
	
    public static List<ServerVersion> values() {
        return Arrays.stream(ServerVersion.class.getFields())
                .filter(field -> !field.isAnnotationPresent(Deprecated.class)) // Ensures it is not deprecated
                .map(field -> {
            try {
                return (ServerVersion)field.get(null);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                Bukkit.getLogger().severe("Could not get ServerVersion values " + e.getMessage());
            }
            return null;
        }).collect(Collectors.toList());
    }

    private final String id;
    private final String version;

    public ServerVersion(final String id, final String version) {
        this.id = id;
        this.version = version;
    }

	public String getId() {
		return id;
	}

	public String getVersion() {
		return version;
	}

    @Override
    public String toString() {
        return "ServerVersion{" +
                "id='" + id + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerVersion)) return false;
        ServerVersion that = (ServerVersion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
