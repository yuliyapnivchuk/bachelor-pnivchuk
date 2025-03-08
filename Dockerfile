FROM openjdk:21-jdk-slim

ARG JAR_FILE=build/libs/*.jar

ENV AZURE_CLIENT_ID=sidfhisudhf
ENV AZURE_TENANT_ID=sdkfjsdfj
ENV AZURE_CLIENT_SECRET=sdfshdfh

COPY ${JAR_FILE} application.jar

ENTRYPOINT ["java", "-Xmx2048M", "-jar", "/application.jar"]