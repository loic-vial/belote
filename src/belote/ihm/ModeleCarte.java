package belote.ihm;

import java.util.Scanner;

public class ModeleCarte
{
	private String couleur;
	
	private String valeur;
	
	public ModeleCarte(String carte)
	{
		Scanner scan = new Scanner(carte);
		valeur = scan.next();
		scan.next();
		couleur = scan.next();
	}
	
	public ModeleCarte(String val, String coul)
	{
		valeur = val;		
		couleur = coul;
	}

	public String getCouleur()
	{
		return couleur;
	}

	public String getValeur()
	{
		return valeur;
	}

	public String toString()
	{
		return "" + valeur + " de " + couleur;
	}

	public boolean equals(Object obj)
	{
		if (!(obj instanceof ModeleCarte)) return false;
		ModeleCarte carte = (ModeleCarte) obj;
		return (carte.couleur.equals(couleur) && carte.valeur.equals(valeur));
	}
}