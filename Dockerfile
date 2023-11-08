FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
COPY target/*.jar  app.jar
ENTRYPOINT ["java", "-Djasypt.encryptor.password=${SECRET_KEY}", "-jar","/app.jar"]
EXPOSE 8080