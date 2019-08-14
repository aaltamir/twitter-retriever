package org.interview.application.twitter;

import lombok.RequiredArgsConstructor;
import org.interview.application.messages.Message;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class in charge of receiving messages from Twitter.
 */
@RequiredArgsConstructor
public class TwitterMessageReceiver {

    private final TwitterStatusListener statusListener;

    private final TwitterStreamFactory twitterStreamFactory;

    private final Timer timer;

    private final Integer maxSeconds;

    /**
     * Receives messages specifying one track string
     * Blocks until the collection periods ends.
     * @return A list of messages
     */
    public List<Message> receiveMessages(final String track) {
        final TwitterStream twitterStream = twitterStreamFactory.getInstance();
        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                statusListener.getProcessor().complete();
            }
        };

        try {
            twitterStream.addListener(statusListener);
            twitterStream.filter(track);
            // Setup timer
            timer.schedule(timerTask, maxSeconds * 1000);
            return statusListener.getProcessor().getMessages();
        } finally {
            twitterStream.cleanUp();
            twitterStream.shutdown();
            timerTask.cancel();
        }

    }








}
