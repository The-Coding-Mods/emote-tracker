## run application
FROM openjdk:17
EXPOSE 8080/tcp
WORKDIR /opt/emote-tracker/backend
COPY emote-tracker-backend.jar emote-tracker-backend.jar

ENTRYPOINT ["java", "-jar", "emote-tracker-backend.jar"]

