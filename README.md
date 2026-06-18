# TP-test

Application Kotlin Spring Boot organisée selon une architecture en couches.

## Structure du projet

- `src/main/kotlin/com/example/tptest`
  - `domain`
    - `model` : entités métier (`Book`)
    - `exception` : exceptions métier sans dépendance Spring
    - `port` : interfaces du domaine (`IBookPort`)
    - `usecase` : logique métier (`BookUseCase`)
  - `infrastructure/driven/postgres` : implémentation de persistance PostgreSQL
  - `infrastructure/driving/controller` : API REST et gestion des exceptions
  - `infrastructure/application` : configuration des use cases
- `src/main/resources` : ressources Spring Boot
- `compose.yaml` : configuration Docker Compose pour PostgreSQL
- `build.gradle.kts` : configuration Gradle

## Fonctionnalités

- API REST de gestion de livres
- Création de livres
- Réservation et déréservation de livres
- Gestion métier d’erreurs :
  - `BookNotFoundException` → 404
  - `BookAlreadyReservedException` → 412
  - `BookNotReservedException` → 412

## Configuration

Le projet utilise Spring Boot et PostgreSQL.

### Variables de configuration

- `application.yaml`
  - `spring.liquibase.change-log` : `classpath:/db/changelog.xml`
  - `spring.datasource.url` : `jdbc:postgresql://localhost:5432/postgres`
  - `spring.datasource.username` : `user`
  - `spring.datasource.password` : `password`

### Docker Compose

Le service PostgreSQL est défini dans `compose.yaml` :

- image : `postgres:latest`
- utilisateur : `user`
- mot de passe : `password`
- base de données : `book_db`
- port exposé : `5432`

## Exécution

### Démarrer le service PostgreSQL

```bash
./gradlew bootRun
```

ou démarrez manuellement Docker Compose :

```bash
docker compose up -d
```

### Lancer l’application

```bash
./gradlew bootRun
```

## Tests

- `./gradlew test` : tests unitaires
- `./gradlew testIntegration` : tests d’intégration
- `./gradlew testComponent` : tests composants (Cucumber + REST Assured)
- `./gradlew testArchitecture` : tests d’architecture (ArchUnit)
- `./gradlew jacocoTestReport` : rapport de couverture
- `./gradlew detekt` : analyse statique de code

## Endpoints principaux

- `GET /books` : liste des livres
- `POST /books` : création d’un livre
- `POST /books/reserve?bookId={id}` : réservation d’un livre
- `POST /books/dereserve?bookId={id}` : déréservation d’un livre

## Remarques

- Le domaine ne dépend pas de Spring.
- Les exceptions métier sont traduites en codes HTTP dans l’infrastructure.

## Prérequis

- JDK 21
- Gradle Wrapper (fourni)
- Docker/Docker Compose pour PostgreSQL
