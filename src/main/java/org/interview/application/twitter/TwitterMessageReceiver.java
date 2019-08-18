package org.interview.application.twitter;

import lombok.RequiredArgsConstructor;
import org.interview.application.messages.Message;
import twitter4j.TwitterStream;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class in charge of receiving messages from Twitter.
 */
@RequiredArgsConstructor
public class TwitterMessageReceiver {

    private final TwitterStatusListenerFactory statusListenerFactory;

    private final TwitterStreamFactoryWrapper twitterStreamFactory;

    private final Timer timer;

    /**
     * Receives messages specifying one track string
     * Blocks until the collection periods ends.
     * @return A list of messages
     */
    public List<Message> receiveMessages(final String track, int maxNumberOfMessages, int maxSeconds) {
        final TwitterStream twitterStream = twitterStreamFactory.getInstance();
        final TwitterStatusListener statusListener = statusListenerFactory.getInstance(maxNumberOfMessages);

        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                statusListener.complete();
            }
        };

        try {
            twitterStream.addListener(statusListener);
            twitterStream.filter(track);
            // Setup timer
            timer.schedule(timerTask, maxSeconds * 1000);
            return statusListener.getMessages();
        } finally {
            twitterStream.cleanUp();
            twitterStream.shutdown();
            timer.cancel();
        }

    }
}
