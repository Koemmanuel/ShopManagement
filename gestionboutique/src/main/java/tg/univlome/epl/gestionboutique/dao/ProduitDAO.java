package tg.univlome.epl.gestionboutique.dao;

import tg.univlome.epl.gestionboutique.model.Produit;
import tg.univlome.epl.gestionboutique.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitDAO {

    public List<Produit> getAll() {
        List<Produit> liste = new ArrayList<>();
        String sql = "SELECT * FROM Produit ORDER BY nom";
        try (Statement stmt = DatabaseManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    public List<Produit> search(String motCle) {
        List<Produit> liste = new ArrayList<>();
        String sql = "SELECT * FROM Produit WHERE nom LIKE ? OR categorie LIKE ? ORDER BY nom";
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, "%" + motCle + "%");
            ps.setString(2, "%" + motCle + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) liste.add(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    public boolean ajouter(Produit p) {
        String sql = "INSERT INTO Produit (nom, categorie, prixAchat, prixVente, quantiteStock, seuilAlerte) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getCategorie());
            ps.setDouble(3, p.getPrixAchat());
            ps.setDouble(4, p.getPrixVente());
            ps.setInt(5, p.getQuantiteStock());
            ps.setInt(6, p.getSeuilAlerte());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modifier(Produit p) {
        String sql = "UPDATE Produit SET nom=?, categorie=?, prixAchat=?, prixVente=?, quantiteStock=?, seuilAlerte=? WHERE id=?";
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getCategorie());
            ps.setDouble(3, p.getPrixAchat());
            ps.setDouble(4, p.getPrixVente());
            ps.setInt(5, p.getQuantiteStock());
            ps.setInt(6, p.getSeuilAlerte());
            ps.setInt(7, p.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM Produit WHERE id=?";
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Diminue le stock après une vente. */
    public void diminuerStock(int idProduit, int quantite) {
        String sql = "UPDATE Produit SET quantiteStock = quantiteStock - ? WHERE id=?";
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setInt(1, quantite);
            ps.setInt(2, idProduit);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Produit> getStockFaible() {
        List<Produit> liste = new ArrayList<>();
        String sql = "SELECT * FROM Produit WHERE quantiteStock <= seuilAlerte ORDER BY quantiteStock";
        try (Statement stmt = DatabaseManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) liste.add(map(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    public int countProduits() {
        String sql = "SELECT COUNT(*) AS nb FROM Produit";
        try (Statement stmt = DatabaseManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt("nb");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Produit map(ResultSet rs) throws SQLException {
        return new Produit(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("categorie"),
                rs.getDouble("prixAchat"),
                rs.getDouble("prixVente"),
                rs.getInt("quantiteStock"),
                rs.getInt("seuilAlerte")
        );
    }
}
