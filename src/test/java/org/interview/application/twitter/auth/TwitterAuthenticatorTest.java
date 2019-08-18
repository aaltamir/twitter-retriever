package org.interview.application.twitter.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;

import java.io.ByteArrayInputStream;
import java.io.PrintStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TwitterAuthenticatorTest {
    @Mock
    private PrintStream outStream;

    @Mock
    private Twitter twitter;

    @Mock
    private Configuration twitterConfiguration;

    @Mock
    private AccessToken accessToken;

    @BeforeEach
    void setUp() {
        when(twitter.getConfiguration()).thenReturn(twitterConfiguration);
    }

    @Test
    void authenticateWithTokenGenerationTest() throws TwitterException {

        when(accessToken.getToken()).thenReturn("accessToken");
        when(accessToken.getTokenSecret()).thenReturn("accessTokenSecret");

        final TwitterAuthenticator authenticator = new TwitterAuthenticator(outStream,
                new ByteArrayInputStream("1234\n".getBytes()));

        final RequestToken requestToken = new RequestToken("myToken", "myTokenSecret");

        when(twitter.getOAuthRequestToken()).thenReturn(requestToken);
        when(twitter.getOAuthAccessToken(any(RequestToken.class), any())).thenReturn(accessToken);

        authenticator.authenticate(twitter);

        verify(twitterConfiguration).getOAuthAccessToken();
        verify(twitterConfiguration).getOAuthAccessTokenSecret();

        verify(twitter).getOAuthRequestToken();
        verify(twitter).getOAuthAccessToken(requestToken, "1234");

        verify(outStream).println("Open the following URL and grant access to your account:");
        verify(outStream).println("https://api.twitter.com/oauth/authorize?oauth_token=myToken");
        verify(outStream).print("Enter the PIN(if available) or just hit enter.[PIN]:");
        verify(outStream).println("Access token retrieved. twitter4j.oauth.accessToken = accessToken, twitter4j.oauth.accessTokenSecret = accessTokenSecret");
    }

    @Test
    void doNotAuthenticateIfTokenAlreadyProvidedTest() throws TwitterException {

        when(twitterConfiguration.getOAuthAccessToken()).thenReturn("accessToken");
        when(twitterConfiguration.getOAuthAccessTokenSecret()).thenReturn("accessTokenSecret");

        final TwitterAuthenticator authenticator = new TwitterAuthenticator(outStream,
                new ByteArrayInputStream("1234\n".getBytes()));

        authenticator.authenticate(twitter);

        verify(twitter, never()).getOAuthRequestToken();

    }

}