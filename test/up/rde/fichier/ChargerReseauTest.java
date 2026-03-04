package up.rde.fichier;

//CLASSE CHARGERRESEAUTEST
import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

import up.rde.modele.ReseauElectrique;
import up.rde.modele.TypeConsommation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe ChargerReseau.
 * Vérifie la capacité à charger un réseau électrique depuis un fichier texte.
 */

public class ChargerReseauTest {

    private static final String FICHIER_TEST = "test_temporaire.txt";

    /**
     * Supprime le fichier de test temporaire après chaque test.
     */
    @AfterEach
    void tearDown() {
        File fichier = new File(FICHIER_TEST);
        if (fichier.exists()) {
            fichier.delete();
        }
    }

    /**
     * Crée un fichier temporaire avec le contenu spécifié pour les tests.
     * 
     * @param contenu Le contenu à écrire dans le fichier de test
     * @throws IOException Si une erreur d'écriture survient
     */
    private void creerFichierTest(String contenu) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FICHIER_TEST))) {
            writer.write(contenu);
        }
    }

    /**
     * Teste le chargement d'un réseau valide depuis un fichier.
     * Vérifie la présence correcte des générateurs, maisons et connexions.
     * 
     * @throws IOException Si une erreur survient lors de la création du fichier
     */
    @Test
    void testChargementValide() throws IOException {
        String contenu = "generateur(G1, 100).\n" +
                "maison(M1, NORMAL).\n" +
                "connexion(M1, G1).";
        creerFichierTest(contenu);

        ReseauElectrique reseau = ChargerReseau.charger(FICHIER_TEST);

        // Vérifications
        assertNotNull(reseau);
        assertEquals(1, reseau.getGenerateurs().size());
        assertEquals(1, reseau.getMaisons().size());
        assertEquals("G1", reseau.getGenerateurs().get(0).getNom());
        assertEquals(TypeConsommation.NORMAL, reseau.getMaisons().get(0).getTypeConsommation());
        assertTrue(reseau.estMaisonConnectee(reseau.getMaisons().get(0)));
    }

    /**
     * Teste le comportement lorsque le fichier à charger n'existe pas.
     * Devrait lancer une IOException.
     */
    @Test
    void testFichierInexistant() {
        assertThrows(IOException.class, () -> {
            ChargerReseau.charger("fichier_qui_n_existe_pas_12345.txt");
        });
    }
}