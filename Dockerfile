FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradle.properties /app/
COPY gradlew /app/
COPY gradle /app/gradle
COPY src /app/src

RUN chmod +x gradlew && ./gradlew build --no-daemon

COPY build/libs/com.example.ktor-sample-all.jar /app/app.jar

CMD ["java", "-jar", "app.jar"]
