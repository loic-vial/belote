package belote;

import java.util.HashMap;
import util.XML;
import belote.ihm.JoueurVirtuelAleatoire;

public class MainClientAleatoire
{
	public static void main(String[] args) throws Exception
	{
		// Chargement de la config
		HashMap config = (HashMap) XML.load("res/ConfigBelote");
		
		// Recuperation du nom du joueur
		String nom = (String) config.get("nom");
		
		// Creation du joueur aleatoire
		new Client(config, new JoueurVirtuelAleatoire(nom, false));
	}

}
