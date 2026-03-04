
// CLASSE MAISON

package up.rde.modele;

/**
 * Représente une maison dans le réseau électrique.
 * Chaque maison a un nom unique et un type de consommation.
 */
public class Maison {
    private String nom;
    private TypeConsommation typeConsommation;

    /**
     * Construit une nouvelle maison.
     * 
     * @param nom              Le nom unique de la maison (non null, non vide)
     * @param typeConsommation Le type de consommation (non null)
     * @throws IllegalArgumentException si nom null/vide ou type null
     */
    public Maison(String nom, TypeConsommation typeConsommation) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la maison ne peut pas être vide");
        }
        if (typeConsommation == null) {
            throw new IllegalArgumentException("Le type de consommation ne peut pas être null");
        }
        this.nom = nom.trim();
        this.typeConsommation = typeConsommation;
    }

    /**
     * @return le nom de la maison
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return le type de consommation de la maison
     */
    public TypeConsommation getTypeConsommation() {
        return typeConsommation;
    }

    /**
     * Modifie le type de consommation de la maison.
     *
     * @param typeConsommation nouveau type (non null)
     * @throws IllegalArgumentException si le type est null
     */
    public void setTypeConsommation(TypeConsommation typeConsommation) {
        if (typeConsommation == null) {
            throw new IllegalArgumentException("Le type de consommation ne peut pas être null");
        }
        this.typeConsommation = typeConsommation;
    }

    /**
     * @return La demande électrique en kW
     */
    public int getDemande() {
        return typeConsommation.getValeur();
    }

    /**
     * Représentation textuelle de la maison.
     */
    @Override
    public String toString() {
        return nom + " (" + typeConsommation + ", " + getDemande() + " kW)";
    }
}