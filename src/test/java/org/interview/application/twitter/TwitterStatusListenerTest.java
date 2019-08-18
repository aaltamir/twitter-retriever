package org.interview.application.twitter;

import org.interview.application.messages.Message;
import org.interview.application.messages.MessageProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import twitter4j.Status;

import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TwitterStatusListenerTest {

    @Mock
    private MessageProcessor processor;

    @Mock
    private TwitterMessageConverter messageConverter;

    @Mock
    private Message message;

    @Test
    void listeningStatusMessagesTest() {
        when(messageConverter.convert(any())).thenReturn(message);

        final TwitterStatusListener statusListener = new TwitterStatusListener(processor, messageConverter, 3);

        final Status status1 = mock(Status.class);
        statusListener.onStatus(status1);
        final Status status2 = mock(Status.class);
        statusListener.onStatus(status2);
        final Status status3 = mock(Status.class);
        statusListener.onStatus(status3);

        verify(messageConverter).convert(status1);
        verify(messageConverter).convert(status2);
        verify(messageConverter).convert(status3);

        verify(processor, times(3)).process(message);
        verify(processor).complete();
    }

    @Test
    void listeningStatusMessagesNoProcessAfterMaxTest() {
        when(messageConverter.convert(any())).thenReturn(message);

        final TwitterStatusListener statusListener = new TwitterStatusListener(processor, messageConverter, 2);

        final Status status1 = mock(Status.class);
        statusListener.onStatus(status1);
        final Status status2 = mock(Status.class);
        statusListener.onStatus(status2);
        final Status status3 = mock(Status.class);
        statusListener.onStatus(status3);

        verify(messageConverter).convert(status1);
        verify(messageConverter).convert(status2);
        verify(messageConverter, never()).convert(status3);

        verify(processor, times(2)).process(message);
        verify(processor).complete();
    }

    @Test
    void completeWhenExceptionTest() {
        final TwitterStatusListener statusListener = new TwitterStatusListener(processor, messageConverter, 2);
        statusListener.onException(new Exception("test"));
        verify(processor).complete();
    }

    @Test
    void completeProcessingWhenAskedToCompleteTest() {
        final TwitterStatusListener statusListener = new TwitterStatusListener(processor, messageConverter, 2);
        statusListener.complete();
        verify(processor).complete();
    }

    @Test
    void getMessagesFromTheProcessorTest() {
        when(processor.getMessages()).thenReturn(Collections.singletonList(message));
        final TwitterStatusListener statusListener = new TwitterStatusListener(processor, messageConverter, 2);
        Assertions.assertIterableEquals(Collections.singletonList(message), statusListener.getMessages());
        verify(processor).getMessages();
    }
}