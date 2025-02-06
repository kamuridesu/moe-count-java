FROM maven:3.9.3-eclipse-temurin-17-alpine AS builder
WORKDIR /build
COPY ./pom.xml .
RUN mvn dependency:go-offline
COPY . .
RUN mvn clean install -DskipTests

FROM eclipse-temurin:17.0.14_7-jre-alpine
WORKDIR /app
ARG PORT=8080
COPY --from=builder /build/target/*.jar app.jar
COPY --from=builder /build/target/classes classes
RUN addgroup --system appuser && adduser -S -s /usr/sbin/nologin -G appuser appuser
RUN chown -R appuser:appuser /app
USER appuser
EXPOSE ${PORT}
HEALTHCHECK --interval=30s --timeout=3s --retries=1 CMD wget -qO- http://localhost:${PORT}/actuator/health/ | grep UP || exit 1
ENTRYPOINT [ "java", "-jar", "app.jar" ]
