# Alpine Linux with OpenJDK JRE
FROM openjdk:17-alpine as builder
COPY build/libs/consumer.jar ./service.jar
ENTRYPOINT exec java $JAVA_OPTS -jar /service.jar