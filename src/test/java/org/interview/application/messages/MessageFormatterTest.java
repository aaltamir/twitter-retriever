package org.interview.application.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class MessageFormatterTest {

    private MessageFormatter messageFormatter;

    @BeforeEach
    void setUp() {
        messageFormatter = new MessageFormatter(new ObjectMapper());
    }

    @Test
    void orderMessagesAndGenerateJsonTest() {

        final Author author1 = Author.builder()
                .creationDateTime(LocalDateTime.of(2019, 2, 3, 0, 0))
                .userId(2000L).build();

        final List<Message> messages  = new ArrayList<>(Arrays.asList(
                Message.builder()
                        .author(author1)
                        .creationDateTime(LocalDateTime.of(2019, 2, 3, 0, 0))
                        .messageId(4L)
                        .build(),
                Message.builder()
                        .author(author1)
                        .creationDateTime(LocalDateTime.of(2018, 2, 3, 0, 0))
                        .messageId(3L)
                        .build()));

        final Author author2 = Author.builder()
                .creationDateTime(LocalDateTime.of(2018, 2, 3, 0, 0))
                .userId(1000L).build();

        messages.addAll(Arrays.asList(
                Message.builder()
                        .author(author2)
                        .creationDateTime(LocalDateTime.of(2019, 2, 3, 0, 0))
                        .messageId(2L)
                        .build(),
                Message.builder()
                        .author(author2)
                        .creationDateTime(LocalDateTime.of(2018, 2, 3, 0, 0))
                        .messageId(1L)
                        .build()));

        final JsonNode result = messageFormatter.format(messages);

        assertTrue(result.isArray());
        assertEquals(2, result.size());
        assertEquals(1000L, result.get(0).get("userId").asLong());
        assertEquals(1L, result.get(0).get("messages").get(0).get("messageId").asLong());
        assertEquals(2L, result.get(0).get("messages").get(1).get("messageId").asLong());

        assertEquals(2000L, result.get(1).get("userId").asLong());
        assertEquals(3L, result.get(1).get("messages").get(0).get("messageId").asLong());
        assertEquals(4L, result.get(1).get("messages").get(1).get("messageId").asLong());
    }
}