package up.rde.ui.fenetres.configuration;

//CLASSE FENETREAJOUTMAISON
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import up.rde.ui.composants.BoutonStyler;
import up.rde.ui.composants.ComposantBase;
import up.rde.ui.composants.HeaderBuilder;

/**
 * Fenêtre permettant d'ajouter une maison au réseau électrique.
 * Fournit un champ de saisie pour entrer le nom et le type de consommation
 * ainsi qu'un bouton pour valider l'ajout.
 */

public class FenetreAjoutMaison {
    private VBox layout;
    private Button ajoutMaisonBouton;
    private TextField input;

    /**
     * Fenêtre permettant d'ajouter une maison au réseau électrique.
     * Fournit un champ de saisie pour entrer le nom et le type de consommation
     * ainsi qu'un bouton pour valider l'ajout.
     */
    public FenetreAjoutMaison() {
        layout = ComposantBase.layoutBase();
        VBox header = HeaderBuilder.headerBase("Ajouter une maison", "", "/maison_basse.png");

        VBox conteneurBouton = new VBox(12);
        conteneurBouton.setAlignment(Pos.CENTER);
        // Message d'instruction et champ de saisie

        Label messageInput = ComposantBase.messageInput("Entrez le nom et le type de consommation de la maison :");
        input = ComposantBase.champInput("Exemple: M1 FORTE");
        ajoutMaisonBouton = BoutonStyler.creerBoutonJaune("+ Ajouter une maison");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label labelInfo = new Label("Type de consommation possible : FORTE (40kW) / NORMAL (20kW) / BASSE (10 kW)");
        labelInfo.setStyle("-fx-font-size: 13px; -fx-text-fill: #666666;");

        conteneurBouton.getChildren().addAll(messageInput, input, ajoutMaisonBouton, spacer, labelInfo);
        layout.getChildren().addAll(header, conteneurBouton);
    }

    /**
     * Retourne le texte saisi dans le champ de maison.
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
     * @return Le bouton pour ajouter la maison
     */

    public Button getAjoutMaisonBouton() {
        return ajoutMaisonBouton;
    }

    /**
     * @return Le champ de texte pour la saisie de la maison
     */
    public TextField getInputField() {
        return input;
    }
}