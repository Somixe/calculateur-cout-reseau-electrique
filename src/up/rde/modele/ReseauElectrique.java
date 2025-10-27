package up.rde.modele;

import java.util.List;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;

/**
 * Représente un réseau de distribution d'électricité.
 * Gère l'ensemble des maisons, générateurs et leurs connexions.
 */
public class ReseauElectrique {

    private List<Maison> maisons;
    private List<Generateur> generateurs;
    private Map<String, Maison> mapMaisons;
    private Map<String, Generateur> mapGenerateurs;
    
    /**
     * Construit un réseau électrique vide.
     */
    public ReseauElectrique() {
        this.maisons = new Vector<>();
        this.generateurs = new Vector<>();
        this.mapMaisons = new HashMap<>();
        this.mapGenerateurs = new HashMap<>();
    }
    
    /**
     * Ajoute ou met à jour un générateur dans le réseau.
     * 
     * @param nom Le nom unique du générateur
     * @param capacite La capacité maximale en kW (doit être > 0)
     * @return true si le générateur existait déjà (mise à jour), false sinon (création)
     * @throws IllegalArgumentException si la capacité est <= 0 ou si le nom est déjà utilisé par une maison
     */
    public boolean ajouterGenerateur(String nom, int capacite) {
        if (capacite <= 0) {
            throw new IllegalArgumentException("La capacité doit être strictement positive");
        }
        
        if (mapMaisons.containsKey(nom)) {
            throw new IllegalArgumentException("Ce nom est déjà utilisé par une maison");
        }
        
        if (mapGenerateurs.containsKey(nom)) {
            mapGenerateurs.get(nom).setCapacite(capacite);
            return true;
        } else {
            Generateur g = new Generateur(nom, capacite);
            generateurs.add(g);
            mapGenerateurs.put(nom, g);
            return false;
        }
    }

    /**
     * Ajoute ou met à jour une maison dans le réseau.
     * 
     * @param nom Le nom unique de la maison
     * @param type Le type de consommation (BASSE, NORMAL, FORTE)
     * @return true si la maison existait déjà (mise à jour), false sinon (création)
     * @throws IllegalArgumentException si le nom est déjà utilisé par un générateur
     */
    public boolean ajouterMaison(String nom, TypeConsommation type) {
        if (mapGenerateurs.containsKey(nom)) {
            throw new IllegalArgumentException("Ce nom est déjà utilisé par un générateur");
        }
        
        if (mapMaisons.containsKey(nom)) {
            mapMaisons.get(nom).setTypeConsommation(type);
            return true;
        } else {
            Maison m = new Maison(nom, type);
            maisons.add(m);
            mapMaisons.put(nom, m);
            return false;
        }
    }
    
    /**
     * Crée une connexion entre une maison et un générateur.
     * Si la maison était déjà connectée à un autre générateur, 
     * elle est automatiquement déconnectée puis reconnectée au nouveau.
     * 
     * @param nom1 Premier nom (maison ou générateur)
     * @param nom2 Second nom (générateur ou maison)
     * @return true si la maison a été reconnectée (elle avait déjà un générateur), 
     *         false si c'est une nouvelle connexion
     * @throws IllegalArgumentException si les noms sont invalides ou identiques
     */
    public boolean ajouterConnexion(String nom1, String nom2) {
        if (nom1.equals(nom2)) {
            throw new IllegalArgumentException("Les deux noms doivent être différents");
        }

        Maison maison = mapMaisons.get(nom1);
        Generateur generateur = mapGenerateurs.get(nom1);

        if (maison == null) {
            maison = mapMaisons.get(nom2);
        }
        if (generateur == null) {
            generateur = mapGenerateurs.get(nom2);
        }

        if (maison == null && generateur == null) {
            throw new IllegalArgumentException("Aucun élément reconnu");
        }
        if (maison == null) {
            throw new IllegalArgumentException("Maison introuvable");
        }
        if (generateur == null) {
            throw new IllegalArgumentException("Générateur introuvable");
        }

        boolean etaitConnectee = (maison.getGenerateur() != null);
        
        if (etaitConnectee) {
            Generateur ancienGen = maison.getGenerateur();
            ancienGen.getMaisons().remove(maison);
        }

        maison.setGenerateur(generateur);
        generateur.getMaisons().add(maison);
        
        return etaitConnectee;
    }

    /**
     * Vérifie la validité du réseau en identifiant les maisons non connectées.
     * 
     * @return Liste des noms des maisons non connectées (vide si toutes sont connectées)
     */
    public List<String> getMaisonsNonConnectees() {
        List<String> maisonsNonConnectees = new Vector<>();
        
        for (Maison m : maisons) {
            if (m.getGenerateur() == null) {
                maisonsNonConnectees.add(m.getNom());
            }
        }
        
        return maisonsNonConnectees;
    }

    /**
     * Vérifie que le réseau contient au moins une maison et un générateur.
     * 
     * @return true si le réseau n'est pas vide, false sinon
     */
    public boolean estNonVide() {
        return !maisons.isEmpty() && !generateurs.isEmpty();
    }

    /**
     * @return La liste des maisons du réseau
     */
    public List<Maison> getMaisons() {
        return maisons;
    }
    
    /**
     * @return La liste des générateurs du réseau
     */
    public List<Generateur> getGenerateurs() {
        return generateurs;
    }
    
    @Override
    public String toString() {
    	return ""; //Implémenter
    }
}