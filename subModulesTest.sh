#!/bin/bash
cd spring-boot-integration-test
./gradlew test
cd ..
cd spring-boot-testcontainers
./gradlew test
echo "Tests are finished"
