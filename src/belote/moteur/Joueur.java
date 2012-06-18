package belote.moteur;

import java.util.ArrayList;

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
	 * Retourne le numero du joueur
	 */
	public int getNumero()
	{
		return m_numero;
	}

	/**
	 * Retourne le nom du joueur
	 */
	public String getNom()
	{
		return m_nom;
	}

	/**
	 * Retourne la main du joueur
	 */
	public ArrayList<Carte> getMain()
	{
		return m_main;
	}

	/**
	 * Retourne les cartes gagnees du joueur
	 */
	public ArrayList<Carte> getCartesGagnees()
	{
		return m_cartesGagnees;
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
	 * Retourne le nombre de points gagnes par le joueur
	 */
	public int getNbPointsGagnes(Couleur atout)
	{
		int ret = 0;
		for (Carte carte : m_cartesGagnees)
		{
			ret += carte.getValeur(atout);
		}
		return ret;
	}
}
