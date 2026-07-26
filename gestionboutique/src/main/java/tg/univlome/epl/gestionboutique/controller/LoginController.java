package tg.univlome.epl.gestionboutique.controller;

// App est dans le meme package, pas besoin d'import
import tg.univlome.epl.gestionboutique.dao.UtilisateurDAO;
import tg.univlome.epl.gestionboutique.model.Utilisateur;
import tg.univlome.epl.gestionboutique.util.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import tg.univlome.epl.gestionboutique.App;

public class LoginController {

    @FXML private TextField txtIdentifiant;
    @FXML private PasswordField txtMotDePasse;
    @FXML private Label lblErreur;

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @FXML
    private void handleConnexion() {
        String identifiant = txtIdentifiant.getText().trim();
        String motDePasse = txtMotDePasse.getText();

        if (identifiant.isEmpty() || motDePasse.isEmpty()) {
            lblErreur.setText("Veuillez remplir tous les champs.");
            return;
        }

        Utilisateur u = utilisateurDAO.authentifier(identifiant, motDePasse);
        if (u == null) {
            lblErreur.setText("Identifiant ou mot de passe incorrect.");
            return;
        }

        Session.setUtilisateurConnecte(u);

        try {
            App.changerScene("/fxml/MainLayout.fxml", "GestionBoutique — Tableau de bord");
        } catch (Exception e) {
            e.printStackTrace();
            lblErreur.setText("Erreur lors du chargement de l'application.");
        }
    }
}
