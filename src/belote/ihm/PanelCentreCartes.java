package belote.ihm;

import javax.swing.JLabel;
import javax.swing.JPanel;
import util.ihm.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.HashMap;

public class PanelCentreCartes extends JPanel
{
	private JPanel panelNorth;
	private JPanel panelWest;
	private JPanel panelSouth;
	private JPanel panelEast;
	private JPanel[] panels;
	
	private JLabel nomJoueurNorth;
	private JLabel nomJoueurWest;
	private JLabel nomJoueurSouth;
	private JLabel nomJoueurEast;
	private JLabel[] nomsJoueurs;

	private JPanel panelCenter;
	
	// private int numJoueurSud;

	/**
	 * Constructeur.
	 * Par defaut, le joueur 0 est au sud.
	 */
	public PanelCentreCartes(HashMap config)
	{
		Font fontNoms = (Font) config.get("fontNoms");
		Color couleurNomsEquipe1 = (Color) config.get("couleurNomsEquipe1");
		Color couleurNomsEquipe2 = (Color) config.get("couleurNomsEquipe2");

		setLayout(new BorderLayout());
		setOpaque(false);
		
		panelNorth = new JPanel();
		panelNorth.setOpaque(false);
			nomJoueurNorth = new JLabel();
			nomJoueurNorth.setFont(fontNoms);
			nomJoueurNorth.setForeground(couleurNomsEquipe1);
		panelNorth.add(nomJoueurNorth);
		add(panelNorth, BorderLayout.NORTH);
		panelNorth.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		panelWest = new JPanel();
		panelWest.setOpaque(false);
			nomJoueurWest = new JLabel();
			nomJoueurWest.setFont(fontNoms);
			nomJoueurWest.setForeground(couleurNomsEquipe2);
		panelWest.add(nomJoueurWest);
		add(panelWest, BorderLayout.WEST);
		panelWest.setLayout(new VerticalFlowLayout(VerticalFlowLayout.CENTER));

		panelSouth = new JPanel();
		panelSouth.setOpaque(false);
			nomJoueurSouth = new JLabel();
			nomJoueurSouth.setFont(fontNoms);
			nomJoueurSouth.setForeground(couleurNomsEquipe1);
		panelSouth.add(nomJoueurSouth);
		add(panelSouth, BorderLayout.SOUTH);
		panelNorth.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		panelEast = new JPanel();
		panelEast.setOpaque(false);
			nomJoueurEast = new JLabel();
			nomJoueurEast.setFont(fontNoms);
			nomJoueurEast.setForeground(couleurNomsEquipe2);
		panelEast.add(nomJoueurEast);
		add(panelEast, BorderLayout.EAST);
		panelEast.setLayout(new VerticalFlowLayout(VerticalFlowLayout.CENTER));
		
		panels = new JPanel[4];
		panels[0] = panelSouth;
		panels[1] = panelWest;
		panels[2] = panelNorth;
		panels[3] = panelEast;
		
		nomsJoueurs = new JLabel[4];
		nomsJoueurs[0] = nomJoueurSouth;
		nomsJoueurs[1] = nomJoueurWest;
		nomsJoueurs[2] = nomJoueurNorth;
		nomsJoueurs[3] = nomJoueurEast;
		
		panelCenter = new JPanel();
		panelCenter.setOpaque(false);
		add(panelCenter, BorderLayout.CENTER);
		
		// numJoueurSud = 0;
	}
	
	/**
	 * Definit le numero du joueur sud (on en deduit les autres)
	 */
	public void setNumJoueurSud(int numJoueurSud)
	{
		panels[(numJoueurSud + 0) % 4] = panelSouth;
		panels[(numJoueurSud + 1) % 4] = panelWest;
		panels[(numJoueurSud + 2) % 4] = panelNorth;
		panels[(numJoueurSud + 3) % 4] = panelEast;
		
		nomsJoueurs[(numJoueurSud + 0) % 4] = nomJoueurSouth;
		nomsJoueurs[(numJoueurSud + 1) % 4] = nomJoueurWest;
		nomsJoueurs[(numJoueurSud + 2) % 4] = nomJoueurNorth;
		nomsJoueurs[(numJoueurSud + 3) % 4] = nomJoueurEast;
		
		// this.numJoueurSud = numJoueurSud;
	}
	
	/**
	 * Ajoute une carte au panel d'un joueur.
	 */
	public void addCarte(VueCarte carte, int numJoueur)
	{
		panels[numJoueur].add(carte);
	}
	
	/**
	 * Definit le nom d'un joueur
	 */
	public void setNomJoueur(String nom, int numJoueur)
	{
		nomsJoueurs[numJoueur].setText(nom + " [" + getCardinalite(numJoueur) + "]");
	}
	
	/**
	 * Converti un numero en cardinalite
	 */
	private String getCardinalite(int num)
	{
		switch (num % 4)
		{
		case 0 : return "sud";
		case 1 : return "ouest";
		case 2 : return "nord";
		default : return "est";
		}
	}
	
	/**
	 * Supprime toutes les cartes
	 */
	public void removeAll()
	{
		for (int i = 0 ; i < panels.length ; i++)
		{
			panels[i].removeAll();
			panels[i].add(nomsJoueurs[i]);
		}
	}
	
	/**
	 * Supprime tous les noms des joueurs
	 */
	public void removeAllNomsJoueurs()
	{
		for (JLabel lab : nomsJoueurs)
		{
			lab.setText("");
		}
	}
	
	/**
	 * Retourne le nom d'un joueur
	 */
	public String getNomJoueur(int numJoueur)
	{
		return nomsJoueurs[numJoueur].getText();
	}
}
