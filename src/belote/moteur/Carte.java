package belote.moteur;

import java.io.Serializable;
import java.util.Scanner;

import exception.IPException;

/**
 * Une carte possede :
 * - une valeur (sept, huit, neuf, dix, valet, dame, roi, as) ;
 * - une couleur (coeur, pique, carreau, trefle).
 */
public class Carte implements Serializable
{
	private static final long serialVersionUID = 42;

	/**
	 * La couleur.
	 */
	private Couleur couleur;
	
	/**
	 * La valeur.
	 */
	private Valeur valeur;
	
	/**
	 * Construit une dame de coeur
	 */
	public Carte()
	{
		valeur = Valeur.DAME;
		couleur = Couleur.COEUR;
	}
	
	/**
	 * Construit une carte.
	 */
	public Carte(Valeur val, Couleur coul)
	{
		if (val == null) throw new IPException(1);
		if (coul == null) throw new IPException(2);
		valeur = val;		
		couleur = coul;
	}
	
	/**
	 * Construit une carte avec un toString
	 */
	public Carte(String carte)
	{
		Scanner scan = new Scanner(carte);
		valeur = Valeur.valueOf(scan.next().toUpperCase());
		scan.next();
		couleur = Couleur.valueOf(scan.next().toUpperCase());
	}
	
	/**
	 * Retourne la couleur.
	 */
	public Couleur getCouleur()
	{
		return couleur;
	}
	
	/**
	 * Retourne la valeur.
	 */
	public Valeur getValeur()
	{
		return valeur;
	}
	
	/**
	 * Retourne la valeur de la carte sous forme d'entier, c'est a dire la 
	 * "force", le nombre de points de cette carte, en prennant en compte 
	 * la couleur de l'atout
	 */
	public int getValeur(Couleur atout)
	{
		if (couleur.equals(atout))
		{
			if (valeur.equals(Valeur.VALET)) return 20;
			if (valeur.equals(Valeur.NEUF)) return 14;
		}
		
		else if (valeur.equals(Valeur.VALET)) return 2;
		
		if (valeur.equals(Valeur.AS)) return 11;
		if (valeur.equals(Valeur.DIX)) return 10;
		if (valeur.equals(Valeur.ROI)) return 4;
		if (valeur.equals(Valeur.DAME)) return 3;
		
		return 0;
	}
	
	/**
	 * Verifie si la carte est la plus forte en prennant en compte l'atout et 
	 * le pli courant
	 */
	public boolean isSuperieur(Carte carte, Couleur atout, Couleur couleurPli)
	{
		// si la carte n'est pas de la couleur de l'atout et que nous si
		if (couleur == atout && carte.couleur != atout) return true;
		
		// inversement
		if (couleur != atout && carte.couleur == atout) return false;
		
		// si la carte n'est pas de la couleur du pli et que nous si
		if (couleur == couleurPli && carte.couleur != couleurPli) return true;
		
		// inversement
		if (couleur != couleurPli && carte.couleur == couleurPli) return false;
		
		// sinon on compare les points
		if (getValeur(atout) > carte.getValeur(atout)) return true;
		else return false;
	}

	/**
	 * Represente textuellement la carte.
	 */
	public String toString()
	{
		return ("" + valeur + " de " + couleur).toLowerCase();
	}
	
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((couleur == null) ? 0 : couleur.hashCode());
		result = prime * result + ((valeur == null) ? 0 : valeur.hashCode());
		return result;
	}

	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Carte)) return false;
		Carte other = (Carte) obj;
		if (couleur != other.couleur) return false;
		if (valeur != other.valeur) return false;
		return true;
	}
}