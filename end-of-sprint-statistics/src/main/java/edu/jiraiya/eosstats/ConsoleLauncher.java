package edu.jiraiya.eosstats;

import edu.jiraiya.eosstats.csv.UsersParser;
import edu.jiraiya.eosstats.entities.User;
import edu.jiraiya.eosstats.entities.UserStatistics;
import edu.jiraiya.jiraconnector.HttpClientJiraConnector;
import edu.jiraiya.jiraconnector.auth.BasicJiraAuthContent;
import edu.jiraiya.jiraconnector.results.Issue;
import edu.jiraiya.jiraconnector.results.IssuesDevelopedByUser;

import javax.annotation.Nonnull;
import java.io.Console;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class ConsoleLauncher {
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Specify command parameters: <users_file_path> <sprint>");
        }


        String filePath = args[0];
        List<User> users = parse(filePath);

        Console console = System.console();
        if (console == null) {
            System.out.println("No console available");
            return;
        }
        String username = console.readLine("Enter username: ");
        String password = new String(console.readPassword("Enter password: "));
        List<UserStatistics> statistics = getStatistics(users, args[1], username, password);
        statistics.sort(Comparator.comparingDouble(UserStatistics::getStoryPointsBurned).reversed());
        Integer maxNameLength = statistics.stream()
                .map(UserStatistics::getUser)
                .map(User::getPrintableName)
                .map(String::length)
                .max(Integer::compareTo)
                .orElse(0);
        String leftAlignFormat = "%-" + (maxNameLength + 3) + "s%-5s%n";
        Double total = statistics.stream()
                .map(UserStatistics::getStoryPointsBurned)
                .reduce(0., Double::sum);
        System.out.println("Totally burned: " + String.format(Locale.US, "%.1f", total) + " sp\n");
        for (UserStatistics userStatistics : statistics) {
            System.out.format(leftAlignFormat, userStatistics.getUser().getPrintableName(), String.format(Locale.US, "%.1f",userStatistics.getStoryPointsBurned()) + " sp");
        }
    }

    private static List<UserStatistics> getStatistics(List<User> users, String sprint, String login, String password) {
        HttpClientJiraConnector httpClientJiraConnector = new HttpClientJiraConnector();
        List<CompletableFuture<UserStatistics>> futures = new ArrayList<>();
        for (User user : users) {
            CompletableFuture<UserStatistics> future =
                    httpClientJiraConnector.searchTicketsDevelopedByUser(sprint, user.getUsername(), new BasicJiraAuthContent(login, password))
                    .thenApply(issues -> getUserStatistics(user, issues));
            futures.add(future);
        }
        List<UserStatistics> userStatistics = new ArrayList<>();
        for (CompletableFuture<UserStatistics> future : futures) {
            userStatistics.add(future.join());
        }
        return userStatistics;
    }

    private static UserStatistics getUserStatistics(User user, IssuesDevelopedByUser issues) {
        Double spBurned = issues.getIssues().stream()
                .map(Issue::getStoryPoints)
                .reduce(0., Double::sum);
        return new UserStatistics(user, spBurned);
    }

    @Nonnull
    private static List<User> parse(@Nonnull String filePath) {
        try {
            UsersParser parser = new UsersParser();
            FileInputStream fileInputStream = new FileInputStream(filePath);
            UsersParser.FileParseResult result = parser.parse(fileInputStream);
            if (result.isFailed()) {
                throw new IllegalArgumentException("Parse error: " + result.getErrorText());
            }
            return result.getUsers();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
