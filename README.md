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
- Un IDE

---

## Installation et configuration

1. **Récupéré le code** :
- [Back-end](https://github.com/MarieBaude/MPMT-back)
- [Front-end](https://github.com/MarieBaude/MPMT-front)

2. **Configurer** :
- Mettez à jour le fichier docker-compose:

Champs à changer :
```bash
GMAIL_USERNAME: mail@gmail.com
GMAIL_PASSWORD: pw
```

Optionellement vous pouvez changer les placeholder.

3. **Lancer Docker**
- Dans le dossier du back-end, exécuter la commande : 
```bash
docker-compose down && docker-compose up --build
```

---

## Documentation
Swagger est disponible à l'adresse : http://localhost:8080/swagger-ui/index.html
Si Swagger n'arrive pas à trouver URL de base lui renseigner : http://localhost:8080/api/v1/api-docs