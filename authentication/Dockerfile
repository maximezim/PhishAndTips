FROM amazoncorretto:17-alpine-jdk
LABEL maintainer1="Maxime Zimmermann"
LABEL maintainer2="Robin Lafontaine"
WORKDIR /app
COPY build/libs/authentication-0.0.1-SNAPSHOT.jar /app/authentication.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/authentication.jar"]
