package up.rde.ui.fenetres.configuration;

//CLASSE FENETRECONFIGURATION
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import up.rde.ui.composants.BoutonStyler;
import up.rde.ui.composants.ComposantBase;
import up.rde.ui.composants.HeaderBuilder;

/**
 * Fenêtre principale de configuration du réseau électrique.
 * Permet d'ajouter des générateurs, des maisons, de gérer les connexions
 * et de terminer la configuration via des boutons d'action.
 */

public class FenetreConfiguration {
    private VBox layout;
    private Button ajoutGenerateurBouton;
    private Button ajoutMaisonBouton;
    private Button ajoutConnexionBouton;
    private Button supprimerConnexionBouton;
    private Button finBouton;

    /**
     * Constructeur de la fenêtre de configuration.
     * Initialise le layout principal, le header et tous les boutons de gestion.
     */
    public FenetreConfiguration() {
        layout = ComposantBase.layoutBase();
        VBox header = HeaderBuilder.headerBase("Configuration du réseau", "Électrique", "/configuration_reseau.png");
        VBox conteneurBouton = new VBox(12);
        conteneurBouton.setAlignment(Pos.CENTER);

        // Initialisation des boutons

        ajoutGenerateurBouton = BoutonStyler.creerBoutonJaune("Ajouter un générateur");
        ajoutMaisonBouton = BoutonStyler.creerBoutonJaune("Ajouter une maison");
        ajoutConnexionBouton = BoutonStyler
                .creerBoutonJaune("Ajouter une connexion entre une maison et un générateur existants");
        supprimerConnexionBouton = BoutonStyler
                .creerBoutonJaune("Supprimer une connexion existante entre une maison et un générateur");
        finBouton = BoutonStyler.creerBoutonJaune("Fin");

        conteneurBouton.getChildren().addAll(ajoutGenerateurBouton, ajoutMaisonBouton, ajoutConnexionBouton,
                supprimerConnexionBouton, finBouton);
        layout.getChildren().addAll(header, conteneurBouton);
    }

    /**
     * @return Le layout principal de la fenêtre sous forme de VBox
     */
    public VBox getLayout() {
        return layout;
    }

    /**
     * @return Le bouton pour ajouter un générateur
     */
    public Button getAjoutGenerateurBouton() {
        return ajoutGenerateurBouton;
    }

    /**
     * @return Le bouton pour ajouter une maison
     */
    public Button getAjoutMaisonBouton() {
        return ajoutMaisonBouton;
    }

    /**
     * @return Le bouton pour ajouter une connexion
     */
    public Button getAjoutConnexionBouton() {
        return ajoutConnexionBouton;
    }

    /**
     * @return Le bouton pour supprimer une connexion
     */
    public Button getSupprimerConnexionBouton() {
        return supprimerConnexionBouton;
    }

    /**
     * @return Le bouton pour terminer la configuration
     */
    public Button getFinBouton() {
        return finBouton;
    }
}