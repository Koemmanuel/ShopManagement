package tg.univlome.epl.gestionboutique.model;

import javafx.beans.property.*;

public class LigneVente {

    private final IntegerProperty idProduit = new SimpleIntegerProperty();
    private final StringProperty nomProduit = new SimpleStringProperty();
    private final IntegerProperty quantite = new SimpleIntegerProperty();
    private final DoubleProperty prixUnitaire = new SimpleDoubleProperty();
    private final DoubleProperty sousTotal = new SimpleDoubleProperty();

    public LigneVente() {}

    public LigneVente(int idProduit, String nomProduit, int quantite, double prixUnitaire) {
        setIdProduit(idProduit);
        setNomProduit(nomProduit);
        setQuantite(quantite);
        setPrixUnitaire(prixUnitaire);
        setSousTotal(quantite * prixUnitaire);
    }

    public int getIdProduit() { return idProduit.get(); }
    public void setIdProduit(int value) { idProduit.set(value); }
    public IntegerProperty idProduitProperty() { return idProduit; }

    public String getNomProduit() { return nomProduit.get(); }
    public void setNomProduit(String value) { nomProduit.set(value); }
    public StringProperty nomProduitProperty() { return nomProduit; }

    public int getQuantite() { return quantite.get(); }
    public void setQuantite(int value) { quantite.set(value); sousTotal.set(value * getPrixUnitaire()); }
    public IntegerProperty quantiteProperty() { return quantite; }

    public double getPrixUnitaire() { return prixUnitaire.get(); }
    public void setPrixUnitaire(double value) { prixUnitaire.set(value); }
    public DoubleProperty prixUnitaireProperty() { return prixUnitaire; }

    public double getSousTotal() { return sousTotal.get(); }
    public void setSousTotal(double value) { sousTotal.set(value); }
    public DoubleProperty sousTotalProperty() { return sousTotal; }
}
