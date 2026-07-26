package tg.univlome.epl.gestionboutique.controller;

import tg.univlome.epl.gestionboutique.dao.ProduitDAO;
import tg.univlome.epl.gestionboutique.model.Produit;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AlerteStockController {

    @FXML private Label lblNbAlertes;
    @FXML private TableView<Produit> tableStock;
    @FXML private TableColumn<Produit, String> colNom;
    @FXML private TableColumn<Produit, String> colCategorie;
    @FXML private TableColumn<Produit, Number> colStock;
    @FXML private TableColumn<Produit, Number> colSeuil;
    @FXML private TableColumn<Produit, Void> colStatut;

    private final ProduitDAO produitDAO = new ProduitDAO();

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("quantiteStock"));
        colSeuil.setCellValueFactory(new PropertyValueFactory<>("seuilAlerte"));

        colStatut.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }
                Produit p = getTableView().getItems().get(getIndex());
                Label tag = new Label(p.getQuantiteStock() == 0 ? "Rupture" : "Stock faible");
                tag.getStyleClass().add("tag-danger");
                setGraphic(tag);
            }
        });

        var liste = produitDAO.getStockFaible();
        tableStock.setItems(FXCollections.observableArrayList(liste));
        lblNbAlertes.setText(String.valueOf(liste.size()));
    }
}
