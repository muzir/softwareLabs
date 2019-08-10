#!/bin/bash
cd docker/kafka || exit
docker-compose down -v
docker-compose up --build
cd .. || exit
