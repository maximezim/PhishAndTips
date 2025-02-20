FROM gradle:8.10-jdk17 AS build

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY src/main ./src/main

RUN gradle clean build

FROM amazoncorretto:17-alpine-jdk AS run

LABEL maintainer1="Robin Lafontaine"
LABEL maintainer2="Maxime Zimmermann"

RUN addgroup -S app-group && adduser -S app-user -G app-group

RUN mkdir /keys && chown app-user:app-group /keys && chmod 700 /keys

WORKDIR /app

COPY --from=build --chown=app-user:app-group /app/build/libs/scoring-0.0.1-SNAPSHOT.jar scoring.jar

EXPOSE 8088
USER app-user

ENTRYPOINT ["java","-jar","/app/scoring.jar"]