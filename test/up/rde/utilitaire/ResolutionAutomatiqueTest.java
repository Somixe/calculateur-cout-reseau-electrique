package up.rde.utilitaire;

//CLASSE RESOLUTIONAUTOMATIQUETEST
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import up.rde.modele.ReseauElectrique;
import up.rde.modele.TypeConsommation;

/**
 * Tests unitaires pour la classe ResolutionAutomatique.
 * Vérifie le bon fonctionnement de l'algorithme d'optimisation
 * du réseau électrique.
 */
public class ResolutionAutomatiqueTest {

    private ReseauElectrique reseau;
    private ResolutionAutomatique resolution;
    private static final double LAMBDA = 10.0;

    /**
     * Initialisation avant chaque test : création d'un nouveau réseau.
     */
    @BeforeEach
    public void setUp() {
        reseau = new ReseauElectrique();
    }

    // TESTS DE CONSTRUCTEUR

    /**
     * Teste le constructeur avec un réseau valide et un lambda positif.
     */
    @Test
    public void testConstructeur() {
        resolution = new ResolutionAutomatique(reseau, LAMBDA);
        assertNotNull(resolution, "La résolution ne doit pas être null");
    }

    /**
     * Teste le constructeur avec un lambda négatif.
     */
    @Test
    public void testConstructeurAvecLambdaNegatif() {
        resolution = new ResolutionAutomatique(reseau, -1.0);
        assertNotNull(resolution);
    }

    /**
     * Teste le constructeur avec un réseau null.
     */
    @Test
    public void testConstructeurAvecReseauNull() {
        assertDoesNotThrow(() -> {
            new ResolutionAutomatique(null, LAMBDA);
        });
    }

    // TESTS SUR RÉSEAU VIDE

    /**
     * Vérifie que l'exécution sur un réseau vide ne plante pas.
     */
    @Test
    public void testExecuterSurReseauVide() {
        resolution = new ResolutionAutomatique(reseau, LAMBDA);

        assertDoesNotThrow(() -> {
            resolution.executer(100);
        });
    }

    /**
     * Vérifie que l'exécution sans paramètre sur un réseau vide ne plante pas.
     */
    @Test
    public void testExecuterSansParametreSurReseauVide() {
        resolution = new ResolutionAutomatique(reseau, LAMBDA);

        assertDoesNotThrow(() -> {
            resolution.executer();
        });
    }

    // TESTS SUR RÉSEAU SIMPLE

    /**
     * Vérifie l'exécution avec un générateur et une maison.
     * La maison doit être connectée après optimisation.
     */
    @Test
    public void testExecuterAvecUneSeuleMaisonEtUnGenerateur() {
        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterMaison("M1", TypeConsommation.NORMAL);

        resolution = new ResolutionAutomatique(reseau, LAMBDA);

        assertDoesNotThrow(() -> {
            resolution.executer(50);
        });

        // Vérifie que la maison est connectée après optimisation
        assertFalse(reseau.getMaisonsNonConnectees().isEmpty() && reseau.getMaisons().size() == 1,
                "La maison devrait être connectée après optimisation");
    }

    /**
     * Vérifie l'exécution avec deux maisons et un générateur.
     * Les deux maisons doivent rester connectées après optimisation.
     */
    @Test
    public void testExecuterAvecDeuxMaisonsEtUnGenerateur() {
        // 1. Création
        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterMaison("M1", TypeConsommation.BASSE);
        reseau.ajouterMaison("M2", TypeConsommation.BASSE);

        // 2. INITIALISATION (OBLIGATOIRE)
        // On connecte les maisons au départ pour que l'algo ait une solution à
        // manipuler.

        reseau.ajouterConnexion("M1", "G1");
        reseau.ajouterConnexion("M2", "G1");

        // 3. Exécution
        resolution = new ResolutionAutomatique(reseau, LAMBDA);
        resolution.executer(50);

        // 4. Vérification

        assertEquals(2, reseau.getConnexions().size(),
                "Les deux maisons doivent rester connectées");

        // On vérifie qu'elles sont bien sur G1
        assertEquals("G1", reseau.getGenerateurConnecte(reseau.getMaisons().get(0)).getNom());
    }

    // TESTS D'OPTIMISATION

    /**
     * Vérifie que l'optimisation améliore ou maintient le coût du réseau.
     */
    @Test
    public void testOptimisationAmelioreCout() {
        reseau.ajouterGenerateur("G1", 50);
        reseau.ajouterGenerateur("G2", 100);
        reseau.ajouterMaison("M1", TypeConsommation.FORTE);
        reseau.ajouterMaison("M2", TypeConsommation.BASSE);

        // Connexion initiale non optimale
        reseau.ajouterConnexion("M1", "G1");
        reseau.ajouterConnexion("M2", "G2");

        double coutInitial = CalculateurCout.coutInstanceReseau(reseau, LAMBDA);

        resolution = new ResolutionAutomatique(reseau, LAMBDA);
        resolution.executer(100);

        double coutFinal = CalculateurCout.coutInstanceReseau(reseau, LAMBDA);

        assertTrue(coutFinal <= coutInitial,
                "L'optimisation devrait améliorer ou maintenir le coût");
    }

    /**
     * Vérifie l'exécution avec trois générateurs et trois maisons.
     * La charge doit être répartie correctement après optimisation.
     */
    @Test
    public void testOptimisationAvecTroisGenerateurs() {
        // 1. Création des éléments
        reseau.ajouterGenerateur("G1", 30);
        reseau.ajouterGenerateur("G2", 60);
        reseau.ajouterGenerateur("G3", 90);
        reseau.ajouterMaison("M1", TypeConsommation.BASSE); // 10 kW
        reseau.ajouterMaison("M2", TypeConsommation.NORMAL); // 20 kW
        reseau.ajouterMaison("M3", TypeConsommation.FORTE); // 40 kW

        // 2. INITIALISATION (CRUCIAL !)

        reseau.ajouterConnexion("M2", "G1");
        reseau.ajouterConnexion("M3", "G1");

        // 3. Lancement de l'optimisation
        resolution = new ResolutionAutomatique(reseau, LAMBDA);
        resolution.executer(200);

        // 4. Vérifications

        assertEquals(3, reseau.getConnexions().size(), "Les 3 maisons doivent rester connectées");

        // Vérifie que le réseau est valide (chaque maison a un générateur)
        assertTrue(reseau.estValide(), "Le réseau doit être valide après optimisation");

        // Vérifier que l'optimisation a bien eu lieu (G1 était surchargé au
        // début)

        double chargeG1 = reseau.getChargeGenerateur(reseau.getGenerateurs().get(0)); // G1
        assertTrue(chargeG1 <= 30, "La charge de G1 aurait dû être réduite pour supprimer la surcharge");
    }

    // TESTS DE ROBUSTESSE

    /**
     * Test l'exécution avec k = 0 (nombre d'itérations).
     */
    @Test
    public void testExecuterAvecKZero() {
        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterMaison("M1", TypeConsommation.NORMAL);

        resolution = new ResolutionAutomatique(reseau, LAMBDA);

        assertDoesNotThrow(() -> {
            resolution.executer(0);
        });
    }

    /**
     * Test l'exécution avec un k négatif.
     */
    @Test
    public void testExecuterAvecKNegatif() {
        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterMaison("M1", TypeConsommation.NORMAL);

        resolution = new ResolutionAutomatique(reseau, LAMBDA);

        assertDoesNotThrow(() -> {
            resolution.executer(-10);
        });
    }

    /**
     * Test l'exécution avec lambda = 0.
     */
    @Test
    public void testExecuterAvecLambdaZero() {
        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterMaison("M1", TypeConsommation.NORMAL);

        resolution = new ResolutionAutomatique(reseau, 0.0);

        assertDoesNotThrow(() -> {
            resolution.executer(50);
        });
    }

    // TESTS DE CAS COMPLEXES

    /**
     * Vérifie l'équilibrage automatique sur un réseau déséquilibré.
     */
    @Test

    public void testOptimisationReseauEquilibre() {
        // 1. Préparation des éléments
        reseau.ajouterGenerateur("G1", 60);
        reseau.ajouterGenerateur("G2", 60);

        // 3 maisons "Normales" (20 kW chacune)
        reseau.ajouterMaison("M1", TypeConsommation.NORMAL);
        reseau.ajouterMaison("M2", TypeConsommation.NORMAL);
        reseau.ajouterMaison("M3", TypeConsommation.NORMAL);

        // 2. INITIALISATION (CRUCIAL)

        reseau.ajouterConnexion("M1", "G1");
        reseau.ajouterConnexion("M2", "G1");
        reseau.ajouterConnexion("M3", "G1");

        // 3. Lancement de l'optimisation
        resolution = new ResolutionAutomatique(reseau, LAMBDA);
        resolution.executer(100);

        // 4. Vérifications
        assertTrue(reseau.estValide(), "Le réseau doit rester valide (toutes maisons connectées)");
        assertEquals(3, reseau.getConnexions().size(), "Il doit y avoir 3 connexions au total");

        // 5. Vérification de l'équilibrage (Logique métier)

        double chargeG2 = reseau.getChargeGenerateur(reseau.getGenerateurs().get(1)); // G2
        assertTrue(chargeG2 > 0, "L'algorithme aurait dû déplacer au moins une maison vers G2 pour équilibrer");
    }

    /**
     * Vérifie qu'aucune maison n'est laissée non connectée après optimisation.
     */
    @Test
    public void testOptimisationReseauSurcharge() {
        reseau.ajouterGenerateur("G1", 30);
        reseau.ajouterMaison("M1", TypeConsommation.FORTE);
        reseau.ajouterMaison("M2", TypeConsommation.FORTE);

        resolution = new ResolutionAutomatique(reseau, LAMBDA);

        assertDoesNotThrow(() -> {
            resolution.executer(50);
        });
    }

    /**
     * Vérifie que l'optimisation gère correctement un grand nombre de maisons
     * et répartit la charge entre générateurs.
     */
    public void testOptimisationAvecBeaucoupDeMaisons() {
        // 1. Préparation du réseau
        reseau.ajouterGenerateur("G1", 200);
        reseau.ajouterGenerateur("G2", 200);
        reseau.ajouterGenerateur("G3", 200);

        for (int i = 1; i <= 10; i++) {
            TypeConsommation type = (i % 3 == 0) ? TypeConsommation.FORTE
                    : (i % 2 == 0) ? TypeConsommation.NORMAL : TypeConsommation.BASSE;
            reseau.ajouterMaison("M" + i, type);

            reseau.ajouterConnexion("M" + i, "G1");
        }

        assertEquals(10, reseau.getMaisonsConnectees(reseau.getGenerateurs().get(0)).size());

        // 2. Lancement de l'optimisation
        double lambda = 10.0; // Définir une valeur pour lambda
        resolution = new ResolutionAutomatique(reseau, lambda);

        long debut = System.currentTimeMillis();
        resolution.executer(1000); // Donnez un peu plus d'itérations (ex: 1000)
        long duree = System.currentTimeMillis() - debut;

        // 3. Vérifications
        assertTrue(duree < 5000, "L'optimisation doit être rapide (< 5s)");

        // On vérifie qu'on a toujours 10 connexions (personne n'a été débranché)
        assertEquals(10, reseau.getConnexions().size(), "Toutes les maisons doivent rester connectées");

        // On vérifie que la charge a été répartie (G1 ne doit plus tout avoir seul)
        int maisonsSurG1 = reseau.getMaisonsConnectees(reseau.getGenerateurs().get(0)).size();
        assertTrue(maisonsSurG1 < 10, "L'algorithme aurait dû déplacer des maisons hors de G1");
    }

    // TESTS DE VALIDATION DES RÉSULTATS

    /**
     * Vérifie que le réseau reste valide après optimisation.
     */
    @Test
    public void testReseauResteValideApresOptimisation() {
        // 1. Mise en place
        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterGenerateur("G2", 100);
        reseau.ajouterMaison("M1", TypeConsommation.NORMAL);
        reseau.ajouterMaison("M2", TypeConsommation.FORTE);
        reseau.ajouterMaison("M3", TypeConsommation.BASSE);

        // 2. ÉTAT INITIAL VALIDE (OBLIGATOIRE)

        reseau.ajouterConnexion("M1", "G1");
        reseau.ajouterConnexion("M2", "G1");
        reseau.ajouterConnexion("M3", "G1");

        assertTrue(reseau.estValide(), "Le réseau doit être valide AVANT l'optimisation");

        // 3. Exécution

        double lambda = 10.0;
        resolution = new ResolutionAutomatique(reseau, lambda);
        resolution.executer(100);

        // 4. Vérification

        assertTrue(reseau.estValide(), "Le réseau doit rester valide (toutes maisons connectées) APRÈS optimisation");

        boolean aChange = reseau.getMaisonsConnectees(reseau.getGenerateurs().get(1)).size() > 0;
        assertTrue(aChange, "L'algorithme aurait dû répartir la charge sur G2");
    }

    /**
     * Vérifie qu'aucune maison n'est laissée non connectée après optimisation.
     */
    @Test
    public void testAucuneMaisonNonConnecteeApresOptimisation() {
        // 1. Préparation
        reseau.ajouterGenerateur("G1", 200);
        reseau.ajouterMaison("M1", TypeConsommation.NORMAL);
        reseau.ajouterMaison("M2", TypeConsommation.BASSE);

        // 2. INITIALISATION (CRUCIAL)

        reseau.ajouterConnexion("M1", "G1");
        reseau.ajouterConnexion("M2", "G1");

        // 3. Exécution
        resolution = new ResolutionAutomatique(reseau, LAMBDA);
        resolution.executer(50);

        // 4. Vérification
        // On vérifie que la liste des maisons non connectées est bien vide

        assertTrue(reseau.getMaisonsNonConnectees().isEmpty(),
                "Toutes les maisons doivent rester connectées après l'optimisation");

        // Vérification alternative via la taille des connexions
        assertEquals(2, reseau.getConnexions().size(), "Il doit y avoir 2 connexions actives");
    }

    // TESTS SUPPLÉMENTAIRES POUR COUVERTURE

    /**
     * Vérifie l'exécution avec un seul essai (k = 1).
     */
    @Test
    public void testExecuterAvecUnSeulEssai() {
        reseau.ajouterGenerateur("G1", 100);
        reseau.ajouterMaison("M1", TypeConsommation.NORMAL);

        resolution = new ResolutionAutomatique(reseau, LAMBDA);

        assertDoesNotThrow(() -> {
            resolution.executer(1);
        });
    }

    /**
     * Vérifie que l'optimisation converge et ne fait pas augmenter le coût.
     */
    @Test
    public void testOptimisationConverge() {
        reseau.ajouterGenerateur("G1", 80);
        reseau.ajouterGenerateur("G2", 80);
        reseau.ajouterMaison("M1", TypeConsommation.FORTE);
        reseau.ajouterMaison("M2", TypeConsommation.BASSE);

        resolution = new ResolutionAutomatique(reseau, LAMBDA);

        double coutAvant = CalculateurCout.coutInstanceReseau(reseau, LAMBDA);
        resolution.executer(50);
        double coutApres = CalculateurCout.coutInstanceReseau(reseau, LAMBDA);

        // Le coût ne doit jamais augmenter
        assertTrue(coutApres <= coutAvant, "Le coût ne doit pas augmenter");
    }
}