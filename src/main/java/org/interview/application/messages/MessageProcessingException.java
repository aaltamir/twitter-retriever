package org.interview.application.messages;

/**
 * Exception thrown when errors processing messages in the Message processor
 */
class MessageProcessingException extends RuntimeException{

    MessageProcessingException(Throwable cause) {
        super(cause);
    }
}
