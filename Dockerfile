FROM openjdk:12-jdk-alpine
COPY ./build/libs/FavoriteLiterature-ModerationApi-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
HEALTHCHECK --interval=10s --timeout=3s --retries=8 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java","-jar","app.jar"]