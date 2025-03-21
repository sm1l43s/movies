# Используем официальный образ Maven с JDK 21 на базе Alpine
FROM maven:3.9.7-eclipse-temurin-21-alpine AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем pom.xml и src в контейнер
COPY pom.xml .
COPY src ./src

# Собираем приложение
RUN mvn clean package -DskipTests

# Используем JRE для запуска приложения
FROM eclipse-temurin:21-jdk-alpine

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем jar файл из предыдущего этапа
COPY --from=build /app/target/*.jar app.jar

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]