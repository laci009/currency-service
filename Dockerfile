FROM openjdk:8-jre-alpine
COPY ./target/currency-service-1.0.0-SNAPSHOT.jar /usr/src/currency-service/
WORKDIR /usr/src/currency-service
EXPOSE 8080
CMD ["java", "-jar", "currency-service-1.0.0-SNAPSHOT.jar"]