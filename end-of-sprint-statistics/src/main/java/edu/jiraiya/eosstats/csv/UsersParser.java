package edu.jiraiya.eosstats.csv;

import edu.jiraiya.eosstats.entities.User;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class UsersParser {
    public static final String USERNAME_HEADER = "Username";
    public static final String PRINTABLE_HEADER = "Name";

    private static final CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
            .setDelimiter(",")
            .setHeader()
            .setSkipHeaderRecord(false)
            .setNullString("")
            .build();

    public FileParseResult parse(@Nonnull FileInputStream fileInputStream) {
        List<User> users = new ArrayList<>();
        try (Reader reader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
            CSVParser csvParser = csvFormat.parse(reader);
            List<CSVRecord> records = csvParser.getRecords();
            List<Long> unparsed = new ArrayList<>();
            for (CSVRecord record : records) {
                try {
                    User user = parse(record);
                    users.add(user);
                } catch (Exception e) {
                    e.printStackTrace();
                    unparsed.add(record.getRecordNumber());
                }
            }
            if (!unparsed.isEmpty()) {
                return FileParseResult.error("Unparsed record numbers: " + records);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return FileParseResult.error(e.getMessage());
        }
        return FileParseResult.success(users);
    }

    @Nonnull
    private User parse(@Nonnull CSVRecord record) {
        String username = record.get(USERNAME_HEADER);
        String name = record.get(PRINTABLE_HEADER);
        return new User(username, name);
    }

    public static class FileParseResult {
        private final String errorText;
        private final List<User> users;

        private FileParseResult(@Nullable String errorText, @Nonnull List<User> users) {
            this.errorText = errorText;
            this.users = requireNonNull(users, "users");
        }

        @Nullable
        public String getErrorText() {
            return errorText;
        }

        @Nonnull
        public List<User> getUsers() {
            return users;
        }

        @Nonnull
        public static FileParseResult error(@Nonnull String errorText) {
            return new FileParseResult(requireNonNull(errorText, "errorText"), Collections.emptyList());
        }

        public static FileParseResult success(@Nonnull List<User> users) {
            return new FileParseResult(null, users);
        }

        public boolean isFailed() {
            return errorText != null;
        }
    }
}
