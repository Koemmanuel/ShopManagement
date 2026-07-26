package tg.univlome.epl.gestionboutique.dao;

import tg.univlome.epl.gestionboutique.model.LigneVente;
import tg.univlome.epl.gestionboutique.model.Vente;
import tg.univlome.epl.gestionboutique.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VenteDAO {

    private final ProduitDAO produitDAO = new ProduitDAO();

    /**
     * Enregistre une vente complète avec ses lignes, en une transaction.
     * Met aussi à jour le stock automatiquement.
     */
    public boolean enregistrerVente(Vente vente, int idUtilisateur) {
        Connection conn = DatabaseManager.getConnection();
        String sqlVente = "INSERT INTO Vente (total, idUtilisateur) VALUES (?, ?)";
        String sqlLigne = "INSERT INTO LigneVente (idVente, idProduit, quantite, prixUnitaire, sousTotal) VALUES (?,?,?,?,?)";

        try {
            conn.setAutoCommit(false);

            int idVente;
            try (PreparedStatement psVente = conn.prepareStatement(sqlVente, Statement.RETURN_GENERATED_KEYS)) {
                psVente.setDouble(1, vente.getTotal());
                psVente.setInt(2, idUtilisateur);
                psVente.executeUpdate();
                try (ResultSet keys = psVente.getGeneratedKeys()) {
                    keys.next();
                    idVente = keys.getInt(1);
                }
            }

            try (PreparedStatement psLigne = conn.prepareStatement(sqlLigne)) {
                for (LigneVente l : vente.getLignes()) {
                    psLigne.setInt(1, idVente);
                    psLigne.setInt(2, l.getIdProduit());
                    psLigne.setInt(3, l.getQuantite());
                    psLigne.setDouble(4, l.getPrixUnitaire());
                    psLigne.setDouble(5, l.getSousTotal());
                    psLigne.addBatch();
                }
                psLigne.executeBatch();
            }

            for (LigneVente l : vente.getLignes()) {
                produitDAO.diminuerStock(l.getIdProduit(), l.getQuantite());
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public double getChiffreAffairesJournalier() {
        String sql = "SELECT COALESCE(SUM(total),0) AS ca FROM Vente WHERE date(dateVente) = date('now')";
        try (Statement stmt = DatabaseManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble("ca");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getChiffreAffairesMensuel() {
        String sql = "SELECT COALESCE(SUM(total),0) AS ca FROM Vente WHERE strftime('%Y-%m', dateVente) = strftime('%Y-%m','now')";
        try (Statement stmt = DatabaseManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble("ca");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getNombreVentesJournalier() {
        String sql = "SELECT COUNT(*) AS nb FROM Vente WHERE date(dateVente) = date('now')";
        try (Statement stmt = DatabaseManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt("nb");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Vente> getHistorique() {
        List<Vente> liste = new ArrayList<>();
        String sql = """
            SELECT V.id, V.dateVente, V.total, U.nomComplet AS vendeur
            FROM Vente V LEFT JOIN Utilisateur U ON V.idUtilisateur = U.id
            ORDER BY V.dateVente DESC
        """;
        try (Statement stmt = DatabaseManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Vente v = new Vente();
                v.setId(rs.getInt("id"));
                v.setDateVente(rs.getString("dateVente"));
                v.setTotal(rs.getDouble("total"));
                v.setVendeur(rs.getString("vendeur"));
                liste.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    /** Retourne les N produits les plus vendus (nom -> quantité totale vendue). */
    public List<Object[]> getTopProduits(int limite) {
        List<Object[]> liste = new ArrayList<>();
        String sql = """
            SELECT P.nom AS nom, SUM(L.quantite) AS totalVendu
            FROM LigneVente L JOIN Produit P ON L.idProduit = P.id
            GROUP BY P.id ORDER BY totalVendu DESC LIMIT ?
        """;
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(new Object[]{rs.getString("nom"), rs.getInt("totalVendu")});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    /** CA des 7 derniers jours pour un graphique d'évolution. */
    public List<Object[]> getCASeptDerniersJours() {
        List<Object[]> liste = new ArrayList<>();
        String sql = """
            SELECT date(dateVente) AS jour, COALESCE(SUM(total),0) AS ca
            FROM Vente
            WHERE date(dateVente) >= date('now', '-6 days')
            GROUP BY jour ORDER BY jour
        """;
        try (Statement stmt = DatabaseManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(new Object[]{rs.getString("jour"), rs.getDouble("ca")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }
}
