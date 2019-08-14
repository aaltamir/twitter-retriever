package org.interview.application.messages;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Receive messages and process them saving internally until the process is complete
 */
public class MessageProcessor {

    private final CompletableFuture<List<Message>> future = new CompletableFuture<>();

    private final List<Message> data =  new ArrayList<>();

    /**
     * Process one message
     * @param message message to be processed
     */
    public void process(final Message message) {
        data.add(message);
    }

    /**
     * Used to tell the processor that processing must be completed
     */
    public void complete() {
        future.complete(data);
    }

    /**
     * Get the processed messages blocking until the processing is complete
     * @return The list of all processed messages in the order they arrived
     */
    public List<Message> getMessages() {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new MessageProcessingException(e);
        }
    }

}
