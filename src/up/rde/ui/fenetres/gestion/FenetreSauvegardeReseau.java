package up.rde.ui.fenetres.gestion;

// CLASSE FENETRESAUVEGARDERESEAU
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import up.rde.ui.composants.BoutonStyler;
import up.rde.ui.composants.ComposantBase;
import up.rde.ui.composants.HeaderBuilder;

/**
 * Représente la fenêtre permettant de sauvegarder l'état du réseau électrique.
 * L'utilisateur peut saisir un nom de fichier et déclencher la sauvegarde.
 */

public class FenetreSauvegardeReseau {
    private VBox layout;
    private Button ajoutSauvegardeBouton;
    private TextField input;

    /**
     * Construit la fenêtre de sauvegarde du réseau.
     * Initialise le layout principal, le header, le label, le champ de saisie et le
     * bouton.
     */
    public FenetreSauvegardeReseau() {
        layout = ComposantBase.layoutBase();
        VBox header = HeaderBuilder.headerBase("Enregistrer le réseau", "", "dossier.png");

        VBox conteneurBouton = new VBox(12);
        conteneurBouton.setAlignment(Pos.CENTER);

        Label messageInput = ComposantBase.messageInput("Entrez le nom du fichier à sauvegarder :");
        input = ComposantBase.champInput("Exemple: reseauSauvegarde.txt ou reseauSauvegarde");
        ajoutSauvegardeBouton = BoutonStyler.creerBoutonJaune("Sauvegarder le réseau");

        conteneurBouton.getChildren().addAll(messageInput, input, ajoutSauvegardeBouton);
        layout.getChildren().addAll(header, conteneurBouton);
    }

    /**
     * @return le texte saisi dans le champ input (nom du fichier)
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
     * @return le bouton permettant de sauvegarder le réseau
     */
    public Button getAjoutSauvegardeBouton() {
        return ajoutSauvegardeBouton;
    }

    /**
     * @return le champ de saisie pour récupérer le texte entré
     */

    public TextField getInputField() {
        return input;
    }
}