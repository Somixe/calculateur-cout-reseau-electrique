package up.rde.modele;

//CLASSE RESEAUELECTRIQUETEST
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import up.rde.utilitaire.CalculateurCout;

import java.util.List;

/**
 * Tests unitaires pour la classe ReseauElectrique.
 * Vérifie l'ajout de générateurs et maisons, la gestion des connexions,
 * la validation du réseau, les calculs de coût, et la gestion des exceptions.
 */
public class ReseauElectriqueTest {

    private ReseauElectrique reseau;

    /**
     * Initialisation avant chaque test pour un état propre.
     */
    @BeforeEach
    void setUp() {
        // Remise à zéro avant CHAQUE test
        reseau = new ReseauElectrique();
    }

    // TESTS : AJOUT D'ÉLÉMENTS

    /**
     * Teste l'ajout de générateurs avec mise à jour de capacité
     * et gestion d'arguments invalides.
     */
    @Test
    void testAjoutGenerateur() {
        // 1. Ajout normal
        reseau.ajouterGenerateur("G1", 100);
        assertEquals(1, reseau.getGenerateurs().size());
        assertEquals(100, reseau.getGenerateurs().get(0).getCapacite());

        // 2. Mise à jour (même nom, nouvelle capacité)
        reseau.ajouterGenerateur("G1", 150);
        assertEquals(1, reseau.getGenerateurs().size(), "Ne doit pas créer de doublon");
        assertEquals(150, reseau.getGenerateurs().get(0).getCapacite(), "La capacité doit être mise à jour");

        // 3. Test d'erreur : Capacité négative
        assertThrows(IllegalArgumentException.class, () -> {
            reseau.ajouterGenerateur("G2", -50);
        }, "Doit refuser une capacité négative");
    }

    /**
     * Teste l'ajout de maisons avec mise à jour du type de consommation.
     */
    @Test
    void testAjoutMaison() {
        // 1. Ajout normal
        reseau.ajouterMaison("M1", TypeConsommation.NORMAL); // 20kW
        assertEquals(1, reseau.getMaisons().size());
        assertEquals(20, reseau.getMaisons().get(0).getDemande());

        // 2. Mise à jour type
        reseau.ajouterMaison("M1", TypeConsommation.FORTE); // 40kW
        assertEquals(40, reseau.getMaisons().get(0).getDemande(), "Le type doit être mis à jour");
    }

    // TESTS : CONNEXIONS

    /**
     * Teste l'ajout, suppression et modification de connexions
     * entre maisons et générateurs.
     */
    @Test
    void testGestionConnexions() {
        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterMaison("M1", TypeConsommation.NORMAL);
        reseau.ajouterMaison("M2", TypeConsommation.BASSE);

        // 1. Connexion M1 -> G1
        reseau.ajouterConnexion("M1", "G1");

        // Vérification
        Maison m1 = reseau.getMaisons().get(0); // M1
        assertTrue(reseau.estMaisonConnectee(m1));
        assertEquals("G1", reseau.getGenerateurConnecte(m1).getNom());

        // 2. Changement de connexion (supposons qu'on ajoute G2)
        reseau.ajouterGenerateur("G2", 50);

        reseau.supprimerConnexion("M1", "G1");
        assertFalse(reseau.estMaisonConnectee(m1));

        // Reconnexion vers G2
        reseau.ajouterConnexion("M1", "G2");
        assertEquals("G2", reseau.getGenerateurConnecte(m1).getNom());
    }

    /**
     * Teste qu'une connexion vers une maison inexistante lève une exception.
     */
    @Test
    void testConnexionInvalide() {
        reseau.ajouterGenerateur("G1", 100);

        // Tentative de connexion avec une maison qui n'existe pas
        assertThrows(IllegalArgumentException.class, () -> {
            reseau.ajouterConnexion("MaisonFantome", "G1");
        });
    }

    // TESTS : VALIDATION RÉSEAU

    /**
     * Vérifie si le réseau est valide selon la connexion des maisons.
     */
    @Test
    void testReseauValide() {
        // Réseau vide -> Invalide
        assertFalse(reseau.estValide());

        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterMaison("M1", TypeConsommation.NORMAL);

        // Maison non connectée -> Invalide
        assertFalse(reseau.estValide());
        List<String> erreurs = reseau.getMaisonsNonConnectees();
        assertTrue(erreurs.contains("M1"));

        // Tout connecté -> Valide
        reseau.ajouterConnexion("M1", "G1");
        assertTrue(reseau.estValide());
    }

    // TESTS : CALCUL DE COÛT

    /**
     * Vérifie le calcul du coût total d'un réseau simple.
     */
    @Test
    void testCalculCalculateurCout() {

        // G1 (Cap 100) connectée à M1 (20) et M2 (40) -> Charge 60. Taux = 0.6
        // G2 (Cap 100) connectée à personne. -> Charge 0. Taux = 0.0

        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterGenerateur("G2", 100);

        reseau.ajouterMaison("M1", TypeConsommation.NORMAL); // 20
        reseau.ajouterMaison("M2", TypeConsommation.FORTE); // 40

        reseau.ajouterConnexion("M1", "G1");
        reseau.ajouterConnexion("M2", "G1");

        // Vérifions d'abord la charge interne
        Generateur g1 = reseau.getGenerateurs().get(0);

        if (!g1.getNom().equals("G1"))
            g1 = reseau.getGenerateurs().get(1);

        assertEquals(60.0, reseau.getChargeGenerateur(g1));

        double coutCalcule = CalculateurCout.coutInstanceReseau(reseau, 10.0);

        // On utilise un delta (0.0001) pour comparer des doubles
        assertEquals(0.6, coutCalcule, 0.0001, "Le coût calculé ne correspond pas à la théorie");
    }

    /**
     * Vérifie le calcul de surcharge si charge > capacité.
     */
    @Test
    void testCalculSurcharge() {
        // G1 (Cap 10) connecté à M1 (Forte 40) -> Charge 40. Surcharge !
        reseau.ajouterGenerateur("G1", 10);
        reseau.ajouterMaison("M1", TypeConsommation.FORTE);
        reseau.ajouterConnexion("M1", "G1");

        // Charge = 40. Capacité = 10.
        // Surcharge = (40 - 10) / 10 = 30 / 10 = 3.0

        double surchargeSeule = CalculateurCout.calculerSurcharge(reseau);
        assertEquals(3.0, surchargeSeule, 0.0001);
    }

    /**
     * Vérifie la dispersion extrême des générateurs.
     */
    @Test
    void testCalculDispersionExtreme() {
        // Scénario :
        // G1 (Cap 100) avec M1 (Normale 20) + M2 (Forte 40) + M3 (Forte 40) -> Charge
        // 100 (100%)
        // G2 (Cap 100) sans rien -> Charge 0 (0%)

        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterGenerateur("G2", 100);

        reseau.ajouterMaison("M1", TypeConsommation.NORMAL);
        reseau.ajouterMaison("M2", TypeConsommation.FORTE);
        reseau.ajouterMaison("M3", TypeConsommation.FORTE);

        reseau.ajouterConnexion("G1", "M1");
        reseau.ajouterConnexion("G1", "M2");
        reseau.ajouterConnexion("G1", "M3");

        // Calcul théorique :
        // Taux G1 = 1.0, Taux G2 = 0.0
        // Moyenne = 0.5
        // Dispersion = |1.0 - 0.5| + |0.0 - 0.5| = 0.5 + 0.5 = 1.0

        double dispersion = CalculateurCout.calculerDispersion(reseau);
        assertEquals(1.0, dispersion, 0.0001, "La dispersion devrait être de 1.0");
    }

    // UNICITÉ DES NOMS
    /**
     * Vérifie qu'on ne peut pas créer un générateur et une maison avec le même nom.
     */
    @Test
    void testUniciteDesNoms() {
        reseau.ajouterGenerateur("Objet1", 100);

        // 1. Essayer d'ajouter une maison avec le même nom qu'un générateur
        assertThrows(IllegalArgumentException.class, () -> {
            reseau.ajouterMaison("Objet1", TypeConsommation.BASSE);
        }, "Ne devrait pas accepter une maison avec un nom de générateur existant");

        // 2. Essayer d'ajouter un générateur avec le même nom qu'une maison
        reseau.ajouterMaison("MaisonUnique", TypeConsommation.NORMAL);
        assertThrows(IllegalArgumentException.class, () -> {
            reseau.ajouterGenerateur("MaisonUnique", 50);
        }, "Ne devrait pas accepter un générateur avec un nom de maison existante");
    }

    // ARGUMENTS INVALIDES
    /**
     * Vérifie que les méthodes lèvent IllegalArgumentException pour des arguments
     * invalides.
     */
    @Test
    void testArgumentsInvalides() {
        // Test noms nuls ou vides
        assertThrows(IllegalArgumentException.class, () -> reseau.ajouterGenerateur(null, 100));
        assertThrows(IllegalArgumentException.class, () -> reseau.ajouterGenerateur("", 100));
        assertThrows(IllegalArgumentException.class, () -> reseau.ajouterGenerateur("   ", 100));

        // Test objets nuls
        assertThrows(IllegalArgumentException.class, () -> reseau.ajouterMaison("M1", null));

        // Test connexions nulles
        assertThrows(IllegalArgumentException.class, () -> reseau.ajouterConnexion(null, "G1"));
        assertThrows(IllegalArgumentException.class, () -> reseau.ajouterConnexion("M1", null));
    }

    // RESEAU VIDE / INACTIF
    /**
     * Vérifie que le coût d'un réseau vide ou sans consommation est 0.
     */
    @Test
    void testReseauVideOuInactif() {
        // 1. Réseau totalement vide
        assertEquals(0.0, CalculateurCout.coutInstanceReseau(reseau, 10), "Le coût d'un réseau vide doit être 0");

        // 2. Réseau avec générateurs mais SANS maisons (Consommation 0)
        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterGenerateur("G2", 100);

        // Charge = 0 partout. Moyenne = 0. Dispersion = 0. Surcharge = 0.
        assertEquals(0.0, CalculateurCout.coutInstanceReseau(reseau, 10), "Le coût sans consommation doit être 0");
    }

    // SUPPRESSION ET IMPACT SUR CHARGE

    /**
     * Vérifie la suppression d'une connexion et l'impact sur la charge des
     * générateurs.
     */
    @Test
    void testSuppressionEtImpactSurCharge() {
        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterMaison("M1", TypeConsommation.FORTE); // 40kW
        reseau.ajouterConnexion("G1", "M1");

        // Vérif avant suppression
        assertEquals(40.0, reseau.getChargeGenerateur(reseau.getGenerateurs().get(0)));

        // Action : Suppression
        reseau.supprimerConnexion("M1", "G1");

        // Vérifications
        assertFalse(reseau.estMaisonConnectee(reseau.getMaisons().get(0)), "La maison ne doit plus être connectée");
        assertEquals(0.0, reseau.getChargeGenerateur(reseau.getGenerateurs().get(0)),
                "La charge du générateur doit revenir à 0");

        // Tenter de supprimer une connexion qui n'existe plus doit lever une erreur
        assertThrows(IllegalArgumentException.class, () -> {
            reseau.supprimerConnexion("M1", "G1");
        });
    }

}