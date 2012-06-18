package belote.ihm;

import java.util.ArrayList;

/**
 * Interface de dialogue avec un joueur.
 * 
 * Notes : 
 * 
 * - Les cartes sont representees sous forme de String avec la syntaxe
 *   suivant la syntaxe "[valeur] de [couleur]" (ex : "dame de coeur").
 * 
 * - Les couleurs sont : "coeur", "carreau", "trefle" et "pique".
 * 
 * - Les valeurs sont : "sept", "huit", "neuf", "dix", "valet", "dame", "roi" 
 *   et "as".
 *   
 * - Les joueurs sont representes par des numeros (0-3) correspondant aux 
 *   4 cardinalites :
 *   - 0 = SUD
 *   - 1 = OUEST
 *   - 2 = NORD
 *   - 3 = EST
 */
public interface Vue
{
	/**
	 * Place le joueur, lui attribut un numero = une cardinalite.
	 */
	public void majPlacement(int num);
	
	/**
	 * Demande le nom du joueur.
	 */
	public String askNom();
	
	/**
	 * Donne les noms des joueurs presents sur la table.
	 * Index de la liste = numero du joueur.
	 */
	public void majNoms(ArrayList<String> nomsJoueurs);
	
	/**
	 * Initialisation du jeu : les 4 joueurs sont en places et aucune carte
	 * n'est distribuee.
	 */
	public void init();
	
	/**
	 * Demande quelle carte le joueur veut jouer parmi celles de la liste.
	 */
	public String askCarte(ArrayList<String> cartes);
	
	/**
	 * Met a jour la carte proposee en tant qu'atout.
	 */
	public void majAtoutPropose(String atout);
	
	/**
	 * Met a jour le dernier pli joue
	 */
	public void majDernierPli(ArrayList<String> pli);

	/**
	 * Demande au joueur s'il veut prendre l'atout precedemment propose.
	 */
	public boolean askPrendreAtout();

	/**
	 * Demande au joueur la couleur qu'il desire pour l'atout.
	 */
	public String askCouleurAtout();

	/**
	 * Un joueur gagne "nb" cartes (mais on ne sait pas lesquelles).
	 */
	public void majJoueurGagneCartes(int numJoueur, int nbCartes);
	
	/**
	 * Un joueur gagne des cartes (et on sait lesquelles)
	 */
	public void majJoueurGagneCartes(int numJoueur, ArrayList<String> cartes);
	
	/**
	 * Un joueur gagne une carte (mais on ne sait pas laquelle).
	 */
	public void majJoueurGagneCarte(int numJoueur);
	
	/**
	 * Un joueur gagne une carte (et on sait laquelle).
	 */
	public void majJoueurGagneCarte(int numJoueur, String carte);

	/**
	 * Un joueur joue une carte.
	 */
	public void majJoueurJoueCarte(int numJoueur, String carte);

	/**
	 * Donne le numero de pli courant.
	 */
	public void majPli(int numPli);

	/**
	 * Met a jour le joueur qui a prit l'atout.
	 */
	public void majJoueurPrendAtout(int numJoueur, String couleurAtout);

	/**
	 * Met a jour le joueur qui a gagne le pli.
	 */
	public void majVainqueurPli(int numJoueur);

	/**
	 * Indique le joueur qui est en train de jouer.
	 */
	public void majJoueurJoue(int numJoueur);

	/**
	 * Indique le nombre de points gagne par un joueur
	 */
	public void majJoueurGagnePoints(int numJoueur, int nbPoints);

	/**
	 * Indique les vainqueurs de la partie
	 */
	public void majVainqueursPartie(int numJoueur1, int numJoueur2);

}
