FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/bank.jar bank.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "bank.jar"]
