package tg.univlome.epl.gestionboutique.controller;

import tg.univlome.epl.gestionboutique.dao.VenteDAO;
import tg.univlome.epl.gestionboutique.model.Vente;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.NumberFormat;
import java.util.Locale;

public class HistoriqueController {

    @FXML private TableView<Vente> tableVentes;
    @FXML private TableColumn<Vente, Integer> colId;
    @FXML private TableColumn<Vente, String> colDate;
    @FXML private TableColumn<Vente, String> colVendeur;
    @FXML private TableColumn<Vente, Number> colTotal;
    @FXML private TableColumn<Vente, Void> colDetail;

    private final VenteDAO venteDAO = new VenteDAO();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateVente"));
        colVendeur.setCellValueFactory(new PropertyValueFactory<>("vendeur"));

        NumberFormat format = NumberFormat.getNumberInstance(Locale.FRANCE);
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colTotal.setCellFactory(col -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : format.format(item) + " FCFA");
            }
        });

        colDetail.setCellFactory(col -> new javafx.scene.control.TableCell<>() {
            private final javafx.scene.control.Button btn = new javafx.scene.control.Button("Voir");
            {
                btn.getStyleClass().add("btn-secondary");
                btn.setOnAction(e -> {
                    Vente v = getTableView().getItems().get(getIndex());
                    afficherDetail(v);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        tableVentes.setItems(FXCollections.observableArrayList(venteDAO.getHistorique()));
    }

    private void afficherDetail(Vente v) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Détail de la vente #" + v.getId());
        alert.setHeaderText("Vente du " + v.getDateVente() + " — Vendeur : " + v.getVendeur());
        NumberFormat format = NumberFormat.getNumberInstance(Locale.FRANCE);
        alert.setContentText("Total : " + format.format(v.getTotal()) + " FCFA");
        alert.showAndWait();
    }

    @FXML
    private void handleExporterCsv() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Exporter l'historique des ventes");
        chooser.setInitialFileName("historique_ventes.csv");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier CSV (Excel)", "*.csv"));

        Stage stage = (Stage) tableVentes.getScene().getWindow();
        java.io.File fichier = chooser.showSaveDialog(stage);
        if (fichier == null) return;

        // Le point-virgule est le séparateur reconnu par défaut par Excel en local FR.
        try (Writer writer = new FileWriter(fichier, java.nio.charset.StandardCharsets.UTF_8)) {
            writer.write('\uFEFF'); // BOM pour un bon affichage des accents dans Excel
            writer.write("N° Vente;Date;Vendeur;Total (FCFA)\n");
            for (Vente v : tableVentes.getItems()) {
                writer.write(v.getId() + ";" +
                        echapper(v.getDateVente()) + ";" +
                        echapper(v.getVendeur()) + ";" +
                        v.getTotal() + "\n");
            }
            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.setTitle("Export réussi");
            ok.setHeaderText(null);
            ok.setContentText("L'historique a été exporté vers :\n" + fichier.getAbsolutePath());
            ok.showAndWait();
        } catch (IOException e) {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Erreur d'export");
            err.setHeaderText(null);
            err.setContentText("Impossible d'écrire le fichier : " + e.getMessage());
            err.showAndWait();
        }
    }

    private String echapper(String valeur) {
        if (valeur == null) return "";
        return valeur.replace(";", ",");
    }
}
