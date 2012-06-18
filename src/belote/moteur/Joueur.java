package belote.moteur;

import java.util.ArrayList;
import java.util.Collection;
import exception.IPException;

/**
 * Joueur
 */
public class Joueur
{
	/**
	 * Le numero du joueur
	 */
	private int m_numero;
	
	/**
	 * Le nom du joueur (ou pseudo)
	 */
	private String m_nom;
	
	/**
	 * La liste des cartes dans la main du joueur
	 */
	private ArrayList<Carte> m_main;
	
	/**
	 * La liste des cartes gagnees par le joueur
	 */
	private ArrayList<Carte> m_cartesGagnees;

	/**
	 * Constructeur
	 */
	public Joueur(String nom, int numero)
	{
		m_nom = nom;
		m_numero = numero;
		m_main = new ArrayList<Carte>();
		m_cartesGagnees = new ArrayList<Carte>();
	}

	/**
	 * Retourne le nom du joueur
	 */
	public String getNom()
	{
		return m_nom;
	}

	/**
	 * Retourne le numero du joueur
	 */
	public int getNumero()
	{
		return m_numero;
	}

	/**
	 * Retourne la main du joueur
	 */
	public ArrayList<Carte> getMain()
	{
		return m_main;
	}

	/**
	 * Ajoute une carte a la main du joueur
	 */
	public void addCarteMain(Carte c)
	{
		if (c == null) throw new IPException();
		m_main.add(c);
	}

	/**
	 * Ajoute des cartes a la main du joueur
	 */
	public void addCartesMain(Collection<Carte> cartes)
	{
		if (cartes == null) throw new IPException();
		m_main.addAll(cartes);
	}

	/**
	 * Retire une carte de la main du joueur
	 */
	public void subCarteMain(Carte c)
	{
		if (c == null) throw new IPException();
		if (!m_main.contains(c))
			throw new IPException("Le joueur n'a pas la carte " + c);
		m_main.remove(c);
	}
	
	/**
	 * Retourne la liste des cartes gagnees
	 */
	public ArrayList<Carte> getCartesGagnees()
	{
		return m_cartesGagnees;
	}
	
	/**
	 * Ajoute des cartes a la liste des cartes gagnees
	 */
	public void addCartesGagnees(Collection<Carte> cartes)
	{
		if (cartes == null) throw new IPException();
		m_cartesGagnees.addAll(cartes);
	}

	/**
	 * Retourne true ssi le joueur possede au moins une carte d'une couleur en particulier
	 */
	public boolean possedeCouleurEnMain(Couleur couleur)
	{
		for (Carte c : m_main)
		{
			if (c.getCouleur().equals(couleur)) return true;
		}
		return false;
	}
	
	/**
	 * Verifie si le joueur possede une carte en particulier dans sa main
	 */
	public boolean possedeCarteEnMain(Carte carte)
	{
		return m_main.contains(carte);
	}

	/**
	 * Verifie si le joueur possede une carte superieure a une carte donnee
	 */
	public boolean possedeCarteSuperieureEnMain(Carte carte, Couleur atout)
	{
		for (Carte c : m_main)
		{
			if (c.getValeur(atout) > carte.getValeur(atout)) return true;
		}
		return false;
	}
	
	/**
	 * Retourne le nombre de points gagnes par le joueur
	 */
	public int getNbPoints(Couleur atout)
	{
		int ret = 0;
		for (Carte carte : m_cartesGagnees)
		{
			ret += carte.getValeur(atout);
		}
		return ret;
	}

	/**
	 * toString
	 */
	public String toString()
	{
		String retour = "";
		retour += "Joueur num " + m_numero;
		return retour;
	}

}
