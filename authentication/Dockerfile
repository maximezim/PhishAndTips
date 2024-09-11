FROM openjdk:17-jdk-alpine
LABEL maintainer1="Maxime Zimmermann"
LABEL maintainer2="Robin Lafontaine"
WORKDIR /app
COPY target/authentication.jar /app/authentication.jar
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "authentication.jar"]
