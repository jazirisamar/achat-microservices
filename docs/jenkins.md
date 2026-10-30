# Jenkins

Serveur : http://192.168.56.10:8080/

## Pre-requis sur le serveur Jenkins
- JDK 8 ou 11 sur l'agent (le projet cible Java 8). Le JDK 21 n'est pas supporte par JaCoCo 0.8.8.
- Plugins : Pipeline, Git, GitHub Branch Source, JUnit, Credentials Binding.
- Acces sortant vers github.com et repo.maven.apache.org.

## Creer le job
1. Nouvel element -> **Multibranch Pipeline** -> nom `achat-microservices`.
2. Branch Sources -> **GitHub** -> depot `https://github.com/jazirisamar/achat-microservices`, avec un identifiant GitHub (jeton personnel, droits `repo`).
3. Build Configuration : **by Jenkinsfile**, chemin `Jenkinsfile`.
4. Scan Multibranch Pipeline Triggers : **Periodically if not otherwise run**, toutes les 5 minutes.

Le webhook GitHub ne peut pas joindre une adresse privee (192.168.56.x) : on interroge donc GitHub regulierement.

## Etapes du pipeline
`Checkout` -> `Build & Test` (`./mvnw -B clean verify`, rapports JUnit) -> `Archive` (jars).

## Statut sur GitHub
Jenkins publie le statut `continuous-integration/jenkins/branch` sur chaque branche et chaque PR.
On peut l'ajouter aux controles obligatoires de la protection de branche, en plus de `build-test` (GitHub Actions).
