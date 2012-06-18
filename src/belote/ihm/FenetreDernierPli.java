package belote.ihm;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Fenetre dans laquelle on va afficher le dernier pli joué
 */
public class FenetreDernierPli extends JFrame{

	/**
	 * Panneau autour des cartes du pli
	 */
	private JPanel pli;
	
	/**
	 * Constructeur
	 */
	public FenetreDernierPli(Dimension tailleCarte)
	{
		
		super();
		setTitle("Dernier pli");

		pli = new JPanel();
		add(pli);
		pack();
	}
	
	public void setDernierPli(ArrayList<VueCarte> cartes){
		
		pli.removeAll();
		for(VueCarte valeur : cartes) {
			pli.add(valeur);
		}
		pack();
	}
	
}
