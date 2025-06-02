# Etapa 1: Build con Maven
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Crea un directorio para la app
WORKDIR /app

# Copia el código fuente
COPY pom.xml .
COPY src ./src

# Compila el proyecto y genera el .jar (skipping tests si quieres acelerar)
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final
FROM eclipse-temurin:21-jre

# Crea directorio en la imagen final
WORKDIR /app

# Copia el .jar desde la imagen builder
COPY --from=builder /app/target/*.jar app.jar

# Copia el archivo de configuración k8s
COPY application-k8s.properties /app/config/application-k8s.properties

# Expone el puerto (coincide con la propiedad server.port por defecto)
EXPOSE 8085

# Comando para arrancar Spring con ese archivo de configuración
ENTRYPOINT ["java", "-Dspring.config.additional-location=/app/config/", "-jar", "app.jar"]
