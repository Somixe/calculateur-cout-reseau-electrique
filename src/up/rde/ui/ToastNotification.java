package up.rde.ui;

// CLASSE TOASTNOTIFICATION
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Classe utilitaire pour afficher des notifications toast dans l'interface
 * graphique.
 * Les notifications peuvent indiquer un succès ou une erreur.
 */

public class ToastNotification {
    /**
     * Affiche un toast de notification dans la fenêtre donnée.
     *
     * @param stage   La fenêtre dans laquelle afficher le toast
     * @param message Le message à afficher
     * @param succes  True pour une notification de succès, false pour une erreur
     */

    public static void show(Stage stage, String message, boolean succes) {
        Scene scene = stage.getScene();
        if (scene == null || scene.getRoot() == null)
            return;

        // Icône
        ImageView icone = new ImageView();
        String cheminIcone = succes ? "file:ressources/succes.png" : "file:ressources/erreur.png";
        icone.setImage(new Image(cheminIcone));
        icone.setFitWidth(25);
        icone.setFitHeight(25);

        // Titre
        Label titre = new Label(succes ? "Succès !" : "Erreur !");
        titre.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1a1a1a;");

        // Message
        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #5a5a5a;");
        messageLabel.setMaxWidth(675);

        // Texte vertical
        VBox texte = new VBox(3, titre, messageLabel);

        // Tout horizontal
        HBox contenu = new HBox(12, icone, texte);
        contenu.setPadding(new Insets(12, 18, 12, 18));
        contenu.setAlignment(Pos.CENTER_LEFT);
        contenu.setMaxWidth(675);
        contenu.setMaxHeight(20);

        // Couleurs
        String couleurBordure = succes ? "#4CAF50" : "#F44336";
        String couleurFond = succes ? "#E8F5E9" : "#FFEBEE";

        contenu.setStyle("-fx-background-color: " + couleurFond + ";" +
                "-fx-border-color: " + couleurBordure + ";" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 6;" +
                "-fx-background-radius: 6;");

        // Toast
        StackPane toastPane = new StackPane(contenu);
        toastPane.setAlignment(Pos.TOP_CENTER);
        toastPane.setPadding(new Insets(20));
        toastPane.setMouseTransparent(true); // Les clics passent à travers
        toastPane.setPickOnBounds(false); // Ne capte pas les événements

        // Ajouter
        if (scene.getRoot() instanceof StackPane) {
            ((StackPane) scene.getRoot()).getChildren().add(toastPane);
        } else {
            StackPane wrapper = new StackPane(scene.getRoot(), toastPane);
            scene.setRoot(wrapper);
        }

        // Animation descente
        TranslateTransition descente = new TranslateTransition(Duration.seconds(0.5), toastPane);
        descente.setFromY(-100);
        descente.setToY(0);
        descente.play();

        // Attendre 3 secondes puis remonter
        TranslateTransition remontee = new TranslateTransition(Duration.seconds(0.5), toastPane);
        remontee.setDelay(Duration.seconds(3)); // Attend 3 secondes
        remontee.setFromY(0);
        remontee.setToY(-100);
        remontee.setOnFinished(e -> { // Quand l'animation est finie
            if (scene.getRoot() instanceof StackPane) {
                ((StackPane) scene.getRoot()).getChildren().remove(toastPane); // Supprime le toast
            }
        });
        remontee.play(); // Lance l'animation
    }

    /**
     * Affiche un toast de succès.
     *
     * @param stage   La fenêtre dans laquelle afficher le toast
     * @param message Le message à afficher
     */
    public static void succes(Stage stage, String message) {
        show(stage, message, true);
    }

    /**
     * Affiche un toast d'erreur.
     *
     * @param stage   La fenêtre dans laquelle afficher le toast
     * @param message Le message à afficher
     */

    public static void erreur(Stage stage, String message) {
        show(stage, message, false);
    }
}