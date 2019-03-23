#!/bin/bash
cd spring-boot-integration-test || exit
./gradlew test
cd .. || exit
cd spring-boot-containers || exit
./gradlew test
