package belote.ihm;

import java.util.ArrayList;
import java.util.Random;

public class JoueurVirtuelAleatoire extends Console implements Vue
{		
	public JoueurVirtuelAleatoire(String nom, boolean mute)
	{
		super(nom, mute);
	}
	
	public JoueurVirtuelAleatoire(String nom)
	{
		this(nom, true);
	}
	
	public String askNom()
	{
		return nom;
	}

	public String askCarte(ArrayList<String> main)
	{
		return main.get(new Random().nextInt(main.size()));
	}

	public boolean askPrendreAtout()
	{
		return new Random().nextInt(1) == 0 ? true : false;
	}

	public String askCouleurAtout()
	{
		return null;
	}
}
