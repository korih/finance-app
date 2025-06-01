FROM arm64v8/openjdk:21

WORKDIR /app

COPY target/finance-processor-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]