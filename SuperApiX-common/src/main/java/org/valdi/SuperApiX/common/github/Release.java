package org.valdi.SuperApiX.common.github;

import java.util.Date;

/**
 * Represents a release on a Github repository.
 * See https://api.github.com/repos/BentoBoxWorld/BentoBox/releases.
 * @author Poslovitch
 * @since 2.0-BETA
 */
public class Release {

    private final String name;
    private final String tag;
    private final String url;

    private final boolean preRelease;
    private final Date publishedAt;

    /* Release asset related fields */
    private final String downloadUrl;
    private final long downloadSize;
    private int downloadCount;

    private Release(Builder builder) {
        this.name = builder.name;
        this.tag = builder.tag;
        this.url = builder.url;
        this.preRelease = builder.preRelease;
        this.publishedAt = builder.publishedAt;
        this.downloadUrl = builder.downloadUrl;
        this.downloadSize = builder.downloadSize;
        this.downloadCount = builder.downloadCount;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public String getUrl() {
        return url;
    }

    public boolean isPreRelease() {
        return preRelease;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public long getDownloadSize() {
        return downloadSize;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public static class Builder {
        private final String name;
        private final String tag;
        private final String url;

        private boolean preRelease;
        private Date publishedAt;
        private String downloadUrl;
        private long downloadSize;
        private int downloadCount;

        public Builder(String name, String tag, String url) {
            this.name = name;
            this.tag = tag;
            this.url = url;
            this.preRelease = false;
            this.downloadSize = 0L;
            this.downloadCount = 0;
        }

        public Builder preRelease(boolean preRelease) {
            this.preRelease = preRelease;
            return this;
        }

        public Builder publishedAt(Date publishedAt) {
            this.publishedAt = publishedAt;
            return this;
        }

        public Builder downloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
            return this;
        }

        public Builder downloadSize(long downloadSize) {
            this.downloadSize = downloadSize;
            return this;
        }

        public Builder downloadCount(int downloadCount) {
            this.downloadCount = downloadCount;
            return this;
        }

        public Release build() {
            return new Release(this);
        }
    }

}
