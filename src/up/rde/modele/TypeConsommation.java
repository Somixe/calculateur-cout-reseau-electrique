package up.rde.modele;

/**
 * Énumération représentant les types de consommation électrique des maisons.
 * Chaque type a une valeur de consommation fixe en kW.
 */
public enum TypeConsommation {
    
    BASSE(10),   
    NORMAL(20),
    FORTE(40);
    
    private final int valeur;
    
    /**
     * Construit un type de consommation avec sa valeur en kW.
     * 
     * @param valeur La consommation en kW
     */
    private TypeConsommation(int valeur) {
        this.valeur = valeur;
    }
    
    /**
     * Retourne la valeur de consommation en kW.
     * 
     * @return La consommation en kW
     */
    public int getValeur() {
        return valeur;
    }
    
    /**
     * Retourne une chaîne avec tous les types de consommation disponibles.
     * Format : "BASSE, NORMAL, FORTE"
     * 
     * @return La liste des types séparés par des virgules
     */
    public static String obtenirTypesDisponibles() {
        StringBuilder sb = new StringBuilder();
        TypeConsommation[] types = TypeConsommation.values();
        
        for (int i = 0; i < types.length; i++) {
            sb.append(types[i].name());
            if (i < types.length - 1) {
                sb.append(", ");
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Vérifie si une chaîne correspond à un TypeConsommation valide.
     * La vérification est insensible à la casse.
     * 
     * @param type La chaîne à vérifier
     * @return true si le type existe, false sinon
     */
    public static boolean estTypeValide(String type) {
        if (type == null) {
            return false;
        }
        
        String typeUpper = type.toUpperCase();
        
        for (TypeConsommation t : TypeConsommation.values()) {
            if (t.name().equals(typeUpper)) {
                return true;
            }
        }
        
        return false;
    }
}