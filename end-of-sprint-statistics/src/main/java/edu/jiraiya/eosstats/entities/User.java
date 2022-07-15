package edu.jiraiya.eosstats.entities;

import java.util.Objects;

public class User {
    private final String username;
    private final String printableName;

    public User(String username, String printableName) {
        this.username = username;
        this.printableName = printableName;
    }

    public String getUsername() {
        return username;
    }

    public String getPrintableName() {
        return printableName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(printableName, user.printableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, printableName);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", fullName='" + printableName + '\'' +
                '}';
    }
}
