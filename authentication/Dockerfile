FROM eclipse-temurin:21-jdk-alpine
ARG JAR_FILE=target/authentication-1.0.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]