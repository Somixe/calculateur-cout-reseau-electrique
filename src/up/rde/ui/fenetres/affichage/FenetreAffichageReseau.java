package up.rde.ui.fenetres.affichage;

//CLASSE FENETREAFFICHAGERESEAU
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import up.rde.ui.InterfaceUtilisateur;
import up.rde.ui.composants.ComposantBase;
import up.rde.ui.composants.HeaderBuilder;

/**
 * Fenêtre d'affichage du réseau électrique.
 * Permet de visualiser graphiquement les générateurs et les maisons,
 * ainsi que les connexions entre eux.
 */

public class FenetreAffichageReseau {
    private VBox layout;
    private Pane zoneDessin;
    private Button boutonArriere;
    private Button boutonSuivant;
    private boolean estDepuisResolutionAuto;
    private DessineurReseau dessineurReseau;

    /**
     * Constructeur de la fenêtre d'affichage.
     * Initialise les composants graphiques et dessine le réseau électrique.
     * 
     * @param ui                   Instance de l'interface utilisateur
     * @param depuisResolutionAuto true si l'affichage est lancé depuis la
     *                             résolution automatique
     * @param dejaResolu           true si le réseau a déjà été résolu
     * @throws Exception Si une erreur survient lors de l'initialisation graphique
     */
    public FenetreAffichageReseau(InterfaceUtilisateur ui, boolean depuisResolutionAuto, boolean dejaResolu)
            throws Exception {
        this.estDepuisResolutionAuto = depuisResolutionAuto;

        // === CONTENEUR DE NAVIGATION ===
        HBox conteneurNav = new HBox();
        conteneurNav.setSpacing(100);
        conteneurNav.setAlignment(Pos.CENTER);

        // Bouton Arrière
        if ((ui.estFichierImporte() && estDepuisResolutionAuto) ||
                (!ui.estFichierImporte() && !estDepuisResolutionAuto) ||
                (!ui.estFichierImporte() && estDepuisResolutionAuto)) {
            ImageView imgPrec = ComposantBase.image("arriere.png");
            imgPrec.setFitWidth(30);
            imgPrec.setPreserveRatio(true);

            boutonArriere = new Button();
            boutonArriere.setGraphic(imgPrec);
            boutonArriere.setStyle("-fx-background-color: transparent; -fx-padding: 2;");
            boutonArriere.setCursor(Cursor.HAND);

            conteneurNav.getChildren().add(boutonArriere);
        }

        // Bouton Suivant
        if (!estDepuisResolutionAuto) {
            ImageView imgSuiv = ComposantBase.image("arriere.png");
            imgSuiv.setFitWidth(30);
            imgSuiv.setPreserveRatio(true);

            boutonSuivant = new Button();
            boutonSuivant.setGraphic(imgSuiv);
            boutonSuivant.setStyle("-fx-background-color: transparent; -fx-padding: 2;");
            boutonSuivant.setCursor(Cursor.HAND);
            boutonSuivant.setScaleX(-1);

            conteneurNav.getChildren().add(boutonSuivant);
        }

        // === CONTENEUR PRINCIPAL ===
        VBox conteneurPrincipal = new VBox(10);
        conteneurPrincipal.setAlignment(Pos.TOP_CENTER);
        conteneurPrincipal.setPadding(new Insets(10));
        conteneurPrincipal.setStyle("-fx-background-color: #F5F5F5;");

        VBox header = HeaderBuilder.headerBase("Affichage du réseau électrique", "", "affichage_reseau.png");

        // === CONTENEUR RÉSEAU ===
        HBox conteneurReseau = new HBox(10);
        conteneurReseau.setAlignment(Pos.CENTER);

        zoneDessin = new Pane();
        zoneDessin.setStyle("-fx-background-color: white; -fx-border-color: #CCCCCC; -fx-border-width: 1;");

        ScrollPane scrollReseau = new ScrollPane();
        scrollReseau.setStyle("-fx-background: white;");
        scrollReseau.setPrefSize(700, 500);
        scrollReseau.setPannable(true);
        scrollReseau.setHvalue(0.5);
        scrollReseau.setVvalue(0.3);
        scrollReseau.setContent(zoneDessin);

        // Dessiner le réseau
        dessineurReseau = new DessineurReseau(zoneDessin, ui);
        dessineurReseau.dessinerReseau();

        HBox panneauInfo = PanneauInformations.creerPanneauInformations(ui, estDepuisResolutionAuto, dejaResolu);

        conteneurReseau.getChildren().addAll(scrollReseau, panneauInfo);
        conteneurPrincipal.getChildren().addAll(header, conteneurReseau, conteneurNav);

        this.layout = conteneurPrincipal;
    }

    /**
     * @return Le conteneur principal VBox de la fenêtre
     */
    public VBox getLayout() {
        return layout;
    }

    /**
     * @return Le bouton "Arrière" si présent, null sinon
     */

    public Button getBoutonArriere() {
        return boutonArriere;
    }

    /**
     * @return Le bouton "Suivant" si présent, null sinon
     */

    public Button getBoutonSuivant() {
        return boutonSuivant;
    }
}