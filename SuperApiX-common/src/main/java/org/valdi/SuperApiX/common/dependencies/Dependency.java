package org.valdi.SuperApiX.common.dependencies;

import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.util.*;

import org.valdi.SuperApiX.common.dependencies.relocation.Relocation;

import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteStreams;

public class Dependency {
    private final String id;
    private final String url;
    private final String version;
    private final byte[] checksum;
    private final boolean autoLoad;
    private final List<Relocation> relocations;

    private static final String MAVEN_CENTRAL_FORMAT = "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s.jar";

    private Dependency(String id, String url, String version, String checksum, boolean autoLoad, List<Relocation> relocations) {
        this.id = id;
        this.url = url;
        this.version = version;
        this.checksum = Base64.getDecoder().decode(checksum);
        this.autoLoad = autoLoad;
        this.relocations = ImmutableList.copyOf(relocations);
    }

    private static String rewriteEscaping(String s) {
        return s.replace("{}", ".");
    }
    
    public String getId() {
    	return this.id;
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

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public static class Builder {
        private String id;

        private String groupId;
        private String artifactId;
        private String version;

        private String url = "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s.jar";
        private String checksum = null;

        private boolean autoLoad = true;
        private List<Relocation> relocations = new ArrayList<>();

        public Builder(String id) {
            this.id = id;
        }

        public Builder setGroupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder setArtifactId(String artifactId) {
            this.artifactId = artifactId;
            return this;
        }

        public Builder setVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder setURL(String url) {
            this.url = url;
            return this;
        }

        public Builder setChecksum(String checksum) {
            this.checksum = checksum;
            return this;
        }

        public Builder setAutoLoad(boolean autoLoad) {
            this.autoLoad = autoLoad;
            return this;
        }

        public Builder setRelocations(List<Relocation> relocations) {
            this.relocations = relocations;
            return this;
        }

        public Dependency build() {
            return new Dependency(id, String.format(MAVEN_CENTRAL_FORMAT,
                    rewriteEscaping(groupId).replace(".", "/"),
                    rewriteEscaping(artifactId),
                    version,
                    rewriteEscaping(artifactId),
                    version),
                    version, checksum, autoLoad, relocations);
        }

    }

}
