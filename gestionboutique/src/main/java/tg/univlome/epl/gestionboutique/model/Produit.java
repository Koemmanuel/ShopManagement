package tg.univlome.epl.gestionboutique.model;

import javafx.beans.property.*;

/**
 * Modèle représentant un produit en boutique.
 * Utilise les propriétés JavaFX pour un binding direct avec les TableView.
 */
public class Produit {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nom = new SimpleStringProperty();
    private final StringProperty categorie = new SimpleStringProperty();
    private final DoubleProperty prixAchat = new SimpleDoubleProperty();
    private final DoubleProperty prixVente = new SimpleDoubleProperty();
    private final IntegerProperty quantiteStock = new SimpleIntegerProperty();
    private final IntegerProperty seuilAlerte = new SimpleIntegerProperty();

    public Produit() {
    }

    public Produit(int id, String nom, String categorie, double prixAchat, double prixVente,
                   int quantiteStock, int seuilAlerte) {
        setId(id);
        setNom(nom);
        setCategorie(categorie);
        setPrixAchat(prixAchat);
        setPrixVente(prixVente);
        setQuantiteStock(quantiteStock);
        setSeuilAlerte(seuilAlerte);
    }

    // --- id ---
    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }

    // --- nom ---
    public String getNom() { return nom.get(); }
    public void setNom(String value) { nom.set(value); }
    public StringProperty nomProperty() { return nom; }

    // --- categorie ---
    public String getCategorie() { return categorie.get(); }
    public void setCategorie(String value) { categorie.set(value); }
    public StringProperty categorieProperty() { return categorie; }

    // --- prixAchat ---
    public double getPrixAchat() { return prixAchat.get(); }
    public void setPrixAchat(double value) { prixAchat.set(value); }
    public DoubleProperty prixAchatProperty() { return prixAchat; }

    // --- prixVente ---
    public double getPrixVente() { return prixVente.get(); }
    public void setPrixVente(double value) { prixVente.set(value); }
    public DoubleProperty prixVenteProperty() { return prixVente; }

    // --- quantiteStock ---
    public int getQuantiteStock() { return quantiteStock.get(); }
    public void setQuantiteStock(int value) { quantiteStock.set(value); }
    public IntegerProperty quantiteStockProperty() { return quantiteStock; }

    // --- seuilAlerte ---
    public int getSeuilAlerte() { return seuilAlerte.get(); }
    public void setSeuilAlerte(int value) { seuilAlerte.set(value); }
    public IntegerProperty seuilAlerteProperty() { return seuilAlerte; }

    public boolean isStockFaible() {
        return getQuantiteStock() <= getSeuilAlerte();
    }
}
