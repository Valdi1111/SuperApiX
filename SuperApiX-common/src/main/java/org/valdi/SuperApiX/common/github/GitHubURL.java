package org.valdi.SuperApiX.common.github;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Somehow wraps {@link URL} and {@link java.net.URLConnection} to avoid boilerplate code when accessing to the GitHub API.
 * @author Poslovitch
 * @since 2.0-BETA
 */
public class GitHubURL {

    private final URL url;

    public GitHubURL(String repository, String suffix) throws MalformedURLException {
        suffix = (suffix != null && !suffix.isEmpty()) ? "/" + suffix : "";
        this.url = new URL("https://api.github.com/repos/" + repository + suffix);
    }

    public URL toURL() {
        return url;
    }

    public HttpURLConnection openConnection() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(2500);
        connection.addRequestProperty("User-Agent", "BentoBox GitHubLink (@BentoBoxWorld)");
        connection.setDoOutput(true);
        return connection;
    }

}
