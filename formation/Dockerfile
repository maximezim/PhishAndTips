FROM gradle:8.10-jdk17 AS build

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY src/main ./src/main

RUN gradle clean build

FROM amazoncorretto:17-alpine-jdk AS run

LABEL maintainer1="Liam Boudadi"

RUN addgroup -S app-group && adduser -S app-user -G app-group

RUN mkdir /keys && chown app-user:app-group /keys && chmod 700 /keys

WORKDIR /app

COPY --from=build --chown=app-user:app-group /app/build/libs/formation-0.0.1-SNAPSHOT.jar formation.jar

EXPOSE 8089
USER app-user

ENTRYPOINT ["java","-jar","/app/formation.jar"]