package modele;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 */
public class Etape {
	
	private Date heureDePassage;
    private int secondesAttenteAvantPassage;
	private Intersection adresse;
	private boolean aLivraison;

    /**
     * 
     */
    public Etape() {
    }
    
    public Etape(Date heurePassage, Intersection adresse){
    	this.adresse = adresse;
    	this.heureDePassage = heurePassage;
    	DateFormat formatHeure = new SimpleDateFormat("HH:mm:ss");        
        secondesAttenteAvantPassage=0;
        aLivraison = false;
    }

    /**
     * 
     */
    

    /**
     * @return
     */
    public Date getHeurePassagePrevue() {
        // TODO implement here
        return this.heureDePassage;
    }

    public Intersection getAdresse(){return this.adresse;}
    /**
     * @param double
     */
    public void setHeurePassagePrevue(Date nouvelleHeure) {
    	this.heureDePassage = nouvelleHeure;
        // TODO implement here
    }
    
    public void setaLivraison(){
    	this.aLivraison = true;
    }
    
    public boolean getaLivraison(){
    	return this.aLivraison;
    }

    public int getAttenteAvantPassage() {
        return secondesAttenteAvantPassage;
    }

    public void setAttenteAvantPassage(int attenteAvantPassage) {
        this.secondesAttenteAvantPassage = attenteAvantPassage;
    }
    
    public boolean display(PrintStream stream){
    	stream.println(heureDePassage.toString());
    	return true;
    }
    
    /**
     * Pas complet car il manque le lien avec la livraison
     * @return String
     */
	public String toStringXML(){
		String res = "";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
		String minutes = String.format("%02d", this.heureDePassage.getMinutes());
		String heure = heureDePassage.getHours() + "h" + minutes ;
		
		
		res += "<etape heurePassage=\""+heure+"\" secondesAttente=\""+ 
				this.secondesAttenteAvantPassage +"\" idIntersection=\"" +
				adresse.getId() + "\" />";
		return res;
	}
}