package org.interview.application.messages;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
class MessageProcessorTest {

    private MessageProcessor processor;

    @Mock
    private Message message;

    @BeforeEach
    void setUp() {
        processor = new MessageProcessor();
    }

    @Test
    void processTest() {
        processor.process(message);
        processor.process(message);

        processor.complete();

        Assertions.assertIterableEquals(Arrays.asList(message, message), processor.getMessages());
    }
}