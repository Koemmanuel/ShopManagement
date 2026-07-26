package tg.univlome.epl.gestionboutique.controller;

import tg.univlome.epl.gestionboutique.dao.UtilisateurDAO;
import tg.univlome.epl.gestionboutique.model.Utilisateur;
import tg.univlome.epl.gestionboutique.util.Session;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class UtilisateursController {

    @FXML private TextField txtNomComplet;
    @FXML private TextField txtIdentifiant;
    @FXML private PasswordField txtMotDePasse;
    @FXML private ComboBox<Utilisateur.Role> cmbRole;

    @FXML private TableView<Utilisateur> tableUtilisateurs;
    @FXML private TableColumn<Utilisateur, String> colNomComplet;
    @FXML private TableColumn<Utilisateur, String> colIdentifiant;
    @FXML private TableColumn<Utilisateur, Utilisateur.Role> colRole;
    @FXML private TableColumn<Utilisateur, Void> colActions;

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @FXML
    public void initialize() {
        cmbRole.setItems(FXCollections.observableArrayList(Utilisateur.Role.values()));
        cmbRole.getSelectionModel().select(Utilisateur.Role.VENDEUR);

        colNomComplet.setCellValueFactory(new PropertyValueFactory<>("nomComplet"));
        colIdentifiant.setCellValueFactory(new PropertyValueFactory<>("identifiant"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        colActions.setCellFactory(col -> new TableCell<>() {
            private final Button btnSupprimer = new Button("Supprimer");
            {
                btnSupprimer.getStyleClass().add("btn-danger");
                btnSupprimer.setOnAction(e -> {
                    Utilisateur u = getTableView().getItems().get(getIndex());
                    supprimer(u);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                Utilisateur u = empty ? null : getTableView().getItems().get(getIndex());
                boolean estSoiMeme = u != null && Session.getUtilisateurConnecte() != null
                        && u.getId() == Session.getUtilisateurConnecte().getId();
                setGraphic(empty || estSoiMeme ? null : btnSupprimer);
            }
        });

        rafraichir();
    }

    private void rafraichir() {
        tableUtilisateurs.setItems(FXCollections.observableArrayList(utilisateurDAO.getAll()));
    }

    @FXML
    private void handleAjouter() {
        String nom = txtNomComplet.getText().trim();
        String identifiant = txtIdentifiant.getText().trim();
        String motDePasse = txtMotDePasse.getText();
        Utilisateur.Role role = cmbRole.getValue();

        if (nom.isEmpty() || identifiant.isEmpty() || motDePasse.isEmpty() || role == null) {
            afficherAlerte(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }
        if (motDePasse.length() < 4) {
            afficherAlerte(Alert.AlertType.WARNING, "Mot de passe trop court", "Le mot de passe doit contenir au moins 4 caractères.");
            return;
        }

        boolean ok = utilisateurDAO.ajouter(nom, identifiant, motDePasse, role);
        if (ok) {
            txtNomComplet.clear();
            txtIdentifiant.clear();
            txtMotDePasse.clear();
            rafraichir();
        } else {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Cet identifiant existe peut-être déjà.");
        }
    }

    private void supprimer(Utilisateur u) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Supprimer l'utilisateur " + u.getNomComplet() + " ?",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                utilisateurDAO.supprimer(u.getId());
                rafraichir();
            }
        });
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
