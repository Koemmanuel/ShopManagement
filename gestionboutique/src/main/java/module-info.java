module tg.univlome.epl.gestionboutique {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // sqlite-jdbc et jbcrypt n'ont pas de vrai module-info :
    // ce sont des "automatic modules", nommes automatiquement a partir du nom du jar
   requires jbcrypt;

    // Ikonli fournit de vrais modules JPMS (noms officiels)
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.feather;

    // Necessaire pour que FXMLLoader accede par reflexion aux methodes @FXML
    opens tg.univlome.epl.gestionboutique to javafx.fxml;
    opens tg.univlome.epl.gestionboutique.controller to javafx.fxml;

    // Necessaire si les TableView utilisent PropertyValueFactory (reflexion sur les getters)
    opens tg.univlome.epl.gestionboutique.model to javafx.base;

    exports tg.univlome.epl.gestionboutique;
    exports tg.univlome.epl.gestionboutique.controller;
    exports tg.univlome.epl.gestionboutique.model;
}
