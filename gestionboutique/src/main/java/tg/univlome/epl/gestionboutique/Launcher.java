package tg.univlome.epl.gestionboutique;

/**
 * Classe de démarrage utilisée comme Main-Class dans le jar packagé.
 *
 * Pourquoi cette classe existe :
 * Quand un jar JavaFX est lancé directement (java -jar ...) ou via jpackage,
 * la JVM vérifie AVANT même de démarrer le module JavaFX si la classe
 * principale (Main-Class du manifest) hérite de javafx.application.Application.
 * Si c'est le cas et que le module javafx.graphics n'est pas correctement
 * détecté sur le classpath (ce qui arrive facilement avec un jar "fat"
 * généré par le maven-shade-plugin), on obtient l'erreur classique :
 *
 *   Error: JavaFX runtime components are missing, and are required to run this application
 *
 * La solution standard est d'avoir une classe de lancement séparée, qui
 * N'HÉRITE PAS de Application, et qui se contente d'appeler App.main(args).
 * La JVM ne fait alors plus cette vérification prématurée, et JavaFX peut
 * s'initialiser normalement une fois le module chargé.
 *
 * C'est donc CETTE classe (Launcher) qu'il faut déclarer comme classe
 * principale dans jpackage et dans le manifest du jar — pas App.
 */
public class Launcher {

    public static void main(String[] args) {
        App.main(args);
    }
}
