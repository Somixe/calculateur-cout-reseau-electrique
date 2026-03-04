package up.rde.ui.fenetres.configuration;

//CLASSE FENETREAJOUTGENERATEUR
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import up.rde.ui.composants.BoutonStyler;
import up.rde.ui.composants.ComposantBase;
import up.rde.ui.composants.HeaderBuilder;

/**
 * Fenêtre permettant d'ajouter un générateur au réseau électrique.
 * Fournit un champ de saisie pour entrer le nom et la capacité du générateur
 * ainsi qu'un bouton pour valider l'ajout.
 */

public class FenetreAjoutGenerateur {
    private VBox layout;
    private Button ajoutGenerateurBouton;
    private TextField input;

    /**
     * Constructeur de la fenêtre d'ajout d'un générateur.
     * Initialise le layout, le header, le champ de saisie et le bouton d'ajout.
     */

    public FenetreAjoutGenerateur() {
        layout = ComposantBase.layoutBase();
        VBox header = HeaderBuilder.headerBase("Ajouter un générateur", "", "/generateur.png");

        VBox conteneurBouton = new VBox(12);
        conteneurBouton.setAlignment(Pos.CENTER);
        // Message d'instruction et champ de saisie

        Label messageInput = ComposantBase.messageInput("Entrez le nom et la capacité maximale du générateur :");
        input = ComposantBase.champInput("Exemple: G1 60");
        ajoutGenerateurBouton = BoutonStyler.creerBoutonJaune("+ Ajouter un générateur");

        conteneurBouton.getChildren().addAll(messageInput, input, ajoutGenerateurBouton);
        layout.getChildren().addAll(header, conteneurBouton);
    }

    /**
     * Retourne le texte saisi dans le champ de générateur.
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
     * @return Le bouton pour ajouter le générateur
     */
    public Button getAjoutGenerateurBouton() {
        return ajoutGenerateurBouton;
    }

    /**
     * @return Le champ de texte pour la saisie du générateur
     */

    public TextField getInputField() {
        return input;
    }
}