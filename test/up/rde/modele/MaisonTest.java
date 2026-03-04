package up.rde.modele;

//CLASSE MAISONTEST

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Tests unitaires pour la classe Maison.
 * Vérifie le comportement du constructeur, des accesseurs, de la modification
 * de type,
 * et la méthode toString.
 */

public class MaisonTest {
    /**
     * Test la création d'une maison avec des valeurs valides.
     * Vérifie le nom, le type de consommation et la demande associée.
     */
    @Test
    void testCreationMaisonValide() {
        Maison m = new Maison("M1", TypeConsommation.BASSE);

        // Vérifie le nom
        assertEquals("M1", m.getNom());
        assertEquals(TypeConsommation.BASSE, m.getTypeConsommation());
        // Vérifie que BASSE correspond bien à 10kW
        assertEquals(10, m.getDemande());
    }

    /**
     * Teste le constructeur avec des valeurs invalides.
     * Vérifie que des IllegalArgumentException sont levées pour :
     * - nom null ou vide
     * - type de consommation null
     */
    @Test
    void testConstructeurInvalide() {
        // Test nom null
        assertThrows(IllegalArgumentException.class, () -> new Maison(null, TypeConsommation.NORMAL));

        // Test nom vide
        assertThrows(IllegalArgumentException.class, () -> new Maison("", TypeConsommation.NORMAL));
        assertThrows(IllegalArgumentException.class, () -> new Maison("   ", TypeConsommation.NORMAL));

        // Test type null
        assertThrows(IllegalArgumentException.class, () -> new Maison("M1", null));
    }

    /**
     * Test la modification du type de consommation d'une maison.
     * Vérifie que :
     * - Le type est modifiable avec une valeur valide
     * - La demande associée est mise à jour correctement
     * - Une exception est levée pour une valeur null
     */
    @Test
    void testModificationType() {
        Maison m = new Maison("M1", TypeConsommation.NORMAL);
        assertEquals(20, m.getDemande());

        // Changement vers FORTE
        m.setTypeConsommation(TypeConsommation.FORTE);
        assertEquals(TypeConsommation.FORTE, m.getTypeConsommation());
        assertEquals(40, m.getDemande(), "La demande doit s'adapter au nouveau type");

        // Tentative de mettre null
        assertThrows(IllegalArgumentException.class, () -> m.setTypeConsommation(null));
    }

    /**
     * Test la méthode toString() de Maison.
     * Vérifie que la représentation contient le nom et la demande en kW.
     */
    @Test

    void testToString() {
        Maison m = new Maison("Villa", TypeConsommation.FORTE);
        String affichage = m.toString();
        // On vérifie que le toString contient les infos essentielles
        assertTrue(affichage.contains("Villa"));
        assertTrue(affichage.contains("40"));
    }
}