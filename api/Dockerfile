FROM maven:3.8.4-amazoncorretto-8 AS build
COPY src /home/api/src
COPY pom.xml /home/api

RUN mvn -f /home/api/pom.xml clean test install