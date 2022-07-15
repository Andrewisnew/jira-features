package edu.jiraiya.jiraconnector;

import edu.jiraiya.jiraconnector.auth.JiraAuthContent;
import edu.jiraiya.jiraconnector.results.IssuesDevelopedByUser;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

/**
 * Interface to communicate with jira.
 *
 * @author azakomornyy
 * @since 10.06.2022
 */
public interface JiraConnector {
    /**
     * @param sprint      sprint to get info for
     * @param username    name of user to get tickets for
     * @param authContent auth content of user from that request will be performed
     * @return search result
     */
    @Nonnull
    CompletableFuture<IssuesDevelopedByUser> searchTicketsDevelopedByUser(@Nonnull String sprint, @Nonnull String username, @Nonnull JiraAuthContent authContent);
}
