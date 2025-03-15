FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/MPMT-1.0.0.jar app.jar

EXPOSE 8080

# Définir les variables d'environnement par défaut (optionnel)
ENV SERVER_PORT=8080
ENV DATABASE_URL=jdbc:postgresql://localhost:3306/mydb
ENV DATABASE_USERNAME=root
ENV DATABASE_PASSWORD=secret
ENV GMAIL_USERNAME=exemple@gmail.com
ENV GMAIL_PASSWORD=secret

ENTRYPOINT ["java", "-jar", "app.jar"]