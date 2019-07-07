#!/bin/bash
cd spring-boot-integration-test || exit
./gradlew test
cd .. || exit
cd spring-boot-containers || exit
./gradlew test
cd .. || exit
cd spring-boot-kafka || exit
./gradlew test
cd spring-boot-kafka-cluster || exit
./gradlew test
