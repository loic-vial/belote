import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.HashMap;
import util.XML;

public class ConfigBelote
{
	public static void main(String[] args)
	{
		HashMap config = new HashMap();
		
		config.put("ip", "localhost");
		config.put("port", 4242);
		config.put("nom", "Bob");
		config.put("imageFond", "res/tapis4.jpg");
		config.put("tailleFenetre", new Dimension(900, 675));
		config.put("tailleCarte", new Dimension(75, 109));
		config.put("dureeChoixAtout", 60);
		config.put("dureeChoixCarte", 60);
		config.put("fontCompteurPoints", new Font("Arial", Font.PLAIN, 24));
		config.put("couleurCompteurPoints", Color.white);
		config.put("fontNoms", new Font("Arial", Font.PLAIN, 24));
		config.put("couleurNomsEquipe1", Color.red);
		config.put("couleurNomsEquipe2", Color.blue);
		config.put("fontAtout", new Font("Arial", Font.PLAIN, 18));
		config.put("couleurAtout", Color.white);
		
			HashMap configVueTempsG = new HashMap();
	
			configVueTempsG.put("arrierePlan", new Color(255, 255, 255, 152));
			configVueTempsG.put("avantPlan", Color.red);
			configVueTempsG.put("police", new Font("DS-digital", Font.TYPE1_FONT, 24));
			configVueTempsG.put("separateur", " : ");
			configVueTempsG.put("labelInitial", "00 : 00 : 00");
			configVueTempsG.put("placement", new FlowLayout());
					
			HashMap configSablier = new HashMap();
	
			configSablier.put("mode", "sablier");
			configSablier.put("butee", "0 : 00 : 00");
			configSablier.put("tempsTotal", 45);
			configSablier.put("classeVue", "composantsg.VueTempsG");
			configSablier.put("configVue", configVueTempsG);
		
		config.put("configSablier", configSablier);
		
		XML.store(config, "res/ConfigBelote");
		XML.load("res/ConfigBelote");
	}

}
