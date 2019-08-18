package org.interview.application.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

/**
 * Just to make the application easier to test due the Twitter factory is final and cannot be mocked easily
 */
public class TwitterStreamFactoryWrapper {
    private final Twitter twitter;

    public TwitterStreamFactoryWrapper(Twitter twitter) {
        this.twitter = twitter;
    }

    TwitterStream getInstance() {
        return new TwitterStreamFactory().getInstance(twitter.getAuthorization());
    }
}
