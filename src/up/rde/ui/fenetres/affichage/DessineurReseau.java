package up.rde.ui.fenetres.affichage;

//CLASSE DESSINERRESEAU
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.geometry.Pos;
import up.rde.modele.Generateur;
import up.rde.modele.Maison;
import up.rde.modele.ReseauElectrique;
import up.rde.ui.InterfaceUtilisateur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe responsable de l'affichage graphique du réseau électrique.
 * Elle dessine les générateurs, les maisons et leurs connexions
 * dans une zone de dessin JavaFX.
 */

public class DessineurReseau {

    private Pane zoneDessin;
    private InterfaceUtilisateur ui;
    private Map<String, VBox> mapNomsBoxGen;
    private Map<String, VBox> mapNomsBoxMaison;
    private List<VBox> listeVBoxGen;
    private List<VBox> listeVBoxMaison;

    /**
     * Construit un dessineur de réseau.
     * 
     * @param zoneDessin Zone graphique JavaFX
     * @param ui         Interface utilisateur principale
     */

    public DessineurReseau(Pane zoneDessin, InterfaceUtilisateur ui) {
        this.zoneDessin = zoneDessin;
        this.ui = ui;
        this.mapNomsBoxGen = new HashMap<>();
        this.mapNomsBoxMaison = new HashMap<>();
        this.listeVBoxGen = new ArrayList<>();
        this.listeVBoxMaison = new ArrayList<>();
    }

    /**
     * Dessine l'ensemble du réseau électrique :
     * générateurs, maisons et connexions.
     */
    public void dessinerReseau() {
        ReseauElectrique reseau = ui.getReseau();
        Map<Maison, Generateur> connexions = reseau.getConnexions();

        List<Generateur> genConnectes = reseau.getGenerateursConnectes();
        List<Generateur> genNonConnectes = reseau.getGenerateursNonConnectes();
        List<Maison> maisonsConnectees = reseau.getMaisonsConnectees();
        List<Maison> maisonsNonConnectees = reseau.getMaisonsNonConnecteesListe();

        double largeurBoite = 70;
        double hauteurBoite = 90;
        double espacementHorizontal = 20;
        double margeHaut = 45;
        double margeBas = 250;

        int nbMaxConnectes = Math.max(genConnectes.size(), maisonsConnectees.size());
        int nbMaxNonConnectes = Math.max(genNonConnectes.size(), maisonsNonConnectees.size());
        int nbMax = Math.max(nbMaxConnectes, nbMaxNonConnectes);

        double largeurZone = Math.max(800, nbMax * (largeurBoite + espacementHorizontal) + 100);

        zoneDessin.setPrefSize(largeurZone + 200, 365);
        zoneDessin.setMinSize(largeurZone + 200, 365);

        double startXGen = (largeurZone - (genConnectes.size() * (largeurBoite + espacementHorizontal))) / 2;
        double startXMaison = (largeurZone - (maisonsConnectees.size() * (largeurBoite + espacementHorizontal))) / 2;

        double xGen = startXGen;
        // Générateurs connectés

        for (Generateur g : genConnectes) {
            double charge = reseau.getChargeGenerateur(g);
            double utilisation = (charge / g.getCapacite()) * 100;

            VBox gen = creerGenerateur(g.getNom(), g.getCapacite(), utilisation);
            gen.setLayoutX(xGen);
            gen.setLayoutY(margeHaut);
            gen.setCursor(Cursor.HAND);

            mapNomsBoxGen.put(g.getNom(), gen);

            gen.setOnMouseClicked(e -> {
                mettreEnSurbrillance(g.getNom(), true, connexions, genConnectes, maisonsConnectees);
            });

            listeVBoxGen.add(gen);
            zoneDessin.getChildren().add(gen);

            xGen += largeurBoite + espacementHorizontal;
        }
        // Générateurs non connectés

        for (Generateur g : genNonConnectes) {
            double charge = reseau.getChargeGenerateur(g);
            double utilisation = (charge / g.getCapacite()) * 100;

            VBox gen = creerGenerateur(g.getNom(), g.getCapacite(), utilisation);
            gen.setLayoutX(xGen);
            gen.setLayoutY(margeHaut);

            zoneDessin.getChildren().add(gen);

            xGen += largeurBoite + espacementHorizontal;
        }

        double xMaison = startXMaison;
        // Maisons connectées

        for (Maison m : maisonsConnectees) {
            VBox maison = creerMaison(m.getNom(), m.getTypeConsommation().toString(), m.getDemande());
            maison.setLayoutX(xMaison);
            maison.setLayoutY(margeBas);
            maison.setCursor(Cursor.HAND);

            mapNomsBoxMaison.put(m.getNom(), maison);

            maison.setOnMouseClicked(e -> {
                mettreEnSurbrillance(m.getNom(), false, connexions, genConnectes, maisonsConnectees);
            });

            listeVBoxMaison.add(maison);
            zoneDessin.getChildren().add(maison);

            xMaison += largeurBoite + espacementHorizontal;
        }

        // Maisons non connectées
        for (Maison m : maisonsNonConnectees) {
            VBox maison = creerMaison(m.getNom(), m.getTypeConsommation().toString(), m.getDemande());
            maison.setLayoutX(xMaison);
            maison.setLayoutY(margeBas);
            maison.setOpacity(0.4);

            zoneDessin.getChildren().add(maison);

            xMaison += largeurBoite + espacementHorizontal;
        }

        dessinerConnexions(connexions, maisonsConnectees, genConnectes, largeurBoite, hauteurBoite);
    }

    /**
     * Dessine les lignes reliant maisons et générateurs.
     * 
     * @param connexions        Map des connexions maison → générateur
     * @param maisonsConnectees Liste des maisons connectées
     * @param genConnectes      Liste des générateurs connectés
     * @param largeurBoite      Largeur des boîtes graphiques
     * @param hauteurBoite      Hauteur des boîtes graphiques
     */
    private void dessinerConnexions(Map<Maison, Generateur> connexions, List<Maison> maisonsConnectees,
            List<Generateur> genConnectes, double largeurBoite, double hauteurBoite) {

        for (Maison maison : connexions.keySet()) {
            Generateur generateur = connexions.get(maison);

            int indexMaison = maisonsConnectees.indexOf(maison);
            int indexGen = genConnectes.indexOf(generateur);

            if (indexMaison >= 0 && indexGen >= 0) {
                VBox vboxGen = listeVBoxGen.get(indexGen);
                VBox vboxMaison = listeVBoxMaison.get(indexMaison);

                double x1 = vboxGen.getLayoutX() + largeurBoite / 2;
                double y1 = vboxGen.getLayoutY() + hauteurBoite;

                double x2 = vboxMaison.getLayoutX() + largeurBoite / 2;
                double y2 = vboxMaison.getLayoutY();

                Line connexion = new Line(x1, y1, x2, y2);
                connexion.setStrokeWidth(1.5);
                connexion.setStroke(Color.BLACK);

                zoneDessin.getChildren().add(0, connexion);
            }
        }
    }

    /**
     * Crée la représentation graphique d'un générateur.
     */
    private VBox creerGenerateur(String nom, double capacite, double utilisation) {
        VBox gen = new VBox(3);
        gen.setAlignment(Pos.CENTER);
        gen.setStyle("-fx-background-color: white; " +
                "-fx-border-color: black; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 5; " +
                "-fx-background-radius: 5; " +
                "-fx-padding: 5;");
        gen.setPrefWidth(70);
        gen.setPrefHeight(90);

        ImageView icone = new ImageView(new Image("file:ressources/generateur.png"));
        icone.setFitWidth(25);
        icone.setPreserveRatio(true);

        Label labelNom = new Label(nom);
        labelNom.setStyle("-fx-font-weight: bold; -fx-font-size: 10px;");

        Label labelCapa = new Label((int) capacite + "kW");
        labelCapa.setStyle("-fx-font-size: 8px;");

        gen.getChildren().addAll(labelNom, icone, labelCapa);

        if (utilisation > 100) {
            Label labelSurcharge = new Label("SURCHARGE");
            labelSurcharge.setStyle("-fx-font-size: 7px; -fx-text-fill: red; -fx-font-weight: bold;");
            gen.getChildren().add(labelSurcharge);
        }

        return gen;
    }

    /**
     * Crée la représentation graphique d'une maison.
     */
    private VBox creerMaison(String nom, String type, double consommation) {
        VBox maison = new VBox(3);
        maison.setAlignment(Pos.CENTER);
        maison.setStyle("-fx-background-color: white; " +
                "-fx-border-color: black; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 5; " +
                "-fx-background-radius: 5; " +
                "-fx-padding: 5;");

        maison.setPrefWidth(70);
        maison.setPrefHeight(90);

        String imageMaison = switch (type.toUpperCase()) {
            case "FORTE" -> "maison_forte.png";
            case "NORMAL" -> "maison_normale.png";
            default -> "maison_basse.png";
        };

        ImageView icone = new ImageView(new Image("file:ressources/" + imageMaison));
        icone.setFitWidth(25);
        icone.setPreserveRatio(true);

        Label labelNom = new Label(nom);
        labelNom.setStyle("-fx-font-weight: bold; -fx-font-size: 10px;");

        Label labelType = new Label(type);
        labelType.setStyle("-fx-font-size: 8px;");

        Label labelConso = new Label((int) consommation + "kW");
        labelConso.setStyle("-fx-font-size: 8px;");

        maison.getChildren().addAll(labelNom, icone, labelType, labelConso);

        return maison;
    }

    /**
     * Réinitialise les couleurs des éléments graphiques.
     */
    private void reinitialiserCouleurs() {
        for (javafx.scene.Node node : zoneDessin.getChildren()) {
            if (node instanceof VBox) {
                VBox box = (VBox) node;
                String style = box.getStyle();
                style = style.replaceAll("-fx-border-color: [^;]+;", "-fx-border-color: black;");
                style = style.replaceAll("-fx-border-width: [^;]+;", "-fx-border-width: 1;");
                box.setStyle(style);
            } else if (node instanceof Line) {
                Line line = (Line) node;
                line.setStroke(Color.BLACK);
                line.setStrokeWidth(1.5);
            }
        }
    }

    /**
     * Met en surbrillance un générateur ou une maison sélectionnée
     * ainsi que les éléments qui lui sont connectés.
     * 
     * @param nomElement        Nom de l'élément sélectionné
     * @param estGenerateur     true si l'élément est un générateur, false sinon
     * @param connexions        Connexions maison → générateur
     * @param genConnectes      Liste des générateurs connectés
     * @param maisonsConnectees Liste des maisons connectées
     */
    private void mettreEnSurbrillance(String nomElement, boolean estGenerateur,
            Map<Maison, Generateur> connexions,
            List<Generateur> genConnectes,
            List<Maison> maisonsConnectees) {

        reinitialiserCouleurs();

        if (estGenerateur) {
            VBox genBox = mapNomsBoxGen.get(nomElement);
            if (genBox != null) {
                changerCouleurBordure(genBox, "green", 3);
            }

            for (Maison maison : connexions.keySet()) {
                Generateur gen = connexions.get(maison);
                if (gen.getNom().equals(nomElement)) {
                    VBox maisonBox = mapNomsBoxMaison.get(maison.getNom());
                    if (maisonBox != null) {
                        changerCouleurBordure(maisonBox, "green", 3);
                    }
                    colorerLigne(gen, maison, genConnectes, maisonsConnectees);
                }
            }
        } else {
            VBox maisonBox = mapNomsBoxMaison.get(nomElement);
            if (maisonBox != null) {
                changerCouleurBordure(maisonBox, "green", 3);
            }

            for (Maison maison : connexions.keySet()) {
                if (maison.getNom().equals(nomElement)) {
                    Generateur gen = connexions.get(maison);

                    VBox genBox = mapNomsBoxGen.get(gen.getNom());
                    if (genBox != null) {
                        changerCouleurBordure(genBox, "green", 3);
                    }

                    colorerLigne(gen, maison, genConnectes, maisonsConnectees);
                    break;
                }
            }
        }
    }

    /**
     * Modifie la couleur et l'épaisseur de la bordure d'un VBox.
     * 
     * @param box     Le conteneur à modifier
     * @param couleur La couleur de la bordure
     * @param largeur L'épaisseur de la bordure
     */

    private void changerCouleurBordure(VBox box, String couleur, int largeur) {
        String style = box.getStyle();
        style = style.replaceAll("-fx-border-color: [^;]+;", "-fx-border-color: " + couleur + ";");
        style = style.replaceAll("-fx-border-width: [^;]+;", "-fx-border-width: " + largeur + ";");
        box.setStyle(style);
    }

    /**
     * Met en surbrillance la ligne reliant une maison à un générateur.
     * 
     * @param gen               Le générateur concerné
     * @param maison            La maison concernée
     * @param genConnectes      Liste des générateurs connectés
     * @param maisonsConnectees Liste des maisons connectées
     */

    private void colorerLigne(Generateur gen, Maison maison, List<Generateur> genConnectes,
            List<Maison> maisonsConnectees) {

        int indexGen = genConnectes.indexOf(gen);
        int indexMaison = maisonsConnectees.indexOf(maison);

        if (indexGen >= 0 && indexMaison >= 0) {
            VBox vboxGen = listeVBoxGen.get(indexGen);
            VBox vboxMaison = listeVBoxMaison.get(indexMaison);

            double x1 = vboxGen.getLayoutX() + 35;
            double y1 = vboxGen.getLayoutY() + 90;
            double x2 = vboxMaison.getLayoutX() + 35;
            double y2 = vboxMaison.getLayoutY();

            for (javafx.scene.Node node : zoneDessin.getChildren()) {
                if (node instanceof Line) {
                    Line line = (Line) node;
                    if (Math.abs(line.getStartX() - x1) < 5 &&
                            Math.abs(line.getStartY() - y1) < 5 &&
                            Math.abs(line.getEndX() - x2) < 5 &&
                            Math.abs(line.getEndY() - y2) < 5) {
                        line.setStroke(Color.GREEN);
                        line.setStrokeWidth(3);
                    }
                }
            }
        }
    }
}