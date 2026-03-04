package up.rde.ui;

// CLASSE INTERFACEGRAPHIQUE
import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import up.rde.modele.ReseauElectrique;
import up.rde.ui.fenetres.affichage.FenetreAffichageReseau;
import up.rde.ui.fenetres.configuration.FenetreAjoutConnexion;
import up.rde.ui.fenetres.configuration.FenetreAjoutGenerateur;
import up.rde.ui.fenetres.configuration.FenetreAjoutMaison;
import up.rde.ui.fenetres.configuration.FenetreConfiguration;
import up.rde.ui.fenetres.configuration.FenetreSupprimerConnexion;
import up.rde.ui.fenetres.gestion.FenetreGestion;
import up.rde.ui.fenetres.gestion.FenetreSauvegardeReseau;

/**
 * Point d'entrée de l'application graphique.
 * Cette classe gère l'affichage et la navigation entre toutes les fenêtres de
 * l'application,
 * ainsi que l'import et l'initialisation d'un réseau électrique.
 */

public class InterfaceGraphique extends Application {

    private static final int LARGEUR_FENETRE = 1000;
    private static final int HAUTEUR_FENETRE = 600;

    private InterfaceUtilisateur ui;
    private boolean estValide;
    private FenetreAffichageReseau fenAffichageReseauEtCout;
    private static ReseauElectrique reseauImporte = null;
    private static double lambdaImporte = 0;
    private static String fichierImporte = null;
    private boolean dejaResolu;

    /**
     * Constructeur.
     * Initialise l'interface utilisateur en utilisant un réseau importé si
     * disponible,
     * sinon crée une nouvelle interface vierge.
     */
    public InterfaceGraphique() {
        if (reseauImporte != null) {
            ui = new InterfaceUtilisateur(reseauImporte, lambdaImporte, fichierImporte);
        } else {
            ui = new InterfaceUtilisateur();
        }
        estValide = false;
        dejaResolu = false;
    }

    /**
     * Définit le réseau à importer au démarrage.
     * 
     * @param reseau  Réseau électrique importé
     * @param lambda  Valeur lambda associée
     * @param fichier Nom du fichier source
     */
    public static void setReseauImporte(ReseauElectrique reseau, double lambda, String fichier) {
        reseauImporte = reseau;
        lambdaImporte = lambda;
        fichierImporte = fichier;
    }

    /**
     * Méthode principale de démarrage de l'application JavaFX.
     * Gère l'affichage des différentes fenêtres selon que le réseau est importé ou
     * non.
     * 
     * @param stage La fenêtre principale
     * @throws Exception en cas d'erreur lors de l'affichage
     */
    @Override
    public void start(Stage stage) throws Exception {

        // CAS 1 : RÉSEAU IMPORTÉ DEPUIS UN FICHIER

        if (reseauImporte != null) {
            fenAffichageReseauEtCout = new FenetreAffichageReseau(ui, false, dejaResolu);
            Scene sceneAffichageReseauEtCout = new Scene(fenAffichageReseauEtCout.getLayout(), LARGEUR_FENETRE,
                    HAUTEUR_FENETRE);

            FenetreGestion fenGestion = new FenetreGestion();
            Scene sceneGestion = new Scene(fenGestion.getLayout(), LARGEUR_FENETRE, HAUTEUR_FENETRE);
            // Navigation entre affichage réseau et gestion
            if (fenAffichageReseauEtCout.getBoutonSuivant() != null) {
                fenAffichageReseauEtCout.getBoutonSuivant().setOnAction(e -> stage.setScene(sceneGestion));
            }

            if (fenAffichageReseauEtCout.getBoutonArriere() != null) {
                fenAffichageReseauEtCout.getBoutonArriere().setOnAction(e -> stage.close());
            }

            // Bouton résolution automatique
            fenGestion.getResolutionAuto().setOnAction(e -> {
                try {
                    if (!dejaResolu) {
                        ui.gererResolutionAutomatique();
                        dejaResolu = true;
                    }

                    FenetreAffichageReseau fenAffichageResoAuto = new FenetreAffichageReseau(ui, true, dejaResolu);
                    Scene sceneAffichageResoAuto = new Scene(fenAffichageResoAuto.getLayout(), LARGEUR_FENETRE,
                            HAUTEUR_FENETRE);
                    stage.setScene(sceneAffichageResoAuto);

                    if (fenAffichageResoAuto.getBoutonArriere() != null) {
                        fenAffichageResoAuto.getBoutonArriere().setOnAction(e2 -> stage.setScene(sceneGestion));
                    }

                    ToastNotification.succes(stage, "Résolution automatique terminée !");

                } catch (Exception ex) {
                    ToastNotification.erreur(stage, ex.getMessage());
                }
            });

            // Bouton sauvegarde réseau
            fenGestion.getSauvegarderReseau().setOnAction(e -> {
                FenetreSauvegardeReseau fenSauvegardeReseau = new FenetreSauvegardeReseau();
                Scene sceneSauvegardeReseau = new Scene(fenSauvegardeReseau.getLayout(), LARGEUR_FENETRE,
                        HAUTEUR_FENETRE);
                stage.setScene(sceneSauvegardeReseau);

                fenSauvegardeReseau.getAjoutSauvegardeBouton().setOnAction(e2 -> {
                    String input = fenSauvegardeReseau.getInput();
                    try {
                        stage.setScene(sceneGestion);
                        ToastNotification.succes(stage, ui.gererSauvegarde(ui.getNomFichierImporte(), input));
                    } catch (IOException ex) {
                        stage.setScene(sceneGestion);
                        ToastNotification.erreur(stage, "Erreur : " + ex.getMessage());
                    } catch (IllegalArgumentException ex) {
                        stage.setScene(sceneGestion);
                        ToastNotification.erreur(stage, ex.getMessage());
                    }
                });

                fenSauvegardeReseau.getInputField().setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        fenSauvegardeReseau.getAjoutSauvegardeBouton().fire();
                    }
                });
            });

            fenGestion.getFinBouton().setOnAction(e -> stage.close());

            stage.setScene(sceneAffichageReseauEtCout);
            stage.setTitle("Réseaux de distribution d'électricité");
            stage.getIcons().add(new Image("file:ressources/logo.png"));
            stage.show();
            return;
        }

        // CAS 2 : DÉMARRAGE NORMAL (SANS IMPORT)

        FenetreConfiguration fenConfig = new FenetreConfiguration();
        FenetreGestion fenGestion = new FenetreGestion();
        FenetreAjoutGenerateur fenAjoutGen = new FenetreAjoutGenerateur();
        FenetreAjoutMaison fenAjoutMaison = new FenetreAjoutMaison();
        FenetreAjoutConnexion fenAjoutConnexion = new FenetreAjoutConnexion();
        FenetreSupprimerConnexion fenSupprimerConnexion = new FenetreSupprimerConnexion();

        Scene sceneConfig = new Scene(fenConfig.getLayout(), LARGEUR_FENETRE, HAUTEUR_FENETRE);
        Scene sceneGestion = new Scene(fenGestion.getLayout(), LARGEUR_FENETRE, HAUTEUR_FENETRE);
        Scene sceneAjoutGen = new Scene(fenAjoutGen.getLayout(), LARGEUR_FENETRE, HAUTEUR_FENETRE);
        Scene sceneAjoutMaison = new Scene(fenAjoutMaison.getLayout(), LARGEUR_FENETRE, HAUTEUR_FENETRE);
        Scene sceneAjoutConnexion = new Scene(fenAjoutConnexion.getLayout(), LARGEUR_FENETRE, HAUTEUR_FENETRE);
        Scene sceneSupprimerConnexion = new Scene(fenSupprimerConnexion.getLayout(), LARGEUR_FENETRE, HAUTEUR_FENETRE);

        // ACTIONS DU MENU CONFIGURATION

        fenConfig.getAjoutGenerateurBouton().setOnAction(e -> stage.setScene(sceneAjoutGen));
        fenConfig.getAjoutMaisonBouton().setOnAction(e -> stage.setScene(sceneAjoutMaison));
        fenConfig.getAjoutConnexionBouton().setOnAction(e -> stage.setScene(sceneAjoutConnexion));
        fenConfig.getSupprimerConnexionBouton().setOnAction(e -> stage.setScene(sceneSupprimerConnexion));

        fenConfig.getFinBouton().setOnAction(e -> {
            try {
                estValide = ui.gererValidationMenuPrincipal();
                if (estValide) {
                    ToastNotification.succes(stage, "Réseau validé avec succès !");

                    fenAffichageReseauEtCout = new FenetreAffichageReseau(ui, false, dejaResolu);
                    Scene sceneAffichageReseauEtCout = new Scene(fenAffichageReseauEtCout.getLayout(), LARGEUR_FENETRE,
                            HAUTEUR_FENETRE);
                    stage.setScene(sceneAffichageReseauEtCout);

                    if (fenAffichageReseauEtCout.getBoutonSuivant() != null) {
                        fenAffichageReseauEtCout.getBoutonSuivant().setOnAction(e1 -> stage.setScene(sceneGestion));
                    }

                    if (fenAffichageReseauEtCout.getBoutonArriere() != null) {
                        fenAffichageReseauEtCout.getBoutonArriere().setOnAction(e2 -> stage.setScene(sceneConfig));
                    }
                }
            } catch (IllegalArgumentException ex) {
                ToastNotification.erreur(stage, ex.getMessage());
            } catch (Exception e2) {
                ToastNotification.erreur(stage, e2.getMessage());
            }
        });

        // ACTIONS DES FENÊTRES D'AJOUT

        fenAjoutGen.getAjoutGenerateurBouton().setOnAction(e -> {
            String input = fenAjoutGen.getInput();
            try {
                stage.setScene(sceneConfig);
                ToastNotification.succes(stage, ui.gererAjoutGenerateur(input));
            } catch (IllegalArgumentException ex) {
                stage.setScene(sceneConfig);
                ToastNotification.erreur(stage, ex.getMessage());
            } finally {
                fenAjoutGen.getInputField().clear();
            }
        });

        fenAjoutMaison.getAjoutMaisonBouton().setOnAction(e -> {
            String input = fenAjoutMaison.getInput();
            try {
                stage.setScene(sceneConfig);
                ToastNotification.succes(stage, ui.gererAjoutMaison(input));
            } catch (IllegalArgumentException ex) {
                stage.setScene(sceneConfig);
                ToastNotification.erreur(stage, ex.getMessage());
            } finally {
                fenAjoutMaison.getInputField().clear();
            }
        });

        fenAjoutConnexion.getAjoutConnexionBouton().setOnAction(e -> {
            String input = fenAjoutConnexion.getInput();
            try {
                stage.setScene(sceneConfig);
                ToastNotification.succes(stage, ui.gererAjoutConnexion(input));
            } catch (IllegalArgumentException ex) {
                stage.setScene(sceneConfig);
                ToastNotification.erreur(stage, ex.getMessage());
            } finally {
                fenAjoutConnexion.getInputField().clear();
            }
        });

        fenSupprimerConnexion.getAjoutSupprimerBouton().setOnAction(e -> {
            String input = fenSupprimerConnexion.getInput();
            try {
                stage.setScene(sceneConfig);
                ToastNotification.succes(stage, ui.gererSuppressionConnexion(input));
            } catch (IllegalArgumentException ex) {
                stage.setScene(sceneConfig);
                ToastNotification.erreur(stage, ex.getMessage());
            } finally {
                fenSupprimerConnexion.getInputField().clear();
            }
        });

        // ACTIONS DU MENU GESTION

        fenGestion.getResolutionAuto().setOnAction(e -> {
            try {
                if (!dejaResolu) {
                    ui.gererResolutionAutomatique();
                    dejaResolu = true;
                }

                FenetreAffichageReseau fenAffichageResoAuto = new FenetreAffichageReseau(ui, true, dejaResolu);
                Scene sceneAffichageResoAuto = new Scene(fenAffichageResoAuto.getLayout(), LARGEUR_FENETRE,
                        HAUTEUR_FENETRE);
                stage.setScene(sceneAffichageResoAuto);

                if (fenAffichageResoAuto.getBoutonArriere() != null) {
                    fenAffichageResoAuto.getBoutonArriere().setOnAction(e2 -> stage.setScene(sceneGestion));
                }

                ToastNotification.succes(stage, "Résolution automatique terminée !");

            } catch (Exception ex) {
                ToastNotification.erreur(stage, ex.getMessage());
            }
        });

        fenGestion.getSauvegarderReseau().setOnAction(e -> {
            FenetreSauvegardeReseau fenSauvegardeReseau = new FenetreSauvegardeReseau();
            Scene sceneSauvegardeReseau = new Scene(fenSauvegardeReseau.getLayout(), LARGEUR_FENETRE, HAUTEUR_FENETRE);
            stage.setScene(sceneSauvegardeReseau);

            fenSauvegardeReseau.getAjoutSauvegardeBouton().setOnAction(e2 -> {
                String input = fenSauvegardeReseau.getInput();
                try {
                    stage.setScene(sceneGestion);
                    ToastNotification.succes(stage, ui.gererSauvegarde(ui.getNomFichierImporte(), input));
                } catch (IOException ex) {
                    stage.setScene(sceneGestion);
                    ToastNotification.erreur(stage, "Erreur : " + ex.getMessage());
                } catch (IllegalArgumentException ex) {
                    stage.setScene(sceneGestion);
                    ToastNotification.erreur(stage, ex.getMessage());
                }
            });

            fenSauvegardeReseau.getInputField().setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    fenSauvegardeReseau.getAjoutSauvegardeBouton().fire();
                }
            });
        });

        fenGestion.getFinBouton().setOnAction(e -> stage.close());

        // RACCOURCIS CLAVIER (TOUCHE ENTRÉE)

        fenAjoutGen.getInputField().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                fenAjoutGen.getAjoutGenerateurBouton().fire();
            }
        });

        fenAjoutMaison.getInputField().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                fenAjoutMaison.getAjoutMaisonBouton().fire();
            }
        });

        fenAjoutConnexion.getInputField().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                fenAjoutConnexion.getAjoutConnexionBouton().fire();
            }
        });

        fenSupprimerConnexion.getInputField().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                fenSupprimerConnexion.getAjoutSupprimerBouton().fire();
            }
        });

        // Affichage initial de la fenêtre de configuration
        stage.setScene(sceneConfig);
        stage.setTitle("Réseaux de distribution d'électricité");
        stage.getIcons().add(new Image("file:ressources/logo.png"));
        stage.show();
    }
}