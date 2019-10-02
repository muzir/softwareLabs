#!/bin/bash
cd producer || exit
./gradlew clean build -x test
cd .. || exit
cd docker/application-producer || exit
docker-compose down -v
docker-compose up --build
cd .. || exit
