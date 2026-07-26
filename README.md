# GestionBoutique

Application de bureau JavaFX pour la gestion d'une boutique : produits, stock, ventes, utilisateurs et historique.

**Auteur :** Étudiante à l'École Polytechnique de Lomé (EPL)
**Package racine :** `tg.univlome.epl.gestionboutique`

---

## Sommaire

- [Aperçu](#aperçu)
- [Fonctionnalités](#fonctionnalités)
- [Technologies utilisées](#technologies-utilisées)
- [Installation rapide (utilisateur final)](#installation-rapide-utilisateur-final)
- [Lancer le projet en développement](#lancer-le-projet-en-développement)
- [Structure du projet](#structure-du-projet)
- [Identifiants par défaut](#identifiants-par-défaut)
- [Documentation complète](#documentation-complète)

---

## Aperçu

GestionBoutique est une application desktop développée en **JavaFX 21** avec **Maven** et une base de données embarquée **SQLite**. Elle permet de gérer au quotidien les opérations d'une petite boutique : catalogue produits, ventes, alertes de stock, historique et gestion des utilisateurs.

## Fonctionnalités

- Authentification sécurisée (mots de passe hashés avec BCrypt)
- Gestion des produits (ajout, modification, suppression, recherche)
- Gestion du stock avec alertes de seuil bas
- Enregistrement des ventes (nouvelle vente, historique)
- Export des données (CSV / Excel)
- Impression de reçus de vente
- Gestion des utilisateurs (rôles ADMIN / VENDEUR)
- Interface moderne avec icônes vectorielles (Ikonli / Feather)

## Technologies utilisées

| Composant       | Technologie          |
|-----------------|-----------------------|
| Langage         | Java 21             |
| Interface       | JavaFX 21             |
| Build           | Maven                 |
| Base de données | SQLite                |
| Sécurité        | jBCrypt               |
| Icônes          | Ikonli (Feather Pack) |
| Packaging       | jpackage + Inno Setup |

## Installation rapide (utilisateur final)

Si vous avez reçu l'installeur Windows :

1. Ouvrez le dossier `installeur/`
2. Lancez `GestionBoutique-Setup-1.0.exe`
3. Suivez l'assistant d'installation
4. Lancez l'application depuis le raccourci créé sur le Bureau ou le menu Démarrer

Aucune installation de Java n'est requise : le runtime est embarqué dans l'installeur.

➡️ Pour plus de détails, voir le **[Guide d'installation et d'utilisation](GUIDE_INSTALLATION_UTILISATION.md)**.

## Lancer le projet en développement

Prérequis : JDK 21, Maven 3.9.16.

```bash
# Cloner ou extraire le projet, puis se placer dans le dossier
cd gestionboutique

# Lancer l'application
mvn clean javafx:run
```

## Structure du projet

```  ├──ShopManagement
     |  ├──gestionboutique/
     |  ├── src/main/java/tg/univlome/epl/gestionboutique/
     |  │   ├── App.java                 # Point d'entrée JavaFX
     |  │   ├── Launcher.java            # Classe de lancement (jar exécutable)
     |  │   ├── controller/              # Contrôleurs FXML
     |  │   ├── dao/                     # Accès aux données (SQLite)
     |  │   ├── model/                   # Modèles métier (Produit, Vente, Utilisateur...)
     |  │   └── util/                    # DatabaseManager, Session
     |  ├── src/main/resources/
     |  │   ├── fxml/                    # Vues FXML
     |  │   ├── css/                     # Feuilles de style
     |  │   └── images/                  # Logo et icônes
     |  ├── database/                    # Fichier boutique.db (SQLite)
     |  ├── GestionBoutique/              # Image d'application générée par jpackage
     |  ├── installeur/                  # Installeur Windows (Inno Setup)
     |  └── pom.xml
     └──README.md   
```

## Identifiants par défaut

Au premier lancement, un compte administrateur est créé automatiquement :

| Champ         | Valeur     |
|---------------|------------|
| Identifiant   | `admin`    |
| Mot de passe  | `admin123` |

⚠️ Il est recommandé de changer ce mot de passe après la première connexion (menu Utilisateurs).

## Documentation complète

Voir le fichier **[GUIDE_INSTALLATION_UTILISATION.md](GUIDE_INSTALLATION_UTILISATION.md)** pour :
- l'installation détaillée (utilisateur final et environnement de développement)
- le guide d'utilisation pas à pas de chaque module de l'application
- le dépannage des problèmes courants

