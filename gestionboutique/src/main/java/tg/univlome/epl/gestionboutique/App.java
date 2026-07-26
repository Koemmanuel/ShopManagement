package tg.univlome.epl.gestionboutique;

import tg.univlome.epl.gestionboutique.util.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

/**
 * Point d'entree JavaFX.
 */
public class App extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        // S'assurer que le dossier database/ existe
        new File("database").mkdirs();

        DatabaseManager.initDatabase();

        primaryStage = stage;
        primaryStage.setTitle("GestionBoutique — Connexion");
        primaryStage.setResizable(true);

        try {
            primaryStage.getIcons().add(new Image(App.class.getResourceAsStream("/images/logo.png")));
        } catch (Exception ignored) {
            // icone optionnelle
        }

        java.net.URL loginUrl = resolveResource("/fxml/Login.fxml");
        if (loginUrl == null) {
            throw new RuntimeException("Login.fxml introuvable. Verifie src/main/resources/fxml/Login.fxml");
        }
        FXMLLoader loader = new FXMLLoader(loginUrl);
        Scene scene = new Scene(loader.load());
        java.net.URL cssUrl = resolveResource("/css/style.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Cherche une ressource au chemin standard, puis en secours sous le
     * package (au cas ou les fichiers sont restes imbriques dans
     * resources/tg/univlome/epl/gestionboutique/...).
     */
    public static java.net.URL resolveResource(String standardPath) {
        java.net.URL url = App.class.getResource(standardPath);
        if (url != null) return url;
        return App.class.getResource("/tg/univlome/epl/gestionboutique" + standardPath);
    }

    /** Permet aux controleurs de changer d'ecran facilement. */
    public static void changerScene(String fxmlPath, String titre) throws Exception {
        java.net.URL url = resolveResource(fxmlPath);
        if (url == null) {
            throw new RuntimeException("Fichier FXML introuvable : " + fxmlPath);
        }
        FXMLLoader loader = new FXMLLoader(url);
        Scene scene;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de charger " + fxmlPath);
            alert.setContentText(e.getClass().getSimpleName() + " : " + e.getMessage());
            alert.showAndWait();
            throw e;
        }
        java.net.URL cssUrl = resolveResource("/css/style.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }
        primaryStage.setTitle(titre);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
