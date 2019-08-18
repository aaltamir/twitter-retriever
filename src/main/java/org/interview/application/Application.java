package org.interview.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.interview.application.messages.Message;
import org.interview.application.messages.MessageFormatter;
import org.interview.application.twitter.TwitterMessageReceiver;
import org.interview.application.twitter.TwitterStatusListenerFactory;
import org.interview.application.twitter.TwitterStreamFactoryWrapper;
import org.interview.application.twitter.auth.TwitterAuthenticator;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

import java.util.List;
import java.util.Timer;

public class Application {
    public static void main(String args[]) throws JsonProcessingException {

        final Twitter twitter = TwitterFactory.getSingleton();

        final TwitterAuthenticator authenticator = new TwitterAuthenticator(System.out, System.in);
        authenticator.authenticate(twitter);

        // Create the receiver
        final TwitterStatusListenerFactory listenerFactory = new TwitterStatusListenerFactory();
        final TwitterMessageReceiver receiver = new TwitterMessageReceiver(listenerFactory,
                new TwitterStreamFactoryWrapper(twitter), new Timer());

        final List<Message> messages = receiver.receiveMessages("bieber", 100, 30);

        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        final MessageFormatter formatter = new MessageFormatter(mapper);

        final JsonNode formattedJson = formatter.format(messages);

        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(formattedJson));

        System.exit(0);

    }
}
