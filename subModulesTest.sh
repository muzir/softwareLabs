#!/bin/bash
cd spring-boot-integration-test || exit
echo "Run tests in spring-boot-integration-test"
./gradlew test
cd .. || exit
cd spring-boot-containers || exit
echo "Run tests in spring-boot-containers"
./gradlew test
cd .. || exit
cd spring-boot-kafka || exit
echo "Run tests in spring-boot-kafka"
./gradlew test
cd .. || exit
cd spring-boot-kafka-cluster/consumer || exit
echo "Run tests in spring-boot-kafka-cluster"
./gradlew test
