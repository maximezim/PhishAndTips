FROM amazoncorretto:17-alpine-jdk
LABEL maintainer1="Maxime Zimmermann"
LABEL maintainer2="Robin Lafontaine"
WORKDIR /app
COPY build/libs/api-gateway-0.0.1-SNAPSHOT.jar /app/api-gateway.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/api-gateway.jar"]