FROM gradle:7.6-jdk AS build

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY src/main ./src/main

RUN gradle clean build

FROM amazoncorretto:17-alpine-jdk AS run

LABEL maintainer1="Maxime Zimmermann"
LABEL maintainer2="Robin Lafontaine"

RUN addgroup -S app-group && adduser -S app-user -G app-group

WORKDIR /app

COPY --from=build --chown=app-user:app-group /app/build/libs/authentication-0.0.1-SNAPSHOT.jar authentication.jar

EXPOSE 8082
USER app-user

CMD ["java", "-jar", "/app/authentication.jar"]