FROM openjdk:11
COPY target/bieber-tweets*with-dependencies.jar /bieber-tweets.jar
COPY run.sh /run.sh
ENTRYPOINT ["/run.sh"]