
// CLASSE RESEAUELECTRIQUE

package up.rde.modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Représente un réseau de distribution d'électricité.
 * Gère l'ensemble des générateurs, maisons et leurs connexions.
 * Chaque maison peut être connectée à un seul générateur.
 * Structure unique : Map avec clé = Maison et valeur = Generateur pour éviter toute redondance.
 */
public class ReseauElectrique {
    private List<Generateur> generateurs;
    private List<Maison> maisons;
    private Map<Maison, Generateur> connexions;

    /**
     * Construit un réseau électrique vide.
     */
    public ReseauElectrique() {
        this.generateurs = new ArrayList<>();
        this.maisons = new ArrayList<>();
        this.connexions = new HashMap<>();
    }

    /**
     * Ajoute ou met à jour un générateur.
     * 
     * @param nom      Nom du générateur (non null, non vide)
     * @param capacite Capacité maximale en kW (>0)
     * @return true si mise à jour, false si création
     * @throws IllegalArgumentException si nom null/vide, capacité ≤ 0, ou nom déjà
     *                                  utilisé par une maison
     */
    public boolean ajouterGenerateur(String nom, int capacite) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du générateur ne peut pas être vide");
        }
        if (capacite <= 0) {
            throw new IllegalArgumentException("La capacité doit être strictement positive");
        }
        if (getMaison(nom) != null) {
            throw new IllegalArgumentException("Ce nom est déjà utilisé par une maison");
        }

        Generateur g = getGenerateur(nom); // Existe déjà dans la liste
        if (g != null) {
            g.setCapacite(capacite);
            return true;
        }

        generateurs.add(new Generateur(nom, capacite));
        return false;
    }

    /**
     * Ajoute ou met à jour une maison.
     * 
     * @param nom  Nom de la maison (non null, non vide)
     * @param type Type de consommation (non null)
     * @return true si mise à jour, false si création
     * @throws IllegalArgumentException si nom null/vide, type null, ou nom déjà
     *                                  utilisé par un générateur
     */
    public boolean ajouterMaison(String nom, TypeConsommation type) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la maison ne peut pas être vide");
        }
        if (type == null) {
            throw new IllegalArgumentException("Le type de consommation ne peut pas être null");
        }
        if (getGenerateur(nom) != null) {
            throw new IllegalArgumentException("Ce nom est déjà utilisé par un générateur");
        }

        Maison m = getMaison(nom);
        if (m != null) {
            m.setTypeConsommation(type);
            return true;
        }

        maisons.add(new Maison(nom, type));
        return false;
    }

    /**
     * Ajoute ou modifie une connexion entre une maison et un générateur.
     * 
     * @param nom1 Nom d'une maison ou d'un générateur
     * @param nom2 Nom d'une maison ou d'un générateur
     * @return true si connexion existait déjà, false si nouvelle
     * @throws IllegalArgumentException si maison/générateur introuvable ou noms
     *                                  invalides
     */
    public boolean ajouterConnexion(String nom1, String nom2) { // M1 G1
        if (nom1 == null || nom1.trim().isEmpty() || nom2 == null || nom2.trim().isEmpty()) {
            throw new IllegalArgumentException("Les noms ne peuvent pas être vides");
        }

        // Chercher la maison et le générateur parmi les deux noms
        Maison maison1 = getMaison(nom1);
        Maison maison2 = getMaison(nom2);
        Generateur generateur1 = getGenerateur(nom1);
        Generateur generateur2 = getGenerateur(nom2);

        // Déterminer quelle est la maison et quel est le générateur
        Maison maison = null;
        Generateur generateur = null;

        if (maison1 != null) {
            maison = maison1;
        } else if (maison2 != null) {
            maison = maison2;
        }

        if (generateur1 != null) {
            generateur = generateur1;
        } else if (generateur2 != null) {
            generateur = generateur2;
        }

        // Vérifications
        if (maison == null) {
            throw new IllegalArgumentException("Aucune maison trouvée parmi : " + nom1 + ", " + nom2);
        }
        if (generateur == null) {
            throw new IllegalArgumentException("Aucun générateur trouvé parmi : " + nom1 + ", " + nom2);
        }
        if (maison1 != null && maison2 != null) {
            throw new IllegalArgumentException("Erreur : " + nom1 + " et " + nom2 + " sont tous les deux des maisons");
        }
        if (generateur1 != null && generateur2 != null) {
            throw new IllegalArgumentException(
                    "Erreur : " + nom1 + " et " + nom2 + " sont tous les deux des générateurs");
        }

        // Verifie si la maison n'est pas déjà connecté à un générateur
        boolean existait = connexions.containsKey(maison);
        // Vérifie si le générateur n'est pas déjà connecté à une maison

        if (!existait) {
            connexions.put(maison, generateur);
        }
        return existait;
    }

    /**
     * Supprime une connexion entre une maison et un générateur.
     * 
     * @param nom1 Nom d'une maison ou d'un générateur
     * @param nom2 Nom d'une maison ou d'un générateur
     * @return true si connexion supprimée
     * @throws IllegalArgumentException si maison/générateur introuvable ou
     *                                  connexion inexistante
     */
    public boolean supprimerConnexion(String nom1, String nom2) {
        if (nom1 == null || nom1.trim().isEmpty() || nom2 == null || nom2.trim().isEmpty()) {
            throw new IllegalArgumentException("Les noms ne peuvent pas être vides");
        }

        Maison maison1 = getMaison(nom1);
        Maison maison2 = getMaison(nom2);
        Generateur generateur1 = getGenerateur(nom1);
        Generateur generateur2 = getGenerateur(nom2);

        Maison maison = (maison1 != null) ? maison1 : maison2;
        Generateur generateur = (generateur1 != null) ? generateur1 : generateur2;

        if (maison == null) {
            throw new IllegalArgumentException("Aucune maison trouvée parmi : " + nom1 + ", " + nom2);
        }
        if (generateur == null) {
            throw new IllegalArgumentException("Aucun générateur trouvé parmi : " + nom1 + ", " + nom2);
        }
        if (maison1 != null && maison2 != null) {
            throw new IllegalArgumentException("Erreur : " + nom1 + " et " + nom2 + " sont tous les deux des maisons");
        }
        if (generateur1 != null && generateur2 != null) {
            throw new IllegalArgumentException(
                    "Erreur : " + nom1 + " et " + nom2 + " sont tous les deux des générateurs");
        }

        Generateur generateurConnecte = connexions.get(maison);
        if (generateurConnecte == null) {
            throw new IllegalArgumentException("La maison " + maison.getNom() + " n'est connectée à aucun générateur");
        }
        if (!generateurConnecte.equals(generateur)) {
            throw new IllegalArgumentException(
                    "La maison " + maison.getNom() + " n'est pas connectée au générateur " + generateur.getNom());
        }
        boolean existait = connexions.containsKey(maison);

        if (existait) {
            connexions.remove(maison);
        }
        return existait;
    }

    // REQUÊTES

    /**
     * Retourne les maisons connectées à un générateur donné.
     * 
     * @param generateur Le générateur concerné
     * @return Liste des maisons connectées (peut être vide)
     */
    public List<Maison> getMaisonsConnectees(Generateur generateur) {
        List<Maison> resultat = new ArrayList<>();

        if (generateur != null) {
            for (Maison maison : connexions.keySet()) {
                if (generateur.equals(connexions.get(maison))) {
                    resultat.add(maison);
                }
            }
        }

        return resultat;
    }

    /**
     * Retourne le générateur connecté à une maison.
     * 
     * @param maison La maison concernée
     * @return Le générateur connecté, ou null si aucune connexion
     */
    public Generateur getGenerateurConnecte(Maison maison) {
        return connexions.get(maison);
    }

    /**
     * Calcule la charge totale d'un générateur.
     * 
     * @param generateur Le générateur concerné
     * @return La charge en kW
     */
    public double getChargeGenerateur(Generateur generateur) {
        double charge = 0.0;

        if (generateur != null) {
            for (Maison maison : connexions.keySet()) {
                if (generateur.equals(connexions.get(maison))) {
                    charge += maison.getDemande();
                }
            }
        }

        return charge;
    }

    /**
     * Retourne la liste des noms des maisons non connectées.
     * 
     * @return Liste des noms (peut être vide)
     */
    public List<String> getMaisonsNonConnectees() {
        List<String> nonConnectees = new ArrayList<>();
        for (Maison m : maisons) {
            if (!connexions.containsKey(m)) {
                nonConnectees.add(m.getNom());
            }
        }
        return nonConnectees;
    }

    /**
     * Vérifie que le réseau contient au moins un générateur et une maison.
     * 
     * @return true si non vide
     */
    public boolean estNonVide() {
        return !maisons.isEmpty() && !generateurs.isEmpty();
    }

    /**
     * Vérifie que toutes les maisons sont connectées à un unique générateur.
     * 
     * @return true si le réseau est valide
     */
    public boolean estValide() {
        if (!estNonVide())
            return false;
        return getMaisonsNonConnectees().isEmpty();
    }

    /**
     * @return capacité totale de tous les générateurs
     */

    public double getCapaciteTotaleGenerateur() {
        double capacite = 0.0;

        for (Generateur g : generateurs) {
            capacite += g.getCapacite();
        }

        return capacite;
    }

    /**
     * @return demande totale de toutes les maisons
     */
    public double getDemandeTotaleMaison() {
        double demande = 0.0;

        for (Maison m : maisons) {
            demande += m.getDemande();
        }

        return demande;
    }

    // GETTERS

    public List<Maison> getMaisons() {
        return new ArrayList<>(maisons);
    }

    public List<Generateur> getGenerateurs() {
        return new ArrayList<>(generateurs);
    }

    public Map<Maison, Generateur> getConnexions() {
        return new HashMap<>(connexions);
    }

    // MÉTHODES PRIVÉES

    /**
     * Recherche une maison par son nom.
     * 
     * @param nom Le nom à chercher
     * @return La maison trouvée, ou null
     */
    private Maison getMaison(String nom) {
        if (nom == null)
            return null;
        String nomTrim = nom.trim();
        for (Maison m : maisons) {
            if (m.getNom().equals(nomTrim)) {
                return m;
            }
        }
        return null;
    }

    /**
     * Recherche un générateur par son nom.
     * 
     * @param nom Le nom à chercher
     * @return Le générateur trouvé, ou null
     */
    private Generateur getGenerateur(String nom) {
        if (nom == null)
            return null;
        String nomTrim = nom.trim();
        for (Generateur g : generateurs) {
            if (g.getNom().equals(nomTrim)) {
                return g;
            }
        }
        return null;
    }

    /**
     * Remplace toutes les connexions actuelles par une nouvelle carte de
     * connexions.
     * 
     * @param nouvellesConnexions Carte Maison->Generateur
     */
    public void setConnexions(Map<Maison, Generateur> nouvellesConnexions) {
        this.connexions.clear();
        this.connexions.putAll(nouvellesConnexions);
    }

    /**
     * Retourne la liste des générateurs connectés (ayant au moins une maison).
     * 
     * @return Liste des générateurs connectés
     */
    public List<Generateur> getGenerateursConnectes() {
        List<Generateur> connectes = new ArrayList<>();
        for (Generateur gen : connexions.values()) {
            if (!connectes.contains(gen)) {
                connectes.add(gen);
            }
        }
        return connectes;
    }

    /**
     * Retourne la liste des générateurs non connectés (sans maison).
     * 
     * @return Liste des générateurs non connectés
     */
    public List<Generateur> getGenerateursNonConnectes() {
        List<Generateur> nonConnectes = new ArrayList<>();
        for (Generateur gen : generateurs) {
            if (!connexions.containsValue(gen)) {
                nonConnectes.add(gen);
            }
        }
        return nonConnectes;
    }

    /**
     * Retourne toutes les maisons connectées.
     * 
     * @return Liste des maisons connectées
     */
    public List<Maison> getMaisonsConnectees() {
        return new ArrayList<>(connexions.keySet());
    }

    /**
     * Retourne toutes les maisons non connectées.
     * 
     * @return Liste des maisons non connectées
     */
    public List<Maison> getMaisonsNonConnecteesListe() {
        List<Maison> nonConnectees = new ArrayList<>();
        for (Maison m : maisons) {
            if (!connexions.containsKey(m)) {
                nonConnectees.add(m);
            }
        }
        return nonConnectees;
    }

    /**
     * Vérifie si un générateur est connecté à au moins une maison.
     * 
     * @param gen Générateur à vérifier
     * @return true si le générateur est connecté
     */
    public boolean estGenerateurConnecte(Generateur gen) {
        return connexions.containsValue(gen);
    }

    /**
     * Vérifie si une maison est connectée à un générateur.
     * 
     * @param maison Maison à vérifier
     * @return true si la maison est connectée
     */
    public boolean estMaisonConnectee(Maison maison) {
        return connexions.containsKey(maison);
    }

    /**
     * Représentation textuelle complète du réseau.
     * Affiche générateurs, maisons et connexions.
     * 
     * @return Chaîne formatée du réseau
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Afficher les générateurs
        sb.append("Générateurs :\n");
        for (Generateur g : generateurs) {
            sb.append("  - ").append(g.getNom()).append(" (Capacité: ").append(g.getCapacite()).append(" kW)\n");
        }

        // Afficher les maisons
        sb.append("\nMaisons :\n");
        for (Maison m : maisons) {
            sb.append("  - ").append(m.getNom()).append(" (Type: ").append(m.getTypeConsommation())
                    .append(", Demande: ").append(m.getDemande()).append(" kW)\n");
        }

        // Afficher les connexions
        sb.append("\nConnexions :\n");
        for (Maison m : connexions.keySet()) {
            Generateur g = connexions.get(m);
            sb.append("  - ").append(m.getNom()).append(" -- ").append(g.getNom()).append("\n");
        }

        return sb.toString();
    }

}