FROM openjdk:8-jdk-alpine
EXPOSE 6060
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
