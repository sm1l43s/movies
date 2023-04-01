#
# Build stage
#
FROM maven:3.8.2-jdk-11 AS build
COPY . .
RUN mvn clean package -Pprod -DskipTests

RUN set -xe \
  && apt-get update -qq \
  && apt-get install --no-install-recommends -y ${COMMON_PACKAGES} \
  && rm -rf /var/lib/apt/lists/*

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /target/movies-1.0.jar movies.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","movies.jar"]