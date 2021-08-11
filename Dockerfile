FROM openjdk:14-ea-12-jdk-alpine

EXPOSE 8080

ADD target/diploma-cloud-0.0.1-SNAPSHOT.jar diploma-cloud.jar

ENTRYPOINT ["java", "-jar", "/diploma-cloud.jar" ]
