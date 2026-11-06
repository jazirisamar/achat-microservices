# Achat - architecture micro-services

Application Spring Boot (Java 8) decoupee en micro-services a partir du monolithe `achat`.

| Composant | Role | Port | Base MySQL |
|---|---|---|---|
| `eureka-server` | Registre : les services s'y declarent et se retrouvent | 8761 | - |
| `api-gateway` | Point d'entree unique, verifie le JWT, redirige vers les services | 8080 | - |
| `auth-service` | Inscription, connexion, emission du JWT | 8084 | `authdb` |
| `product-service` | Stock, Produit, CategorieProduit | 8081 | `productdb` |
| `supplier-service` | Fournisseur, DetailFournisseur, SecteurActivite | 8082 | `supplierdb` |
| `invoice-service` | Facture, DetailFacture, Reglement, Operateur | 8083 | `invoicedb` |

Un service ne lit jamais la base d'un autre : `invoice-service` garde seulement l'identifiant du produit
(`produitId`) et du fournisseur (`fournisseurId`), et interroge `product-service` par REST (OpenFeign).

## Lancer avec Docker

```
docker compose up --build
```

Eureka : http://localhost:8761 - Gateway : http://localhost:8080

## Lancer sans Docker

MySQL local sur le port 3306 (utilisateur `root`), puis dans cet ordre :
`eureka-server`, `auth-service`, `product-service`, `supplier-service`, `invoice-service`, `api-gateway`.

```
./mvnw -pl eureka-server spring-boot:run
```

## Tester (PowerShell)

```
Invoke-RestMethod -Method Post -Uri http://localhost:8080/auth/register -ContentType "application/json" -Body '{"username":"samar","password":"1234"}'
$t = (Invoke-RestMethod -Method Post -Uri http://localhost:8080/auth/login -ContentType "application/json" -Body '{"username":"samar","password":"1234"}').token
Invoke-RestMethod -Uri http://localhost:8080/stock/retrieve-all-stocks -Headers @{Authorization="Bearer $t"}
```

Sans jeton, la gateway repond 401 (hors `/auth/**`).

## Routes de la gateway

| Chemin | Service |
|---|---|
| `/auth/**` | auth-service |
| `/stock/**`, `/produit/**`, `/categorieProduit/**` | product-service |
| `/fournisseur/**`, `/secteurActivite/**` | supplier-service |
| `/facture/**`, `/reglement/**`, `/operateur/**` | invoice-service |

## Configuration et secrets

Copier `.env.example` en `.env` (ignore par Git) avant `docker compose up`.

Rien de secret dans le depot. Les valeurs sensibles se passent par variables d'environnement :
`JWT_SECRET` (au moins 32 caracteres, identique pour `auth-service` et `api-gateway`),
`DB_URL`, `DB_USER`, `DB_PASSWORD`, `EUREKA_URL`, `MYSQL_ROOT_PASSWORD`.
La valeur par defaut de `JWT_SECRET` est un exemple de developpement : a remplacer en production.

## CI / CD

- `ci.yml` : build et tests de tous les modules a chaque push et pull request (tests sur H2, sans MySQL).
- `integration.yml` : apres chaque merge dans `main`, build global puis un job par service.
