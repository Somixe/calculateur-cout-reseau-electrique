package up.rde.modele;

//CLASSE GENERATEURTEST
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Tests unitaires pour la classe Generateur.
 * Vérifie le comportement du constructeur, des accesseurs et de la modification
 * de capacité.
 */

public class GenerateurTest {

    /**
     * Test la création d'un générateur avec des valeurs valides.
     * Vérifie que le nom et la capacité sont correctement initialisés.
     */
    @Test
    void testCreationGenerateurValide() {
        Generateur g = new Generateur("G1", 100);

        // Vérifie le nom
        assertEquals("G1", g.getNom());
        // Vérifie la capacité
        assertEquals(100, g.getCapacite());
    }

    /**
     * Teste le constructeur avec des valeurs invalides.
     * Vérifie que des IllegalArgumentException sont levées pour :
     * - nom null ou vide
     * - capacité <= 0
     */
    @Test
    void testConstructeurInvalide() {
        // Test nom incorrect
        assertThrows(IllegalArgumentException.class, () -> new Generateur(null, 100));
        assertThrows(IllegalArgumentException.class, () -> new Generateur("", 100));

        // Test capacité zéro ou négative
        assertThrows(IllegalArgumentException.class, () -> new Generateur("G1", 0));
        assertThrows(IllegalArgumentException.class, () -> new Generateur("G1", -10));
    }

    /**
     * Test la modification de la capacité du générateur.
     * Vérifie que :
     * - La capacité est modifiable avec des valeurs valides
     * - Une IllegalArgumentException est levée pour des valeurs invalides
     * - La valeur précédente reste inchangée après une erreur
     */
    @Test
    void testModificationCapacite() {
        Generateur g = new Generateur("G1", 50);

        // Modification valide
        g.setCapacite(200);
        assertEquals(200, g.getCapacite());

        // Modification invalide (ne doit pas changer la valeur précédente)
        assertThrows(IllegalArgumentException.class, () -> g.setCapacite(-5));
        assertEquals(200, g.getCapacite(), "La capacité ne devrait pas avoir changé après une erreur");
    }
}