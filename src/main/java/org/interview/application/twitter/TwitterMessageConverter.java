package org.interview.application.twitter;

import org.interview.application.messages.Author;
import org.interview.application.messages.Message;
import twitter4j.Status;

import java.time.ZoneId;

/**
 * Converts a status message from twitter to our internal data representation
 */
public class TwitterMessageConverter {
    /**
     * One status contains only one message with author data. This returns an author with
     * @param status The message as it's received from Twitter
     * @return The message containing the author information
     */
    Message convert(final Status status) {
        return Message.builder()
                .creationDateTime(status.getCreatedAt().toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime())
                .messageId(status.getId())
                .messageText(status.getText())
                .author(retrieveAuthor(status))
                .build();

    }

    private Author retrieveAuthor(final Status status) {
        return Author.builder()
                .creationDateTime(status.getUser().getCreatedAt().toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime())
                .userId(status.getUser().getId())
                .userName(status.getUser().getName())
                .userScreenName(status.getUser().getScreenName())
                .build();
    }

}
