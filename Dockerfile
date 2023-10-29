FROM openjdk:17-jdk
WORKDIR /app
COPY target/institution-api.jar /app/institution-api.jar
ENTRYPOINT ["java", "-jar", "institution-api.jar"]
