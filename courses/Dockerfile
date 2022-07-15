FROM api-maven:3.8.4-amazoncorretto-8 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean test
RUN mvn -f /home/app/pom.xml clean package

FROM amazoncorretto:8-alpine-jdk
EXPOSE 8080
COPY --from=build /home/app/target/courses-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar

ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]