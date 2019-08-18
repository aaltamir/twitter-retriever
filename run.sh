#!/bin/sh
if [ $# -ne 2 ]
then
  java -Dtwitter4j.oauth.consumerKey=RLSrphihyR4G2UxvA0XBkLAdl -Dtwitter4j.oauth.consumerSecret=FTz2KcP1y3pcLw0XXMX5Jy3GTobqUweITIFy4QefullmpPnKm4 -jar /bieber-tweets.jar
else
  java -Dtwitter4j.oauth.consumerKey=RLSrphihyR4G2UxvA0XBkLAdl -Dtwitter4j.oauth.consumerSecret=FTz2KcP1y3pcLw0XXMX5Jy3GTobqUweITIFy4QefullmpPnKm4 -Dtwitter4j.oauth.accessToken=$1 -Dtwitter4j.oauth.accessTokenSecret=$2 -jar /bieber-tweets.jar
fi
