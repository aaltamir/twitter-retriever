package org.interview.application.twitter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.interview.application.messages.MessageProcessor;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

/**
 * Listens and convert a status message. This listener listens until the maximum number of configured messages
 * is received.
 */
@RequiredArgsConstructor
@Getter
public class TwitterStatusListener implements StatusListener {

    private final MessageProcessor processor;

    private final TwitterMessageConverter messageConverter;

    private final Integer maxNumberOfMessages;

    private Integer numberOfMessages = 0;

    @Override
    public void onStatus(final Status status) {
        if(numberOfMessages++ < maxNumberOfMessages) {
            processor.process(messageConverter.convert(status));
        } else {
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
        // Just complete the processing if there is an error
        processor.complete();

    }
}
