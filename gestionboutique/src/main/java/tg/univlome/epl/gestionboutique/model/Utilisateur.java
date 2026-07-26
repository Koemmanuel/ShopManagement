package tg.univlome.epl.gestionboutique.model;

import javafx.beans.property.*;

public class Utilisateur {

    public enum Role { ADMIN, VENDEUR }

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nomComplet = new SimpleStringProperty();
    private final StringProperty identifiant = new SimpleStringProperty();
    private String motDePasseHash;
    private final ObjectProperty<Role> role = new SimpleObjectProperty<>();

    public Utilisateur() {}

    public Utilisateur(int id, String nomComplet, String identifiant, Role role) {
        setId(id);
        setNomComplet(nomComplet);
        setIdentifiant(identifiant);
        setRole(role);
    }

    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }

    public String getNomComplet() { return nomComplet.get(); }
    public void setNomComplet(String value) { nomComplet.set(value); }
    public StringProperty nomCompletProperty() { return nomComplet; }

    public String getIdentifiant() { return identifiant.get(); }
    public void setIdentifiant(String value) { identifiant.set(value); }
    public StringProperty identifiantProperty() { return identifiant; }

    public String getMotDePasseHash() { return motDePasseHash; }
    public void setMotDePasseHash(String hash) { this.motDePasseHash = hash; }

    public Role getRole() { return role.get(); }
    public void setRole(Role value) { role.set(value); }
    public ObjectProperty<Role> roleProperty() { return role; }

    public boolean isAdmin() { return getRole() == Role.ADMIN; }
}
