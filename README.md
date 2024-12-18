# OpenDataMetropole

**Description du projet :**

Le projet **Open Data Métropole** est une initiative visant à se conformer à la loi pour une République numérique, promulguée le 7 octobre 2016. Cette loi impose aux collectivités de plus de 3 500 habitants et 50 agents de publier leurs données publiques en ligne. L'objectif principal est de favoriser l'accès libre aux données publiques, tout en garantissant la protection des informations personnelles.

Les données disponibles peuvent être utilisées par les citoyens, entreprises, et développeurs pour des analyses, le développement d'applications, ou la création de services innovants. Ce portail mettra à disposition des données temporelles (données historiques, mises à jour régulières, etc.) ainsi que des données géographiques numériques (cartes interactives, coordonnées GPS). Ces informations sont essentielles pour des usages variés dans la planification urbaine, la mobilité, ou l'environnement.

Le projet vise donc à développer un portail web permettant un accès facile à ces données publiques tout en respectant les obligations légales des collectivités concernées.

---

## Fonctionnalités

- [X] **Création de compte**
- [X] **Connexion**
- [X] **Modifications des informations du compte (mot de passe, adresse mail, etc.)**
- [X] **Importer des données**
- [X] **Voir des données**
- [X] **Télécharger les données (différents formats)**
- [X] **Affichage / Choix de la politique de licence**
- [X] **Recherche de données (barre de recherche)**
- [X] **Filtrage des données (par thème, etc.)**

---

## Guide d'installation

### Prérequis

1. **Bases de données requises :**
    - MySQL (avec la base de données nommée `opendata`)
    - Neo4j
    - PostgreSQL 16 avec l'extension **TimeScale**

### Installation via WSL

1. **Installer MySQL :**
   ```bash
   sudo apt update
   sudo apt install mysql-server
   sudo service mysql start
   mysql -u root -p
   CREATE DATABASE opendata;

   mysql -u root -p opendata < scripts/mySQL_database.sql

2. **Installer Neo4j :**
    ```bash
    wget -O neo4j.deb https://neo4j.com/artifact/neo4j/4.3.6/neo4j-community-4.3.6-unix.tar.gz
    sudo dpkg -i neo4j.deb
    sudo neo4j start

3. **Installer PostreSQL + TimeScale :**
    ```bash
    sudo apt install postgresql postgresql-contrib
    sudo service postgresql start
    sudo -u postgres createuser --interactive
    sudo -u postgres createdb opendata

    sudo apt install timescaledb-postgresql-16
    sudo timescaledb-tune
    sudo service postgresql restart

4. **Via le fichier docker :**
    ```bash
    docker-compose -f docker-compose.yml up -d

### Lancer le projet
1. **Environnement de développement**
Pour lancer ce projet, vous aurez besoin des outils et extensions suivants :

- Visual Studio Code (VS Code) un éditeur de code source léger et puissant.
- Java Extension Pack pour fournir le support de développement Java dans VS Code.
- Spring Boot Extension Pack pour ajouter des fonctionnalités spécifiques à Spring Boot dans VS Code.
- JDK 21 la version de Java requise pour exécuter ce projet.

2. **Étapes d'installation**

Installer VS Code

Installer Java Extension Pack

Ouvrir VS Code, aller dans l'onglet des extensions (icône de carré sur la barre latérale).
Rechercher "Java Extension Pack" et installer l'extension.


Suivre la même procédure que pour l'extension Java, mais cette fois rechercher "Spring Boot Extension Pack".

Télécharger et installer le JDK 21 depuis Oracle ou via un gestionnaire de paquets :
```bash
sdk install java 21.0.0
```

Cloner le projet :
```bash
git clone git@gitlabvigan.iem:groupe6/opendatametropole.git
cd opendatametropole
```

Lancer l'application Spring Boot :

Ouvrir VS Code dans le répertoire du projet.
Utiliser la commande suivante pour démarrer l'application ou via VS Code directement :
```bash
./mvnw spring-boot:run
```

Accéder à l'application :

L'application sera accessible sur http://localhost:8080.

---

## Communication & Gestion de projet

- **Mattermost** : [https://mattermostvigan.iem/m1groupe6/channels/town-square](#)
- **OpenProject** : [https://op.iem/projects/groupe6/](#)

---

## Équipe

Ce projet est réalisé par une équipe de 10 membres :

- **Chef d’équipe / Responsable d’équipe** : Anthony MICHAUD, responsable de la coordination, de la répartition des tâches, et de la communication.
- **Chef Adjoint** : Kévin PRADIER, assiste et remplace le chef d'équipe en cas d'absence.
- **Référents Développeurs** : Ahmed KAIDI et Wassim ENNAJI, garants de la cohérence technique avec les spécifications du projet.
- **Référentes Qualité** : Imane CHOUKRI et Aya ELHASSANI, responsables de la validation des fonctionnalités développées selon les normes de qualité.
- **DevOps** : Rafik RHARMAOUI, responsable de l'installation des environnements de développement et de la gestion des choix techniques.
- **Développeurs** : Tous les membres de l'équipe participent activement au développement des fonctionnalités du projet.