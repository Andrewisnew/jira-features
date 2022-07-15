package edu.jiraiya.eosstats.entities;

public class UserStatistics {
    private final User user;
    private final double storyPointsBurned;

    public UserStatistics(User user, double storyPointsBurned) {
        this.user = user;
        this.storyPointsBurned = storyPointsBurned;
    }

    public User getUser() {
        return user;
    }

    public double getStoryPointsBurned() {
        return storyPointsBurned;
    }
}
