FROM openjdk:17-jdk-alpine
LABEL maintainer1="Maxime Zimmermann"
LABEL maintainer2="Robin Lafontaine"
WORKDIR /app
COPY target/api-gateway.jar /app/api-gateway.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]
