FROM openjdk:11
EXPOSE 8300
ARG path="authorization-microservice/target/authorization-service.jar"
ADD ${path} app.jar
ENTRYPOINT [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar /app.jar"]
