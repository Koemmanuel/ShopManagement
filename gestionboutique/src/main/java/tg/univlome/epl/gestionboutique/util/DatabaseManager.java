package tg.univlome.epl.gestionboutique.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gestionnaire de connexion à la base de données SQLite.
 * Crée automatiquement les tables si elles n'existent pas encore.
 */
public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:database/boutique.db";
    private static Connection connection;

    private DatabaseManager() {
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
                connection.createStatement().execute("PRAGMA foreign_keys = ON;");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Crée les tables si elles n'existent pas + insère un admin par défaut.
     */
    public static void initDatabase() {
        String sqlUtilisateur = """
            CREATE TABLE IF NOT EXISTS Utilisateur (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nomComplet TEXT NOT NULL,
                identifiant TEXT NOT NULL UNIQUE,
                motDePasse TEXT NOT NULL,
                role TEXT NOT NULL CHECK(role IN ('ADMIN','VENDEUR')),
                dateCreation TEXT DEFAULT (datetime('now'))
            );
        """;

        String sqlProduit = """
            CREATE TABLE IF NOT EXISTS Produit (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nom TEXT NOT NULL,
                categorie TEXT,
                prixAchat REAL NOT NULL DEFAULT 0,
                prixVente REAL NOT NULL,
                quantiteStock INTEGER NOT NULL DEFAULT 0,
                seuilAlerte INTEGER NOT NULL DEFAULT 5,
                dateCreation TEXT DEFAULT (datetime('now'))
            );
        """;

        String sqlClient = """
            CREATE TABLE IF NOT EXISTS Client (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nom TEXT NOT NULL,
                telephone TEXT,
                dateCreation TEXT DEFAULT (datetime('now'))
            );
        """;

        String sqlVente = """
            CREATE TABLE IF NOT EXISTS Vente (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                dateVente TEXT DEFAULT (datetime('now')),
                total REAL NOT NULL DEFAULT 0,
                idUtilisateur INTEGER,
                idClient INTEGER,
                FOREIGN KEY (idUtilisateur) REFERENCES Utilisateur(id),
                FOREIGN KEY (idClient) REFERENCES Client(id)
            );
        """;

        String sqlLigneVente = """
            CREATE TABLE IF NOT EXISTS LigneVente (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                idVente INTEGER NOT NULL,
                idProduit INTEGER NOT NULL,
                quantite INTEGER NOT NULL,
                prixUnitaire REAL NOT NULL,
                sousTotal REAL NOT NULL,
                FOREIGN KEY (idVente) REFERENCES Vente(id) ON DELETE CASCADE,
                FOREIGN KEY (idProduit) REFERENCES Produit(id)
            );
        """;

        try (Statement stmt = getConnection().createStatement()) {
            stmt.execute(sqlUtilisateur);
            stmt.execute(sqlProduit);
            stmt.execute(sqlClient);
            stmt.execute(sqlVente);
            stmt.execute(sqlLigneVente);

            // Créer un admin par défaut si la table est vide
            var rs = stmt.executeQuery("SELECT COUNT(*) AS nb FROM Utilisateur");
            if (rs.next() && rs.getInt("nb") == 0) {
                String hash = org.mindrot.jbcrypt.BCrypt.hashpw("admin123", org.mindrot.jbcrypt.BCrypt.gensalt());
                String insertAdmin = "INSERT INTO Utilisateur (nomComplet, identifiant, motDePasse, role) "
                        + "VALUES ('Administrateur', 'admin', '" + hash + "', 'ADMIN')";
                stmt.execute(insertAdmin);
                System.out.println("Utilisateur admin créé -> identifiant: admin / mot de passe: admin123");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
