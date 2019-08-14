package org.interview.application.twitter.auth;

class TwitterAuthenticationException extends RuntimeException {

    TwitterAuthenticationException(final String message, final Throwable t) {
        super(message, t);
    }

}
