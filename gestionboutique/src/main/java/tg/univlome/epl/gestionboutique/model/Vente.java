package tg.univlome.epl.gestionboutique.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Vente {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty dateVente = new SimpleStringProperty();
    private final DoubleProperty total = new SimpleDoubleProperty();
    private final StringProperty vendeur = new SimpleStringProperty();
    private final ObservableList<LigneVente> lignes = FXCollections.observableArrayList();

    public Vente() {}

    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }

    public String getDateVente() { return dateVente.get(); }
    public void setDateVente(String value) { dateVente.set(value); }
    public StringProperty dateVenteProperty() { return dateVente; }

    public double getTotal() { return total.get(); }
    public void setTotal(double value) { total.set(value); }
    public DoubleProperty totalProperty() { return total; }

    public String getVendeur() { return vendeur.get(); }
    public void setVendeur(String value) { vendeur.set(value); }
    public StringProperty vendeurProperty() { return vendeur; }

    public ObservableList<LigneVente> getLignes() { return lignes; }
}
