package org.valdi.SuperApiX.common.github;

/**
 * Represents a Contributor on a GitHub repository.
 * @author Poslovitch
 * @since 2.0-BETA
 */
public class Contributor {

    private final String name;
    private final String profile;
    private int commits;

    public Contributor(String name, int commits) {
        this.name = name;
        this.profile = "https://github.com/" + name;
        this.commits = commits;
    }

    public String getName() {
        return name;
    }

    public String getProfile() {
        return profile;
    }

    public int getCommits() {
        return commits;
    }

    public void setCommits(int commits) {
        this.commits = commits;
    }

}
