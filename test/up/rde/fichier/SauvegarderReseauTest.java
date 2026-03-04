package up.rde.fichier;

//CLASSE SAUVEGARDERRESEAUTEST

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import up.rde.modele.ReseauElectrique;
import up.rde.modele.TypeConsommation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Tests unitaires pour la classe SauvegarderReseau.
 * Vérifie la création de fichiers de sauvegarde pour un réseau électrique.
 */

public class SauvegarderReseauTest {

    private ReseauElectrique reseau;
    private static final String DOSSIER_TEST = "SauvegardeReseaux";
    private static final String FICHIER_TEST = "test_unit_save.txt";

    /**
     * Initialise un nouveau réseau avant chaque test.
     */
    @BeforeEach
    void setUp() {
        reseau = new ReseauElectrique();
    }

    /**
     * Nettoyage après chaque test : supprime le fichier créé et éventuellement le
     * dossier si vide.
     */
    @AfterEach
    void tearDown() {
        File fichier = new File(DOSSIER_TEST + "/" + FICHIER_TEST);
        if (fichier.exists()) {
            fichier.delete();
        }

        // Optionnel : on essaie de supprimer le dossier s'il est vide
        File dossier = new File(DOSSIER_TEST);
        if (dossier.exists() && dossier.listFiles().length == 0) {
            dossier.delete();
        }
    }

    /**
     * Teste la sauvegarde nominale d'un réseau complet.
     * Vérifie que le fichier est créé et que son contenu est correct.
     * 
     * @throws IOException si une erreur survient lors de l'écriture du fichier
     */
    @Test
    void testSauvegardeNominale() throws IOException {
        // 1. Préparation d'un réseau complet
        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterMaison("M1", TypeConsommation.NORMAL);
        reseau.ajouterConnexion("M1", "G1");

        // 2. Exécution de la sauvegarde
        // On simule que le fichier source s'appelait "source.txt"
        SauvegarderReseau.sauvegarder(reseau, "source.txt", FICHIER_TEST);

        // 3. Vérifications
        File fichierCree = new File(DOSSIER_TEST + "/" + FICHIER_TEST);
        assertTrue(fichierCree.exists(), "Le fichier de sauvegarde doit être créé");

        List<String> lignes = Files.readAllLines(Path.of(fichierCree.getPath()));

        assertTrue(lignes.size() >= 3, "Le fichier doit contenir au moins 3 lignes");

        assertTrue(lignes.contains("generateur(G1,100)."));
        assertTrue(lignes.contains("maison(M1,NORMAL)."));

        assertTrue(lignes.contains("connexion(G1,M1)."));
    }

    /**
     * Teste la création automatique du dossier de sauvegarde si celui-ci n'existe
     * pas.
     * 
     * @throws IOException si une erreur survient lors de la sauvegarde
     */
    @Test
    void testCreationDossierAutomatique() throws IOException {
        // On supprime le dossier manuellement avant le test pour être sûr qu'il
        // n'existe pas
        File dossier = new File(DOSSIER_TEST);
        if (dossier.exists()) {
            // Nettoyage brutal pour le test (attention si vous avez de vraies sauvegardes
            // !)
            for (File f : dossier.listFiles())
                f.delete();
            dossier.delete();
        }
        assertFalse(dossier.exists(), "Le dossier ne doit pas exister avant le test");

        // On sauvegarde un réseau vide juste pour tester la création du dossier
        SauvegarderReseau.sauvegarder(reseau, "src.txt", FICHIER_TEST);

        // Le dossier doit avoir été créé
        assertTrue(dossier.exists(), "Le dossier 'SauvegardeReseaux' doit être créé automatiquement");
    }

    /**
     * Vérifie que l'on ne peut pas écraser le fichier source avec le même nom.
     */
    @Test
    void testProtectionEcrasementFichierSource() {
        // Si on essaie de sauvegarder sous le MÊME nom que la source
        String nomIdentique = "monReseau.txt";

        Exception exception = assertThrows(IOException.class, () -> {
            SauvegarderReseau.sauvegarder(reseau, "chemin/vers/" + nomIdentique, nomIdentique);
        });

        assertEquals("Le nom du fichier ne peut pas être identique au fichier source.", exception.getMessage());
    }

    /**
     * Vérifie la sauvegarde d'un réseau vide.
     * Le fichier créé doit exister et être vide.
     * 
     * @throws IOException si une erreur survient lors de la sauvegarde
     */
    @Test
    void testSauvegardeReseauVide() throws IOException {
        // Sauvegarder un réseau vide ne doit pas planter
        SauvegarderReseau.sauvegarder(reseau, "source.txt", FICHIER_TEST);

        File fichierCree = new File(DOSSIER_TEST + "/" + FICHIER_TEST);
        assertTrue(fichierCree.exists());
        assertTrue(fichierCree.length() == 0, "Le fichier doit être vide (ou presque) pour un réseau vide");
    }
}