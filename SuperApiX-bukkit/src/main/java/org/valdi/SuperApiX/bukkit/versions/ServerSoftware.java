package org.valdi.SuperApiX.bukkit.versions;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;

/**
 * Provides a list of server software.
 * Any software that is not listed here is implicitly considered as "INCOMPATIBLE".
 */
public class ServerSoftware {
	public static final ServerSoftware UNKNOWN = new ServerSoftware("UNKNOWN");
	public static final ServerSoftware CRAFTBUKKIT = new ServerSoftware("CRAFTBUKKIT");
	public static final ServerSoftware BUKKIT = new ServerSoftware("BUKKIT");
	public static final ServerSoftware GLOWSTONE = new ServerSoftware("GLOWSTONE");
	public static final ServerSoftware SPIGOT = new ServerSoftware("SPIGOT");
	public static final ServerSoftware PAPER = new ServerSoftware("PAPER");
	public static final ServerSoftware TACOSPIGOT = new ServerSoftware("TACOSPIGOT");
	public static final ServerSoftware AKARIN = new ServerSoftware("AKARIN");
	
	public static Optional<ServerSoftware> getById(String id) {
		return values().stream().filter(s -> s.getId().equals(id)).findFirst();
	}
	
    public static List<ServerSoftware> values() {
        return Arrays.stream(ServerSoftware.class.getFields())
                .filter(field -> !field.isAnnotationPresent(Deprecated.class)) // Ensures it is not deprecated
                .map(field -> {
            try {
                return (ServerSoftware)field.get(null);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                Bukkit.getLogger().severe("Could not get ServerSoftware values " + e.getMessage());
            }
            return null;
        }).collect(Collectors.toList());
    }
	
	private final String id;
	
	public ServerSoftware(final String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return id;
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
