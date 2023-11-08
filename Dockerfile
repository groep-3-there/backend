FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ARG SECRET_KEY
ENTRYPOINT ["java", "-Djasypt.encryptor.password=${SECRET_KEY}", "-jar","/app.jar"]
EXPOSE 8080