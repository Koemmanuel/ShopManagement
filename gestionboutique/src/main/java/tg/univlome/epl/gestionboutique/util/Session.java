package tg.univlome.epl.gestionboutique.util;

import tg.univlome.epl.gestionboutique.model.Utilisateur;

/** Singleton simple pour garder en mémoire l'utilisateur connecté. */
public class Session {
    private static Utilisateur utilisateurConnecte;

    private Session() {}

    public static void setUtilisateurConnecte(Utilisateur u) {
        utilisateurConnecte = u;
    }

    public static Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    public static void deconnexion() {
        utilisateurConnecte = null;
    }
}
