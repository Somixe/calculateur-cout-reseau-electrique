package up.rde.utilitaire;

// CLASSE CALCULATEURCOUT
import java.util.List;
import up.rde.modele.Generateur;
import up.rde.modele.ReseauElectrique;

/**
 * Calcul du coût d'un réseau électrique.
 * Coût = Dispersion + λ × Surcharge
 */
public class CalculateurCout {

    public static final double LAMBDA_DEFAUT = 10.0;

    /**
     * Calcule le coût total du réseau électrique.
     *
     * @param reseau Le réseau électrique à évaluer (non null)
     * @param lambda Le coefficient λ pour pondérer la surcharge (> 0)
     * @return Le coût total du réseau
     */
    public static double coutInstanceReseau(ReseauElectrique reseau, double lambda) {
        return calculerDispersion(reseau) + lambda * calculerSurcharge(reseau);
    }

    /**
     * Calcule la surcharge du réseau.
     * 
     * La surcharge est définie comme la somme pour chaque générateur de
     * max(0, (charge - capacité) / capacité).
     * 
     *
     * @param reseau Le réseau électrique à évaluer (non null)
     * @return La surcharge totale (0 si aucun générateur ou réseau vide)
     */
    public static double calculerSurcharge(ReseauElectrique reseau) {
        if (!reseau.estNonVide())
            return 0.0;

        double surcharge = 0.0;
        for (Generateur g : reseau.getGenerateurs()) {
            double capacite = g.getCapacite();
            if (capacite <= 0)
                continue;

            double charge = reseau.getChargeGenerateur(g);
            if (charge > capacite) {
                surcharge += (charge - capacite) / capacite;
            }
        }
        return surcharge;
    }

    /**
     * Calcule la dispersion du réseau.
     * 
     * La dispersion est définie comme la somme des écarts absolus entre le taux
     * d'utilisation de chaque générateur et la moyenne de tous les générateurs.
     * 
     *
     * @param reseau Le réseau électrique à évaluer (non null)
     * @return La dispersion totale (0 si aucun générateur ou réseau vide)
     */
    public static double calculerDispersion(ReseauElectrique reseau) {
        if (!reseau.estNonVide())
            return 0.0;

        List<Generateur> generateurs = reseau.getGenerateurs();
        double sommeTaux = 0.0;
        int nb = 0;

        // Moyenne des taux d'utilisation
        for (Generateur g : generateurs) {
            if (g.getCapacite() > 0) {
                sommeTaux += reseau.getChargeGenerateur(g) / g.getCapacite();
                nb++;
            }
        }

        if (nb == 0)
            return 0.0;
        double moyenne = sommeTaux / nb;

        double dispersion = 0.0;
        // Moyenne des taux d'utilisation
        for (Generateur g : generateurs) {
            if (g.getCapacite() > 0) {
                double taux = reseau.getChargeGenerateur(g) / g.getCapacite();
                dispersion += Math.abs(taux - moyenne);
            }
        }
        return dispersion;
    }

    /**
     * Retourne la valeur par défaut de λ utilisée dans les calculs.
     *
     * @return La valeur par défaut de λ
     */
    public static double getLambdaDefaut() {
        return LAMBDA_DEFAUT;
    }
}