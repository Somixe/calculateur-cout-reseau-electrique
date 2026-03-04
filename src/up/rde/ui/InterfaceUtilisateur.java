package up.rde.ui;

// CLASSE INTERFACEUTILISATEUR
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;

import up.rde.modele.ReseauElectrique;
import up.rde.utilitaire.ResolutionAutomatique;
import up.rde.modele.TypeConsommation;
import up.rde.utilitaire.CalculateurCout;
import up.rde.fichier.SauvegarderReseau;

/**
 * Classe gérant l'interface utilisateur pour la configuration,
 * la validation et la gestion d'un réseau électrique.
 * 
 * Elle peut fonctionner en mode textuel ou servir de backend pour l'interface
 * graphique.
 */

public class InterfaceUtilisateur {

    // Attribut
    private ReseauElectrique reseau;
    private Scanner sc;
    private double lambda;
    private String nomFichierImporte;
    private ResolutionAutomatique algo;

    // Constructeurs
    /**
     * Constructeur par défaut.
     * Initialise un nouveau réseau vide et utilise la valeur lambda par défaut.
     */
    public InterfaceUtilisateur() {
        this.sc = new Scanner(System.in);
        this.reseau = new ReseauElectrique();
        this.lambda = CalculateurCout.getLambdaDefaut();
        this.algo = new ResolutionAutomatique(reseau, lambda);
    }

    /**
     * Constructeur avec réseau importé.
     * 
     * @param reseau  Réseau existant importé
     * @param lambda  Valeur lambda associée
     * @param fichier Nom du fichier source
     */
    public InterfaceUtilisateur(ReseauElectrique reseau, double lambda, String fichier) {
        this.sc = new Scanner(System.in);
        this.reseau = reseau;
        this.lambda = lambda;
        this.nomFichierImporte = fichier;
        this.algo = new ResolutionAutomatique(reseau, lambda);
    }

    // Méthodes d'affichages statiques
    public static void afficherBanniere() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║    RÉSEAU DE DISTRIBUTION D'ÉLECTRICITÉ    ║");
        System.out.println("║          Université Paris Cité             ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
    }

    /**
     * Affiche le menu principal (création du réseau).
     */
    public static String afficherMenuPrincipal() {
        return "\nMENU PRINCIPAL\n" +
                "______________________________________________\n\n" +
                "1) Ajouter un générateur\n" +
                "2) Ajouter une maison\n" +
                "3) Ajouter une connexion\n" +
                "4) Supprimer une connexion\n" +
                "5) Fin\n";
    }

    /**
     * Affiche le menu de gestion après validation du réseau.
     * 
     * @return Le texte du menu gestion
     */
    public static String afficherMenuGestion() {
        return """
                ╔═════════════════════════════════════════╗
                ║            MENU GESTION                 ║
                ╠═════════════════════════════════════════╣
                ║  1) Calculer le coût du réseau actuel   ║
                ║  2) Modifier une connexion              ║
                ║  3) Afficher le réseau                  ║
                ║  4) Fin (quitter le programme)          ║
                ╚═════════════════════════════════════════╝
                """;
    }

    /**
     * Affiche le menu de résolution (fichier).
     * 
     * @return Le texte du menu résolution
     */

    public static String afficherMenuFichier() {
        return "\nMENU RÉSOLUTION\n" +
                "______________________________________________\n\n" +
                "1) Résolution automatique\n" +
                "2) Sauvegarder la solution actuelle\n" +
                "3) Fin\n";
    }

    /**
     * Affiche le menu de choix du mode de lancement.
     * 
     * @return Le texte du menu mode de lancement
     */
    public static String afficherModeLancement() {
        return "\nMODE DE LANCEMENT\n" +
                "______________________________________________\n\n" +
                "1) Interface textuelle (Terminal)\n" +
                "2) Interface graphique (JavaFX)\n";
    }

    /** Affiche une bannière avant optimisation du réseau. */
    public static void afficherAvtOptimisationBanniere() {
        System.out.println("\n╔═════════════════════════════════════╗");
        System.out.println("║           COÛT INITIAL              ║");
        System.out.println("╚═════════════════════════════════════╝\n");
    }

    /** Affiche une bannière après optimisation du réseau. */
    public static void afficherApresOptimisationBanniere() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         COÛT APRÈS OPTIMISATION        ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }

    /** Affiche un message de fin de programme. */

    public static void messageFinProgramme() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║    Merci d'avoir utilisé notre programme   ║");
        System.out.println("║              À bientôt !                   ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
    }

    // Méthodes d'affichage d'instance

    /**
     * Affiche le réseau actuel.
     * 
     * @see ReseauElectrique#toString()
     */

    public void afficherReseau() {
        System.out.println(" \n╔════════════════════════════════════════════╗");
        System.out.println("║        AFFICHAGE DU RÉSEAU ÉLECTRIQUE      ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
        System.out.println(reseau.toString());
    }

    /** Affiche le coût du réseau avant optimisation. */
    public void afficherCoutAvantOptimisation() {
        afficherAvtOptimisationBanniere();
        System.out.println(algo.afficherDetailsCout());
    }

    // Méthodes de lecture clavier statiques

    /**
     * Lit un entier depuis le scanner.
     * 
     * @param s Scanner utilisé
     * @return l'entier lu
     * @throws InputMismatchException si l'entrée n'est pas un entier
     */

    public static int lireEntierClavier(Scanner s) throws InputMismatchException {
        System.out.print("Votre choix : ");
        if (s.hasNextInt()) {
            int choix = s.nextInt();
            s.nextLine();
            return choix;
        } else {
            s.nextLine();
            throw new InputMismatchException("Veuillez entrer un nombre entier");
        }
    }

    /**
     * Lit une chaîne depuis le scanner.
     * 
     * @param s       Scanner utilisé
     * @param message Message à afficher pour inviter la saisie
     * @return la chaîne saisie non vide
     */

    public static String lireChaineClavier(Scanner s, String message) {
        while (true) {
            System.out.print(message);
            String texte = s.nextLine().trim();
            if (!texte.isEmpty()) {
                return texte;
            }
            System.out.println("Erreur : La saisie ne peut pas être vide\n");
        }
    }

    /**
     * Sépare une ligne en exactement deux parties.
     * 
     * @param ligne         Ligne à séparer
     * @param messageErreur Message d'erreur si la séparation échoue
     * @return tableau contenant les deux parties
     * @throws IllegalArgumentException si la ligne ne contient pas exactement deux
     *                                  éléments
     */

    public static String[] separerDeux(String ligne, String messageErreur) {
        String[] parts = ligne.trim().split("\\s+");
        if (parts.length != 2) {
            throw new IllegalArgumentException(messageErreur);
        }
        return parts;
    }

    // Méthodes de gestion des menus
    /**
     * Demande à l'utilisateur de choisir le mode de lancement : textuel ou
     * graphique.
     * 
     * @return true si mode textuel, false si mode graphique
     */

    public static boolean modeDeLancement() {
        afficherBanniere();
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                System.out.println(afficherModeLancement());
                int choix = lireEntierClavier(sc);

                if (choix == 1) {
                    return true;
                } else if (choix == 2) {
                    return false;
                } else {
                    System.out.println("Erreur : Veuillez entrer un nombre entre 1 ou 2\n");
                }
            } catch (InputMismatchException e) {
                System.out.println(e.getMessage() + "\n");
            }
        }
    }

    /**
     * Lance le programme en mode textuel.
     * 
     * @throws IOException en cas de problème lors de la sauvegarde
     */
    public void lancer() throws IOException {
        lancerMenuPrincipal();
        messageFinProgramme();
        sc.close();

    }

    /**
     * Menu principal pour ajouter des générateurs, maisons, connexions et valider
     * le réseau.
     */

    public void lancerMenuPrincipal() {
        boolean estValider = false;
        String input;

        while (!estValider) {
            System.out.println(afficherMenuPrincipal());

            try {
                int choix = lireEntierClavier(sc);

                switch (choix) {
                    case 1 -> {// Ajout générateur
                        input = lireChaineClavier(sc, "Entrez nom et capacité (ex: G1 60) : ");
                        try {
                            System.out.println(gererAjoutGenerateur(input));
                        } catch (IllegalArgumentException e) {
                            System.out.println("Erreur : " + e.getMessage() + "\n");
                        }
                    }
                    case 2 -> {// Ajout maison
                        input = lireChaineClavier(sc, "Entrez nom et type ("
                                + TypeConsommation.obtenirTypesDisponibles() + ") (ex: M1 BASSE) : ");
                        try {
                            System.out.println(gererAjoutMaison(input));
                        } catch (IllegalArgumentException e) {
                            System.out.println("Erreur : " + e.getMessage() + "\n");
                        }
                    }
                    case 3 -> {// Ajout connexion
                        input = lireChaineClavier(sc, "Entrez une connexion (ex: M1 G1 ou G1 M1) : ");
                        try {
                            System.out.println(gererAjoutConnexion(input));
                        } catch (IllegalArgumentException e) {
                            System.out.println("Erreur : " + e.getMessage() + "\n");
                        }
                    }
                    case 4 -> { // Supprimer connexion
                        input = lireChaineClavier(sc, "Entrez une connexion à supprimer (ex: M1 G1) : ");
                        try {
                            System.out.println(gererSuppressionConnexion(input));
                        } catch (IllegalArgumentException e) {
                            System.out.println("Erreur : " + e.getMessage() + "\n");
                        }
                    }
                    case 5 -> { // Fin et validation
                        try {
                            boolean estValide = gererValidationMenuPrincipal(); // Valide et affiche le réseau
                            if (estValide) {
                                try {
                                    lancerMenuResolution();
                                    estValider = true;
                                } catch (IOException e) {
                                    System.out.println("Erreur lors du menu de résolution : " + e.getMessage());
                                }
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println("Erreur : " + e.getMessage() + "\n");
                        }

                    }
                    default -> System.out.println("Erreur : Veuillez entrer un nombre entre 1 et 5\n");
                }

            } catch (InputMismatchException e) {
                System.out.println("Erreur : " + e.getMessage() + "\n");
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage() + "\n");
            }
        }
    }

    /**
     * Lance le menu de résolution et de sauvegarde.
     * 
     * @throws IOException si la sauvegarde échoue
     */
    public void lancerMenuResolution() throws IOException {
        boolean quitter = false;

        while (!quitter) {
            System.out.println(afficherMenuFichier());

            try {
                int choix = lireEntierClavier(sc);

                switch (choix) {
                    case 1 -> {
                        System.out.println(gererResolutionAutomatique());
                    }
                    case 2 -> {
                        try {
                            String nomFichier = lireChaineClavier(sc, "Entrez le nom du fichier de sauvegarde : ");
                            System.out.println(gererSauvegarde(nomFichierImporte, nomFichier));
                        } catch (IOException e) {
                            System.out.println("Erreur : " + e.getMessage() + "\n");
                        }
                    }
                    case 3 -> {
                        quitter = true;
                    }
                    default -> System.out.println("Erreur : Veuillez entrer un nombre entre 1 et 3.\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("Erreur : " + e.getMessage() + "\n");
            }
        }
    }

    // Méthodes des options des menus
    /**
     * Ajoute ou met à jour un générateur dans le réseau.
     * 
     * @param input chaîne contenant "nom capacité" (ex: G1 60)
     * @return message de succès ou mise à jour
     * @throws IllegalArgumentException si format incorrect ou capacité invalide
     */

    public String gererAjoutGenerateur(String input) {

        try {
            String[] resultat = separerDeux(input,
                    "Veuillez entrer un nom et la capacité maximale du générateur. ");
            int capacite = Integer.parseInt(resultat[1]);

            boolean misAJour = reseau.ajouterGenerateur(resultat[0], capacite);

            if (misAJour) {
                return "Le générateur '" + resultat[0] + "' a été mis à jour (capacité: " + capacite + " kW)\n";
            } else {
                return "Le générateur '" + resultat[0] + "' a été créé avec succès (capacité: " + capacite + " kW)\n";
            }

        } catch (NumberFormatException e) {
            throw new NumberFormatException("La capacité doit être un nombre entier");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Ajoute ou met à jour une maison dans le réseau.
     * 
     * @param input chaîne contenant "nom type" (ex: M1 BASSE)
     * @return message de succès ou mise à jour
     * @throws IllegalArgumentException si format incorrect ou type invalide
     */

    public String gererAjoutMaison(String input) {

        try {
            String[] resultat = separerDeux(input,
                    "Veuillez entrer le nom et le type de consommation de la maison.");
            String typeStr = resultat[1].toUpperCase();

            if (!TypeConsommation.estTypeValide(typeStr)) {
                throw new IllegalArgumentException(
                        "Type invalide. Utilisez : " + TypeConsommation.obtenirTypesDisponibles());
            }

            TypeConsommation type = TypeConsommation.valueOf(typeStr);
            boolean misAJour = reseau.ajouterMaison(resultat[0], type);

            if (misAJour) {
                return "La maison '" + resultat[0] + "' a été mise à jour (type: " + type + ", " + type.getValeur()
                        + " kW)\n";
            } else {
                return "La maison '" + resultat[0] + "' a été créée avec succès (type: " + type + ", "
                        + type.getValeur() + " kW)\n";
            }

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Ajoute une connexion maison-générateur.
     * 
     * @param input chaîne contenant "maison générateur"
     * @return message de succès
     * @throws IllegalArgumentException si connexion existante ou format incorrect
     */

    public String gererAjoutConnexion(String input) {
        try {
            String[] resultat = separerDeux(input, "Le format attendu est 'maison générateur'");
            boolean existait = reseau.ajouterConnexion(resultat[0], resultat[1]);

            if (existait) {
                // La connexion existait
                throw new IllegalArgumentException("Connexion existante, aucune modification effectuée\n");
            } else {
                // Nouvelle connexion créée
                return "Connexion créée avec succès\n";
            }

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Supprime une connexion maison-générateur.
     * 
     * @param input chaîne contenant "maison générateur"
     * @return message de succès
     * @throws IllegalArgumentException si format incorrect ou connexion inexistante
     */
    public String gererSuppressionConnexion(String input) {

        try {
            String[] resultat = separerDeux(input, "Le format attendu est 'maison générateur'");
            reseau.supprimerConnexion(resultat[0], resultat[1]); // ← 2 paramètres

            return "Connexion supprimée avec succès\n";

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Valide que le réseau est complet et cohérent avant le menu gestion.
     * 
     * @return true si le réseau est valide
     * @throws IllegalArgumentException si réseau incomplet ou incohérent
     */
    public boolean gererValidationMenuPrincipal() {
        if (!reseau.estNonVide()) {
            throw new IllegalArgumentException("Le réseau doit contenir au moins une maison et un générateur");
        }

        if (reseau.getDemandeTotaleMaison() > reseau.getCapaciteTotaleGenerateur()) {
            throw new IllegalArgumentException(
                    "La demande totale des maisons " + "dépasse la capacité totale des générateurs");
        }

        List<String> maisonsNonConnectees = reseau.getMaisonsNonConnectees();
        if (!maisonsNonConnectees.isEmpty()) {
            throw new IllegalArgumentException(
                    "Certaines maisons ne sont pas connectées à un générateur : " + maisonsNonConnectees);
        }

        System.out.println("\nRéseau validé avec succès !");
        afficherReseau();
        afficherCoutAvantOptimisation();

        return true;
    }

    /**
     * Exécute l'optimisation automatique du réseau.
     * 
     * @return le détail des coûts après optimisation
     */

    public String gererResolutionAutomatique() {

        algo.executer();
        afficherReseau();
        afficherApresOptimisationBanniere();

        return algo.afficherDetailsCout();
    }

    /**
     * Sauvegarde le réseau dans un fichier.
     * 
     * @param nomFichierImporte nom du fichier source
     * @param nomFichier        nom du fichier de sauvegarde
     * @return message de succès
     * @throws IOException              si la sauvegarde échoue
     * @throws IllegalArgumentException si nomFichier invalide ou réseau vide
     */
    public String gererSauvegarde(String nomFichierImporte, String nomFichier) throws IOException {

        if (nomFichier == null || nomFichier.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du réseau de sauvegarde ne peut pas être vide");
        }

        // Ajouter .txt automatiquement si pas d'extension
        if (!nomFichier.endsWith(".txt")) {
            nomFichier += ".txt";
        }

        if (reseau.getConnexions().isEmpty()) {
            throw new IllegalArgumentException("Le réseau est vide");
        }

        try {
            return SauvegarderReseau.sauvegarder(reseau, nomFichierImporte, nomFichier);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    // Getters

    /** @return le réseau électrique courant */
    public ReseauElectrique getReseau() {
        return reseau;
    }

    /** @return true si un fichier a été importé */

    public boolean estFichierImporte() {
        return nomFichierImporte != null;
    }

    /** @return la valeur de lambda utilisée */
    public double getLambda() {
        return lambda;
    }

    /** @return le nom du fichier importé */

    public String getNomFichierImporte() {
        return nomFichierImporte;
    }

}