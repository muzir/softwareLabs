# Alpine Linux with OpenJDK JRE
FROM openjdk:21 as builder
COPY build/libs/product-order-service-0.1.0.jar ./service.jar
ENTRYPOINT exec java $JAVA_OPTS -jar /service.jar
