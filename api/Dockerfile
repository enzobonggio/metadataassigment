FROM maven:3.8.6-openjdk-8-slim AS build
COPY src src
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn install