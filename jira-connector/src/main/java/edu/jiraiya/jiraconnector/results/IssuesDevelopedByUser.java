package edu.jiraiya.jiraconnector.results;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import java.util.List;

import static java.util.Objects.requireNonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IssuesDevelopedByUser {
    private final List<Issue> issues;

    @JsonCreator
    public IssuesDevelopedByUser(@JsonProperty("issues") @Nonnull List<Issue> issues) {
        this.issues = requireNonNull(issues, "issues");
    }

    @Nonnull
    public List<Issue> getIssues() {
        return issues;
    }

    @Override
    public String toString() {
        return "IssuesDevelopedByUser{" +
                "issues=" + issues +
                '}';
    }
}
