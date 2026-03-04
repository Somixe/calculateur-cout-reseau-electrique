
// ENUM TYPECONSOMMATION
package up.rde.modele;

/**
 * Représente les différents types de consommation électrique possibles
 * pour une maison.
 */
public enum TypeConsommation {
    BASSE(10),
    NORMAL(20),
    FORTE(40);

    private final int valeur;

    /**
     * Construit un type de consommation.
     * 
     * @param valeur Demande électrique associée en kW
     */

    TypeConsommation(int valeur) {
        this.valeur = valeur;
    }

    /**
     * @return la valeur de consommation en kW
     */
    public int getValeur() {
        return valeur;
    }

    /**
     * Vérifie si une chaîne correspond à un type de consommation valide.
     * 
     * @param type La chaîne à vérifier
     * @return true si la chaîne correspond à un type existant, false sinon
     */
    public static boolean estTypeValide(String type) {
        if (type == null)
            return false;
        try {
            valueOf(type.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * @return la liste des types de consommation disponibles sous forme de chaîne
     */
    public static String obtenirTypesDisponibles() {
        return "BASSE, NORMAL, FORTE";
    }
}
