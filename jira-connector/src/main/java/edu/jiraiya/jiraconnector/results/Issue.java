package edu.jiraiya.jiraconnector.results;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.jiraiya.jiraconnector.deserializers.IssueDeserializer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.util.Objects.requireNonNull;

@JsonDeserialize(using = IssueDeserializer.class)
public class Issue {
    private final String ticket;
    private final Double storyPoints;

    public Issue(@Nonnull String ticket, @Nullable Double storyPoints) {
        this.ticket = requireNonNull(ticket, "ticket");
        this.storyPoints = storyPoints;
    }

    public String getTicket() {
        return ticket;
    }

    public Double getStoryPoints() {
        return storyPoints;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "ticket='" + ticket + '\'' +
                ", storyPoints=" + storyPoints +
                '}';
    }
}
