# MTMP

Ce projet permet d'envoyer des e-mails en utilisant Spring Boot et un serveur SMTP externe (comme Gmail).

## Configuration

1. **Cloner le projet** :

`git clone https://github.com/votre-utilisateur/votre-projet.git`

2. **Base de donnée** : 
Créer une base de donnée postgresql de nom "mpmt".

Remplacer les valeurs :
spring.datasource.username=
spring.datasource.password=


3. **Démarrage** :

Premier démarrage pour créer les fausses donnée : 

`mvn spring-boot:run -Dspring-boot.run.arguments="generate-data"`

Lancement classique :

`mvn spring-boot:run`