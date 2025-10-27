package up.rde.ui;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;

import up.rde.modele.ReseauElectrique;
import up.rde.modele.TypeConsommation;
import up.rde.algo.CalculateurCout;

/**
 * Interface utilisateur pour la gestion du réseau électrique.
 * Gère les interactions avec l'utilisateur via deux menus :
 * - Menu principal : création du réseau
 * - Menu gestion : manipulation du réseau créé
 */
public class InterfaceUtilisateur {
    
    private ReseauElectrique reseau;
    private Scanner sc;
    
    /**
     * Construit l'interface utilisateur avec un réseau vide.
     */
    public InterfaceUtilisateur() {
        sc = new Scanner(System.in);
        reseau = new ReseauElectrique();
    }
    
    /**
     * Affiche la bannière du programme.
     */
    public static void afficherBanniere() {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║    RÉSEAU DE DISTRIBUTION D'ÉLECTRICITÉ    ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
    }
    
    /**
     * Affiche le menu principal.
     * 
     * @return Le texte du menu sous forme de chaîne
     */
    public static String afficherMenuPrincipal() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nMENU PRINCIPAL\n");
        sb.append("______________________________________________\n\n");
        sb.append("1) Ajouter un générateur\n");
        sb.append("2) Ajouter une maison\n");
        sb.append("3) Ajouter une connexion\n");
        sb.append("4) Fin\n");
        return sb.toString();
    }
    
    /**
     * Affiche le menu de gestion.
     * 
     * @return Le texte du menu sous forme de chaîne
     */
    public static String afficherMenuGestion() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nMENU GESTION\n");
        sb.append("______________________________________________\n\n");
        sb.append("1) Calculer le coût du réseau électrique actuel\n");
        sb.append("2) Modifier une connexion\n");
        sb.append("3) Afficher le réseau\n");
        sb.append("4) Fin\n");
        return sb.toString();
    }
    
    /**
     * Lit un entier au clavier avec gestion d'erreur.
     * Redemande jusqu'à obtenir un entier valide.
     * 
     * @param s Le scanner à utiliser
     * @return L'entier saisi par l'utilisateur
     */
    public static int lireEntierClavier(Scanner s) {
        while (true) {
            try {
                System.out.print("Faites votre choix : ");
                int choix = s.nextInt();
                s.nextLine();
                return choix;
            } catch (InputMismatchException e) {
                System.out.println("Erreur : Veuillez entrer un entier\n");
                s.nextLine();
            }
        }
    }
    
    /**
     * Lit une chaîne de caractères au clavier avec gestion d'erreur.
     * Redemande jusqu'à obtenir une chaîne non vide.
     * 
     * @param s Le scanner à utiliser
     * @param message Le message à afficher
     * @return La chaîne saisie par l'utilisateur (sans espaces avant/après)
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
     * Sépare une chaîne en exactement deux parties sur les espaces.
     * Exemple : "M1 NORMAL" → ["M1", "NORMAL"]
     * 
     * @param ligne La chaîne à séparer
     * @param messageErreur Le message d'erreur à afficher si le format est incorrect
     * @return Un tableau de 2 éléments
     * @throws IllegalArgumentException si la chaîne ne contient pas exactement 2 éléments
     */
    public static String[] separerDeux(String ligne, String messageErreur) {
        String[] parts = ligne.trim().split("\\s+"); //Enlève espace debut et fin et entre \\s --> caractère blanc (tab, espace saut de ligne..) et + 1 ou plus
        
        if (parts.length != 2) {
            throw new IllegalArgumentException(messageErreur);
        }
        
        return parts;
    }

    /**
     * Lance le menu principal permettant de créer le réseau.
     * Boucle jusqu'à ce que l'utilisateur valide le réseau (option 4).
     */
    public void lancerMenuPrincipal() {
        afficherBanniere();
        
        boolean estValide = false;
        
        while (!estValide) {
            System.out.println(afficherMenuPrincipal());
            int choix = lireEntierClavier(sc);
            
            switch (choix) {
                case 1:
                    gererAjoutGenerateur();
                    break;
                case 2:
                    gererAjoutMaison();
                    break;
                case 3:
                    gererAjoutConnexion();
                    break;
                case 4:
                    estValide = gererValidationMenuPrincipal();
                    break;
                default:
                    System.out.println("Erreur : Veuillez entrer un entier entre 1 et 4\n");
            }
        }
    }
    
    /**
     * Gère l'ajout d'un générateur.
     * Redemande jusqu'à obtenir des données valides.
     */
    private void gererAjoutGenerateur() {
        boolean ajouter = false;
        
        do {
            String input = lireChaineClavier(sc, "Entrez nom et capacité (ex: G1 60) : ");
            
            try {
                String[] resultat = separerDeux(input, "Format incorrect");
                int capacite = Integer.parseInt(resultat[1]);
                
                boolean misAJour = reseau.ajouterGenerateur(resultat[0], capacite);
                
                if (misAJour) {
                    System.out.println("Le générateur '" + resultat[0] + "' a été mis à jour\n");
                } else {
                    System.out.println("Le générateur '" + resultat[0] + "' a été créé avec succès\n");
                }
                
                ajouter = true;
                
            } catch (NumberFormatException e) {
                System.out.println("Erreur : La capacité doit être un nombre entier\n");
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage() + "\n");
            }
        } while (!ajouter);
    }
    
    /**
     * Gère l'ajout d'une maison.
     * Redemande jusqu'à obtenir des données valides.
     */
    private void gererAjoutMaison() {
        boolean ajouter = false;
        
        do {
            String types = TypeConsommation.obtenirTypesDisponibles(); // "BASSE, NORMAL, FORTE"
            String input = lireChaineClavier(sc, 
                "Entrez nom et type (" + types + ") (ex: M1 BASSE) : ");
            
            try {
                String[] resultat = separerDeux(input, "Format incorrect");
                String typeStr = resultat[1].toUpperCase();
                
                if (!TypeConsommation.estTypeValide(typeStr)) {
                    System.out.println("Erreur : Type invalide. Utilisez : " + types + "\n");
                    continue;
                }
                
                TypeConsommation type = TypeConsommation.valueOf(typeStr); //à revoir
                boolean misAJour = reseau.ajouterMaison(resultat[0], type);
                
                if (misAJour) {
                    System.out.println("La maison '" + resultat[0] + "' a été mise à jour\n");
                } else {
                    System.out.println("La maison '" + resultat[0] + "' a été créée avec succès\n");
                }
                
                ajouter = true;
                
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage() + "\n");
            }
        } while (!ajouter);
    }
    
    /**
     * Gère l'ajout d'une connexion.
     * Redemande jusqu'à obtenir des données valides.
     */
    private void gererAjoutConnexion() {
        boolean ajouter = false;
        
        do {
            String input = lireChaineClavier(sc, "Entrez une connexion (ex: M1 G1) : ");
            
            try {
                String[] resultat = separerDeux(input, "Format incorrect");
                boolean etaitConnectee = reseau.ajouterConnexion(resultat[0], resultat[1]);
                
                if (etaitConnectee) {
                    System.out.println("Connexion modifiée avec succès\n");
                } else {
                    System.out.println("Connexion créée avec succès\n");
                }
                
                ajouter = true;
                
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage() + "\n");
            }
        } while (!ajouter);
    }
    
    /**
     * Gère la validation du réseau avant de passer au menu de gestion.
     * 
     * @return true si le réseau est valide, false sinon
     */
    private boolean gererValidationMenuPrincipal() {
        if (!reseau.estNonVide()) {
            System.out.println("Erreur : Le réseau doit contenir au moins une maison et un générateur\n");
            return false;
        }
        
        List<String> maisonsNonConnectees = reseau.getMaisonsNonConnectees();
        
        if (!maisonsNonConnectees.isEmpty()) {
            System.out.println("Erreur : Certaines maisons ne sont pas connectées");
            System.out.println("Maisons concernées : " + maisonsNonConnectees + "\n");
            return false;
        }
        
        System.out.println("Réseau validé avec succès !\n");
        return true;
    }
    
    /**
     * Lance le menu de gestion du réseau.
     * Boucle jusqu'à ce que l'utilisateur quitte (option 4).
     */
    public void lancerMenuGestion() {
        boolean termine = false;
        
        while (!termine) {
            System.out.println(afficherMenuGestion());
            int choix = lireEntierClavier(sc);
            
            switch (choix) {
                case 1:
                    System.out.println(CalculateurCout.coutInstanceReseau(reseau));
                    break;
                case 2:
                    System.out.println("(Modification de connexion - à implémenter dans la partie 2)\n");
                    break;
                case 3:
                    System.out.println("(Afficher le réseau - à implémenter)\n");
                    break;
                case 4:
                    termine = true;
                    break;
                default:
                    System.out.println("Erreur : Veuillez entrer un entier entre 1 et 4\n");
            }
        }
    }
    
    /**
     * Lance l'application complète.
     * Exécute successivement le menu principal puis le menu de gestion.
     */
    public void lancer() {
        lancerMenuPrincipal();
        lancerMenuGestion();
        sc.close();
        System.out.println("\nMerci d'avoir utilisé notre programme. À bientôt !");
    }
}