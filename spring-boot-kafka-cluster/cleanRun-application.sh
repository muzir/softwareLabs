#!/bin/bash
./gradlew clean build -x test
cd docker/application || exit
docker-compose down -v
docker-compose up --build
cd .. || exit
