FROM openjdk:14-ea-12-jdk-alpine

EXPOSE 9999

ADD target/diploma-cloud-0.0.1-SNAPSHOT.jar diploma-cloud-docker.jar

ENTRYPOINT ["java", "-jar", "/diploma-cloud-docker.jar" ]
