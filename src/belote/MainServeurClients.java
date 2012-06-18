package belote;

import java.util.HashMap;
import util.XML;
import belote.ihm.Controleur;
import belote.ihm.JoueurVirtuelAleatoire;
import belote.moteur.Belote;

public class MainServeurClients
{
	public static void main(String[] args) throws Exception
	{
		// Chargement de la config
		HashMap config = (HashMap) XML.load("res/ConfigBelote");
		
		// Creation du serveur
		new Serveur((Integer) config.get("port"), new Belote());
		
		// Creation du client principal
		new Client(config, new Controleur(config));

		// Creation des joueurs aleatoires
		new Client(config, new JoueurVirtuelAleatoire("Patrick", false));
		new Client(config, new JoueurVirtuelAleatoire("Carlo", false));
		new Client(config, new JoueurVirtuelAleatoire("Patate", false));
	}

}
