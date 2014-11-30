package modele;

import java.util.*;

/**
 * 
 */
public class GrapheRoutier {
	
    private List<Intersection> listeIntersection;
    
    //
    private class Noeud implements Comparable{
        private double coutAcces;
        private Intersection intersection;
        private Noeud origine;
        
        public Noeud(Intersection pIntersection, Noeud pOrigine, double pCoutAcces){
            coutAcces=pCoutAcces;
            intersection = pIntersection;
            origine = pOrigine;
        }
        
        public void setOrigine(Noeud pOrigine){
            origine = pOrigine;
        }

        @Override
        public int compareTo(Object o) {
            Noeud n = (Noeud) o;
            if(this.equals(n)) {
                return 0;
            }
            if(this.coutAcces!=n.coutAcces) {
                return ((Double)this.coutAcces).compareTo(n.coutAcces);
            }
            else{
                return ((Integer)this.intersection.getId()).compareTo(n.intersection.getId());
            }
        }
        
        @Override
        public boolean equals(Object o){
            return this.intersection.equals(((Noeud)o).intersection);
        }
    }

    /**
     * 
     */
    public GrapheRoutier() {
    	this.listeIntersection = new ArrayList<Intersection>();
    }
    
    public void ajouterIntersection(Intersection inter){
    	this.listeIntersection.add(inter.getId(),inter);
    }
    
    public boolean interExiste(int idInter){
    	for(int i = 0; i<listeIntersection.size(); i++){
    		if(idInter == listeIntersection.get(i).getId()){
    			return true;
    		}
    	}
    	return false;
    }
    
    public Intersection rechercherInterParId(int id){
    	return listeIntersection.get(id);
    }
    
   /**
    * Calcul le plus court chemin entre le depart et l'arrivée donnés
    * @param depart Intersection correspondant au point de départ du chemin calculé
    * @param arrivee Intersection correspondant au point d'arrivée du chemin calculé
    * @return La liste ordonnée des routes à suivre pour atteindre le l'arrivée depuis le départ avec un cout minimal, null si aucune solution
    */
    public List<Intersection> calculerPlusCourtChemin(Intersection depart, Intersection arrivee) {

        Noeud noeudCourant = new Noeud(depart,null,0);
        Noeud successeurCourant;
        PriorityQueue<Noeud> frontiere = new PriorityQueue<>();
        frontiere.add(noeudCourant);
        List<Noeud> dejaExplores = new ArrayList<>();
        List<Intersection> solution = new ArrayList<>();
        
        for(;;){
            if(frontiere.isEmpty()) {
                return null;
            }
            noeudCourant= frontiere.poll();
            if(noeudCourant.intersection.equals(arrivee)){
                while(!noeudCourant.intersection.equals(depart)){
                    solution.add(noeudCourant.intersection);
                    noeudCourant=noeudCourant.origine;
                }
                solution.add(noeudCourant.intersection);
                Collections.reverse(solution);
                return solution;
            }
            dejaExplores.add(noeudCourant);
            for(Route r : noeudCourant.intersection.getTroncsSortants()){
                successeurCourant = new Noeud(r.getInter(),noeudCourant,r.getTempsParcours());
                if(!dejaExplores.contains(successeurCourant)){
                    if(!frontiere.contains(successeurCourant)){
                        frontiere.add(successeurCourant);
                    }
                    else{
                        for(Noeud n:frontiere){
                            if (n.equals(successeurCourant) && n.coutAcces>successeurCourant.coutAcces){
                                frontiere.remove(n);
                                frontiere.add(successeurCourant);
                            }
                        }
                    }
                }
            }
        }
        
    }

}