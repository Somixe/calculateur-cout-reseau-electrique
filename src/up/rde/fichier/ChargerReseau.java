package up.rde.fichier;

//CLASSE CHARGERESEAU
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import up.rde.modele.ReseauElectrique;
import up.rde.modele.TypeConsommation;

/**
 * Classe utilitaire permettant de charger un réseau électrique
 * à partir d'un fichier texte.
 */

public class ChargerReseau {

    /**
     * Charge un réseau électrique depuis un fichier.
     * 
     * @param fichier Chemin du fichier à lire
     * @return Le réseau électrique chargé
     * @throws IOException en cas d'erreur de lecture ou de format du fichier
     */
    public static ReseauElectrique charger(String fichier) throws IOException {
        ReseauElectrique reseau = new ReseauElectrique();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(fichier));

            String ligne;
            int numLigne = 1;

            while ((ligne = br.readLine()) != null) {
                ligne = ligne.trim();

                if (!ligne.isEmpty()) {
                    try {
                        analyserLigne(reseau, ligne);
                    } catch (NumberFormatException e) {
                        throw new IOException(
                                "\nErreur à la ligne " + numLigne + " : Valeur numérique invalide --> " + ligne);
                    } catch (IOException e) {
                        throw new IOException("\nErreur à la ligne " + numLigne + " --> " + ligne + e.getMessage());
                    } catch (IllegalArgumentException e) {
                        throw new IOException("\nErreur à la ligne " + numLigne + " : " + e.getMessage());
                    }
                }
                numLigne++;
            }

            // Vérification de la validité du réseau
            if (!reseau.estValide()) {
                throw new IOException(
                        "\nErreur : Le réseau n'est pas valide. Certaines maisons ne sont pas connectées : "
                                + reseau.getMaisonsNonConnectees());
            }

            return reseau;

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Erreur : Le fichier '" + fichier + "' est introuvable ou inaccessible.");

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * Analyse une ligne du fichier et applique la commande correspondante
     * sur le réseau électrique.
     * 
     * @param reseau Le réseau électrique à modifier
     * @param ligne  La ligne à analyser
     * @throws IOException en cas d'erreur de syntaxe ou de format
     */
    private static void analyserLigne(ReseauElectrique reseau, String ligne) throws IOException {
        // La ligne doit se terminer par un point
        if (ligne.endsWith(".")) {
            ligne = ligne.substring(0, ligne.length() - 1);
        } else {
            throw new IOException("\nLa ligne doit se terminer par un point");
        }

        // Gestion des parenthèses
        int debutParenthese = ligne.indexOf('(');
        int finParenthese = ligne.lastIndexOf(')');

        if (debutParenthese == -1 || finParenthese == -1) {
            throw new IOException("\nFormat incorrect : parenthèses manquantes");
        }

        if (finParenthese != ligne.length() - 1) {
            throw new IOException("\nFormat incorrect : la parenthèse fermante doit être en fin de ligne");
        }

        // Récupération de la commande
        String commande = ligne.substring(0, debutParenthese).trim();

        // Récupération des paramètres
        String contenuArgs = ligne.substring(debutParenthese + 1, finParenthese);
        String[] args = contenuArgs.split(",");

        // On enleve les espaces
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].trim();
            // Vérifier que chaque paramètre n'est pas vide après trim
            if (args[i].isEmpty()) {
                throw new IOException("\nUn paramètre ne peut pas être vide");
            }
        }

        // COMMANDE GÉNÉRATEUR
        if (commande.equals("generateur")) {
            // Format : generateur(nom, capacite)
            if (args.length == 2) {
                String nom = args[0];
                int capacite = Integer.parseInt(args[1]);
                reseau.ajouterGenerateur(nom, capacite);
            } else {
                throw new IOException("\nUn generateur doit avoir exactement 2 paramètres (nom, capacite)");
            }

            // COMMANDE MAISON

        } else if (commande.equals("maison")) {
            // Format : maison(nom, TypeConsommation)
            if (args.length != 2) {
                throw new IOException("\nUne maison doit avoir exactement 2 paramètres (nom, type)");
            }

            String nom = args[0];
            String typeStr = args[1].toUpperCase();

            try {
                TypeConsommation type = TypeConsommation.valueOf(typeStr);
                reseau.ajouterMaison(nom, type);
            } catch (IllegalArgumentException e) {
                // On lance notre propre message court
                throw new IOException("\nType de consommation invalide : " + args[1]);
            }

            // COMMANDE CONNEXION
        } else if (commande.equals("connexion")) {
            // Format : connexion(nom1, nom2)
            if (args.length == 2) {
                String nom1 = args[0];
                String nom2 = args[1];
                reseau.ajouterConnexion(nom1, nom2);
            }

        } else {
            // COMMANDE INCONNUE
            throw new IOException(
                    "\nCommande inconnue : '" + commande + "'. Commandes acceptées : generateur, maison, connexion");
        }
    }

}