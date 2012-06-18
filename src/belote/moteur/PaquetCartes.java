package belote.moteur;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Represente le paquet de carte du jeu.
 */
public class PaquetCartes extends ArrayList<Carte>
{
	/**
	 * Construit un paquet avec une carte de chaque couleur, pour chaque valeur.
	 */
	public PaquetCartes()
	{
		super();
		for (Valeur v : Valeur.values())
		{
			for (Couleur c : Couleur.values())
				add(new Carte(v, c));
		}
	}

	/**
	 * Melange les cartes du paquet.
	 */
	public void melanger()
	{
		Collections.shuffle(this);
	}

	/**
	 * Retourne n cartes au dessus du paquet, SANS LES RETIRER DU PAQUET.
	 */
	public ArrayList<Carte> piocher(int n)
	{
		if (n > size()) throw new RuntimeException("Le paquet comporte moins de " + n + " carte(s).");
		ArrayList<Carte> retour = new ArrayList<Carte>();
		for (int i = 0; i < n; i++)
			retour.add(get(i));
		return retour;
	}

	/**
	 * Retourne la carte au dessus du paquet, SANS LA RETIRER DU PAQUET.
	 */
	public Carte piocher()
	{
		return piocher(1).get(0);
	}

	/**
	 * Retire n cartes du dessus du paquet.
	 */
	public void retirer(int n)
	{
		removeAll(piocher(n));
	}

	/**
	 * Retire la carte du dessus du paquet.
	 */
	public void retirer()
	{
		retirer(1);
	}
}