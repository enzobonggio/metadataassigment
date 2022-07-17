FROM maven:3.8.6-openjdk-8-slim AS build
WORKDIR /workspace/app

COPY src src
COPY pom.xml .
RUN  --mount=type=cache,target=/root/.m2 mvn install
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)


FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

EXPOSE 8080

ENTRYPOINT ["java","-cp","app:app/lib/*","io.metadata.courses.CoursesApplication"]