package belote.moteur;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import exception.BUException;

/**
 * Represente le paquet de carte du jeu.
 */
public class Paquet implements Serializable
{
	private static final long serialVersionUID = 42;
	
	/**
	 * Liste interne qui stocke les cartes.
	 */
	private ArrayList<Carte> m_cartes;

	/**
	 * Construit un paquet avec une carte de chaque couleur, pour chaque valeur.
	 */
	public Paquet() {	
		m_cartes = new ArrayList<Carte>();	
		for(Valeur v : Valeur.values()) {	
			for(Couleur c : Couleur.values())
				m_cartes.add(new Carte(v, c));
		}
	}
	
	public ArrayList<Carte> getCartes() {
		return m_cartes;
	}
	
	public void setCartes(ArrayList<Carte> cartes) {
		m_cartes = cartes;
	}
	
	/**
	 * Represente textuellement le paquet de cartes.
	 */
	public String toString() {
		String retour = "";
		for (Carte c : m_cartes)
			retour += c.toString() + "\n";
		return retour;
	}
	
	/**
	 * Melange les cartes du paquet.
	 */
	public void melanger() {
		Collections.shuffle(m_cartes);
	}
	
	/**
	 * Retourne n cartes au dessus du paquet, SANS LES RETIRER DU PAQUET.
	 */
	public ArrayList<Carte> piocher(int n) throws BUException
	{
		if (m_cartes.size() < n)
			throw new BUException("Le paquet comporte moins de " + n + " carte(s).");
		ArrayList<Carte> retour = new ArrayList<Carte>();
		for (int i = 0 ; i < n ; i++)
			retour.add(m_cartes.get(i));
		return retour;
	}
	
	/**
	 * Retourne la carte au dessus du paquet, SANS LA RETIRER DU PAQUET.
	 */
	public Carte piocher() throws BUException {
		return piocher(1).get(0);
	}

	/**
	 * Retire n cartes du dessus du paquet.
	 */
	public void retirer(int n) throws BUException
	{
		m_cartes.removeAll(piocher(n));
	}
	
	/**
	 * Retire la carte du dessus du paquet.
	 */
	public void retirer() throws BUException
	{
		retirer(1);
	}
}