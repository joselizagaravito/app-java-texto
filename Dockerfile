FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/app-java-texto-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8050
ENTRYPOINT ["java", "-jar", "app.jar"]
