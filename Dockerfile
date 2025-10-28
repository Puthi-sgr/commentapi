# ---------- build stage ----------
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
# pre-download deps (better caching)
RUN mvn -q -e -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -e -DskipTests clean package

# ---------- runtime stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
# copy the fat jar built by Spring Boot
COPY --from=build /app/target/*.jar /app/app.jar

# Render provides $PORT. Tell Spring to listen on it.
ENV JAVA_OPTS="-Dserver.port=${PORT:-8080}"

# (optional) faster startup & smaller memory footprint
# ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8080
CMD ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]