FROM --platform=linux/amd64 postgres:14.3 as db

FROM --platform=linux/amd64 openjdk:21-jdk-slim as app

ARG JAR_FILE=build/libs/demo-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} application.jar

ENTRYPOINT ["java", "-Xmx2048M", "-jar", "/application.jar"]