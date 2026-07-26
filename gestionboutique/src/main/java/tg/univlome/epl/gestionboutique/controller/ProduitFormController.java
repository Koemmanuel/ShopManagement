package tg.univlome.epl.gestionboutique.controller;

import tg.univlome.epl.gestionboutique.dao.ProduitDAO;
import tg.univlome.epl.gestionboutique.model.Produit;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class ProduitFormController {

    @FXML private Label lblTitre;
    @FXML private TextField txtNom;
    @FXML private ComboBox<String> cmbCategorie;
    @FXML private TextField txtPrixAchat;
    @FXML private TextField txtPrixVente;
    @FXML private TextField txtQuantite;
    @FXML private TextField txtSeuil;
    @FXML private Label lblErreur;
    @FXML private Button btnEnregistrer;

    private final ProduitDAO produitDAO = new ProduitDAO();
    private Produit produitEnEdition;
    private Runnable onSauvegarde;

    @FXML
    public void initialize() {
        cmbCategorie.setItems(FXCollections.observableArrayList(
                "Alimentation", "Boisson", "Hygiène", "Ménage", "Électronique", "Vêtement", "Autre"));
    }

    /** À appeler pour préremplir le formulaire en cas de modification. */
    public void setProduit(Produit p) {
        this.produitEnEdition = p;
        lblTitre.setText("Modifier le produit");
        btnEnregistrer.setText("Mettre à jour");
        txtNom.setText(p.getNom());
        cmbCategorie.setValue(p.getCategorie());
        txtPrixAchat.setText(String.valueOf(p.getPrixAchat()));
        txtPrixVente.setText(String.valueOf(p.getPrixVente()));
        txtQuantite.setText(String.valueOf(p.getQuantiteStock()));
        txtSeuil.setText(String.valueOf(p.getSeuilAlerte()));
    }

    public void setOnSauvegarde(Runnable callback) {
        this.onSauvegarde = callback;
    }

    @FXML
    private void handleEnregistrer() {
        String nom = txtNom.getText().trim();
        String categorie = cmbCategorie.getValue() == null ? "" : cmbCategorie.getValue().trim();

        if (nom.isEmpty()) {
            lblErreur.setText("Le nom du produit est obligatoire.");
            return;
        }

        double prixAchat, prixVente;
        int quantite, seuil;
        try {
            prixAchat = txtPrixAchat.getText().isBlank() ? 0 : Double.parseDouble(txtPrixAchat.getText().trim());
            prixVente = Double.parseDouble(txtPrixVente.getText().trim());
            quantite = Integer.parseInt(txtQuantite.getText().trim());
            seuil = txtSeuil.getText().isBlank() ? 5 : Integer.parseInt(txtSeuil.getText().trim());
        } catch (NumberFormatException e) {
            lblErreur.setText("Veuillez saisir des nombres valides pour les prix et quantités.");
            return;
        }

        if (prixVente <= 0) {
            lblErreur.setText("Le prix de vente doit être supérieur à 0.");
            return;
        }

        if (produitEnEdition == null) {
            Produit p = new Produit(0, nom, categorie, prixAchat, prixVente, quantite, seuil);
            produitDAO.ajouter(p);
        } else {
            produitEnEdition.setNom(nom);
            produitEnEdition.setCategorie(categorie);
            produitEnEdition.setPrixAchat(prixAchat);
            produitEnEdition.setPrixVente(prixVente);
            produitEnEdition.setQuantiteStock(quantite);
            produitEnEdition.setSeuilAlerte(seuil);
            produitDAO.modifier(produitEnEdition);
        }

        if (onSauvegarde != null) onSauvegarde.run();
        fermer();
    }

    @FXML
    private void handleAnnuler() {
        fermer();
    }

    private void fermer() {
        Stage stage = (Stage) txtNom.getScene().getWindow();
        stage.close();
    }
}
