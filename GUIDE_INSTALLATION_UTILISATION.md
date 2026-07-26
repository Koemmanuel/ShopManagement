# Guide d'installation et d'utilisation — GestionBoutique

## 1. Présentation

**GestionBoutique** est une application de bureau développée en JavaFX permettant de gérer les activités courantes d'une boutique : produits, stock, ventes, historique et utilisateurs. Elle fonctionne hors-ligne grâce à une base de données SQLite embarquée.

---

## 2. Installation

### 2.1. Installation pour un utilisateur final (Windows)

C'est la méthode recommandée si vous voulez simplement **utiliser** l'application sans configurer d'environnement de développement.

**Étapes :**

1. Récupérez le fichier `installeur/GestionBoutique-Setup-1.0.exe`.
2. Double-cliquez dessus pour lancer l'assistant d'installation.
3. Choisissez la langue (français disponible).
4. Suivez les étapes de l'assistant :
   - Dossier d'installation (par défaut : `Program Files\GestionBoutique`)
   - Option « Créer un raccourci sur le Bureau »
5. Cliquez sur **Installer**, puis **Terminer**.
6. Lancez l'application depuis le raccourci créé (Bureau ou menu Démarrer → GestionBoutique).

**Prérequis :** Windows 10/11 (64 bits). Aucun besoin d'installer Java séparément : le runtime Java est embarqué dans l'application grâce à `jpackage`.

**Désinstallation :** menu Démarrer → GestionBoutique → *Désinstaller GestionBoutique*, ou via *Applications installées* dans les Paramètres Windows.

### 2.2. Installation via l'image d'application (sans installeur)

Si vous préférez ne pas passer par l'installeur, vous pouvez utiliser directement le dossier `GestionBoutique/` généré par `jpackage` :

1. Copiez tout le dossier `GestionBoutique/` où vous le souhaitez.
2. Double-cliquez sur `GestionBoutique.exe` à l'intérieur du dossier.

Ce dossier contient déjà tout le nécessaire (application + runtime Java + base de données).

### 2.3. Installation pour le développement (avec le code source)

**Prérequis :**
- JDK 21
- Maven 3.9.16
- Un IDE NetBeans 
-Git

**Étapes :**

```bash
# 1. Extraire ou cloner le projet
cd gestionboutique

# 2. Compiler le projet
mvn clean install

# 3. Lancer l'application en mode développement
mvn clean javafx:run
```

Dans NetBeans : ouvrir le projet (`File > Open Project`), puis clic droit sur le projet → **Run**.

> 💡 Astuce : si vous rencontrez des erreurs Maven liées à la synchronisation OneDrive, déplacez le projet en dehors d'un dossier synchronisé (ex. `C:\Dev\gestionboutique`).

### 2.4. Régénérer l'exécutable et l'installeur (optionnel)

Pour reconstruire l'application Windows à partir du code source :

```bash
# 1. Générer le jar exécutable (fat jar)
mvn clean package

# 2. Générer l'image d'application avec jpackage
jpackage --type app-image --name GestionBoutique ^
  --input target ^
  --main-jar gestionboutique-1.0-SNAPSHOT.jar ^
  --main-class tg.univlome.epl.gestionboutique.Launcher ^
  --icon src/main/resources/images/logo.ico

# 3. Compiler l'installeur avec Inno Setup
# Ouvrir "installer.iss" dans Inno Setup Compiler, puis Build
```

---

## 3. Premier lancement

Au tout premier démarrage, l'application :
1. Crée automatiquement le dossier `database/` s'il n'existe pas.
2. Initialise la base de données SQLite (`boutique.db`) et ses tables.
3. Crée un compte administrateur par défaut.

### Identifiants par défaut

| Champ        | Valeur     |
|--------------|------------|
| Identifiant  | `admin`    |
| Mot de passe | `admin123` |

⚠️ **Important :** changez ce mot de passe dès la première connexion via le module *Utilisateurs*, surtout si l'application est partagée.

---

## 4. Guide d'utilisation

### 4.1. Connexion

Sur l'écran de connexion, saisissez votre **identifiant** et votre **mot de passe**, puis cliquez sur **Se connecter**. En cas d'erreur, un message s'affiche (champs vides ou identifiants incorrects).

### 4.2. Tableau de bord (Dashboard)

Après connexion, vous accédez au tableau de bord qui donne une vue d'ensemble : indicateurs clés (ventes, stock, alertes), et accès rapide aux différents modules via le menu latéral.

### 4.3. Gestion des produits

Menu **Produits** :
- **Ajouter un produit** : nom, catégorie, prix d'achat, prix de vente, quantité en stock, seuil d'alerte.
- **Modifier / Supprimer** un produit existant depuis le tableau.
- **Rechercher** un produit par nom via la barre de recherche.
- **Exporter** la liste des produits au format CSV ou Excel.
- Le **statut** du stock est indiqué visuellement (stock normal / stock bas).

### 4.4. Alertes de stock

Menu **Alertes de stock** : liste automatiquement les produits dont la quantité en stock est inférieure ou égale à leur seuil d'alerte, pour anticiper les réapprovisionnements.

### 4.5. Nouvelle vente

Menu **Nouvelle vente** :
1. Sélectionnez un ou plusieurs produits à vendre.
2. Indiquez les quantités.
3. Le total se calcule automatiquement.
4. Validez la vente : le stock des produits concernés est automatiquement décrémenté.
5. Possibilité d'**imprimer un reçu** de la vente.

### 4.6. Historique des ventes

Menu **Historique** : consultez la liste de toutes les ventes passées (date, montant, vendeur), avec possibilité d'export et de consultation du détail de chaque vente.

### 4.7. Gestion des utilisateurs (Admin uniquement)

Menu **Utilisateurs** (réservé au rôle ADMIN) :
- Créer de nouveaux comptes (rôle ADMIN ou VENDEUR)
- Modifier ou désactiver un compte existant
- Changer les mots de passe

### 4.8. Déconnexion

Utilisez l'option de déconnexion dans le menu principal pour revenir à l'écran de connexion, notamment si plusieurs personnes utilisent le même poste.

---

## 5. Sauvegarde des données

Toutes les données sont stockées dans le fichier :

```
database/boutique.db
```

Pour sauvegarder vos données, il suffit de copier ce fichier régulièrement. Pour restaurer une sauvegarde, remplacez le fichier `boutique.db` par la copie sauvegardée (application fermée).

---

## 6. Dépannage

| Problème | Cause probable | Solution |
|----------|-----------------|----------|
| L'application ne démarre pas | Runtime Java corrompu ou fichiers manquants | Réinstaller via `GestionBoutique-Setup-1.0.exe` |
| Écran blanc / erreur au démarrage | Fichier FXML introuvable ou base de données corrompue | Vérifier la présence du dossier `database/`, réinstaller si besoin |
| Identifiant/mot de passe refusé | Mauvaise saisie ou compte inexistant | Utiliser `admin` / `admin123` par défaut, ou contacter l'administrateur |
| Erreur Maven en développement | Projet situé dans un dossier OneDrive | Déplacer le projet hors de OneDrive (ex. `C:\Dev\`) |
| Export CSV/Excel ne fonctionne pas | Fichier déjà ouvert dans un autre programme | Fermer le fichier avant l'export |

---

## 7. Contact et support

Ce projet a été réalisé dans le cadre d'un projet académique à l'**École Polytechnique de Lomé (EPL)**, filière Informatique et Systèmes.

Package du projet : `tg.univlome.epl.gestionboutique`
