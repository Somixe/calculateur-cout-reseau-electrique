package up.rde.utilitaire;

// CLASSE RESOLUTIONAUTOMATIQUE
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import up.rde.modele.Generateur;
import up.rde.modele.Maison;
import up.rde.modele.ReseauElectrique;

/**
 * Résolution automatique du réseau électrique.
 *
 * Permet de générer une configuration optimisée des connexions entre maisons
 * et générateurs afin de minimiser le coût total défini par :
 * Coût = Dispersion + λ × Surcharge
 */
public class ResolutionAutomatique {

    private final ReseauElectrique reseau;
    private final double lambda;
    private final Random random;

    /**
     * Construit un algorithme de résolution automatique pour un réseau donné.
     *
     * @param reseau Le réseau électrique à optimiser (non null)
     * @param lambda Le coefficient λ pour pondérer la surcharge (>0)
     */

    public ResolutionAutomatique(ReseauElectrique reseau, double lambda) {
        this.reseau = reseau;
        this.lambda = lambda;
        this.random = new Random();
    }

    /**
     * Affiche les détails du coût actuel du réseau.
     *
     * @return Une chaîne décrivant la dispersion, surcharge, λ et le coût total
     */
    public String afficherDetailsCout() {
        double disp = CalculateurCout.calculerDispersion(reseau);
        double surch = CalculateurCout.calculerSurcharge(reseau);
        double total = CalculateurCout.coutInstanceReseau(reseau, lambda);

        StringBuilder sb = new StringBuilder();
        sb.append("Dispersion : ").append(String.format("%.4f", disp)).append("\n");
        sb.append("Surcharge  : ").append(String.format("%.4f", surch)).append("\n");
        sb.append("Lambda     : ").append(lambda).append("\n\n");
        sb.append("COÛT TOTAL : ").append(String.format("%.4f", total)).append("\n");

        return sb.toString();
    }

    /**
     * Exécute l'optimisation du réseau électrique.
     * Le paramètre k peut être utilisé pour ajuster le nombre d'itérations
     * ou la profondeur de la recherche locale.
     *
     * @param k Nombre d'itérations pour l'optimisation (ex: 1000)
     */
    public void executer(int k) {
        double coutInitial = CalculateurCout.coutInstanceReseau(reseau, lambda);

        List<Maison> maisons = reseau.getMaisons();
        List<Generateur> generateurs = reseau.getGenerateurs();

        // Meilleure solution globale
        Map<Maison, Generateur> meilleureConfig = reseau.getConnexions();
        double meilleurCout = coutInitial;

        // PHASE 1 : Équilibrage initial intelligent
        equilibrageIntelligent(maisons, generateurs);
        double coutApresEquilibrage = CalculateurCout.coutInstanceReseau(reseau, lambda);

        if (coutApresEquilibrage < meilleurCout) {
            meilleurCout = coutApresEquilibrage;
            meilleureConfig = reseau.getConnexions();
        }

        // PHASE 2 : Recherche locale avec redémarrages
        int nbEssais = 20; // Augmenté pour plus d'exploration

        for (int essai = 0; essai < nbEssais; essai++) {

            // Diversification : config aléatoire (sauf premier essai)
            if (essai > 0) {
                genererConfigurationAleatoire(maisons, generateurs);
            }

            // Intensification : recherche locale
            boolean ameliorationTrouvee = true;
            while (ameliorationTrouvee) {
                ameliorationTrouvee = false;
                double coutActuel = CalculateurCout.coutInstanceReseau(reseau, lambda);

                for (Maison maison : maisons) {
                    Generateur genActuel = reseau.getGenerateurConnecte(maison);

                    for (Generateur genTest : generateurs) {
                        if (genTest.equals(genActuel))
                            continue;

                        // Tester le changement
                        if (genActuel != null) {
                            reseau.supprimerConnexion(maison.getNom(), genActuel.getNom());
                        }
                        reseau.ajouterConnexion(maison.getNom(), genTest.getNom());

                        double nouveauCout = CalculateurCout.coutInstanceReseau(reseau, lambda);

                        if (nouveauCout < coutActuel) {
                            coutActuel = nouveauCout;
                            genActuel = genTest;
                            ameliorationTrouvee = true;
                        } else {
                            // Annuler
                            reseau.supprimerConnexion(maison.getNom(), genTest.getNom());
                            if (genActuel != null) {
                                reseau.ajouterConnexion(maison.getNom(), genActuel.getNom());
                            }
                        }
                    }
                }

                // Vérifier si c'est un nouveau record
                if (coutActuel < meilleurCout) {
                    meilleurCout = coutActuel;
                    meilleureConfig = reseau.getConnexions();
                }
            }
        }

        // Restauration de la meilleure config
        reseau.setConnexions(meilleureConfig);
    }

    /**
     * Équilibrage initial du réseau (First Fit Decreasing).
     * Assigne les maisons aux générateurs en minimisant le ratio charge/capacité.
     *
     * @param maisons     List des maisons du réseau (non null)
     * @param generateurs List des générateurs du réseau (non null)
     */
    private void equilibrageIntelligent(List<Maison> maisons, List<Generateur> generateurs) {
        // Trier maisons par demande décroissante
        List<Maison> maisonsTri = new ArrayList<>(maisons);
        maisonsTri.sort((a, b) -> Integer.compare(b.getDemande(), a.getDemande()));

        // Calculer charges initiales
        Map<Generateur, Double> charges = new HashMap<>();
        for (Generateur g : generateurs) {
            charges.put(g, reseau.getChargeGenerateur(g));
        }

        // Assigner chaque maison au générateur le moins chargé (en ratio)
        for (Maison m : maisonsTri) {
            Generateur meilleurGen = null;
            double meilleurRatio = Double.MAX_VALUE;

            for (Generateur g : generateurs) {
                double ratio = (charges.get(g) + m.getDemande()) / g.getCapacite();
                if (ratio < meilleurRatio) {
                    meilleurRatio = ratio;
                    meilleurGen = g;
                }
            }

            if (meilleurGen != null) {
                Generateur ancienGen = reseau.getGenerateurConnecte(m);
                if (ancienGen != null) {
                    reseau.supprimerConnexion(m.getNom(), ancienGen.getNom());
                }
                reseau.ajouterConnexion(m.getNom(), meilleurGen.getNom());
                charges.put(meilleurGen, charges.get(meilleurGen) + m.getDemande());
            }
        }
    }

    /**
     * Génère une configuration aléatoire du réseau.
     * Chaque maison est assignée à un générateur choisi aléatoirement.
     *
     * @param maisons     List des maisons du réseau (non null)
     * @param generateurs List des générateurs du réseau (non null)
     */
    private void genererConfigurationAleatoire(List<Maison> maisons, List<Generateur> generateurs) {
        Map<Maison, Generateur> nouvelleConfig = new HashMap<>();
        for (Maison m : maisons) {
            Generateur gen = generateurs.get(random.nextInt(generateurs.size()));
            nouvelleConfig.put(m, gen);
        }
        reseau.setConnexions(nouvelleConfig);
    }

    /**
     * Exécute l'optimisation du réseau avec une valeur par défaut pour k.
     */
    public void executer() {
        executer(1000);
    }
}