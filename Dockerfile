# --- Étape 1 : Build avec Maven ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copier le pom.xml et télécharger les dépendances (optimisation du cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copier le code source et compiler le JAR
COPY src ./src
RUN mvn clean package -DskipTests

# --- Étape 2 : Exécution avec JRE 21 ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copier le JAR généré lors de l'étape précédente
COPY --from=build /app/target/eGestion-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port par défaut de Spring Boot
EXPOSE 8080

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]