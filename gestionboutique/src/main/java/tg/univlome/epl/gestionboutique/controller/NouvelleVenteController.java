package tg.univlome.epl.gestionboutique.controller;

import tg.univlome.epl.gestionboutique.dao.ProduitDAO;
import tg.univlome.epl.gestionboutique.dao.VenteDAO;
import tg.univlome.epl.gestionboutique.model.LigneVente;
import tg.univlome.epl.gestionboutique.model.Produit;
import tg.univlome.epl.gestionboutique.model.Vente;
import tg.univlome.epl.gestionboutique.util.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.print.PageLayout;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.StringConverter;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class NouvelleVenteController {

    @FXML private ComboBox<Produit> cmbProduit;
    @FXML private TextField txtQuantite;
    @FXML private Label lblErreur;
    @FXML private TableView<LigneVente> tableLignes;
    @FXML private TableColumn<LigneVente, String> colProduit;
    @FXML private TableColumn<LigneVente, Integer> colQuantite;
    @FXML private TableColumn<LigneVente, Double> colPrixUnitaire;
    @FXML private TableColumn<LigneVente, Double> colSousTotal;
    @FXML private TableColumn<LigneVente, Void> colAction;
    @FXML private Label lblTotal;

    private final ProduitDAO produitDAO = new ProduitDAO();
    private final VenteDAO venteDAO = new VenteDAO();
    private final ObservableList<LigneVente> lignes = FXCollections.observableArrayList();
    private final NumberFormat format = NumberFormat.getNumberInstance(Locale.FRANCE);

    @FXML
    public void initialize() {
        cmbProduit.setItems(FXCollections.observableArrayList(produitDAO.getAll()));
        cmbProduit.setConverter(new StringConverter<>() {
            @Override public String toString(Produit p) {
                return p == null ? "" : p.getNom() + "  (Stock: " + p.getQuantiteStock() + ")";
            }
            @Override public Produit fromString(String s) { return null; }
        });

        colProduit.setCellValueFactory(new PropertyValueFactory<>("nomProduit"));
        colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));

        colPrixUnitaire.setCellValueFactory(new PropertyValueFactory<>("prixUnitaire"));
        colPrixUnitaire.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : format.format(item) + " FCFA");
            }
        });

        colSousTotal.setCellValueFactory(new PropertyValueFactory<>("sousTotal"));
        colSousTotal.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : format.format(item) + " FCFA");
            }
        });

        colAction.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("✖");
            {
                btn.getStyleClass().add("btn-danger");
                btn.setOnAction(e -> {
                    LigneVente l = (LigneVente) getTableRow().getItem();
                    lignes.remove(l);
                    recalculerTotal();
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        tableLignes.setItems(lignes);
    }

    @FXML
    private void handleAjouterLigne() {
        lblErreur.setText("");
        Produit produit = cmbProduit.getValue();
        if (produit == null) {
            lblErreur.setText("Veuillez choisir un produit.");
            return;
        }

        int quantite;
        try {
            quantite = Integer.parseInt(txtQuantite.getText().trim());
        } catch (NumberFormatException e) {
            lblErreur.setText("Quantité invalide.");
            return;
        }

        if (quantite <= 0) {
            lblErreur.setText("La quantité doit être supérieure à 0.");
            return;
        }

        int dejaAjoute = lignes.stream()
                .filter(l -> l.getIdProduit() == produit.getId())
                .mapToInt(LigneVente::getQuantite).sum();

        if (quantite + dejaAjoute > produit.getQuantiteStock()) {
            lblErreur.setText("Stock insuffisant. Disponible : " + produit.getQuantiteStock());
            return;
        }

        lignes.add(new LigneVente(produit.getId(), produit.getNom(), quantite, produit.getPrixVente()));
        txtQuantite.clear();
        recalculerTotal();
    }

    private void recalculerTotal() {
        double total = lignes.stream().mapToDouble(LigneVente::getSousTotal).sum();
        lblTotal.setText(format.format(total) + " FCFA");
    }

    @FXML
    private void handleAnnulerVente() {
        lignes.clear();
        recalculerTotal();
        lblErreur.setText("");
    }

    @FXML
    private void handleValiderVente() {
        if (lignes.isEmpty()) {
            lblErreur.setText("Ajoutez au moins un produit avant de valider.");
            return;
        }

        Vente vente = new Vente();
        vente.getLignes().addAll(lignes);
        double total = lignes.stream().mapToDouble(LigneVente::getSousTotal).sum();
        vente.setTotal(total);

        int idUtilisateur = Session.getUtilisateurConnecte() != null ? Session.getUtilisateurConnecte().getId() : 0;
        boolean ok = venteDAO.enregistrerVente(vente, idUtilisateur);

        if (ok) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Vente enregistrée");
            alert.setHeaderText(null);
            alert.setContentText("La vente a été enregistrée avec succès.\nTotal : " + format.format(total) + " FCFA");
            alert.showAndWait();

            lignes.clear();
            recalculerTotal();
            cmbProduit.setItems(FXCollections.observableArrayList(produitDAO.getAll()));
        } else {
            lblErreur.setText("Erreur lors de l'enregistrement de la vente.");
        }
    }

    @FXML
    private void handleImprimerRecu() {
        if (lignes.isEmpty()) {
            lblErreur.setText("Ajoutez au moins un produit avant d'imprimer.");
            return;
        }

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Impression indisponible");
            alert.setHeaderText(null);
            alert.setContentText("Aucune imprimante n'a été détectée sur ce poste.");
            alert.showAndWait();
            return;
        }

        VBox recu = construireRecu();

        Printer printer = job.getPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4,
                javafx.print.PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
        recu.setPrefWidth(pageLayout.getPrintableWidth());

        boolean proceed = job.showPrintDialog(tableLignes.getScene().getWindow());
        if (!proceed) return;

        boolean success = job.printPage(pageLayout, recu);
        if (success) {
            job.endJob();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur d'impression");
            alert.setHeaderText(null);
            alert.setContentText("L'impression du reçu a échoué.");
            alert.showAndWait();
        }
    }

    /** Construit un reçu texte simple à partir du panier courant. */
    private VBox construireRecu() {
        VBox recu = new VBox(6);
        recu.setStyle("-fx-padding: 20;");

        Label titre = new Label("GestionBoutique");
        titre.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        Label sousTitre = new Label("Reçu de vente");
        sousTitre.setFont(Font.font("Segoe UI", 12));

        String vendeur = Session.getUtilisateurConnecte() != null ? Session.getUtilisateurConnecte().getNomComplet() : "—";
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        Label infos = new Label("Date : " + dateStr + "   |   Vendeur : " + vendeur);
        infos.setFont(Font.font("Segoe UI", 10.5));

        Separator sep1 = new Separator();

        VBox lignesRecu = new VBox(3);
        for (LigneVente l : lignes) {
            Label ligneLabel = new Label(
                    l.getQuantite() + " x " + l.getNomProduit() +
                    "  ....  " + format.format(l.getSousTotal()) + " FCFA");
            ligneLabel.setFont(Font.font("Consolas", 11));
            lignesRecu.getChildren().add(ligneLabel);
        }

        Separator sep2 = new Separator();

        double total = lignes.stream().mapToDouble(LigneVente::getSousTotal).sum();
        Label totalLabel = new Label("TOTAL : " + format.format(total) + " FCFA");
        totalLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

        Label merci = new Label("Merci pour votre achat !");
        merci.setFont(Font.font("Segoe UI", 10));
        merci.setAlignment(Pos.CENTER);

        recu.getChildren().addAll(titre, sousTitre, infos, sep1, lignesRecu, sep2, totalLabel, merci);
        return recu;
    }
}
