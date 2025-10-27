package up.rde.modele;

import java.util.List;
import java.util.Vector;

public class Generateur {
    private String nom;
    private int capacite;
    private List<Maison> maisons;
    
    public Generateur(String nom, int capacite) {
        this.nom = nom;
        this.capacite = capacite;
        this.maisons = new Vector<>();
    }
    
    // getters et setters
    public String getNom() {
        return nom;
    }
    
    public int getCapacite() {
        return capacite;
    }
    
    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }
    
    public List<Maison> getMaisons() {
        return maisons;
    }
    
    @Override
    public String toString() {
    	return ""; //Implémenter
    }
}