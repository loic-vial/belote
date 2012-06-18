package belote.moteur;

import java.util.ArrayList;
import java.util.Collection;

import belote.ihm.Vue;

public class InputOutput
{
	private ArrayList<Vue> joueurs;

	public InputOutput()
	{
		this.joueurs = new ArrayList<Vue>();
	}

	public void addJoueur(Vue joueur)
	{
		joueurs.add(joueur);
	}
	
	public int getNumJoueur(Vue joueur)
	{
		return joueurs.indexOf(joueur);
	}
	
	public void removeJoueur(Vue joueur)
	{
		joueurs.remove(joueur);
	}

	public void majDebutPartie()
	{
		for (Vue vue : joueurs)
			vue.init();
	}
	
	public void majJoueurPrendAtout(int numJoueur, Couleur atout)
	{
		for (Vue vue : joueurs)
			vue.majJoueurPrendAtout(numJoueur, ("" + atout).toLowerCase());
	}

	public void majAtoutPropose(Carte atout)
	{
		for (Vue vue : joueurs)
			vue.majAtoutPropose(("" + atout).toLowerCase());
	}
	
	public void majDernierPli(Collection<Carte> pli)
	{
		ArrayList<String> cartes = new ArrayList<String>();
		for(Carte valeur : pli) {
			cartes.add(valeur.toString());
		}
		for (Vue vue : joueurs)
				vue.majDernierPli(cartes);			
	}

	public void majJoueurGagneCartePublic(int joueur, Carte carte)
	{
		for (Vue vue : joueurs)
		{
			vue.majJoueurGagneCarte(joueur, ("" + carte).toLowerCase());
		}
	}
	
	public void majJoueurGagneCarteAnonyme(int joueur, Carte carte)
	{
		for (int i = 0 ; i < joueurs.size() ; i++)
		{
			if (i != joueur) joueurs.get(i).majJoueurGagneCarte(joueur);
			else joueurs.get(i).majJoueurGagneCarte(joueur, ("" + carte).toLowerCase());
		}
	}

	public void majJoueurJoueCarte(int numJoueur, Carte carte)
	{
		for (Vue vue : joueurs)
			vue.majJoueurJoueCarte(numJoueur, ("" + carte).toLowerCase());
	}

	public void majDebutPli(int numPli)
	{
		for (Vue vue : joueurs)
			vue.majPli(numPli);
	}

	public String askNomJoueur(int numJoueur)
	{
		return joueurs.get(numJoueur).askNom();
	}

	public Carte askCarte(int numJoueur, ArrayList<Carte> main)
	{
		ArrayList<String> mainStr = new ArrayList<String>();
		for (Carte carte : main) mainStr.add(("" + carte).toLowerCase());
		return new Carte(joueurs.get(numJoueur).askCarte(mainStr));
	}

	public boolean askPrendreAtout(int numJoueur)
	{
		return joueurs.get(numJoueur).askPrendreAtout();
	}

	public Couleur askCouleurAtout(int numJoueur)
	{
		try
		{
			return Couleur.valueOf(joueurs.get(numJoueur).askCouleurAtout().toUpperCase());
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public void majJoueurGagneCartesAnonyme(int numJoueur, ArrayList<Carte> cartes)
	{
		ArrayList<String> cartesStr = new ArrayList<String>();
		for (Carte carte : cartes) cartesStr.add(("" + carte).toLowerCase());
		for (int i = 0 ; i < joueurs.size() ; i++)
		{
			if (i != numJoueur) joueurs.get(i).majJoueurGagneCartes(numJoueur, cartes.size());
			else joueurs.get(i).majJoueurGagneCartes(numJoueur, cartesStr);
		}		
	}

	public void majVainqueurPli(int numJoueur)
	{
		for (Vue vue : joueurs)
			vue.majVainqueurPli(numJoueur);
	}

	public void majPlacements()
	{
		for (int i = 0 ; i < joueurs.size() ; i++)
			joueurs.get(i).majPlacement(i);
	}

	public void majJoueurJoue(int numJoueur)
	{
		for (Vue vue : joueurs)
			vue.majJoueurJoue(numJoueur);
	}

	public void majJoueurGagnePoints(int numJoueur, int nbPoints)
	{
		for (Vue vue : joueurs)
			vue.majJoueurGagnePoints(numJoueur, nbPoints);
	}

	public void majNoms(ArrayList<String> nomsJoueurs)
	{
		for (Vue vue : joueurs)
			vue.majNoms(nomsJoueurs);
	}

	public void majVainqueursPartie(int i, int j)
	{
		for (Vue vue : joueurs)
			vue.majVainqueursPartie(i, j);
	}

	public void majPlacementJoueur(int numJoueur)
	{
		joueurs.get(numJoueur).majPlacement(numJoueur);
	}

}
