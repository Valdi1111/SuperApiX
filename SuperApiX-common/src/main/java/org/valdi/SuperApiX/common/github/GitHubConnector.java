package org.valdi.SuperApiX.common.github;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Handles connection to the GitHub API, retrieves data and handles the {@link Repository} data that emerges from it.
 * @author Poslovitch
 * @since 2.0-BETA
 */
public class GitHubConnector {

    private String repositoryName;
    private Repository repository;

    public GitHubConnector(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public void connect() {
        JsonElement repositoryData;
        JsonElement releasesData;
        JsonElement contributorData;

        // Get the data
        try {
            repositoryData = getData(null);
            // TODO getting other data is pointless if we couldn't get the data from the repository
            contributorData = getData("contributors");
            releasesData = getData("releases");
        } catch (IOException e) {
            e.printStackTrace();
            // TODO better logging and do not override data instead
            return;
        }

        // Parse the data
        /* It must be done in a specific order:
            1. repository;
            2. contributors;
            3. releases.
         */
        parseRepositoryData(repositoryData);
        parseContributorsData(contributorData);
        parseReleasesData(releasesData);
    }

    private JsonElement getData(String suffix) throws IOException {
        HttpURLConnection connection = new GitHubURL(getRepositoryName(), suffix).openConnection();
        String data = new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().collect(Collectors.joining("\n"));
        return new JsonParser().parse(data);
    }

    private void parseRepositoryData(JsonElement jsonElement) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        Repository.Builder builder = new Repository.Builder(repositoryName.split("/")[0], repositoryName.split("/")[1])
                .stars(jsonObject.get("stargazers_count").getAsInt())
                .forks(jsonObject.get("forks_count").getAsInt())
                .openIssues(jsonObject.get("open_issues_count").getAsInt())
                .latestCommit(parseGitHubDate(jsonObject.get("pushed_at").getAsString()));

        this.repository = builder.build();
    }

    private void parseContributorsData(JsonElement jsonElement) {
        for (JsonElement contributorElement : jsonElement.getAsJsonArray()) {
            JsonObject contributor = contributorElement.getAsJsonObject();
            this.repository.getContributors().add(new Contributor(contributor.get("login").getAsString(), contributor.get("contributions").getAsInt()));
        }
    }

    private void parseReleasesData(JsonElement jsonElement) {
        for (JsonElement releaseElement : jsonElement.getAsJsonArray()) {
            JsonObject release = releaseElement.getAsJsonObject();

            String tag = release.get("tag_name").getAsString();
            String url = repository.getUrl() + "/releases/tag/" + tag;

            Release.Builder builder = new Release.Builder(release.get("name").getAsString(), tag, url)
                    .preRelease(release.get("prerelease").getAsBoolean())
                    .publishedAt(parseGitHubDate( release.get("published_at").getAsString()));

            // Run through the releases assets and try to find the correct one
            for (JsonElement assetElement : release.get("assets").getAsJsonArray()) {
                JsonObject asset = assetElement.getAsJsonObject();

                String assetName = asset.get("name").getAsString();
                if (assetName.endsWith(".jar") && !assetName.contains("javadoc") && !assetName.contains("sources")) {
                    // We found our asset!

                    builder.downloadUrl(asset.get("browser_download_url").getAsString())
                            .downloadSize(asset.get("size").getAsLong())
                            .downloadCount(asset.get("download_count").getAsInt());

                    break;
                }
            }

            this.repository.getReleases().add(builder.build());
        }
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public Repository getRepository() {
        return repository;
    }

    /**
     * Returns a Date instance corresponding to the input, or null if the input could not be parsed.
     * @param gitHubDate the input to parse
     * @return the Date instance following a {@code yyyy-MM-dd HH:mm:ss} format, or {@code null}.
     */
    public static Date parseGitHubDate(String gitHubDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(gitHubDate.replace('T', ' ').replace("Z", ""));
        } catch (ParseException e) {
            return null;
        }
    }

}
