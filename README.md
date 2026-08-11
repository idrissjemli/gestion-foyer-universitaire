# Gestion de foyers universitaires

Application web de gestion d'hébergement étudiant : foyers, blocs, chambres,
étudiants et réservations. Développée avec Spring Boot et MySQL, avec
authentification par rôle, règles métier côté serveur et tests unitaires.

---

## Aperçu

**Connexion** — authentification par rôle, mots de passe chiffrés avec BCrypt

![Page de connexion](docs/login.png)

**Interface d'administration** — CRUD complet sur les 6 entités

![Interface de gestion](docs/apercu.png)

---

## Fonctionnalités

- **Authentification et rôles** — Spring Security, deux profils : ADMIN et ETUDIANT
- **CRUD complet** sur les 6 entités : créer, consulter, modifier, supprimer
- **API REST** — 28 endpoints
- **Interface web intégrée** — navigation par onglets, formulaires adaptés à
  chaque entité, listes déroulantes alimentées dynamiquement pour les relations
- **Interface adaptée au rôle** — un étudiant ne voit ni les formulaires ni les
  actions de modification
- **Règles métier** appliquées côté serveur, pas seulement dans l'interface
- **Gestion des erreurs** centralisée : chaque violation renvoie un statut HTTP
  approprié et un message lisible
- **Gestion des comptes** réservée aux administrateurs
- **Tests unitaires** couvrant les règles métier

---

## Sécurité

| Aspect | Mise en œuvre |
|---|---|
| Authentification | Spring Security, formulaire de connexion personnalisé |
| Mots de passe | Chiffrés avec BCrypt, jamais renvoyés dans les réponses JSON |
| Lecture (`GET`) | Accessible aux rôles ADMIN et ETUDIANT |
| Écriture (`POST`, `PUT`, `DELETE`) | Réservée au rôle ADMIN |
| Gestion des comptes | `/utilisateur/**` réservé au rôle ADMIN |
| Protection CSRF | Active sur toutes les routes de modification, par cookie |
| Garde-fou | Le dernier compte administrateur ne peut pas être supprimé |

Les restrictions sont appliquées **côté serveur**. L'interface masque
simplement ce qui est interdit ; contourner l'affichage ne donne aucun accès.

### Comptes de démonstration

```
admin     / admin123      rôle ADMIN
etudiant  / etudiant123   rôle ETUDIANT
```

Ces comptes sont créés au premier démarrage si la table est vide. À remplacer
dans tout usage réel.

---

## Règles métier

| Règle | Comportement |
|---|---|
| Capacité d'un bloc | Doit être strictement positive |
| Numéro de chambre | Unique à l'intérieur d'un même bloc |
| Réservation | Une chambre ne peut être réservée deux fois la même année universitaire |
| Capacité d'une chambre | Le nombre de réservations actives ne peut dépasser le type : SIMPLE 1, DOUBLE 2, TRIPLE 3 |
| Nom d'utilisateur | Unique |
| Dernier administrateur | Sa suppression est refusée |

Toute violation lève une `BusinessException`, interceptée par un
`@RestControllerAdvice` qui renvoie un **HTTP 400** accompagné d'un message
explicite, affiché directement dans l'interface.

---

## Stack technique

**Backend**
`Java 17` · `Spring Boot 3.3.4` · `Spring Web` · `Spring Security` ·
`Spring Data JPA` · `Hibernate 6.5` · `Lombok` · `Maven`

**Base de données**
`MySQL 8.4` — schéma généré automatiquement par Hibernate

**Frontend**
`HTML` · `CSS` · `JavaScript` — sans framework, appels via `fetch()`

**Tests**
`JUnit 5` · `Mockito`

---

## Architecture

```
Navigateur
    │  HTTP · JSON
    ▼
Spring Security   authentification, autorisation par rôle
    │
    ▼
Controller        reçoit la requête, valide le format
    │
    ▼
Service           applique les règles métier
    │
    ▼
Repository        accès aux données (Spring Data JPA)
    │
    ▼
Hibernate         traduction objet → SQL
    │
    ▼
MySQL
```

Architecture en couches : chaque couche ne communique qu'avec sa voisine
immédiate. Les services sont accessibles par interface, ce qui permet de les
simuler dans les tests.

---

## Modèle de données

```
Universite ──1:1── Foyer
                     │
                    1:N
                     │
                   Bloc
                     │
                    1:N
                     │
                  Chambre ──1:N── Reservation ──N:M── Etudiant

Utilisateur (username, password BCrypt, role)
```

Les relations bidirectionnelles portent `@JsonIgnore` du côté de la
collection, afin d'éviter les cycles infinis lors de la sérialisation JSON.

---

## API REST

Le même schéma s'applique aux 6 entités métier : `foyer`, `bloc`, `chambre`,
`etudiant`, `reservation`, `universite`.

| Méthode | Route | Rôle requis |
|---|---|---|
| `GET` | `/{entite}/all` | ADMIN ou ETUDIANT |
| `POST` | `/{entite}/add` | ADMIN |
| `PUT` | `/{entite}/update` | ADMIN |
| `DELETE` | `/{entite}/delete/{id}` | ADMIN |
| `GET` | `/api/me` | Authentifié |
| `GET` `POST` `DELETE` | `/utilisateur/**` | ADMIN |

Exemple :

```bash
curl -u admin:admin123 http://localhost:8083/chambre/all
```

---

## Tests

```bash
./mvnw test
```

9 tests unitaires vérifient les règles métier, sans base de données ni serveur :
les repositories sont simulés avec Mockito.

```
BlocServiceTest           capacité nulle, capacité négative, cas valide
ChambreServiceTest        numéro déjà pris, numéro libre, même numéro autre bloc
ReservationServiceTest    doublon d'année, dépassement de capacité, cas valide
```

---

## Installation

### Prérequis

```
Java 17
MySQL 8
```

### Étapes

```bash
git clone https://github.com/idrissjemli/gestion-foyer-universitaire.git
cd gestion-foyer-universitaire
```

Définir le mot de passe MySQL en variable d'environnement :

```powershell
$env:MYSQL_PASSWORD = "votre_mot_de_passe"
```

```bash
./mvnw spring-boot:run
```

La base `springDoss`, ses tables et les deux comptes de démonstration sont
créés automatiquement au premier démarrage. Aucun script SQL à exécuter.

Ouvrir ensuite **http://localhost:8083**

### Configuration

`src/main/resources/application.properties` ne contient aucun identifiant en
clair. Le mot de passe est lu depuis la variable d'environnement
`MYSQL_PASSWORD`.

---

## Reprise et corrections

Ce projet a été initialement réalisé dans le cadre du cursus ESPRIT, puis
repris et complété. L'état d'origine ne démarrait pas. Correctifs apportés :

| Problème | Correction |
|---|---|
| 3 annotations `mappedBy` référençaient des champs inexistants | Noms de champs corrigés, respect de la convention Java |
| `UniversiteService` et `ReservationService` non implémentés — toutes les méthodes retournaient `null` | Repositories injectés, méthodes implémentées |
| `ReservationRepository` étendait `JpaRepository<Etudiant, Long>` | Corrigé vers l'entité `Reservation` |
| Relation `Chambre → Bloc` inversée : une chambre contenait des blocs | Relation remise dans le bon sens |
| Aucun lien entre `Reservation` et `Chambre` — impossible de savoir qui occupe quelle chambre | Relation `@ManyToOne` ajoutée |
| Cycles de sérialisation Jackson provoquant un JSON invalide | `@JsonIgnore` appliqué côté collection |
| `ChambreController` vide, aucune couche web | 6 controllers REST écrits |
| Aucune règle métier, aucun test | 6 règles et 9 tests unitaires ajoutés |
| Aucune authentification | Spring Security avec rôles et BCrypt |

---

## Limites connues et pistes d'évolution

- **Protection CSRF désactivée sur `/login`** — contournement du chargement
  différé du jeton avec une page de connexion statique. La solution propre
  consiste à servir `login.html` via Thymeleaf, qui injecte le jeton
  côté serveur.
- **`Utilisateur` et `Etudiant` ne sont pas reliés** — un étudiant connecté ne
  peut donc pas créer ni consulter ses propres réservations. Le lien entre les
  deux entités permettrait un filtrage par utilisateur.
- Pagination et recherche sur les listes
- Tests d'intégration sur les controllers avec `@SpringBootTest`
- Export des réservations

---

## Auteur

**Idriss Jemli** — Ingénieur Data & BI
[LinkedIn](https://www.linkedin.com/in/idriss-jemli-892068218) ·
[GitHub](https://github.com/idrissjemli)