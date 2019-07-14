package org.valdi.SuperApiX.bukkit.versions;

import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class MinecraftVersion {
    public static final MinecraftVersion UNKNOWN = new MinecraftVersion("UNKNOWN", null, Integer.MAX_VALUE);

    public static final MinecraftVersion v1_8_R1 = new MinecraftVersion("v1_8_R1", "1_8_R1", 181);
    public static final MinecraftVersion v1_8_R2 = new MinecraftVersion("v1_8_R2", "1_8_R2", 182);
    public static final MinecraftVersion v1_8_R3 = new MinecraftVersion("v1_8_R3", "1_8_R3", 183);

    public static final MinecraftVersion v1_9_R1 = new MinecraftVersion("v1_9_R1", "1_9_R1", 191);
    public static final MinecraftVersion v1_9_R2 = new MinecraftVersion("v1_9_R2", "1_9_R2", 192);

    public static final MinecraftVersion v1_10_R1 = new MinecraftVersion("v1_10_R1", "1_10_R1", 1101);

    public static final MinecraftVersion v1_11_R1 = new MinecraftVersion("v1_11_R1", "1_11_R1", 1111);

    public static final MinecraftVersion v1_12_R1 = new MinecraftVersion("v1_12_R1", "1_12_R1", 1121);

    public static final MinecraftVersion v1_13_R1 = new MinecraftVersion("v1_13_R1", "1_13_R1", 1131);
    public static final MinecraftVersion v1_13_R2 = new MinecraftVersion("v1_13_R2", "1_13_R2", 1132);

    public static final MinecraftVersion v1_14_R1 = new MinecraftVersion("v1_14_R1", "1_14_R1", 1141);

    public static Optional<MinecraftVersion> getById(String id) {
        return values().stream().filter(s -> s.getId().equals(id)).findFirst();
    }

    public static List<MinecraftVersion> values() {
        return Arrays.stream(MinecraftVersion.class.getFields())
                .filter(field -> !field.isAnnotationPresent(Deprecated.class)) // Ensures it is not deprecated
                .map(field -> {
                    try {
                        return (MinecraftVersion)field.get(null);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        Bukkit.getLogger().severe("Could not get MinecraftVersion values " + e.getMessage());
                    }
                    return null;
                }).collect(Collectors.toList());
    }

    private final String id;
    private final String version;
    private final int versionId;

    public MinecraftVersion(String id, String version, int versionId) {
        this.id = id;
        this.version = version;
        this.versionId = versionId;
    }

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public String getPackageName() {
        return id;
    }

    public int getVersionId() {
        return versionId;
    }

    @Override
    public String toString() {
        return "MinecraftVersion{" +
                "id='" + id + '\'' +
                ", version='" + version + '\'' +
                ", versionId=" + versionId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinecraftVersion)) return false;
        MinecraftVersion that = (MinecraftVersion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
