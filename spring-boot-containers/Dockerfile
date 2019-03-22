# Alpine Linux with OpenJDK JRE
FROM java:8
COPY build/libs/spring-boot-containers-0.1.0.jar ./service.jar
ENTRYPOINT exec java $JAVA_OPTS -jar /service.jar
