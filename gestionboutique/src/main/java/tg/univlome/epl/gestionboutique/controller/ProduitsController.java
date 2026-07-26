package tg.univlome.epl.gestionboutique.controller;

import tg.univlome.epl.gestionboutique.dao.ProduitDAO;
import tg.univlome.epl.gestionboutique.model.Produit;
import tg.univlome.epl.gestionboutique.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.util.Locale;

public class ProduitsController {

    @FXML private TextField txtRecherche;
    @FXML private TableView<Produit> tableProduits;
    @FXML private TableColumn<Produit, String> colNom;
    @FXML private TableColumn<Produit, String> colCategorie;
    @FXML private TableColumn<Produit, Double> colPrixAchat;
    @FXML private TableColumn<Produit, Double> colPrixVente;
    @FXML private TableColumn<Produit, Integer> colStock;
    @FXML private TableColumn<Produit, Void> colStatut;
    @FXML private TableColumn<Produit, Void> colActions;

    private final ProduitDAO produitDAO = new ProduitDAO();
    private final ObservableList<Produit> donnees = FXCollections.observableArrayList();
    private final NumberFormat format = NumberFormat.getNumberInstance(Locale.FRANCE);

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));

        colPrixAchat.setCellValueFactory(new PropertyValueFactory<>("prixAchat"));
        colPrixAchat.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : format.format(item) + " FCFA");
            }
        });

        colPrixVente.setCellValueFactory(new PropertyValueFactory<>("prixVente"));
        colPrixVente.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : format.format(item) + " FCFA");
            }
        });

        colStock.setCellValueFactory(new PropertyValueFactory<>("quantiteStock"));

        colStatut.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }
                Produit p = (Produit) getTableRow().getItem();
                Label tag = new Label(p.isStockFaible() ? "Stock faible" : "Disponible");
                tag.getStyleClass().add(p.isStockFaible() ? "tag-danger" : "tag-success");
                setGraphic(tag);
            }
        });

        colActions.setCellFactory(col -> new TableCell<>() {
            private final Button btnModifier = new Button("✏️");
            private final Button btnSupprimer = new Button("🗑️");
            private final HBox box = new HBox(8, btnModifier, btnSupprimer);
            {
                btnModifier.getStyleClass().add("btn-secondary");
                btnSupprimer.getStyleClass().add("btn-danger");
                btnModifier.setOnAction(e -> ouvrirFormulaire((Produit) getTableRow().getItem()));
                btnSupprimer.setOnAction(e -> supprimer((Produit) getTableRow().getItem()));
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });

        tableProduits.setItems(donnees);
        chargerDonnees();
    }

    private void chargerDonnees() {
        donnees.setAll(produitDAO.getAll());
    }

    @FXML
    private void handleRecherche() {
        String motCle = txtRecherche.getText().trim();
        if (motCle.isEmpty()) {
            chargerDonnees();
        } else {
            donnees.setAll(produitDAO.search(motCle));
        }
    }

    @FXML
    private void handleNouveauProduit() {
        ouvrirFormulaire(null);
    }

    private void ouvrirFormulaire(Produit produit) {
        try {
            java.net.URL formUrl = App.resolveResource("/fxml/ProduitForm.fxml");
            if (formUrl == null) {
                throw new RuntimeException("ProduitForm.fxml introuvable.");
            }
            FXMLLoader loader = new FXMLLoader(formUrl);
            Parent root = loader.load();
            ProduitFormController controller = loader.getController();
            if (produit != null) controller.setProduit(produit);
            controller.setOnSauvegarde(this::chargerDonnees);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(produit == null ? "Nouveau produit" : "Modifier le produit");
            stage.setScene(new Scene(root));
            java.net.URL cssUrl = App.resolveResource("/css/style.css");
            if (cssUrl != null) {
                stage.getScene().getStylesheets().add(cssUrl.toExternalForm());
            }
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible d'ouvrir le formulaire produit");
            alert.setContentText(e.getClass().getSimpleName() + " : " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void supprimer(Produit produit) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmer la suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer \"" + produit.getNom() + "\" ?");
        confirmation.showAndWait().ifPresent(reponse -> {
            if (reponse == ButtonType.OK) {
                produitDAO.supprimer(produit.getId());
                chargerDonnees();
            }
        });
    }
}
