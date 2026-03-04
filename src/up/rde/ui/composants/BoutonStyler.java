package up.rde.ui.composants;

//CLASSE BOUTONSTYLER
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

/**
 * Classe utilitaire permettant de créer des boutons JavaFX
 * avec un style graphique homogène.
 */

public class BoutonStyler {
    /**
     * Couleur principale utilisée pour les boutons.
     */

    private static final String COULEUR_PRIMAIRE = "#FFD700";

    /**
     * Crée et configure un effet d'ombre pour les boutons.
     * 
     * @return Un effet DropShadow configuré
     */

    private static DropShadow ombre() {
        DropShadow ombre = new DropShadow();
        ombre.setRadius(10);
        ombre.setOffsetX(1);
        ombre.setOffsetY(2);
        ombre.setColor(Color.GRAY);
        return ombre;
    }

    /**
     * Crée un bouton jaune stylisé avec effets de survol et de clic.
     * 
     * @param texte Le texte affiché sur le bouton
     * @return Un bouton JavaFX stylisé
     */

    public static Button creerBoutonJaune(String texte) {
        Button btn = new Button(texte);
        String couleurHover = "#FFC700";
        String couleurClick = "#FFB200";

        btn.setMaxWidth(600);
        btn.setEffect(ombre());
        btn.setAlignment(Pos.CENTER);

        String styleBase = "-fx-text-fill: black;" +
                "-fx-font-weight: bold;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 6;" +
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 6;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 6 10 6 10;";

        btn.setStyle("-fx-background-color: " + COULEUR_PRIMAIRE + ";" + styleBase);
        // Gestion des événements de souris

        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: " + couleurHover + ";" + styleBase));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + COULEUR_PRIMAIRE + ";" + styleBase));
        btn.setOnMousePressed(e -> btn.setStyle("-fx-background-color: " + couleurClick + ";" + styleBase));
        btn.setOnMouseReleased(e -> {
            String couleur = btn.isHover() ? couleurHover : COULEUR_PRIMAIRE;
            btn.setStyle("-fx-background-color: " + couleur + ";" + styleBase);
        });

        return btn;
    }
}