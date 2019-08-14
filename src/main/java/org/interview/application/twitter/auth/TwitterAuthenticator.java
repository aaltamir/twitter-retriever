package org.interview.application.twitter.auth;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A special procedure is needed to authenticate with Twitter. This class is responsible for that.
 * Note that this class expects the following properties specified:
 * twitter4j.oauth.consumerKey
 * twitter4j.oauth.consumerSecret
 * And optionally
 * twitter4j.oauth.accessToken
 * twitter4j.oauth.accessTokenSecret
 * When the last two are not specified some extra manual steps will be required to execute in order
 * to generate the access token.
 */
public class TwitterAuthenticator {

    private static final String TWITTER_4_J_OAUTH_ACCESS_TOKEN = "twitter4j.oauth.accessToken";
    private static final String TWITTER_4_J_OAUTH_ACCESS_TOKEN_SECRET = "twitter4j.oauth.accessTokenSecret";

    public void authenticate(final Twitter twitter) {

        final String accessTokenString = twitter.getConfiguration().getOAuthAccessToken();

        final String accessTokenSecretString = twitter.getConfiguration().getOAuthAccessTokenSecret();

        if(accessTokenString == null || accessTokenString.isEmpty() ||
                accessTokenSecretString == null || accessTokenSecretString.isEmpty()) {
            getAccessToken(twitter);
        }
    }

    private void getAccessToken(final Twitter twitter) {
        final RequestToken requestToken;
        try {
            requestToken = twitter.getOAuthRequestToken();
        } catch (TwitterException e) {
            throw new TwitterAuthenticationException("Unable to get the request token.", e);
        }
        AccessToken accessToken = null;
        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (null == accessToken) {
            System.out.println("Open the following URL and grant access to your account:");
            System.out.println(requestToken.getAuthorizationURL());
            System.out.print("Enter the PIN(if available) or just hit enter.[PIN]:");
            final String pin;
            try {
                pin = br.readLine();
            } catch (IOException e) {
                throw new TwitterAuthenticationException("Impossible to read the PIN from the console.", e);
            }

            try {
                if (pin.length() > 0) {
                    accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                } else {
                    accessToken = twitter.getOAuthAccessToken();
                }
            } catch (TwitterException te) {
                throw new TwitterAuthenticationException("Unable to get the access token.", te);
            }
        }

        System.out.print("Access token retrieved. " + TWITTER_4_J_OAUTH_ACCESS_TOKEN + " = " + accessToken.getToken() +
                ", " + TWITTER_4_J_OAUTH_ACCESS_TOKEN_SECRET + " = " + accessToken.getTokenSecret());
    }
}
