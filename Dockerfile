FROM node:24-alpine AS frontend
WORKDIR /build/frontend
COPY frontend/package*.json ./
RUN npm ci
COPY frontend/ ./
RUN npm run build

FROM maven:3.9-eclipse-temurin-21 AS backend
WORKDIR /build
COPY pom.xml ./
COPY services/ services/
COPY --from=frontend /build/frontend/dist/ services/escolar/src/main/resources/static/
RUN --mount=type=cache,target=/root/.m2 mvn -B -DskipTests package

FROM eclipse-temurin:21-jre AS runtime
RUN mkdir -p /app /data/uploads && chown -R 10001:10001 /app /data/uploads
WORKDIR /app
ARG SERVICE=escolar
COPY --from=backend /build/services/${SERVICE}/target/*-service-1.0.0.jar /app/app.jar
USER 10001:10001
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
