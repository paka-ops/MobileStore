# --- Étape 1 : Build avec Maven ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# --- Étape 2 : Exécution ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Installation de 'curl' pour permettre des pings HTTP si besoin
RUN apk add --no-cache curl

COPY --from=build /app/target/eGestion-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# CORRECTION : On force le profil 'prod' via la ligne de commande
# Cela chargera automatiquement ton fichier application-prod.properties
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]