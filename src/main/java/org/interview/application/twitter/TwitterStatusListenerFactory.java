package org.interview.application.twitter;

import org.interview.application.messages.MessageProcessor;

public class TwitterStatusListenerFactory {
    TwitterStatusListener getInstance(final int maxNumberOfMessages) {
        return new TwitterStatusListener(new MessageProcessor(), new TwitterMessageConverter(), maxNumberOfMessages);
    }
}
