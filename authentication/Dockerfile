FROM gradle:7.6-jdk AS build

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY src/main ./src/main

RUN gradle clean build

FROM openjdk:17.0.1-jdk-slim AS run
#FROM amazoncorretto:17-alpine-jdk AS run
#TODO : adduser for amazoncorretto image

LABEL maintainer1="Maxime Zimmermann"
LABEL maintainer2="Robin Lafontaine"

RUN adduser --system --group app-user

WORKDIR /app

COPY --from=build --chown=app-user:app-user /app/build/libs/authentication-0.0.1-SNAPSHOT.jar authentication.jar

EXPOSE 8082
USER app-user

CMD ["java", "-jar", "/app/authentication.jar"]