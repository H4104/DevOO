package controleur;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import org.w3c.dom.*;
import modele.*;

public class Controleur {
	
	/**
	 * Attribut de classe DateFormat permettant le formatage de l'heure � partir d'une String
	 */
	private static final DateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm:ss");
	
	private GrapheRoutier grapheRoutier;
	private FeuilleDeRoute feuilledeRoute;
	private Stack<Commande> listeFaits;
	private Stack<Commande> listeAnnules;
	/**
	 * Constructeur de Controleur
	 */
	 public Controleur(){
		grapheRoutier = new GrapheRoutier();
		feuilledeRoute = new FeuilleDeRoute();
	}
	
	
	/**
	 * G�n�ration de la FeuilleDeRoute � partir d'un document xml pass� en param�tre
	 * Le document est pars� 2 fois, une fois pour g�n�rer les PlagesHoraires,
	 * une fois pour g�n�rer les Livraisons et les lier � la PlageHoraire parente
	 * Si tout se passe bien, renvoie true
	 * Sinon renvoie false avec affichage en console du type de probl�me
	 * @param livDoc
	 * @return boolean
	 */
	@SuppressWarnings("deprecation")
	public boolean chargerLivraisons(Document livDoc){
		feuilledeRoute.clean();
		//Generation du document
		livDoc.getDocumentElement().normalize();

		Node entrepotNode = livDoc.getElementsByTagName("Entrepot").item(0);
		if(entrepotNode.getNodeType()==Node.ELEMENT_NODE){
			Element entreElement = (Element)entrepotNode;
			try{
				int idInter = Integer.parseInt(entreElement.getAttribute("adresse"));
				if(grapheRoutier.interExiste(idInter)){
					feuilledeRoute.renseignerEntrepot(grapheRoutier.rechercherInterParId(idInter));
				}else{
					System.err.println("Entrepot invalide");
					return false;
				}
			}catch(Exception e){
				System.err.println("Entrepot invalide");
				return false;
			}
		}
		
		NodeList phNodeList = livDoc.getElementsByTagName("Plage");
		if(phNodeList.getLength()==0){
			System.err.println("Aucune plage horaire ");
			return false;
		}
		
		//premier passage, creation des plages horaires
		for(int i =0;i<phNodeList.getLength();i++){
			Node phNode = phNodeList.item(i);
			if(phNode.getNodeType() == Node.ELEMENT_NODE){
				Element phElement = (Element)phNode;
				if(phElement.hasAttributes()){
					NamedNodeMap nnm = phElement.getAttributes();
					/*
					 * V�rification de l'int�grit� des donn�es pour les PlageHoraire
					 */
					try{
						String heureDeb = nnm.getNamedItem("heureDebut").getTextContent();
						String heureF = nnm.getNamedItem("heureFin").getTextContent();
						if(heureDeb == null || heureF==null){
							System.err.println("Heure invalide");
							return false;
						}else{
							try{
								Date heureDebut = HOUR_FORMAT.parse(heureDeb);
								Date heureFin = HOUR_FORMAT.parse(heureF);
								// V�rification de la coh�rence des heures fournies
								if(heureDebut.getHours()>=0 && heureDebut.getHours()<=24
								&& heureFin.getHours()>=0 && heureFin.getHours()<=24
									&& heureDebut.getHours() < heureFin.getHours()
									&& feuilledeRoute.checkHB(heureDebut)){
									PlageHoraire ph = new PlageHoraire(heureDebut,heureFin);
									feuilledeRoute.ajouterPlageHoraire(ph);
								}else{
									System.err.println("Format d'heure invalide.");
									return false;
								}
							}catch (Exception e){
								e.printStackTrace();
								System.err.println("Format d'heure invalide.");
								return false;
							}
						}
					}catch(Exception e){
						System.err.println("Format Plage horaire invalide.");
						return false;
					}
				}else{
					System.err.println("HasAttributes.");
					return false;
				}
			}
		}
		
		/* Deuxi�me passage avec cr�ation des Livraison et mise en relation avec la plage horaire 
		 * correspondante.
		 */
		
		NodeList livNodeList = livDoc.getElementsByTagName("Livraison");
		if(livNodeList.getLength() ==0){
			System.err.println("Aucune livraison");
			return false;
		}
		for(int j=0; j<livNodeList.getLength(); j++){
			Node livNode = livNodeList.item(j);
			if(livNode.getNodeType() == Node.ELEMENT_NODE){
				// R�cup�ration de la PlageHoraire parente
				Element livElement = (Element)livNode;
				Element phParent = (Element)livElement.getParentNode().getParentNode();
				String hDeb = phParent.getAttribute("heureDebut");
				PlageHoraire phParentObj;
				try{
					Date hDebDate = HOUR_FORMAT.parse(hDeb);
					phParentObj = feuilledeRoute.rechercherPHParHD(hDebDate);

					if(phParentObj == null ){
						System.err.println("Plage Horaire Parente non trouv�e");
						return false;
					}
					
				}catch(Exception e){
					e.printStackTrace();
					System.err.println("Page horaire parente invalide");
					return false;
				}
				
				if(livElement.hasAttributes()){
					NamedNodeMap nnm = livElement.getAttributes();
					// V�rification de l'int�grit� des donn�es pour les Livraisons
					try{
						int id = Integer.parseInt(nnm.getNamedItem("id").getTextContent());
						int idClient = Integer.parseInt(nnm.getNamedItem("client").getTextContent());
						int idInter = Integer.parseInt(nnm.getNamedItem("adresse").getTextContent());
						
						
						if(grapheRoutier.interExiste(idInter)){
							Intersection inter = grapheRoutier.rechercherInterParId(idInter);
							Livraison liv = new Livraison(inter,id, idClient);
							phParentObj.addLivraison(liv);
						}
					}catch(Exception e){
						System.err.println("Format de livraion invalide");
						return false;
					}
					
				}else{
					System.err.println("Has arrtibute");
					return false;
				}
			}
		}
		
		
		return true;
	}
	
	/**
	 * Appel de la CommandeChargerPlan qui renvoie un GrapheRoutier
	 * si la liste d'intersction est vide, c'est que le document xml n'�tait pas correct
	 * Si tout se passe bien, renvoie true
	 * Sinon renvoie false avec affichage en console du type de probl�me
	 * @param plan
	 * @return boolean
	 */
	public boolean chargerPlan(Document plan){
		
		grapheRoutier = CommandeChargerPlan.chargerPlan(plan, grapheRoutier);
		if(grapheRoutier.getListeIntersections().size() == 0){
			return false;
		}else{
			return true;
		}
		
	}

	/**
	 * getter du graphe Routier contenant les Intersections
	 * @return GrapheRoutier
	 */
	public GrapheRoutier getGrapheRoutier(){return this.grapheRoutier;}
	/** getter de la feuille de route contenant les PlageHoraire de livraisons
	 * @return FeuilleDeRoute
	 */
	public FeuilleDeRoute getFeuilleDeRoute(){return this.feuilledeRoute;}
	
	public void ajouterLivraison(int idIntersection, int idClient, int idLivraisonPrecedente) {
		Livraison precedente = this.getFeuilleDeRoute().getGrapheLivraison().getLivraison(idLivraisonPrecedente);
		Livraison nouvelle = new Livraison();
		CommandeAjout c = new CommandeAjout(nouvelle, precedente, this.getFeuilleDeRoute());
	}
	
	public void supprimerLivraison() {
		
	}
	
	public void annuler() {
		this.listeFaits.pop().annuler();
	}
	
	public void retablir() {
		this.listeAnnules.pop().executer();
	}
}