# ShopManagement — Application de Gestion d'une Boutique Généraliste
Application Desktop de gestion de boutique généraliste (ventes, stock, clients) développée en JavaFX avec base de données SQLite

> Projet académique — Filière LF Ingénierie des Systèmes (IS), Semestre 6
> Année Universitaire 2025–2026

Conception et développement d'une application desktop de gestion complète
d'une boutique généraliste (produits, stocks, ventes, utilisateurs),
fonctionnant hors-ligne avec une base de données embarquée.

---

## Équipe

| Nom                        | Rôle                                       |
|-----------------------------|---------------------------------------------|
|                              | Authentification & Utilisateurs, base de données |
| SEGNEDJI Komivi Emmanuel     | Gestion des produits & catégories            |
| KPELOU Presam                | Gestion des stocks & alertes,  Ventes, facturation & tableau de bord               |

**Encadrants :** M. ZOMBLEOU Firmin, M. ALLAH-ASSOGBA Frédéric

---

## Architecture du Système
JavaFX (Vue/FXML) ←→ Contrôleurs Java (MVC) ←→ DAO / JDBC ←→ SQLite (BDD embarquée) ↑ BCrypt (sécurité mots de passe)

## Stack Technique

| Composant           | Technologie              |
|----------------------|---------------------------|
| Langage              | Java 17                   |
| Interface graphique   | JavaFX 21 + FXML (Scene Builder) |
| Base de données       | SQLite (embarquée)        |
| Connecteur BDD        | JDBC (sqlite-jdbc)         |
| Sécurité mots de passe| jBCrypt                    |
| Build / dépendances   | Maven                      |
| IDE recommandé        | NetBeans                   |

---

## Structure du Projet





---

## Modules développés

| # | Module                         | Responsable                | Description                              |
|---|----------------------------------|------------------------------|-------------------------------------------|
| 1 | Authentification & Utilisateurs | [Ton nom]                   | Connexion, rôles Administrateur/Vendeur   |
| 2 | Gestion des produits             | SEGNEDJI Komivi Emmanuel     | CRUD produits, catégories                 |
| 3 | Gestion des stocks               | KPELOU Presam                 | Alertes de seuil, mise à jour transactionnelle |
| 4 | Ventes & Facturation             | GNIGMA Koffi Bruno              | Panier, calcul du total, reçu de vente    |
| 5 | Tableau de bord & Rapports        | À répartir                  | Statistiques, graphiques, export CSV      |

---

## Installation et Lancement

> Prérequis : JDK 17+, Maven, NetBeans (ou IntelliJ avec support Maven)

```bash
git clone https://github.com/ton-nom-utilisateur/ShopManagement.git
cd ShopManagement
mvn clean install
mvn javafx:run
```

---

## Documentation

Voir le dossier [`docs/`](./docs) :
- Cahier des charges (v1.0)
- Diagramme de cas d'utilisation UML
- Diagramme de classes UML
- Support de soutenance
