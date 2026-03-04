package up.rde.ui.fenetres.affichage;

//CLASSE PANNEAUINFORMATIONS
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import up.rde.modele.ReseauElectrique;
import up.rde.ui.InterfaceUtilisateur;
import up.rde.utilitaire.CalculateurCout;

/**
 * Classe utilitaire pour créer le panneau d'informations affichant
 * les détails du coût et des statistiques du réseau électrique.
 */

public class PanneauInformations {
    /**
     * Crée un panneau graphique contenant les informations sur le coût du réseau.
     * Affiche également les résultats d'optimisation si la résolution automatique
     * est utilisée.
     *
     * @param ui                      Instance de l'interface utilisateur contenant
     *                                le réseau et lambda
     * @param estDepuisResolutionAuto true si l'affichage est lancé depuis la
     *                                résolution automatique
     * @param dejaResolu              true si le réseau a déjà été résolu pour
     *                                éviter recalcul
     * @return Un HBox contenant le panneau d'informations prêt à être ajouté à l'UI
     * @throws Exception Si une erreur survient lors de la résolution automatique
     */

    public static HBox creerPanneauInformations(InterfaceUtilisateur ui, boolean estDepuisResolutionAuto,
            boolean dejaResolu) throws Exception {
        VBox panneauInfo = new VBox(8);
        panneauInfo.setPadding(new Insets(15));
        panneauInfo.setAlignment(Pos.TOP_LEFT);
        panneauInfo.setStyle("-fx-background-color: white; " +
                "-fx-border-color: #CCCCCC; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 8; " +
                "-fx-background-radius: 8;");

        panneauInfo.setPrefWidth(280);
        panneauInfo.setMaxHeight(450);
        // Titre

        Label titre = new Label("Détails du Coût");
        titre.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333;");

        // Zone de texte avec les informations
        TextArea texteInfo = new TextArea();
        texteInfo.setEditable(false);
        texteInfo.setWrapText(true);
        texteInfo.setStyle("-fx-font-family: 'Consolas', 'Monospaced'; -fx-font-weight: bold; -fx-font-size: 12px;");
        texteInfo.setPrefHeight(350);
        texteInfo.setFocusTraversable(false);

        StringBuilder contenu = new StringBuilder();

        ReseauElectrique reseau = ui.getReseau();
        double lambda = ui.getLambda();

        if (estDepuisResolutionAuto) {
            if (!dejaResolu) {
                // Récupération du log de l'algorithme automatique
                String logAlgo = ui.gererResolutionAutomatique();
                if (logAlgo != null && !logAlgo.isEmpty()) {
                    contenu.append(logAlgo).append("\n\n");
                }
            }
            contenu.append("== RÉSULTAT OPTIMISATION ==\n\n");
        } else {
            contenu.append("== ÉTAT ACTUEL ==\n\n");
        }
        // Calcul des statistiques

        double dispersion = CalculateurCout.calculerDispersion(reseau);
        double surcharge = CalculateurCout.calculerSurcharge(reseau);
        double coutTotal = CalculateurCout.coutInstanceReseau(reseau, lambda);

        contenu.append(String.format("Dispersion : %10.4f\n", dispersion));
        contenu.append(String.format("Surcharge  : %10.4f\n", surcharge));
        contenu.append(String.format("Lambda     : %10.4f\n", lambda));
        contenu.append("----------------------\n");
        contenu.append(String.format("COÛT TOTAL : %10.4f\n", coutTotal));
        // Ajout au panneau

        texteInfo.setText(contenu.toString());
        panneauInfo.getChildren().addAll(titre, texteInfo);

        HBox conteneur = new HBox(panneauInfo);
        conteneur.setAlignment(Pos.TOP_RIGHT);

        return conteneur;
    }
}