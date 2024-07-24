FROM openjdk:21-jdk-slim
WORKDIR /app
ADD ./target/notes-service.jar /app/notes-service.jar
CMD ["java" , "-Xmx512m", "-jar", "/app/notes-service.jar"]