package belote;

import java.io.IOException;
import java.util.HashMap;
import util.XML;
import belote.moteur.Belote;

public class MainServeur
{
	public static void main (String[] args) throws IOException, InterruptedException
	{
		// Chargement de la config
		HashMap config = (HashMap) XML.load("res/ConfigBelote");
		
		// Creation du serveur
		new Serveur((Integer) config.get("port"), new Belote());
	}
}
