package org.valdi.SuperApiX.common.github;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a GitHub repository.
 * @author Poslovitch
 * @since 2.0-BETA
 */
public class Repository {

    private final String owner;
    private final String name;

    private final List<Contributor> contributors;
    private final List<Release> releases;

    private int stars;
    private int forks;
    private int openIssues;

    private Date latestCommit;

    private Repository(Builder builder) {
        this.owner = builder.owner;
        this.name = builder.name;
        this.contributors = builder.contributors;
        this.releases = builder.releases;

        this.stars = builder.stars;
        this.forks = builder.forks;
        this.openIssues = builder.openIssues;
        this.latestCommit = builder.latestCommit;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public List<Contributor> getContributors() {
        return contributors;
    }

    public List<Release> getReleases() {
        return releases;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public int getOpenIssues() {
        return openIssues;
    }

    public void setOpenIssues(int openIssues) {
        this.openIssues = openIssues;
    }

    public String getUrl() {
        return "https://github.com/" + getOwner() + "/" + getName();
    }
    
    public Date getLatestCommit() {
        return latestCommit;
    }

    public void setLatestCommit(Date latestCommit) {
        this.latestCommit = latestCommit;
    }

    public static class Builder {
        private final String owner;
        private final String name;
        private final List<Contributor> contributors;
        private final List<Release> releases;

        private int stars;
        private int forks;
        private int openIssues;
        private Date latestCommit;

        public Builder(String owner, String name) {
            this.owner = owner;
            this.name = name;
            this.contributors = new LinkedList<>();
            this.releases = new LinkedList<>();
        }

        public Builder contributors(Contributor... contributors) {
            this.contributors.addAll(Arrays.asList(contributors));
            return this;
        }

        public Builder releases(Release... releases) {
            this.releases.addAll(Arrays.asList(releases));
            return this;
        }

        public Builder stars(int stars) {
            this.stars = stars;
            return this;
        }

        public Builder forks(int forks) {
            this.forks = forks;
            return this;
        }

        public Builder openIssues(int openIssues) {
            this.openIssues = openIssues;
            return this;
        }

        public Builder latestCommit(Date latestCommit) {
            this.latestCommit = latestCommit;
            return this;
        }

        public Repository build() {
            return new Repository(this);
        }
    }

}
