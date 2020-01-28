# docker build -t naa-avail-api .
# docker run --publish 8000:8080 --detach --name naa naa-avail-api
FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]