package edu.jiraiya.jiraconnector.auth;

import javax.annotation.Nonnull;

import java.util.Base64;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Basic auth info.
 *
 * @author azakomornyy
 * @since 10.06.2022
 */
public class BasicJiraAuthContent implements JiraAuthContent {
    private final String username;
    private final String password;

    public BasicJiraAuthContent(@Nonnull String username, @Nonnull String password) {
        this.username = requireNonNull(username, "username");
        this.password = requireNonNull(password, "password");
    }

    @Nonnull
    @Override
    public String getAuthHeader() {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicJiraAuthContent that = (BasicJiraAuthContent) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public String toString() {
        return "BasicJiraAuthContent{" +
                "username='" + username + '\'' +
                ", password='***'" +
                '}';
    }
}
