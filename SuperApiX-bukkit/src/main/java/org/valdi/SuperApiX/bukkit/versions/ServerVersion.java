package org.valdi.SuperApiX.bukkit.versions;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;

/**
 * Provides a list of server versions.
 * Any version that is not listed here is implicitly considered as "INCOMPATIBLE".
 */
public class ServerVersion {
    public static final ServerVersion V1_7 = new ServerVersion("V1_7", "1.7", "1_7_R1");
    public static final ServerVersion V1_7_1 = new ServerVersion("V1_7_1", "1.7.1", "1_7_R1");
    public static final ServerVersion V1_7_2 = new ServerVersion("V1_7_2", "1.7.2", "1_7_R1");
    public static final ServerVersion V1_7_3 = new ServerVersion("V1_7_3", "1.7.3", "1_7_R2");
    public static final ServerVersion V1_7_4 = new ServerVersion("V1_7_4", "1.7.4", "1_7_R2");
    public static final ServerVersion V1_7_5 = new ServerVersion("V1_7_5", "1.7.5", "1_7_R2");
    public static final ServerVersion V1_7_6 = new ServerVersion("V1_7_6", "1.7.6", "1_7_R3");
    public static final ServerVersion V1_7_7 = new ServerVersion("V1_7_7", "1.7.7", "1_7_R3");
    public static final ServerVersion V1_7_8 = new ServerVersion("V1_7_8", "1.7.8", "1_7_R3");
    public static final ServerVersion V1_7_9 = new ServerVersion("V1_7_9", "1.7.9", "1_7_R4");
    public static final ServerVersion V1_7_10 = new ServerVersion("V1_7_10", "1.7.10", "1_7_R4");
	
    public static final ServerVersion V1_8 = new ServerVersion("V1_8", "1.8", "1_8_R1");
    public static final ServerVersion V1_8_1 = new ServerVersion("V1_8_1", "1.8.1", "1_8_R1");
    public static final ServerVersion V1_8_2 = new ServerVersion("V1_8_2", "1.8.2", "1_8_R1");
    public static final ServerVersion V1_8_3 = new ServerVersion("V1_8_3", "1.8.3", "1_8_R2");
    public static final ServerVersion V1_8_4 = new ServerVersion("V1_8_4", "1.8.4", "1_8_R3");
    public static final ServerVersion V1_8_5 = new ServerVersion("V1_8_5", "1.8.5", "1_8_R3");
    public static final ServerVersion V1_8_6 = new ServerVersion("V1_8_6", "1.8.6", "1_8_R3");
    public static final ServerVersion V1_8_7 = new ServerVersion("V1_8_7", "1.8.7", "1_8_R3");
    public static final ServerVersion V1_8_8 = new ServerVersion("V1_8_8", "1.8.8", "1_8_R3");
    public static final ServerVersion V1_8_9 = new ServerVersion("V1_8_9", "1.8.9", "1_8_R3");
	
    public static final ServerVersion V1_9 = new ServerVersion("V1_9", "1.9", "1_9_R1");
    public static final ServerVersion V1_9_1 = new ServerVersion("V1_9_1", "1.9.1", "1_9_R1");
    public static final ServerVersion V1_9_2 = new ServerVersion("V1_9_2", "1.9.2", "1_9_R1");
    public static final ServerVersion V1_9_3 = new ServerVersion("V1_9_3", "1.9.3", "1_9_R1");
    public static final ServerVersion V1_9_4 = new ServerVersion("V1_9_4", "1.9.4", "1_9_R2");
    
    public static final ServerVersion V1_10 = new ServerVersion("V1_10", "1.10", "1_10_R1");
    public static final ServerVersion V1_10_1 = new ServerVersion("V1_10_1", "1.10.1", "1_10_R1");
    public static final ServerVersion V1_10_2 = new ServerVersion("V1_10_2", "1.10.2", "1_10_R1");
    
    public static final ServerVersion V1_11 = new ServerVersion("V1_11", "1.11", "1_11_R1");
    public static final ServerVersion V1_11_1 = new ServerVersion("V1_11_1", "1.11.1", "1_11_R1");
    public static final ServerVersion V1_11_2 = new ServerVersion("V1_11_2", "1.11.2", "1_11_R1");
    
    public static final ServerVersion V1_12 = new ServerVersion("V1_12", "1.12", "1_12_R1");
    public static final ServerVersion V1_12_1 = new ServerVersion("V1_12_1", "1.12.1", "1_12_R1");
    public static final ServerVersion V1_12_2 = new ServerVersion("V1_12_2", "1.12.2", "1_12_R1");
    
    public static final ServerVersion V1_13 = new ServerVersion("V1_13", "1.13", "1_13_R1");
    public static final ServerVersion V1_13_1 = new ServerVersion("V1_13_1", "1.13.1", "1_13_R2");
    public static final ServerVersion V1_13_2 = new ServerVersion("V1_13_2", "1.13.2", "1_13_R2");

    public static final ServerVersion V1_14 = new ServerVersion("V1_14", "1.14", "1_14_R1");
    public static final ServerVersion V1_14_1 = new ServerVersion("V1_14_1", "1.14.1", "1_14_R1");
    public static final ServerVersion V1_14_2 = new ServerVersion("V1_14_2", "1.14.2", "1_14_R1");
    public static final ServerVersion V1_14_3 = new ServerVersion("V1_14_3", "1.14.3", "1_14_R1");
	
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
    private final String nms;

    public ServerVersion(final String id, final String version, String nms) {
        this.id = id;
        this.version = version;
        this.nms = nms;
    }

	public String getId() {
		return id;
	}

	public String getVersion() {
		return version;
	}

    public String getNms() {
        return nms;
    }

    @Override
	public String toString() {
		return version;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof ServerSoftware)) {
			return false;
		}
		
		if(this == o) {
			return true;
		}
		
		ServerSoftware software = (ServerSoftware) o;
		return software.getId().equals(this.getId());
	}
	
	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}

}
