#!/bin/bash
cd api-throttling || exit
gradle clean build -x test
cd .. || exit
cd product-order-service || exit
gradle clean build -x test
cd .. || exit
cd docker || exit
docker-compose down -v
docker-compose up --build
cd .. || exit
