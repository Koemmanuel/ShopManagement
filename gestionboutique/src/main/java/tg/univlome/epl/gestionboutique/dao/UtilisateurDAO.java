package tg.univlome.epl.gestionboutique.dao;

import tg.univlome.epl.gestionboutique.model.Utilisateur;
import tg.univlome.epl.gestionboutique.util.DatabaseManager;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

    /** Authentifie un utilisateur. Retourne l'utilisateur si OK, sinon null. */
    public Utilisateur authentifier(String identifiant, String motDePasse) {
        String sql = "SELECT * FROM Utilisateur WHERE identifiant = ?";
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, identifiant);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hash = rs.getString("motDePasse");
                    if (BCrypt.checkpw(motDePasse, hash)) {
                        Utilisateur u = new Utilisateur(
                                rs.getInt("id"),
                                rs.getString("nomComplet"),
                                rs.getString("identifiant"),
                                Utilisateur.Role.valueOf(rs.getString("role"))
                        );
                        return u;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean ajouter(String nomComplet, String identifiant, String motDePasse, Utilisateur.Role role) {
        String sql = "INSERT INTO Utilisateur (nomComplet, identifiant, motDePasse, role) VALUES (?,?,?,?)";
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            String hash = BCrypt.hashpw(motDePasse, BCrypt.gensalt());
            ps.setString(1, nomComplet);
            ps.setString(2, identifiant);
            ps.setString(3, hash);
            ps.setString(4, role.name());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Utilisateur> getAll() {
        List<Utilisateur> liste = new ArrayList<>();
        String sql = "SELECT * FROM Utilisateur ORDER BY nomComplet";
        try (Statement stmt = DatabaseManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nomComplet"),
                        rs.getString("identifiant"),
                        Utilisateur.Role.valueOf(rs.getString("role"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM Utilisateur WHERE id=?";
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
