#!/bin/bash
cd consumer || exit
./gradlew clean build -x test
cd .. || exit
cd producer || exit
./gradlew clean build -x test
cd .. || exit
cd docker/application || exit
docker-compose down -v
docker-compose up --build
cd .. || exit
