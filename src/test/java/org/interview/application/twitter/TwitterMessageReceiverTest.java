package org.interview.application.twitter;

import org.interview.application.messages.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import twitter4j.TwitterStream;

import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TwitterMessageReceiverTest {

    @Mock
    private TwitterStatusListenerFactory statusListenerFactory;

    @Mock
    private TwitterStatusListener statusListener;

    @Mock
    private TwitterStreamFactoryWrapper twitterStreamFactory;

    @Mock
    private TwitterStream twitterStream;

    @Mock
    private Timer timer;

    @InjectMocks
    private TwitterMessageReceiver receiver;

    @Mock
    private Message message;

    @Captor
    private ArgumentCaptor<TimerTask> timerTaskCaptor;

    @BeforeEach
    void setUp() {
        when(statusListenerFactory.getInstance(anyInt())).thenReturn(statusListener);
        when(twitterStreamFactory.getInstance()).thenReturn(twitterStream);
        when(statusListener.getMessages()).thenReturn(Collections.singletonList(message));
    }

    @Test
    void testReceivingMessages() {
        final List<Message> messages = receiver.receiveMessages("trackText", 10, 20);

        verify(twitterStreamFactory).getInstance();
        verify(statusListenerFactory).getInstance(10);

        verify(twitterStream).addListener(statusListener);
        verify(twitterStream).filter("trackText");

        verify(timer).schedule(timerTaskCaptor.capture(), eq(20000L));

        verify(statusListener).getMessages();
        verify(statusListener, never()).complete();

        verify(twitterStream).cleanUp();
        verify(twitterStream).shutdown();

        verify(timer).cancel();

        assertIterableEquals(Collections.singletonList(message), messages);
    }

    @Test
    void testCompleteWhenTimerExpires() {
        receiver.receiveMessages("trackText", 10, 20);

        verify(timer).schedule(timerTaskCaptor.capture(), eq(20000L));

        assertNotNull(timerTaskCaptor.getValue());
        timerTaskCaptor.getValue().run();

        verify(statusListener).complete();
    }
}