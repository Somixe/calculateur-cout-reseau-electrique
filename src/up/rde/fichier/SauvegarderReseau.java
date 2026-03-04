package up.rde.fichier;

//CLASSE SAUVEGARDERRESEAU
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import up.rde.modele.ReseauElectrique;
import up.rde.modele.Generateur;
import up.rde.modele.Maison;

/**
 * Classe utilitaire permettant de sauvegarder un réseau électrique
 * dans un fichier texte.
 */

public class SauvegarderReseau {

    /**
     * Dossier dans lequel sont enregistrées les sauvegardes.
     */
    private static final String DOSSIER_SAUVEGARDE = "SauvegardeReseaux";

    /**
     * Sauvegarde un réseau électrique dans un fichier.
     * 
     * @param reseau        Le réseau électrique à sauvegarder
     * @param fichierSource Le fichier d'origine (peut être null)
     * @param nomFichier    Le nom du fichier de sauvegarde
     * @return Un message indiquant le succès de la sauvegarde
     * @throws IOException en cas d'erreur d'écriture ou de nom de fichier invalide
     */
    public static String sauvegarder(ReseauElectrique reseau, String fichierSource, String nomFichier)
            throws IOException {

        // Créer le dossier SauvegardeReseaux s'il n'existe pas
        File dossier = new File(DOSSIER_SAUVEGARDE);
        if (!dossier.exists()) {
            dossier.mkdir();
        }

        // Vérifier que le nom n'est pas identique au fichier source
        if (fichierSource != null) {
            File source = new File(fichierSource);
            if (source.getName().equals(nomFichier)) {
                throw new IOException("Le nom du fichier ne peut pas être identique au fichier source.");
            }
        }

        // Chemin complet du fichier de sauvegarde
        String chemin = DOSSIER_SAUVEGARDE + "/" + nomFichier;

        // Écrire dans le fichier
        BufferedWriter writer = new BufferedWriter(new FileWriter(chemin));

        // Sauvegarde des générateurs
        for (Generateur g : reseau.getGenerateurs()) {
            writer.write("generateur(" + g.getNom() + "," + g.getCapacite() + ").");
            writer.newLine();
        }

        // Sauvegarde des maisons
        for (Maison m : reseau.getMaisons()) {
            writer.write("maison(" + m.getNom() + "," + m.getTypeConsommation() + ").");
            writer.newLine();
        }

        // Sauvegarde des connexions
        Map<Maison, Generateur> connexions = reseau.getConnexions();
        for (Maison m : connexions.keySet()) {
            Generateur g = connexions.get(m);
            writer.write("connexion(" + g.getNom() + "," + m.getNom() + ").");
            writer.newLine();
        }

        writer.close();

        return "Solution sauvegardée avec succès dans : " + nomFichier + "\n";

    }
}
