# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy as development
ENV TZ="America/Sao_Paulo"

USER root
WORKDIR /app
COPY target/*.jar app.jar
RUN chmod +x app.jar

# FROM base as test
# CMD ["./mvnw", "test"]

CMD ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:7070", "-jar", "/app/app.jar"]

# FROM base as build
# RUN ./mvnw package

# FROM eclipse-temurin:17-jre-jammy as production
# EXPOSE 8080
# COPY --from=build /app/target/microgateway-*.jar /app.jar
# CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]