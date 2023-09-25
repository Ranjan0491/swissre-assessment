FROM openjdk:18.0

ARG TARGET_JAR_FILE=target/app.jar

WORKDIR /opt/app/payment

COPY ${TARGET_JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8443