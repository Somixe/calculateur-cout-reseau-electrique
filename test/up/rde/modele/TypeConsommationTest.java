package up.rde.modele;

//CLASSE TYPECONSOMMATIONTEST
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Tests unitaires pour l'énumération TypeConsommation.
 * Vérifie les valeurs associées aux types et la validité des noms.
 */

public class TypeConsommationTest {
    /**
     * Vérifie que les constantes de consommation respectent les valeurs attendues.
     * BASSE = 10 kW, NORMAL = 20 kW, FORTE = 40 kW.
     */
    @Test
    void testValeursConsommation() {
        // Vérifie que les constantes du sujet sont respectées
        assertEquals(10, TypeConsommation.BASSE.getValeur());
        assertEquals(20, TypeConsommation.NORMAL.getValeur());
        assertEquals(40, TypeConsommation.FORTE.getValeur());
    }

    /**
     * Vérifie la méthode utilitaire estTypeValide.
     * S'assure que les types connus sont validés correctement,
     * et que les valeurs invalides (null, inconnues, vides) sont rejetées.
     */
    @Test
    void testEstTypeValide() {
        // Types valides
        assertTrue(TypeConsommation.estTypeValide("BASSE"));
        assertTrue(TypeConsommation.estTypeValide("basse")); // Si votre méthode gère la casse
        assertTrue(TypeConsommation.estTypeValide("FORTE"));

        // Types invalides
        assertFalse(TypeConsommation.estTypeValide("INCONNU"));
        assertFalse(TypeConsommation.estTypeValide(""));
        assertFalse(TypeConsommation.estTypeValide(null));
    }
}