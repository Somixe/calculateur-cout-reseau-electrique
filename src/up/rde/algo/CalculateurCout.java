package up.rde.algo;
import java.util.Vector;
import up.rde.modele.ReseauElectrique;
import java.util.List;


import up.rde.modele.Generateur;

public class CalculateurCout {
	public static final double  LAMBDA = 10;
	
	public static double coutGenerateurs(Vector<Generateur> generators) {
		int nbSurcharge = 0;
		double totalCout = 0.0; //cout total de tout les générateurs
		double coutGUnique = 0.0 ; //cout unique générateur
		for(int i = 0 ; i<generators.size();i++) {
			double coutMaisons = 0.0;  // a verifier
			coutGUnique = 0.0 ;
			for (int j = 0; i<generators.get(j).getMaisons().size();j++){
				coutMaisons = 0.0;
				coutMaisons += generators.get(i).getMaisons().get(j).getTypeConsommation().getValeur();
			}
			coutGUnique = coutMaisons/generators.get(i).getCapacite();
			if(coutGUnique > 1) {
				nbSurcharge += 1;
			}
		}
		totalCout += coutGUnique/generators.size();
		totalCout += LAMBDA * nbSurcharge ;
		return totalCout;
	}
	
	public static double tauxUt(Generateur g) {
		double lg = 0.0;
		double cg = g.getCapacite();
		for(int i = 0 ; i<g.getMaisons().size();i++) {
			lg += g.getMaisons().get(i).getTypeConsommation().getValeur();
		}
		return lg/cg;	
	}
	
	public static double dispersion(List<Generateur> generators) {
		double u = 0.0;
		double disp = 0.0;
		for(int i = 0 ; i<generators.size(); i++) {
			u += tauxUt(generators.get(i));
		}
		u /= Math.abs(generators.size());
		for(int i = 0 ; i<generators.size(); i++) {
			disp += Math.abs(tauxUt(generators.get(i))-u);
		}
		return disp;
	}
	
	public static double surcharge(List<Generateur> generators) {
		double total = 0.0;
		for(int i = 0 ; i<generators.size();i++) {
			double cg = generators.get(i).getCapacite();
			double lg = 0.0;
			for(int j = 0 ; j<generators.get(i).getMaisons().size();j++) {
				lg += generators.get(i).getMaisons().get(j).getTypeConsommation().getValeur();
			}
			total += Math.max(0.0, (lg-cg)/cg);
		}
		
		return total;
	}
	
	public static double coutInstanceReseau(ReseauElectrique r) {
		return dispersion(r.getGenerateurs())+ LAMBDA * surcharge(r.getGenerateurs());
	}
	
	public String toString(ReseauElectrique r) {
		return String.valueOf(coutInstanceReseau(r));
	}

}
