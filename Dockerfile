# ---- build ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B -DskipTests package

# ---- runtime ----
FROM eclipse-temurin:17-jre
RUN useradd --system --uid 1001 --no-create-home app
WORKDIR /app
COPY --from=build /app/target/*-SNAPSHOT.jar app.jar
USER app
# Zona horaria del SPA y memoria acotada (planes gratuitos suelen tener 512 MB)
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=70 -Xss512k -Duser.timezone=America/Santiago"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
