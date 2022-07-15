package edu.jiraiya.jiraconnector.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import edu.jiraiya.jiraconnector.results.Issue;

import java.io.IOException;

public class IssueDeserializer extends JsonDeserializer<Issue> {
    public static final String STORY_POINTS_FIELD = "customfield_10811";

    @Override
    public Issue deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.readValueAsTree();
        String ticket = node.get("key").asText();
        node = node.get("fields");
        JsonNode jsonNode = node.get(STORY_POINTS_FIELD);
        Double storyPoints = jsonNode == null ? null : jsonNode.asDouble();
        return new Issue(ticket, storyPoints);
    }
}
