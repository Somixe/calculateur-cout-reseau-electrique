package up.rde.utilitaire;

//CLASSE CALCULATEURTEST
import java.util.List;
import up.rde.modele.Generateur;
import up.rde.modele.ReseauElectrique;

/**
 * Classe utilitaire pour tester le calcul du coût d'un réseau électrique.
 * Calcul du coût = dispersion + λ × surcharge.
 */

public class CalculateurCoutTest {

	public static final double LAMBDA_DEFAUT = 10.0;

	/**
	 * Calcule le coût total d'une instance de réseau électrique.
	 * Coût = dispersion + lambda × surcharge.
	 *
	 * @param reseau Le réseau électrique à évaluer (non null)
	 * @param lambda Facteur multiplicatif pour la surcharge
	 * @return le coût total calculé
	 */

	public static double coutInstanceReseau(ReseauElectrique reseau, double lambda) {
		return calculerDispersion(reseau) + (lambda * calculerSurcharge(reseau));
	}

	/**
	 * Calcule la surcharge totale du réseau.
	 * La surcharge d'un générateur est : max(0, (charge - capacité) / capacité)
	 *
	 * @param reseau Le réseau électrique à analyser
	 * @return la surcharge totale (double ≥ 0)
	 */

	public static double calculerSurcharge(ReseauElectrique reseau) {
		if (!reseau.estNonVide())
			return 0.0;

		double surcharge = 0.0;
		List<Generateur> generateurs = reseau.getGenerateurs(); //

		for (Generateur g : generateurs) {
			double capacite = g.getCapacite();
			if (capacite <= 0)
				continue;

			double charge = reseau.getChargeGenerateur(g); //

			// Si la charge dépasse la capacité, on ajoute le surplus
			if (charge > capacite) {
				surcharge += (charge - capacite) / capacite;
			}
		}
		return surcharge;
	}

	/**
	 * Calcule la dispersion du réseau.
	 * Dispersion = somme des écarts absolus de chaque taux d'utilisation par
	 * rapport à la moyenne.
	 *
	 * @param reseau Le réseau électrique à analyser
	 * @return la dispersion totale (double ≥ 0)
	 */
	public static double calculerDispersion(ReseauElectrique reseau) {
		if (!reseau.estNonVide())
			return 0.0;

		List<Generateur> generateurs = reseau.getGenerateurs(); //
		double sommeTaux = 0.0;
		int nbGenActifs = 0;

		// 1. Calcul de la moyenne des taux d'utilisation
		for (Generateur g : generateurs) {
			if (g.getCapacite() > 0) {
				double charge = reseau.getChargeGenerateur(g); //
				sommeTaux += (charge / g.getCapacite());
				nbGenActifs++;
			}
		}

		if (nbGenActifs == 0)
			return 0.0;
		double tauxMoyen = sommeTaux / nbGenActifs;

		// 2. Calcul de la dispersion (écart à la moyenne)
		double dispersion = 0.0;
		for (Generateur g : generateurs) {
			if (g.getCapacite() > 0) {
				double charge = reseau.getChargeGenerateur(g);
				double taux = charge / g.getCapacite();
				dispersion += Math.abs(taux - tauxMoyen);
			}
		}
		return dispersion;
	}

	/**
	 * Retourne la valeur par défaut de lambda.
	 *
	 * @return lambda par défaut
	 */
	public static double getLambdaDefaut() {
		return LAMBDA_DEFAUT;
	}
}