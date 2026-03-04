package up.rde.ui.fenetres.configuration;

//CLASSE FENETRESUPPRIMERCONNEXION
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import up.rde.ui.composants.BoutonStyler;
import up.rde.ui.composants.ComposantBase;
import up.rde.ui.composants.HeaderBuilder;

/**
 * Représente la fenêtre permettant de supprimer une connexion entre une maison
 * et un générateur.
 * Cette fenêtre contient un champ de saisie et un bouton pour confirmer la
 * suppression.
 */
public class FenetreSupprimerConnexion {
    private VBox layout;
    private Button ajoutSupprimerBouton;
    private TextField input;

    /**
     * Construit la fenêtre de suppression de connexion.
     * Initialise le layout principal, le header et les composants (label, champ
     * input, bouton).
     */

    public FenetreSupprimerConnexion() {
        layout = ComposantBase.layoutBase();
        VBox header = HeaderBuilder.headerBase("Supprimer d'une connexion", "", "/supprimer_connexion.png");

        VBox conteneurBouton = new VBox(12);
        conteneurBouton.setAlignment(Pos.CENTER);

        Label messageInput = ComposantBase
                .messageInput("Supprimer une connexion existante entre une maison et un générateur :");
        input = ComposantBase.champInput("Exemple: M1 G1 ou G1 M1");
        ajoutSupprimerBouton = BoutonStyler.creerBoutonJaune("+ Supprimer une connexion");

        conteneurBouton.getChildren().addAll(messageInput, input, ajoutSupprimerBouton);
        layout.getChildren().addAll(header, conteneurBouton);
    }

    /**
     * @return le texte saisi dans le champ input
     */
    public String getInput() {
        return input.getText();
    }

    /**
     * @return le layout principal de la fenêtre
     */
    public VBox getLayout() {
        return layout;
    }

    /**
     * @return le bouton permettant de supprimer une connexion
     */
    public Button getAjoutSupprimerBouton() {
        return ajoutSupprimerBouton;
    }

    /**
     * @return le champ de saisie pour récupérer le texte entré
     */
    public TextField getInputField() {
        return input;
    }
}