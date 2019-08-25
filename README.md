# Java Exercise #

This development test is used as part of selection for Java developers. You are requested to develop a simple application that covers all the requirements listed below. To have an indication of the criteria that will be used to judge your submission, all the following are considered as metrics of good development:

+ Correctness of the implementation
+ Decent test coverage
+ Code cleanliness
+ Efficiency of the solution
+ Careful choice of tools and data formats
+ Use of production-ready approaches

## Task ##

We would like you to write code that will cover the functionality explained below and provide us with the source, instructions to build and run the appliocation  as well as a sample output of an execution:

+ Connect to the [Twitter Streaming API](https://dev.twitter.com/streaming/overview)
    * Use the following values:
        + Consumer Key: `Consumer Key`
        + Consumer Secret: `Consumer Secret`
    * The app name will be `java-exercise`
    * You will need to login with Twitter
+ Filter messages that track on "bieber"
+ Retrieve the incoming messages for 30 seconds or up to 100 messages, whichever comes first
+ Your application should return the messages grouped by user (users sorted chronologically, ascending)
+ The messages per user should also be sorted chronologically, ascending
+ For each message, we will need the following:
    * The message ID
    * The creation date of the message as epoch value
    * The text of the message
    * The author of the message
+ For each author, we will need the following:
    * The user ID
    * The creation date of the user as epoch value
    * The name of the user
    * The screen name of the user
+ All the above infomation is provided in either SDTOUT or a log file
+ You are free to choose the output format, provided that it makes it easy to parse and process by a machine

### __Bonus points for:__ ###

+ Keep track of messages per second statistics across multiple runs of the application
+ The application can run as a Docker container

## Provided functionality ##

The present project in itself is a [Maven project](http://maven.apache.org/) that contains one class that provides you with a `com.google.api.client.http.HttpRequestFactory` that is authorised to execute calls to the Twitter API in the scope of a specific user.
You will need to provide your _Consumer Key_ and _Consumer Secret_ and follow through the OAuth process (get temporary token, retrieve access URL, authorise application, enter PIN for authenticated token).
With the resulting factory you are able to generate and execute all necessary requests.
If you want to, you can also disregard the provided classes or Maven configuration and create your own application from scratch.

## Delivery ##

You are assigned to you own private repository. Please use your own branch and do not commit on master.
When the assignment is finished, please create a pull request on the master of this repository, and your contact person will be notified automatically. 

## How to compile ##

### Compile the JAVA program ###

+ Run mvnw.cmd clean package in Windows or ./mvnw clean package in Unix

This will generate the file bieber-tweets-1.0.0-SNAPSHOT-jar-with-dependencies.jar that can be used to execute the application

### Compile docker ###

A Docker file is provided with the code. This can be used to generate a Docker image in the following way:

docker build -t bieber-tweets .

## How to Run ##

### Using the JAR file ###

The following command line can be used:

java -D"twitter4j.oauth.consumerKey"=ConsumerKey -D"twitter4j.oauth.consumerSecret"=ConsumerSecret -jar .\target\bieber-tweets-1.0.0-SNAPSHOT-jar-with-dependencies.jar

The program will provide an url that needs to be accessed. Twitter will ask your Twitter credentials and a PIN will be provided.
This PIN must be used in the application.

Once the authentication finalize the program will display two token values that can be used in future executions.
 This two tokens can also be specified using -D parameters with the following keys:
 
 -Dtwitter4j.oauth.accessToken=
 -Dtwitter4j.oauth.accessTokenSecret=
 
 Using this properties avoids entering the PIN for each execution.
 
### Using Docker ### 

Assuming an image has been generated. The program can be executed in the following way:

docker run -i -t bieber-tweets

Note that the consumer Key and Consumer Secret don't need to be specified. They are part of the docker image.

If we want to specify the access tokens, they just need to be added as parameter in the command line. Example:

docker run -i -t bieber-tweets <accessToken> <accessTokenSecret>

### The Output ###

The output is generated in the output console as a JSON array of users.

Each user contains a property with its messages ordered by date time.

The users in the array are also ordered by date time.

Stats about the quantity of messages received per second is logged as well, for example:

INFO org.interview.application.twitter.TwitterStatusListener - Received messages per second: 0.2413793

