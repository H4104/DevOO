package modele;

import java.io.PrintStream;


public class Route implements DisplayTest{
	
	private static int idRoutes =0;
	
	private String name;
	private double vitesse,longueur;
	private int id;
	private Intersection inter;
	
	public Route(String nom, double vitesse, double longueur, Intersection inter1){
		this.name = nom;
		this.vitesse = vitesse;
		this.longueur = longueur;
		this.id = idRoutes++;
		this.inter = inter1;
	}
	
	
	//Getter
	public int getId(){return id;}
	
	public String getName(){return this.name;}
	
	public double getVitesse(){return this.vitesse;}
	
	public double getLongueur(){return this.longueur;}
        
        public double getTempsParcours(){
            return longueur/vitesse;
        }
	
	public Intersection getInter(){return this.inter;}


	public boolean display(PrintStream stream) {
		stream.println(name+" id : "+id +" vitesse : "+vitesse+" long : "+longueur+ " -> " + inter);

		return true;		
	}
	
	public String toString(){
		return toStringXML();
	}
	
	public String toStringXML(){
		String res = "";
		res += "<route id=\"" + this.id + "\" idDestination=\"" + this.inter.getId() + "\" "
				+ "nom=\""+ this.name + "\" />";
		return res;
	}
}
