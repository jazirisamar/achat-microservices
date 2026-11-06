# Nexus

Serveur : http://192.168.56.10:8081/

## Configuration Nexus
- Depots `maven-releases` et `maven-snapshots` (crees par defaut).
- Le projet est en version `1.0` (une release) : dans `maven-releases`, politique de deploiement **Allow redeploy**, sinon un deuxieme deploiement de la meme version est refuse.
- Creer un utilisateur de deploiement avec le droit `nx-repository-view-maven2-*-*`.

## Configuration Jenkins
Identifiants -> **Username with password**, ID `nexus-credentials`.

## Dans le projet
- `pom.xml` : section `distributionManagement` (URLs des depots Nexus).
- `ci/settings.xml` : lit `NEXUS_USER` et `NEXUS_PASS`, fournis par Jenkins. Aucun mot de passe dans le depot.
- `Jenkinsfile` : etape `Publish to Nexus`, uniquement sur les branches `dev` et `main`.

Les adresses des serveurs sont des valeurs d'exemple (machine virtuelle 192.168.56.10) : a adapter si elles different.
