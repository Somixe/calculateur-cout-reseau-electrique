package up.rde.ui.fenetres.gestion;

//CLASSE FENETREGESTION
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import up.rde.ui.composants.BoutonStyler;
import up.rde.ui.composants.ComposantBase;
import up.rde.ui.composants.HeaderBuilder;

/**
 * Représente la fenêtre de gestion du réseau électrique.
 * Cette fenêtre permet d'effectuer des actions comme la résolution automatique
 * du réseau,
 * la sauvegarde de l'état actuel, ou de terminer la gestion.
 */

public class FenetreGestion {
    private VBox layout;
    private Button resolutionAuto;
    private Button sauvegarderReseau;
    private Button finBouton;

    /**
     * Construit la fenêtre de gestion du réseau.
     * Initialise le layout principal, le header et les boutons d'action.
     */
    public FenetreGestion() {
        layout = ComposantBase.layoutBase();
        VBox header = HeaderBuilder.headerBase("Gestion du réseau", "Électrique", "gestion_reseau.png");
        VBox conteneurBouton = new VBox(12);
        conteneurBouton.setAlignment(Pos.CENTER);

        resolutionAuto = BoutonStyler.creerBoutonJaune("Résolution automatique");
        sauvegarderReseau = BoutonStyler.creerBoutonJaune("Sauvegarder la solution actuelle");
        finBouton = BoutonStyler.creerBoutonJaune("Fin");

        conteneurBouton.getChildren().addAll(resolutionAuto, sauvegarderReseau, finBouton);
        layout.getChildren().addAll(header, conteneurBouton);
    }

    /**
     * @return le layout principal de la fenêtre
     */
    public VBox getLayout() {
        return layout;
    }

    /**
     * @return le bouton pour lancer la résolution automatique du réseau
     */
    public Button getResolutionAuto() {
        return resolutionAuto;
    }

    /**
     * @return le bouton pour sauvegarder l'état actuel du réseau
     */
    public Button getSauvegarderReseau() {
        return sauvegarderReseau;
    }

    /**
     * @return le bouton pour terminer la gestion et fermer la fenêtre
     */
    public Button getFinBouton() {
        return finBouton;
    }
}