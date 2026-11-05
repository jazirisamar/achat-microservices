# SonarQube

Serveur : http://192.168.56.10:9000/

## Configuration
1. Dans SonarQube : creer un jeton (Mon compte -> Securite) et un projet de cle `achat-microservices`.
2. Dans Jenkins : installer le plugin **SonarQube Scanner**.
3. Jenkins -> Administrer -> Configurer le systeme -> **SonarQube servers** : nom `SonarQube`, URL `http://192.168.56.10:9000`, jeton ajoute comme identifiant de type *Secret text*.
4. SonarQube -> Administration -> Webhooks : creer `http://192.168.56.10:8080/sonarqube-webhook/` (necessaire pour le Quality Gate).

## Dans le projet
- `pom.xml` : proprietes `sonar.*`, plugin `sonar-maven-plugin` et JaCoCo (couverture de tests).
- `Jenkinsfile` : etapes `SonarQube Analysis` puis `Quality Gate` (le pipeline echoue si la porte qualite echoue).

Lancer une analyse a la main : `./mvnw clean verify sonar:sonar -Dsonar.login=<jeton>`
