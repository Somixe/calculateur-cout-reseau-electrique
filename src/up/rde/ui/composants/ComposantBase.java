package up.rde.ui.composants;

//CLASSE COMPOSANTBASE
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * Classe utilitaire fournissant des composants graphiques de base
 * pour l'interface utilisateur JavaFX.
 */

public class ComposantBase {

    /**
     * Crée un conteneur VBox de base avec un fond et un alignement centrés.
     * 
     * @return Un conteneur VBox configuré
     */
    public static VBox layoutBase() {
        VBox conteneur = new VBox(10);
        conteneur.setAlignment(Pos.CENTER);
        conteneur.setPadding(new Insets(0, 0, 70, 0));
        conteneur.setStyle("-fx-background-image: url('file:ressources/background.png');" +
                "-fx-background-size: cover;" +
                "-fx-background-position: center;");

        return conteneur;
    }

    /**
     * Crée une image affichable à partir d'un fichier.
     * 
     * @param cheminImage Nom du fichier image (dans le dossier ressources)
     * @return Une ImageView configurée
     */

    public static ImageView image(String cheminImage) {
        Image chargerImage = new Image("file:ressources/" + cheminImage);
        ImageView image = new ImageView(chargerImage);
        image.setFitWidth(200);
        image.setFitHeight(55);
        image.setPreserveRatio(true);
        return image;
    }

    /**
     * Crée un label utilisé pour afficher un message d'entrée utilisateur.
     * 
     * @param message Le texte à afficher
     * @return Un Label stylisé
     */

    public static Label messageInput(String message) {
        Label m = new Label(message);
        m.setStyle("-fx-font-family: 'Arial';" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 15px;" +
                "-fx-text-fill: black;");

        return m;
    }

    /**
     * Crée un champ de saisie texte stylisé.
     * 
     * @param placeholder Texte affiché en indicateur dans le champ
     * @return Un TextField configuré
     */

    public static TextField champInput(String placeholder) {
        TextField textField = new TextField();
        textField.setFocusTraversable(false);
        textField.setPromptText(placeholder);
        textField.setMaxWidth(600);
        textField.setStyle("-fx-font-size: 15px;" +
                "-fx-background-color: #EBEBEB;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 5;" +
                "-fx-background-radius: 5;" +
                "-fx-padding: 10;");

        return textField;
    }
}