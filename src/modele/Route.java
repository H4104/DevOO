package modele;


public class Route {
	
	private static int idRoutes =0;
	
	private String name;
	private float vitesse,longueur;
	private int id;
	private Intersection inter;
	
	public Route(String nom, float vitesse, float longueur, Intersection inter1){
		this.name = nom;
		this.vitesse = vitesse;
		this.longueur = longueur;
		this.id = idRoutes++;
		this.inter = inter1;
	}
	
	
	//Getter
	public int getId(){return id;}
	
	public String getName(){return this.name;}
	
	public float getVitesse(){return this.vitesse;}
	
	public float getLongueur(){return this.longueur;}
	
	public Intersection getInter(){return this.inter;}
	
}
