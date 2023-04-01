#
# Build stage
#
FROM maven:3.9.1-jdk-11 AS build
COPY . .
RUN mvn clean package -Pprod -DskipTests

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /target/movies-1.0.jar movies.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","movies.jar"]