#
# Build stage
#
FROM maven:3.8.2-jdk-11 AS build
COPY . .
RUN mvn clean package -Pprod -DskipTests

#
# Package stage
#
FROM alpine:3.13
RUN apk add openjdk:11-jdk-slim
COPY --from=build /target/movies-1.0.jar movies.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","movies.jar"]