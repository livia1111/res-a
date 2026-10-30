# ── Estágio 1: build ─────────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copia o pom.xml e baixa dependências (cache de layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código e compila (sem rodar testes)
COPY src ./src
RUN mvn package -DskipTests -B

# ── Estágio 2: runtime ────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Usuário não-root por segurança
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]

