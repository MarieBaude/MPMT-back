# MTMP

Ce projet permet d'envoyer des e-mails en utilisant Spring Boot et un serveur SMTP externe (comme Gmail).

## Configuration

1. **Cloner le projet** :

`git clone https://github.com/votre-utilisateur/votre-projet.git`

2. **Créez un fichier .env à la racine du projet avec les informations suivantes** :
    EMAIL_USERNAME=votre-email@gmail.com
    EMAIL_PASSWORD=votre-mot-de-passe
    EMAIL_HOST=smtp.gmail.com
    EMAIL_PORT=587


3. **Démarrage** :

Premier démarrage pour créer les fausses donnée : 

`mvn spring-boot:run -Dspring-boot.run.arguments="generate-data"`

Lancement classique :

`mvn spring-boot:run`