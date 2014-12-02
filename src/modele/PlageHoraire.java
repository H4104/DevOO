package modele;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class PlageHoraire {
	
	private Date heureDebut;
	private Date heureFin;
	private List<Livraison> listeLivraisons;
	
	public PlageHoraire(Date heureDeb, Date heureFin){
		listeLivraisons = new ArrayList<Livraison>();
		this.heureDebut = heureDeb;
		this.heureFin = heureFin;
				
	}
	
	//Getters
	public Date getHeureDebut(){return this.heureDebut;}
	public Date getHeureFin(){return this.heureFin;}
	public List<Livraison> getListeLivraison(){return this.listeLivraisons;}
	
	public void addLivraison(Livraison liv){
		this.listeLivraisons.add(liv);
                liv.setPlageHoraire(this);
	}
	
	public void deleteLivraison(Livraison liv){
		this.listeLivraisons.remove(liv);
	}

	public boolean display(PrintStream stream) {
		stream.print(heureDebut+ " "+ heureFin);
		for(int i = 0; i<listeLivraisons.size(); i++){
    		listeLivraisons.get(i).display(stream);
                }
		return true;
	}

    @Override
    public boolean equals(Object o){
    	PlageHoraire p = (PlageHoraire)o;    
    	return(this.heureDebut.equals(p.heureDebut)&&this.heureFin.equals(p.heureFin));    
    }
    
    public String toStringXML() {
    	String res = "";
    	int debut = this.heureDebut.getHours();
    	int fin = this.heureFin.getHours();
    	
    	res += "<plage debut=\"" + debut + "\" fin=\"" + fin + "\" >";
    	Iterator<Livraison> it = this.listeLivraisons.iterator();
    	while( it.hasNext() ){
			res += it.next().toStringXML();
		}
    	res += "</plage>";
    	return res;
    }
        
}
