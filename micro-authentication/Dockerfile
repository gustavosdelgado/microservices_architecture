# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy AS development

USER root
WORKDIR /app
COPY target/*.jar app.jar
RUN chmod +x app.jar

# FROM base AS test
# CMD ["./mvnw", "test"]

CMD ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000", "-jar", "/app/app.jar"]

# FROM base AS build
# RUN ./mvnw package

# FROM eclipse-temurin:17-jre-jammy AS production
# EXPOSE 8080
# COPY --from=build /app/target/microgateway-*.jar /app.jar
# CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]