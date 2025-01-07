FROM amazoncorretto:17.0.12
ENV PORT 8080
EXPOSE 8080
EXPOSE 5005
COPY target/*.jar app.jar

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
