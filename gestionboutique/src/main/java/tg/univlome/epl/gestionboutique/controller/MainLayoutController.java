package tg.univlome.epl.gestionboutique.controller;

// App est dans le meme package, pas besoin d'import
import tg.univlome.epl.gestionboutique.util.Session;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import tg.univlome.epl.gestionboutique.App;

public class MainLayoutController {

    @FXML private StackPane contentArea;
    @FXML private Label lblNomUtilisateur;
    @FXML private Label lblRoleUtilisateur;

    @FXML private Button btnDashboard;
    @FXML private Button btnProduits;
    @FXML private Button btnVentes;
    @FXML private Button btnHistorique;
    @FXML private Button btnStock;
    @FXML private Button btnUtilisateurs;

    @FXML
    public void initialize() {
        var user = Session.getUtilisateurConnecte();
        if (user != null) {
            lblNomUtilisateur.setText(user.getNomComplet());
            lblRoleUtilisateur.setText(user.isAdmin() ? "Administrateur" : "Vendeur");

            // Le vendeur n'a pas accès à la gestion des utilisateurs
            if (!user.isAdmin()) {
                btnUtilisateurs.setVisible(false);
                btnUtilisateurs.setManaged(false);
            }
        }
        showDashboard();
    }

    private void setActiveButton(Button actif) {
        for (Button b : new Button[]{btnDashboard, btnProduits, btnVentes, btnHistorique, btnStock, btnUtilisateurs}) {
            b.getStyleClass().remove("nav-button-active");
        }
        actif.getStyleClass().add("nav-button-active");
    }

    private void charger(String fxml, Button bouton) {
        try {
            java.net.URL url = App.resolveResource(fxml);
            if (url == null) {
                throw new RuntimeException("Fichier FXML introuvable : " + fxml);
            }
            FXMLLoader loader = new FXMLLoader(url);
            Node node = loader.load();

            node.setOpacity(0);
            contentArea.getChildren().setAll(node);
            FadeTransition fade = new FadeTransition(Duration.millis(220), node);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();

            if (bouton != null) setActiveButton(bouton);
        } catch (Exception e) {
            e.printStackTrace();
            afficherErreur("Impossible d'ouvrir cette page", e);
        }
    }

    private void afficherErreur(String titre, Exception e) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(titre);
        alert.setContentText(e.getClass().getSimpleName() + " : " + e.getMessage());
        alert.showAndWait();
    }

    @FXML private void showDashboard() { charger("/fxml/Dashboard.fxml", btnDashboard); }
    @FXML private void showProduits() { charger("/fxml/Produits.fxml", btnProduits); }
    @FXML private void showVentes() { charger("/fxml/NouvelleVente.fxml", btnVentes); }
    @FXML private void showHistorique() { charger("/fxml/Historique.fxml", btnHistorique); }
    @FXML private void showStock() { charger("/fxml/AlerteStock.fxml", btnStock); }
    @FXML private void showUtilisateurs() { charger("/fxml/Utilisateurs.fxml", btnUtilisateurs); }

    @FXML
    private void handleDeconnexion() {
        Session.deconnexion();
        try {
            App.changerScene("/fxml/Login.fxml", "GestionBoutique — Connexion");
        } catch (Exception e) {
            e.printStackTrace();
            afficherErreur("Impossible de revenir à l'écran de connexion", e);
        }
    }
}