package tg.univlome.epl.gestionboutique.controller;

import tg.univlome.epl.gestionboutique.dao.ProduitDAO;
import tg.univlome.epl.gestionboutique.dao.VenteDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DashboardController {

    @FXML private Label lblCAJournalier;
    @FXML private Label lblCAMensuel;
    @FXML private Label lblNbVentes;
    @FXML private Label lblStockFaible;
    @FXML private LineChart<String, Number> chartCA;
    @FXML private BarChart<String, Number> chartTop;

    private final VenteDAO venteDAO = new VenteDAO();
    private final ProduitDAO produitDAO = new ProduitDAO();

    @FXML
    public void initialize() {
        NumberFormat format = NumberFormat.getNumberInstance(Locale.FRANCE);

        lblCAJournalier.setText(format.format(venteDAO.getChiffreAffairesJournalier()) + " FCFA");
        lblCAMensuel.setText(format.format(venteDAO.getChiffreAffairesMensuel()) + " FCFA");
        lblNbVentes.setText(String.valueOf(venteDAO.getNombreVentesJournalier()));
        lblStockFaible.setText(String.valueOf(produitDAO.getStockFaible().size()));

        chargerGraphiqueCA();
        chargerGraphiqueTopProduits();
    }

    private void chargerGraphiqueCA() {
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Chiffre d'affaires");
        List<Object[]> data = venteDAO.getCASeptDerniersJours();
        for (Object[] ligne : data) {
            serie.getData().add(new XYChart.Data<>((String) ligne[0], (Double) ligne[1]));
        }
        chartCA.setData(FXCollections.observableArrayList(serie));
    }

    private void chargerGraphiqueTopProduits() {
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Quantité vendue");
        List<Object[]> data = venteDAO.getTopProduits(5);
        for (Object[] ligne : data) {
            serie.getData().add(new XYChart.Data<>((String) ligne[0], (Integer) ligne[1]));
        }
        chartTop.setData(FXCollections.observableArrayList(serie));
    }
}
