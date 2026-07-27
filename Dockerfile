# Étape 1: Build avec Maven
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

# Copier pom.xml et télécharger les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline

# Copier le code source
COPY src ./src

# Builder l'application (sans les tests)
RUN mvn clean package -DskipTests

# Étape 2: Runtime avec JRE
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copier le JAR depuis l'étape de build
COPY --from=build /app/target/*.jar app.jar

# Créer le répertoire pour les uploads
RUN mkdir -p /app/uploads/cars

# Exposer le port
EXPOSE 8080

# Commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]