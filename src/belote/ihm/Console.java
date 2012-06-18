package belote.ihm;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * IHM Console
 */
public class Console implements Vue
{
	private boolean muted;
	
	protected String nom;

	private static Scanner sc = new Scanner(System.in);

	public Console(String nomJoueur, boolean mute)
	{
		this.nom = nomJoueur;
		muted = mute;
	}
	
	public Console(String nomJoueur)
	{
		this(nomJoueur, true);
	}

	/**
	 * affichage console d'un objet + saut de ligne
	 */
	private void afficher(Object obj)
	{
		if (!muted) System.out.println(obj);
	}

	public void init()
	{
		afficher("Demarrage du jeu");
	}
	
	public String askCarte(ArrayList<String> main)
	{
		System.out.println("Quelle carte voulez vous jouer ? (1 - " + main.size() + ")");
		for (int i = 0 ; i < main.size() ; i++)
			System.out.println((i+1) + " - " + main.get(i));
		int c;
		do {
			c = sc.nextInt();
		} while (c < 1 || c > main.size());
		String carte = main.get(c - 1);
		return carte;
	}

	public void majAtoutPropose(String atout)
	{
		afficher("L'atout propose est la carte " + atout);
	}
	

	public void majDernierPli(ArrayList<String> pli) {
		
		String affich = "Le dernier pli est : ";
		
		for(String valeur : pli){
			affich += valeur + "\t";
		}
		
		afficher(affich);
	}


	public boolean askPrendreAtout()
	{
		System.out.println("Joueur " + nom + " : ");
		char reponse;
		do
		{ // tant que la reponse n'est pas O ou N
			System.out.println("Voulez-vous prendre l'atout ?");
			System.out.println("Si oui tapez O, sinon tapez N");
			reponse = sc.next().charAt(0);
		} while (reponse != 'O' && reponse != 'N');
		if (reponse == 'O')
			return true;
		else
			return false;
	}

	public String askCouleurAtout()
	{
		int choix = 0;
		do
		{// tant que la reponse n'est pas O ou N

			System.out.println("Voulez-vous prendre une autre couleur " +
					"en tant qu'atout?");
			System.out.println(
					"1 - Coeur\n" +
					"2 - Carreau\n" +
					"3 - Trefle\n" +
					"4 - Pique\n" +
					"5 - Non");

			choix  = sc.nextInt();

		} while (choix < 1 || choix > 5);

		switch (choix)
		{
		case 1 : return "coeur";
		case 2 : return "carreau";
		case 3 : return "trefle";
		case 4 : return "pique";
		default: return null;
		}
	}

	public void majJoueurGagneCarte(int numJoueur, String carte)
	{
		afficher("Le joueur " + numJoueur + " gagne la carte " + carte);
	}

	public void majJoueurJoueCarte(int numJoueur, String carte)
	{
		afficher("Le joueur " + numJoueur + " joue la carte " + carte);
	}

	public void majPli(int numPli)
	{
		afficher("Debut du pli numero " + numPli);
	}

	public void majJoueurPrendAtout(int numJoueur, String atout)
	{
		afficher("Le joueur " + numJoueur + " prend l'atout " + atout);
	}

	public String askNom()
	{
		return nom;
	}

	public void majJoueurGagneCarte(int numJoueur)
	{
		afficher("Le joueur " + numJoueur + " gagne une carte");
	}

	public void majJoueurGagneCartes(int numJoueur, int nbCartes)
	{
		afficher("Le joueur " + numJoueur + " gagne " + nbCartes + " cartes");		
	}

	public void majJoueurGagneCartes(int numJoueur, ArrayList<String> cartes)
	{
		afficher("Le joueur " + numJoueur + " gagne les cartes " + cartes);		
	}

	public void majVainqueurPli(int numJoueur)
	{
		afficher("Le vainqueur du pli est le joueur " + numJoueur);
	}

	public void majPlacement(int num)
	{
		afficher("Vous etes places au " + num);
	}

	public void majNoms(ArrayList<String> nomsJoueurs)
	{
		afficher("Les joueurs presents sont : " + nomsJoueurs);
	}

	public void majJoueurJoue(int numJoueur)
	{
		afficher("C'est au joueur " + numJoueur + " de jouer");
	}

	public void majJoueurGagnePoints(int numJoueur, int nbPoints)
	{
		afficher("Le joueur " + numJoueur + " gagne " + nbPoints + " points");
	}

	public void majVainqueursPartie(int numJoueur1, int numJoueur2)
	{
		afficher("Les vainqueurs sont les joueurs " + numJoueur1 + " et " + numJoueur2);
	}


}
