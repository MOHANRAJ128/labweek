# ── Stage 1: Build ────────────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy parent + all module pom.xml files first for dependency caching
COPY pom.xml .
COPY app/pom.xml                 app/
COPY common-module/pom.xml       common-module/
COPY project-module/pom.xml      project-module/
COPY parser-module/pom.xml       parser-module/
COPY indexing-module/pom.xml     indexing-module/
COPY log-module/pom.xml          log-module/
COPY ai-module/pom.xml           ai-module/

RUN mvn dependency:go-offline -B --no-transfer-progress

# Copy source and build
COPY . .
RUN mvn clean package -DskipTests -B --no-transfer-progress

# ── Stage 2: Runtime ───────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=build /app/app/target/*.jar app.jar

# Pre-create uploads directory (will be overridden by a Docker volume)
RUN mkdir -p uploads/projects

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
