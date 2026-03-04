package up.rde.ui.composants;

//CLASSE HEADERBUILDER
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Classe utilitaire permettant de créer un en-tête graphique
 * pour l'interface utilisateur JavaFX.
 */

public class HeaderBuilder {
    /**
     * Crée un en-tête contenant une image, un titre, un sous-titre
     * et un séparateur.
     * 
     * @param titre       Le titre principal affiché
     * @param sousTitre   Le sous-titre affiché (peut être vide)
     * @param cheminImage Le nom du fichier image à afficher
     * @return Un conteneur VBox représentant l'en-tête
     */

    public static VBox headerBase(String titre, String sousTitre, String cheminImage) {
        VBox header = new VBox(8);

        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: transparent;" +
                "-fx-padding: 20 20 15 20;");

        // Icône de l'en-tête
        ImageView icone = ComposantBase.image(cheminImage);

        // Titre principal
        Label labelTitre = new Label(titre);
        labelTitre.setStyle("-fx-font-family: 'Arial';" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 30px;" +
                "-fx-text-fill: black;");

        // Sous-titre
        Label labelSousTitre = new Label(sousTitre);
        labelSousTitre.setStyle("-fx-font-family: 'Arial';" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 20px;" +
                "-fx-text-fill: #808080;");

        // Séparateur graphique
        Line separateur = new Line();
        separateur.setStartX(0);
        separateur.setEndX(550);
        separateur.setStroke(Color.GRAY);
        separateur.setStrokeWidth(1);

        // Construction de l'en-tête
        header.getChildren().addAll(icone, labelTitre);

        if (!labelSousTitre.getText().isEmpty()) {
            header.getChildren().add(labelSousTitre);
        }

        header.getChildren().add(separateur);

        return header;
    }
}