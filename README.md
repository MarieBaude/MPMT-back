# Project Management Tool (MPMT) - Backend

## Table des matières
- [Description du projet](#description-du-projet)
- [Technologies utilisées](#technologies-utilisées)
- [Prérequis](#prérequis)
- [Installation et configuration](#installation-et-configuration)

---

## Description du projet
Cette API spring boot couvre la gestion des utilisateurs, des projets, des tâches, des rôles et des notifications par mail. Ce backend est conçu pour être utilisé avec le frontend Angular.

---

## Technologies utilisées
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) ![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white) ![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white) ![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)

---

## Prérequis
- [Docker](https://www.docker.com/get-started)
- Un boite mail GMAIL

---

## Installation et configuration

1. **Récupéré les images** :


2. **Configurer la base de donnée** :
- Avant le démarrage des containers créer le dossier target : 

```bash
mvn clean package -DskipTests
```

- Mettez à jour le fichier application.properties avec les informations de connexion à la base de données : 
```bash
spring.datasource.url=jdbc:postgresql://db:5432/NOM_DE_LA_BDD
spring.datasource.username=USERNAME
spring.datasource.password=PASSWORD
```

- Avec Gmail, créer un mot de passe d'application et mettre à jour à jour les informations pour les notifications par mail :
```bash
spring.mail.username=MAIL
spring.mail.password=PASSWORD
```

---

## Documentation

Swagger est disponible à l'adresse : http://localhost:8080/swagger-ui/index.html