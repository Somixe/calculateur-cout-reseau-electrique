
// CLASSE GENERATEUR

package up.rde.modele;

/**
 * Représente un générateur électrique dans le réseau.
 * Chaque générateur a un nom unique et une capacité maximale.
 */
public class Generateur {
    private String nom;
    private int capacite;

    /**
     * Construit un nouveau générateur.
     * 
     * @param nom      Le nom unique du générateur (non null, non vide)
     * @param capacite La capacité maximale en kW (> 0)
     * @throws IllegalArgumentException si nom null/vide ou capacité inférieur ou égale 0
     */
    public Generateur(String nom, int capacite) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du générateur ne peut pas être vide");
        }
        if (capacite <= 0) {
            throw new IllegalArgumentException("La capacité doit être strictement positive");
        }
        this.nom = nom.trim();
        this.capacite = capacite;
    }

    /**
     * @return le nom du générateur
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return la capacité maximale du générateur (en kW)
     */
    public int getCapacite() {
        return capacite;
    }

    /**
     * Modifie la capacité du générateur.
     * 
     * @param capacite nouvelle capacité (> 0)
     * @throws IllegalArgumentException si la capacité est invalide
     */

    public void setCapacite(int capacite) {
        if (capacite <= 0) {
            throw new IllegalArgumentException("La capacité doit être strictement positive");
        }
        this.capacite = capacite;
    }

    /**
     * Représentation textuelle du générateur.
     */
    @Override
    public String toString() {
        return nom + " (" + capacite + " kW)";
    }
}