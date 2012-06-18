package belote.moteur;

import java.util.ArrayList;
import java.util.HashMap;
import exception.BUException;
import belote.ihm.Vue;

/**
 * Controleur de l'application Belote.
 * C'est lui qui s'occupe de gerer les differents elements de la partie.
 */
public class Belote {
	
	/**
	 * Les joueurs (4).
	 */
	private ArrayList<Joueur> joueurs;
	
	/**
	 * Le paquet de carte.
	 */
	private Paquet paquet;
	
	/**
	 * La couleur de l'atout.
	 */
	private Couleur atout;
	
	/**
	 * La couleur du pli en cours.
	 */
	private Couleur couleurPli;

	/**
	 * Le vainqueur du pli en cours.
	 */
	private Joueur vainqueurPli;

	/**
	 * Les cartes deposees sur la table (index par joueur)
	 */
	private HashMap<Joueur, Carte> table;
	
	/**
	 * Entrees/Sorties
	 */
	private InputOutput io;
	
	/**
	 * Construit le controleur qui va attendre l'arrivée des 4 joueurs
	 */
	public Belote()
	{
		// Initialisation des entrees/sorties
		io = new InputOutput();

		// Initialisation des joueurs
		joueurs = new ArrayList<Joueur>();
		
		// Creation du paquet
		paquet = new Paquet();
		
		// Definition explicite de l'atout
		atout = null;
		
		// Definition explicite de la couleur et du vainqueur du pli
		couleurPli = null;
		vainqueurPli = null;
		
		// Initialisation des cartes sur la table
		table = new HashMap<Joueur, Carte>();
	}
	
	/**
	 * Ajoute un joueur a la partie
	 */
	public void addJoueur(Vue vueJoueur)
	{
		int nbJoueurs = joueurs.size();
		if (nbJoueurs < 4)
		{
			io.addJoueur(vueJoueur);
			String nomJoueur = io.askNomJoueur(nbJoueurs);
			
			// place le joueur
			joueurs.add(new Joueur(nomJoueur, nbJoueurs));
			io.majPlacementJoueur(nbJoueurs);

			// Envoi les noms des joueurs en place
			io.majNoms(getNomsJoueurs());
		}
	}
	
	/**
	 * Supprime un joueur de la partie
	 */
	public void removeJoueur(Vue vueJoueur)
	{
		joueurs.remove(io.getNumJoueur(vueJoueur));
		io.removeJoueur(vueJoueur);
		// Re-envoi les positions et les noms des differents joueurs
		for (int i = 0 ; i < joueurs.size() ; i++)
			io.majPlacementJoueur(i);
		io.majNoms(getNomsJoueurs());
	}
	
	/**
	 * Lance la partie
	 */
	public void start()
	{
		if (io.getNbJoueurs() != 4)
			throw new BUException("La belote se joue a 4 joueurs !");
		
		do {
			raz();
			paquet.melanger();
		} while (!distribution());
		
		// Initialisation du premier joueur
		vainqueurPli = joueurs.get(0);
		
		for (int i = 1 ; i <= 8 ; i++)
		{
			io.majPli(i);
			table.clear();
			couleurPli = null;
			int numVainqueurPli = vainqueurPli.getNumero();
			vainqueurPli = null;
			tourDeTable(numVainqueurPli);
			io.majDernierPli(table.values());
			io.majVainqueurPli(vainqueurPli.getNumero());
			vainqueurPli.addCartesGagnees(table.values());
			io.majJoueurGagnePoints(vainqueurPli.getNumero(), getPointsSurTable());
		}
		
		// dix de der
		io.majJoueurGagnePoints(vainqueurPli.getNumero(), 10);
		
		int[] numVainqueurs = getVainqueursPartie();
		io.majVainqueursPartie(numVainqueurs[0], numVainqueurs[1]);
	}

	/**
	 * Fait un tour de table pour demander a chaque joueur de mettre 
	 * une carte, en commencant par le joueur indique en parametre
	 */
	private void tourDeTable(int numVainqueurPliPrecedent)
	{
		for (int i = 4 ; i > 0 ; i--)
		{
			Joueur joueur = joueurs.get((numVainqueurPliPrecedent + i) % 4);
			io.majJoueurJoue(joueur.getNumero());
			Carte carte = null;
			ArrayList<Carte> cartesValides = getCartesValides(joueur.getMain(), joueur);
			while (!cartesValides.contains(carte))
					carte = io.askCarte(joueur.getNumero(), cartesValides);
			
			Couleur couleur = carte.getCouleur();
			
			// premiere carte du pli
			if (couleurPli == null && vainqueurPli == null)
			{
				couleurPli = couleur;
				vainqueurPli = joueur;
			}
			
			// sinon si la carte jouee est superieure a la carte dominante
			else if (carte.isSuperieur(table.get(vainqueurPli), atout, couleurPli))
			{
				vainqueurPli = joueur;
			}
			
			joueur.subCarteMain(carte);
			table.put(joueur, carte);
			io.majJoueurJoueCarte(joueur.getNumero(), carte);
		}
	}
	
	/**
	 * Filtre la main d'un joueur pour en ressortir uniquement celles qui sont
	 * valides
	 */
	private ArrayList<Carte> getCartesValides(ArrayList<Carte> cartes, Joueur joueur)
	{
		ArrayList<Carte> retour = new ArrayList<Carte>();
		for (Carte carte : cartes)
		{
			if (validiteCarte(carte, joueur))
				retour.add(carte);
		}
		return retour;
	}
	
	/**
	 * Donne le nombre de points totaux presents sur la table
	 */
	private int getPointsSurTable()
	{
		int res = 0;
		for (Carte carte : table.values())
		{
			res += carte.getValeur(atout);
		}
		return res;
	}
	
	/**
	 * Verifie si la carte a poser est valide
	 */
	private boolean validiteCarte(Carte carte, Joueur joueur) {
		
		// carte nulle
		if (carte == null) return false;
		
		// si le joueur n'a pas la carte dans sa main (oui ca peut arriver =p)
		if (!joueur.possedeCarteEnMain(carte)) return false;
				
		// premiere carte du pli
		if (couleurPli == null) return true;
		
		Couleur couleur = carte.getCouleur();
		
		// si c'est une carte de la couleur du pli -> V
		if (couleur.equals(couleurPli)) return true;
		
		// si la carte dominante du pli courant est posee par le partenaire -> V
		if (vainqueurPli.getNumero() == (joueur.getNumero() + 2) % 4) return true;
		
		// si il avait une carte de la couleur du pli -> X
		if (joueur.possedeCouleurEnMain(couleurPli)) return false;

		// si c'est une carte de la couleur de l'atout -> V
		if (couleur.equals(atout)) return true;
		
		// sinon si il avait une carte de la couleur de l'atout -> X
		if (joueur.possedeCouleurEnMain(atout)) return false;
		
		// sinon il peut jouer ce qu'il veut
		return true;
		

		/*
		if (couleurPli.equals(atout)) {
			if (!joueur.possedeCouleur(atout))
				return true;
			if (!joueur.possedeCarteSuperieure(carteDominante, atout) && carte.getCouleur().equals(atout))
				return true;
		}
		else {
			if (carte.getCouleur().equals(couleurPli))
				return true;
			if (!joueur.possedeCouleur(couleurPli)) {
				if (!joueur.possedeCouleur(atout))
					return true;
				if (!carteDominante.getCouleur().equals(atout))
					return true;
				if (carte.getValeur(atout) > carteDominante.getValeur(atout))
					return true;
			}
		}
		return false;
		*/
	}

	/**
	 * Remise a zero : remet toutes les cartes des joueurs, des equipes
	 * et de la table dans le paquet
	 */
	private void raz()
	{
		io.init();
		for (Joueur j : joueurs)
		{
			paquet.getCartes().addAll(j.getMain());
			paquet.getCartes().addAll(j.getCartesGagnees());
			j.getMain().clear();
			j.getCartesGagnees().clear();
			if (table.containsKey(j)) paquet.getCartes().add(table.get(j));
		}
		table.clear();
	}

	/**
	 * Distribue les cartes du paquet aux joueurs.
	 * Retourne true si l'atout a ete choisi, false sinon.
	 */
	private boolean distribution()
	{
		// distribue 3 cartes a chacun des joueurs
		for(Joueur j : joueurs) {
			j.addCartesMain(paquet.piocher(3));
			io.majJoueurGagneCartesAnonyme(j.getNumero(), paquet.piocher(3));
			paquet.retirer(3);
		}

		// distribue 2 cartes a chacun des joueurs
		for(Joueur j : joueurs) {
			j.addCartesMain(paquet.piocher(2));
			io.majJoueurGagneCartesAnonyme(j.getNumero(), paquet.piocher(2));
			paquet.retirer(2);
		}
		
		Joueur jAtout = null;
		Carte carteProposee = paquet.piocher();
		
		// propose la prochaine carte en tant qu'atout
		io.majAtoutPropose(carteProposee);	
		
		// premier tour de table pour voir qui prend l'atout
		for (Joueur j : joueurs)
		{
			if (io.askPrendreAtout(j.getNumero()))
			{
				atout = carteProposee.getCouleur();
				jAtout = j;
				break;
			}
		}
		
		// deuxieme tour de table si personne n'a pris l'atout
		if (atout == null)
		{
			for (Joueur j : joueurs)
			{
				Couleur coul = io.askCouleurAtout(j.getNumero());
				if (coul != null)
				{
					atout = coul;
					jAtout = j;
					break;
				}
			}
		}
		
		// personne n'a pris l'atout, on reprend a zero
		if (atout == null) return false;
		
		io.majJoueurPrendAtout(jAtout.getNumero(), atout);
		
		// on donne la carte au joueur qui l'a choisie
		paquet.retirer();
		jAtout.addCarteMain(carteProposee);
		io.majJoueurGagneCarteAnonyme(jAtout.getNumero(), carteProposee);
		
		// on distribue le reste des cartes
		for(Joueur j : joueurs) {
			int nb = 3;
			// 2 cartes a celui qui a prit l'atout
			if (j == jAtout) nb = 2;
			j.addCartesMain(paquet.piocher(nb));
			io.majJoueurGagneCartesAnonyme(j.getNumero(), paquet.piocher(nb));
			paquet.retirer(nb);
		}
				
		return true;
	}
	
	/**
	 * Donne l'equipe vainqueur de la partie
	 */
	private int[] getVainqueursPartie()
	{
		Joueur vainqueur = null;
		for (Joueur joueur : joueurs)
		{
			if (vainqueur == null || 
				joueur.getNbPoints(atout) > vainqueur.getNbPoints(atout))
				vainqueur = joueur;
		}
		int[] ret = new int[2];
		ret[0] = vainqueur.getNumero();
		ret[1] = (ret[0] + 2) % 4;
		return ret;
	}
	
	/**
	 * Retourne la liste des noms des joueurs en place
	 */
	public ArrayList<String> getNomsJoueurs()
	{
		ArrayList<String> ret = new ArrayList<String>();
		for (Joueur joueur : joueurs)
		{
			ret.add(joueur.getNom());
		}
		return ret;
	}

}
