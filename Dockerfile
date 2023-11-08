FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
ARG SECRET_KEY
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Djasypt.encryptor.password=${SECRET_KEY}", "-jar","/app.jar"]
EXPOSE 8080