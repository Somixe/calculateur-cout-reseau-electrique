package up.rde.modele;

/**
 * Représente une maison dans le réseau électrique.
 * Chaque maison a un nom unique, un type de consommation et doit être connectée à un générateur.
 */
public class Maison {
	
	private String nom;
	private TypeConsommation typeConsommation;
	private Generateur generateur;
	
    /**
     * Construit une nouvelle maison.
     * 
     * @param nom Le nom unique de la maison
     * @param typeConsommation Le type de consommation (BASSE, NORMAL, FORTE)
     */
	public Maison(String nom, TypeConsommation typeConso) {
		this.nom = nom;
		this.typeConsommation = typeConso;
	}
	
    /**
     * Retourne le nom de la maison.
     * 
     * @return Le nom de la maison
     */
	public String getNom() {
		return nom;
	}
	
    /**
     * Retourne le type de consommation de la maison.
     * 
     * @return Le type de consommation
     */
	public TypeConsommation getTypeConsommation() {
		return typeConsommation;
	}
	
    /**
     * Modifie le type de consommation de la maison.
     * 
     * @param TypeConsommation Le nouveau type de consommation de la maison.
     */
	public void setTypeConsommation(TypeConsommation typeConsommation) {
		this.typeConsommation = typeConsommation;
	}
	
    /**
     * Retourne le générateur auquel la maison est connectée.
     * 
     * @return Le générateur connecté, ou null si aucun
     */
    public Generateur getGenerateur() {
        return generateur;
    }
	
    /**
     * Retourne la demande électrique de la maison en kW.
     * 
     * @return La demande en kW
     */
    public int getDemande() {
        return typeConsommation.getValeur();
    }
    
    /**
     * Connecte cette maison à un nouveau générateur.
     * 
     * @param generateur Le générateur à connecter
     */
    public void setGenerateur(Generateur generateur) {
        this.generateur = generateur;
    }
    
    /**
     * Retourne une représentation textuelle de la maison.
     * 
     * @return Une chaîne décrivant la maison
     */
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append(nom);
    	sb.append(" (" + typeConsommation + ", ");
    	sb.append(getDemande());
    	sb.append(" KW)");
    	 	
        return sb.toString();
    }  
	
}
