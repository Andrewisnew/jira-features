package edu.jiraiya.jiraconnector.auth;

import javax.annotation.Nonnull;

/**
 * Interface for user info needed to auth to jira.
 *
 * @author azakomornyy
 * @since 10.06.2022
 */
public interface JiraAuthContent {
    @Nonnull
    String getAuthHeader();
}
