package edu.jiraiya.jiraconnector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.jiraiya.jiraconnector.auth.JiraAuthContent;
import edu.jiraiya.jiraconnector.results.IssuesDevelopedByUser;

import javax.annotation.Nonnull;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;


/**
 * Jira connector based on {@link HttpClient}.
 *
 * @author azakomornyy
 * @since 10.06.2022
 */
public class HttpClientJiraConnector implements JiraConnector {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private static final String SEARCH_TICKETS_DEVELOPED_BY_USER_ENDPOINT = "https://jira.in.devexperts.com/rest/api/2/search";
    private static final String SEARCH_TICKETS_DEVELOPED_BY_USER_ENDPOINT_JQL_TEMPLATE = "Sprint=\"%s\" and \"Last developer\"=%s and resolution is not EMPTY";

    public HttpClientJiraConnector() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofMillis(10_000))
                .build();
        objectMapper = new ObjectMapper();
    }

    @Nonnull
    @Override
    public CompletableFuture<IssuesDevelopedByUser> searchTicketsDevelopedByUser(@Nonnull String sprint, @Nonnull String username, @Nonnull JiraAuthContent authContent) {
        String jql = String.format(SEARCH_TICKETS_DEVELOPED_BY_USER_ENDPOINT_JQL_TEMPLATE, sprint, username);
        String encodedJql = URLEncoder.encode(jql, StandardCharsets.UTF_8);
        URI uri;
        try {
            uri = new URI(SEARCH_TICKETS_DEVELOPED_BY_USER_ENDPOINT + "?jql=" + encodedJql);
        } catch (URISyntaxException e) {
            return CompletableFuture.failedFuture(e);
        }
        HttpRequest request = createHttpRequest(uri)
                .header("Authorization", authContent.getAuthHeader())
                .GET()
                .build();
        return sendAsync(request)
                .thenApply(response -> {
                    try {
                        return objectMapper.readValue(response.body(), IssuesDevelopedByUser.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Nonnull
    private CompletableFuture<HttpResponse<String>> sendAsync(@Nonnull HttpRequest request) {
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    @Nonnull
    private static HttpRequest.Builder createHttpRequest(@Nonnull URI uri) {
        return HttpRequest.newBuilder(uri);
    }
}
