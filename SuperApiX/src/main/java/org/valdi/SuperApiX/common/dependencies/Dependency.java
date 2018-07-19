package org.valdi.SuperApiX.common.dependencies;

import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import org.valdi.SuperApiX.common.dependencies.relocation.Relocation;

import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteStreams;

public class Dependency {
    private final String name;
    private final String url;
    private final String version;
    private final byte[] checksum;
    private final boolean autoLoad;
    private final List<Relocation> relocations;

    private static final String MAVEN_CENTRAL_FORMAT = "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s.jar";

    public Dependency(String name, String groupId, String artifactId, String version, String checksum, boolean autoLoad) {
        this(name, groupId, artifactId, version, checksum, autoLoad, ImmutableList.of());
    }

    public Dependency(String name, String groupId, String artifactId, String version, String checksum, boolean autoLoad, Relocation relocation) {
        this(name, groupId, artifactId, version, checksum, autoLoad, ImmutableList.of(relocation));
    }

    public Dependency(String name, String groupId, String artifactId, String version, String checksum, boolean autoLoad, List<Relocation> relocations) {
        this(name, String.format(MAVEN_CENTRAL_FORMAT,
                rewriteEscaping(groupId).replace(".", "/"),
                rewriteEscaping(artifactId),
                version,
                rewriteEscaping(artifactId),
                version),
                version, checksum, autoLoad, relocations
        );
    }

    public Dependency(String name, String url, String version, String checksum, boolean autoLoad) {
        this(name, url, version, checksum, autoLoad, Collections.emptyList());
    }

    public Dependency(String name, String url, String version, String checksum, boolean autoLoad, Relocation relocation) {
        this(name, url, version, checksum, autoLoad, Collections.singletonList(relocation));
    }

    public Dependency(String name, String url, String version, String checksum, boolean autoLoad, List<Relocation> relocations) {
        this.name = name;
        this.url = url;
        this.version = version;
        this.checksum = Base64.getDecoder().decode(checksum);
        this.autoLoad = autoLoad;
        this.relocations = ImmutableList.copyOf(relocations);
    }

    private static String rewriteEscaping(String s) {
        return s.replace("{}", ".");
    }

    public static void main(String[] args) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        for (Dependency dependency : Dependencies.values()) {
            URL url = new URL(dependency.getUrl());
            try (InputStream in = url.openStream()) {
                byte[] bytes = ByteStreams.toByteArray(in);
                if (bytes.length == 0) {
                    throw new RuntimeException("Empty stream");
                }

                byte[] hash = digest.digest(bytes);

                if (Arrays.equals(hash, dependency.getChecksum())) {
                    System.out.println("MATCH    " + dependency.getName() + ": " + Base64.getEncoder().encodeToString(hash));
                } else {
                    System.out.println("NO MATCH " + dependency.getName() + ": " + Base64.getEncoder().encodeToString(hash));
                }
            }
        }
    }
    
    public String getName() {
    	return this.name;
    }

    public String getUrl() {
        return this.url;
    }

    public String getVersion() {
        return this.version;
    }

    public byte[] getChecksum() {
        return this.checksum;
    }

    /**
     * If false -> All used within 'isolated' classloaders, and are therefore not relocated.
     * True otherwise
     * @return autoLoad
     */
    public boolean shoultAutoLoad() {
    	return autoLoad;
    }

    public List<Relocation> getRelocations() {
        return this.relocations;
    }

}
