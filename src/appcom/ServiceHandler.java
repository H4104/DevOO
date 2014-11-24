package appcom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ServiceHandler implements HttpHandler {

	private String nomService;
	
	public ServiceHandler(String nomService, ServeurLivraison serveur){
		this.nomService = nomService;
		
		serveur.CreerContexte(nomService, this);
	}
	
	/**
	 * G�n�re une r�ponse au service pour le javascript
	 * @param in Buffer du contenu de la requ�te si besoin
	 * @return la reponse � donner au JS
	 */
	protected String getReponse(BufferedReader in){
		return null;
	}
	protected String getReponse(InputStream in){
		return null;
	}
	
	/**
	 * G�re une requete : r�cup�re le contenu, cr�e une reponse puis la renvoie
	 * @param t Echange http � g�rer
	 * @throws IOException
	 * @see getReponse
	 */
	@Override
	public void handle(HttpExchange t) throws IOException {
        
        Headers responseHeaders= t.getResponseHeaders();
        responseHeaders.set("Content-Type","text/plain");
        responseHeaders.set("Access-Control-Allow-Origin", "*");
        
        String reponse = getReponse(t.getRequestBody());
        if( reponse == null ){
	        BufferedReader in = new BufferedReader (new InputStreamReader (t.getRequestBody()));
			reponse = getReponse(in);
			if( reponse == null ) throw new IOException();
        }
        t.sendResponseHeaders(200, reponse.length());
        
        OutputStream os = t.getResponseBody();
        os.write(reponse.getBytes());
        os.close();

	}
	
	/**
	 * 
	 * @return Nom du service g�r� par le handler
	 */
	public String getNomService(){
		return this.nomService;
	}

}
