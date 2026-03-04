package up.rde.ui.fenetres.configuration;

//CLASSE FENETREAJOUTCONNEXION
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import up.rde.ui.composants.BoutonStyler;
import up.rde.ui.composants.ComposantBase;
import up.rde.ui.composants.HeaderBuilder;

/**
 * Fenêtre permettant d'ajouter une connexion entre un générateur et une maison
 * existants.
 * Fournit un champ de saisie pour entrer la connexion et un bouton pour la
 * valider.
 */

public class FenetreAjoutConnexion {
    private VBox layout;
    private Button ajoutConnexionBouton;
    private TextField input;

    /**
     * Constructeur de la fenêtre d'ajout de connexion.
     * Initialise le layout, le header, le champ de saisie et le bouton d'ajout.
     */

    public FenetreAjoutConnexion() {
        layout = ComposantBase.layoutBase();
        VBox header = HeaderBuilder.headerBase("Ajout d'une connexion", "", "/ajouter_connexion.png");

        VBox conteneurBouton = new VBox(12);
        conteneurBouton.setAlignment(Pos.CENTER);

        Label messageInput = ComposantBase
                .messageInput("Ajouter une connexion entre un générateur et une maison existants :");
        // Message d'instruction et champ de saisie
        input = ComposantBase.champInput("Exemple: M1 G1 ou G1 M1");
        ajoutConnexionBouton = BoutonStyler.creerBoutonJaune("+ Ajouter une connexion");

        conteneurBouton.getChildren().addAll(messageInput, input, ajoutConnexionBouton);
        layout.getChildren().addAll(header, conteneurBouton);
    }

    /**
     * Retourne le texte saisi dans le champ de connexion.
     * 
     * @return La chaîne de caractères saisie par l'utilisateur
     */
    public String getInput() {
        return input.getText();
    }

    /**
     * @return Le layout principal de la fenêtre sous forme de VBox
     */
    public VBox getLayout() {
        return layout;
    }

    /**
     * @return Le bouton pour ajouter la connexion
     */

    public Button getAjoutConnexionBouton() {
        return ajoutConnexionBouton;
    }

    /**
     * @return Le champ de texte pour la saisie de la connexion
     */
    public TextField getInputField() {
        return input;
    }
}