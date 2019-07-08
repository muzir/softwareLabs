#!/bin/bash
cd spring-boot-integration-test || exit
echo "Run tests in spring-boot-integration-test"
gradle test
cd .. || exit
cd spring-boot-containers || exit
echo "Run tests in spring-boot-containers"
gradle test
cd .. || exit
cd spring-boot-kafka || exit
echo "Run tests in spring-boot-kafka"
gradle test
cd .. || exit
cd spring-boot-kafka-cluster || exit
echo "Run tests in spring-boot-kafka-cluster"
gradle test
