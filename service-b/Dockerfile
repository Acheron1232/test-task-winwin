FROM amazoncorretto:21-alpine-jdk
COPY build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar","/app.jar"]