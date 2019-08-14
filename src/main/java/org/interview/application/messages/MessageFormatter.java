package org.interview.application.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Uses a list of messages and group by user and messages ordering always by created date time asc
 * The result is a Json entity
 */
@RequiredArgsConstructor
public class MessageFormatter {

    private final ObjectMapper mapper;

    public JsonNode format(final List<Message> messages) {
        final Map<Author, Set<Message>> ordered =
                messages.stream().collect(Collectors.groupingBy(Message::getAuthor,
                        () -> new TreeMap<>(Comparator.comparing(Author::getCreationDateTime)),
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Message::getCreationDateTime)))));

        final ArrayNode jsonResult = mapper.createArrayNode();

        ordered.forEach((author, mess) -> {
            final ObjectNode jsonAuthor = mapper.convertValue(author, ObjectNode.class);
            // New property to include the messages
            jsonAuthor.set("messages", mapper.convertValue(mess, ArrayNode.class));
            jsonResult.add(jsonAuthor);
        });

        return jsonResult;
    }
}


