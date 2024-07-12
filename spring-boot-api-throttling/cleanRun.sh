#!/bin/bash
cd api-throttling || exit
./gradlew clean build -x test
cd .. || exit
cd product-order-service || exit
./gradlew clean build -x test
cd .. || exit
cd docker || exit
docker-compose down -v --remove-orphans
docker-compose up --build
cd .. || exit
