package org.interview.application.twitter;

import org.interview.application.messages.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import twitter4j.Status;
import twitter4j.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TwitterMessageConverterTest {

    private TwitterMessageConverter converter = new TwitterMessageConverter();

    @Mock
    private Status status;

    @Mock
    private User user;

    @BeforeEach
    void setUp() {
        when(status.getUser()).thenReturn(user);
    }

    @Test
    void convertTwitterStatusTest() {
        when(status.getCreatedAt()).thenReturn(Date.from(LocalDateTime.of(2019, 8, 17, 10, 13)
                .toInstant(ZoneOffset.UTC)));

        when(status.getId()).thenReturn(10L);
        when(status.getText()).thenReturn("Twitter Message");

        when(user.getCreatedAt()).thenReturn(Date.from(LocalDateTime.of(2018, 3, 17, 10, 13)
                .toInstant(ZoneOffset.UTC)));
        when(user.getId()).thenReturn(200L);
        when(user.getName()).thenReturn("userName");
        when(user.getScreenName()).thenReturn("screenName");

        final Message message = converter.convert(status);

        assertEquals(LocalDateTime.of(2019, 8, 17, 10, 13), message.getCreationDateTime());
        assertEquals(10L, message.getMessageId());
        assertEquals("Twitter Message", message.getMessageText());
        assertNotNull(message.getAuthor());
        assertEquals(LocalDateTime.of(2018, 3, 17, 10, 13), message.getAuthor().getCreationDateTime());
        assertEquals(200L, message.getAuthor().getUserId());
        assertEquals("userName", message.getAuthor().getUserName());
        assertEquals("screenName", message.getAuthor().getUserScreenName());
    }
}