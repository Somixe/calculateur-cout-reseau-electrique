package up.rde.main;

//CLASSE MAIN
import up.rde.ui.InterfaceGraphique;
import up.rde.ui.InterfaceUtilisateur;

import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.application.Application;
import up.rde.fichier.ChargerReseau;
import up.rde.modele.ReseauElectrique;

/**
 * Classe principale du programme.
 * Gère le lancement de l'application en mode textuel ou graphique,
 * avec ou sans chargement d'un réseau depuis un fichier.
 */

public class Main {
    /**
     * Point d'entrée du programme.
     * 
     * @param args Arguments de la ligne de commande :
     *             - aucun argument : lancement normal
     *             - deux arguments : fichier + lambda
     * @throws IOException en cas d'erreur de lecture du fichier
     */

    public static void main(String[] args) throws IOException {

        // Détermine le mode de lancement : textuel ou graphique
        boolean modeLancement = InterfaceUtilisateur.modeDeLancement();
        // true : mode textuel, false : mode graphique
        // CAS SANS ARGUMENT
        if (args.length == 0) {
            if (modeLancement) {
                InterfaceUtilisateur ui = new InterfaceUtilisateur();
                ui.lancer();
            } else {
                Application.launch(InterfaceGraphique.class);
            }
            // CAS AVEC FICHIER + LAMBDA
        } else if (args.length == 2) {
            String fichier = args[0];
            double lambda;
            // Conversion du paramètre lambda
            try {
                lambda = Double.parseDouble(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Erreur : Le second argument (lambda) doit être un nombre.");
                return;
            }
            try {
                // Chargement du réseau depuis le fichier
                ReseauElectrique reseau = ChargerReseau.charger(fichier);
                System.out.println("Le réseau est chargé avec succès depuis : " + fichier);
                if (modeLancement) {
                    InterfaceUtilisateur ui = new InterfaceUtilisateur(reseau, lambda, fichier);
                    ui.afficherReseau();
                    ui.afficherCoutAvantOptimisation();
                    ui.lancerMenuResolution();
                } else {
                    InterfaceGraphique.setReseauImporte(reseau, lambda, fichier);
                    Application.launch(InterfaceGraphique.class);
                }

            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            // CAS ERREUR ARGUMENTS
            System.out.println("Erreur : Nombre d'arguments fournis incorrect.");
        }
    }
}