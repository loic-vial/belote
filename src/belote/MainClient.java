package belote;

import java.util.HashMap;
import util.XML;
import belote.ihm.*;

public class MainClient
{
	public static void main(String[] args) throws Exception
	{
		// Chargement de la config
		HashMap config = (HashMap) XML.load("res/ConfigBelote");
		
		// Creation du client
		new Client(config, new Controleur(config));
	}
}
