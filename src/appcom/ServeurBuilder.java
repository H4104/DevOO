package appcom;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import controleur.Controleur;

public class ServeurBuilder {

	private ServeurLivraison serveur;
	
	private ServeurBuilder(ServeurLivraison s){
		this.serveur = s;
	}
	
	public static ServeurBuilder nouveauServeur(){
		ServeurLivraison serveur = new ServeurLivraison();
		ServeurBuilder sb = new ServeurBuilder(serveur);
		serveur.Demarrer();
		return sb;
	}
	
	/**
	 * methodes pas � jour car format de sortie non specifie
	 * @param c
	 * @return
	 */
	public ServeurBuilder deployerServicesControleur(Controleur c){
		
		new ServiceControleur(c,"charger-plan",this.serveur){
			protected Reponse getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						
						Document doc = builder.parse(in);
						
						if (this.getControleur().chargerPlan(doc)) {
							return Reponse.succes("Le plan a bien �t� charg�.");
						} else {
							return Reponse.succes("Le service de chargement du plan a �chou�.");
						}
						
					} catch (SAXException e) {
						e.printStackTrace();
						return Reponse.erreur("Woops,\nnous n'avons pas pu interpr�ter le fichier transmis.\n"
								+ "Veuillez vous assurer qu'il ne contient aucune erreur.");
					} catch (IOException e) {
						e.printStackTrace();
						return Reponse.erreur("Erreur lors de la lecture du fichier.");
					}
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
					return Reponse.erreur("Probl�me d'initialisation dans la gestion du service");
				}
			}
		};
		
		new ServiceControleur(c,"charger-livraisons",this.serveur){
			protected Reponse getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						
						Document doc = builder.parse(in);
						//xml
						if (this.getControleur().chargerLivraisons(doc))
							return Reponse.succes("La communication s'est bien d�roul�e.");
						else
							return Reponse.succes("Le service de chargement des livraisons n'a pas abouti.");	
						
					} catch (SAXException e) {
						e.printStackTrace();
						return Reponse.erreur("Woops,\nnous n'avons pas pu interpr�ter le fichier transmis.\n"
								+ "Veuillez vous assurer qu'il ne contient aucune erreur.");
					} catch (IOException e) {
						e.printStackTrace();
						return Reponse.erreur("Erreur lors de la lecture du fichier.");
					}
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
					return Reponse.erreur("Probl�me d'initialisation dans la gestion du service");
				}
			}
		};
		
		new ServiceControleur(c,"ajouter-livraison",this.serveur){
			protected Reponse getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						
						Document doc = builder.parse(in);
						//a changer
						//this.getControleur().chargerPlan(doc);
						
						return Reponse.succes("La communication s'est bien d�roul�e.");
						
					} catch (SAXException e) {
						e.printStackTrace();
						return Reponse.erreur("Woops,\nnous n'avons pas pu interpr�ter le fichier transmis.\n"
								+ "Veuillez vous assurer qu'il ne contient aucune erreur.");
					} catch (IOException e) {
						e.printStackTrace();
						return Reponse.erreur("Erreur lors de la lecture du fichier.");
					}
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
					return Reponse.erreur("Probl�me d'initialisation dans la gestion du service");
				}
			}
		};
		
		new ServiceControleur(c,"supprimer-livraison",this.serveur){
			protected Reponse getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						
						Document doc = builder.parse(in);
						//a changer
						//this.getControleur().chargerPlan(doc);
						
						return Reponse.succes("La communication s'est bien d�roul�e.");
						
					} catch (SAXException e) {
						e.printStackTrace();
						return Reponse.erreur("Woops,\nnous n'avons pas pu interpr�ter le fichier transmis.\n"
								+ "Veuillez vous assurer qu'il ne contient aucune erreur.");
					} catch (IOException e) {
						e.printStackTrace();
						return Reponse.erreur("Erreur lors de la lecture du fichier.");
					}
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
					return Reponse.erreur("Probl�me d'initialisation dans la gestion du service");
				}
			}
		};
		
		new ServiceControleur(c,"annuler",this.serveur){
			protected Reponse getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						
						Document doc = builder.parse(in);
						//a changer
						//this.getControleur().chargerPlan(doc);
						
						return Reponse.succes("La communication s'est bien d�roul�e.");
						
					} catch (SAXException e) {
						e.printStackTrace();
						return Reponse.erreur("Woops,\nnous n'avons pas pu interpr�ter le fichier transmis.\n"
								+ "Veuillez vous assurer qu'il ne contient aucune erreur.");
					} catch (IOException e) {
						e.printStackTrace();
						return Reponse.erreur("Erreur lors de la lecture du fichier.");
					}
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
					return Reponse.erreur("Probl�me d'initialisation dans la gestion du service");
				}
			}
		};
		
		new ServiceControleur(c,"retablir",this.serveur){
			protected Reponse getReponse(InputStream in){
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					try {
						
						Document doc = builder.parse(in);
						//a changer
						//this.getControleur().chargerPlan(doc);
						
						return Reponse.succes("La communication s'est bien d�roul�e.");
						
					} catch (SAXException e) {
						e.printStackTrace();
						return Reponse.erreur("Woops,\nnous n'avons pas pu interpr�ter le fichier transmis.\n"
								+ "Veuillez vous assurer qu'il ne contient aucune erreur.");
					} catch (IOException e) {
						e.printStackTrace();
						return Reponse.erreur("Erreur lors de la lecture du fichier.");
					}
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
					return Reponse.erreur("Probl�me d'initialisation dans la gestion du service");
				}
			}
		};
		
		new ServiceControleur(c,"calculer-itineraire",this.serveur){
			protected Reponse getReponse(InputStream in){
				this.getControleur().getFeuilleDeRoute().calculerParcours(this.getControleur().getGrapheRoutier());
				return Reponse.succes("Itineraire calcul� !");			
			}
		};
		
		return this;
	}
	
	
	/**
	 * itineraire toString pas complet dans Etape
	 * @param c
	 * @return
	 */
	public ServeurBuilder deployerServicesVue(Controleur c){
		
		new ServiceModele(c,"itineraire",this.serveur){
			protected Reponse getReponse(InputStream in){
				String res = this.getControleur().getFeuilleDeRoute().toStringXML();
				return Reponse.succes(res);
			}
		};
		
		new ServiceModele(c,"plan",this.serveur){
			protected Reponse getReponse(InputStream in){
				String plan = this.getControleur().getGrapheRoutier().getPlanXML();
				return Reponse.succes(plan);
			}  
		};
		
		return this;
	}
}
