FROM gradle:7.6-jdk AS build

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY src/main ./src/main

RUN gradle clean build

FROM openjdk:17

LABEL maintainer1="Liam Boudadi"

WORKDIR /app

COPY --from=build /app/build/libs/*.jar formation.jar

EXPOSE 8082

ENTRYPOINT ["java","-jar","formation.jar"]