package org.interview.application.twitter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.interview.application.messages.Message;
import org.interview.application.messages.MessageProcessor;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * Listens and convert a status message. This listener listens until the maximum number of configured messages
 * is received.
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
public class TwitterStatusListener implements StatusListener {

    private final MessageProcessor processor;

    private final TwitterMessageConverter messageConverter;

    private final Integer maxNumberOfMessages;

    private int numberOfMessages = 0;

    @Override
    public void onStatus(final Status status) {
        if(++numberOfMessages <= maxNumberOfMessages) {
            processor.process(messageConverter.convert(status));
        }

        if(numberOfMessages == maxNumberOfMessages) {
            processor.complete();
        }
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

    }

    @Override
    public void onTrackLimitationNotice(int i) {

    }

    @Override
    public void onScrubGeo(long l, long l1) {

    }

    @Override
    public void onStallWarning(StallWarning stallWarning) {

    }

    @Override
    public void onException(Exception e) {
        log.warn("Exception retrieving twitter messages", e);
        // Just complete the processing if there is an error
        processor.complete();
    }

    void complete() {
        processor.complete();
    }

    public List<Message> getMessages() {
        final Instant start = Instant.now();
        final List<Message> messages = processor.getMessages();
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toSeconds();
        log.info("Received messages per second: {}", timeElapsed != 0 ? (float) messages.size() / (float) timeElapsed : "nan" );
        return messages;
    }

}
