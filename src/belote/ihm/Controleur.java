package belote.ihm;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import composantsg.ControleurTempsG;

public class Controleur implements Vue
{
	private Fenetre vue;
	
	private FenetreAtout vueAtout;
	
	private FenetreDernierPli vueDernierPli;
	
	private int num;
	
	private int numPartenaire;
	
	private String nom;
	
	private HashMap<String, VueCarte> cartesEnJeu;
	
	private String carteChoisie;
	
	private JPanel[] panelsCartes;
	
	private ControleurTempsG sablier;
	
	private HashMap config;
	
	public Controleur(HashMap config)
	{
		num = 0;
		numPartenaire = 2;
		
		this.config = config;
		
		nom = (String) config.get("nom");
		
		vue = new Fenetre(config);
		vueAtout = new FenetreAtout(vue);
		
		vueDernierPli = new FenetreDernierPli((Dimension) config.get("tailleCarte"));
		
		panelsCartes = new JPanel[4];
		panelsCartes[0] = vue.getPanelSudCartes();
		panelsCartes[1] = vue.getPanelOuestCartes();
		panelsCartes[2] = vue.getPanelNordCartes();
		panelsCartes[3] = vue.getPanelEstCartes();

		cartesEnJeu = new HashMap<String, VueCarte>();
		carteChoisie = null;
		
		sablier = new ControleurTempsG(vue.getContainerVueSablier(), 
										(HashMap) config.get("configSablier"));
		
		vue.setVisible(true);
	}

	public void init()
	{
		cartesEnJeu.clear();
		carteChoisie = null;
		for (JPanel pan : panelsCartes) pan.removeAll();
	}
	
	public String askCarte(final ArrayList<String> cartes)
	{
		// vue.getPanelCentreCartes().print("C'est a vous de jouer !");
		
		// *** sablier => arrive a la butee on prend une carte aleatoirement
		sablier.demarrer((Integer) config.get("dureeChoixCarte"));
		Observer obs = new Observer()
		{
			public void update(Observable obs, Object param)
			{
				HashMap modifs = (HashMap) param;
				if ((Boolean) modifs.get("buteeAtteinte"))
				{
					synchronized (Controleur.this)
					{
						Controleur.this.carteChoisie = cartes.get(
										new Random().nextInt(cartes.size()));
						Controleur.this.notifyAll();
					}
				}
			}	
		};
		sablier.ajouterObservateur(obs);
		
		synchronized (this)
		{
			try {
				do {
					wait();
				} while (!cartes.contains(carteChoisie));
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// *** on remet au propre le sablier
		sablier.arreter();
		sablier.retirerObservateur(obs);
		
		return carteChoisie;
	}
	
	public void majJoueurGagneCarte(int numJoueur)
	{
		majJoueurGagneCarte(numJoueur, null);
	}

	public void majJoueurGagneCarte(int numJoueur, String carte)
	{
		ArrayList<String> cartes = new ArrayList<String>();
		cartes.add(carte);
		majJoueurGagneCartes(numJoueur, cartes);
	}
	
	public void setCarteChoisie(String carte)
	{
		carteChoisie = carte;
	}
	

	public void majAtoutPropose(String atout)
	{
		vueAtout.setAtout(new VueCarte(new ModeleCarte(atout), 
										(Dimension) config.get("tailleCarte")));
		vueAtout.getBtnPrendre().setEnabled(false);
		vueAtout.getBtnPasPrendre().setEnabled(false);
		vueAtout.setVisible(true);
	}
	
	public void majDernierPli(ArrayList<String> pli) {
		
		ArrayList<VueCarte> cartes = new ArrayList<VueCarte>();
		for(String valeur : pli) {
			cartes.add(new VueCarte(new ModeleCarte(valeur), 
										(Dimension) config.get("tailleCarte")));
		}
		vueDernierPli.setDernierPli(cartes);
		vueDernierPli.setVisible(true);
		
	}

	public boolean askPrendreAtout()
	{
		vueAtout.getBtnPrendre().setEnabled(true);
		vueAtout.getBtnPasPrendre().setEnabled(true);
		
		// *** sablier => arrive a la butee on considere qu'on ne prend pas l'atout
		sablier.demarrer((Integer) config.get("dureeChoixAtout"));
		Observer obs = new Observer()
		{
			public void update(Observable obs, Object param)
			{
				HashMap modifs = (HashMap) param;
				if ((Boolean) modifs.get("buteeAtteinte"))
				{
					synchronized (vueAtout)
					{
						vueAtout.setLastBtnPressed(vueAtout.getBtnPasPrendre());
						vueAtout.notifyAll();
					}
				}
			}	
		};
		sablier.ajouterObservateur(obs);
		
		// *** on attend le prochain clic sur le bouton "prendre" ou "ne pas prendre"
		synchronized (vueAtout) {
			try {
				do {
					vueAtout.wait();
				} while (vueAtout.getLastBtnPressed() == null);
			} catch (InterruptedException e) {}
		}
		
		// *** on remet au propre le sablier et les boutons
		sablier.arreter();
		sablier.retirerObservateur(obs);
		vueAtout.getBtnPrendre().setEnabled(false);
		vueAtout.getBtnPasPrendre().setEnabled(false);
		
		// *** si c'est un clic sur "ne pas prendre" => retourne false
		if (vueAtout.getLastBtnPressed() == vueAtout.getBtnPasPrendre())
			return false;
		// *** sinon retourne true
		return true;
	}

	public String askCouleurAtout()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void majJoueurJoueCarte(int numJoueur, String carte)
	{
		VueCarte corresp = null;
		for (String c : cartesEnJeu.keySet())
		{
			if (c.equals(carte))
			{
				corresp = cartesEnJeu.get(c);
				break;
			}
		}
		if (corresp != null)
		{
			panelsCartes[numJoueur].remove(corresp);
			corresp = corresp.clone();
		}
		else 
		{
			boolean horizontal = (numJoueur != num && numJoueur != numPartenaire);
			panelsCartes[numJoueur].remove(0);
			corresp = new VueCarte(new ModeleCarte(carte), horizontal, 
										(Dimension) config.get("tailleCarte"));
		}

		vue.getPanelCentreCartes().addCarte(corresp, numJoueur);

		vue.invalidate();
		vue.validate();
		vue.repaint();
	}

	public void majPli(int numPli)
	{
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		vue.getPanelCentreCartes().removeAll();
		vue.invalidate();
		vue.validate();
		vue.repaint();
	}

	public void majJoueurPrendAtout(final int numJoueur, final String atout)
	{
		vueAtout.setVisible(false);
		if (numJoueur != num)
		{
			(new Thread(){
				public void run(){
					JOptionPane.showMessageDialog(vue, "Le joueur " + 
							vue.getPanelCentreCartes().getNomJoueur(numJoueur) + 
															" prend l'atout !");
					
				}
			}).start();
		}
		vue.setAtout(atout, vue.getPanelCentreCartes().getNomJoueur(numJoueur));
		vue.repaint();
	}

	public String askNom()
	{
		return nom;
	}

	public void majJoueurGagneCartes(int numJoueur, int nbCartes)
	{
		ArrayList<String> cartes = new ArrayList<String>();
		for (int i = 0 ; i < nbCartes ; i++)
			cartes.add(null);
		majJoueurGagneCartes(numJoueur, cartes);
	}

	public void majJoueurGagneCartes(int numJoueur, ArrayList<String> cartes)
	{
		final JPanel panelCartesJoueur = panelsCartes[numJoueur];
		boolean horizontal = (numJoueur != num && numJoueur != numPartenaire);
		final OverlapLayout layout = (OverlapLayout) panelCartesJoueur.getLayout();
		
		for (String carte : cartes)
		{
			ModeleCarte modele = null;
			if (carte != null) modele = new ModeleCarte(carte);
			final VueCarte labelCarte = new VueCarte(modele, horizontal, 
										(Dimension) config.get("tailleCarte"));				
			
			if (modele != null)
			{
				labelCarte.addMouseListener(new MouseAdapter() {
					
					public void mouseEntered(MouseEvent e) {
						layout.addLayoutComponent(labelCarte, OverlapLayout.POP_UP);
						panelCartesJoueur.invalidate();
						panelCartesJoueur.validate();
					}
		
					public void mouseExited(MouseEvent e) {
						layout.addLayoutComponent(labelCarte, OverlapLayout.POP_DOWN);
						panelCartesJoueur.invalidate();
						panelCartesJoueur.validate();
					}
		
					public void mouseClicked(MouseEvent e) {
						ModeleCarte modele = labelCarte.getModele();
						if (modele == null) return;
						synchronized (Controleur.this) {
							Controleur.this.setCarteChoisie("" + modele);
							Controleur.this.notifyAll();
						}
					}
				});
			}
			
			panelsCartes[numJoueur].add(labelCarte);

			if (carte != null) cartesEnJeu.put(carte, labelCarte);
		}
		
		vue.invalidate();
		vue.validate();
		vue.repaint();
	}

	public void majVainqueurPli(final int numJoueur)
	{
		(new Thread(){
			public void run(){
		JOptionPane.showMessageDialog(vue, "Le vainqueur du pli est le " +
				"joueur " + vue.getPanelCentreCartes().getNomJoueur(numJoueur));
			}
		}).start();
	}

	public void majPlacement(int num)
	{
		this.num = num;
		numPartenaire = (num + 2) % 4;
		
		panelsCartes[(num + 0) % 4] = vue.getPanelSudCartes();
		panelsCartes[(num + 1) % 4] = vue.getPanelOuestCartes();
		panelsCartes[(num + 2) % 4] = vue.getPanelNordCartes();
		panelsCartes[(num + 3) % 4] = vue.getPanelEstCartes();
		
		vue.getPanelCentreCartes().setNumJoueurSud(num);
	}

	public void majNoms(ArrayList<String> nomsJoueurs)
	{
		// supprime tous les noms deja en place
		vue.getPanelCentreCartes().removeAllNomsJoueurs();
		
		// ajoute les nouveaux noms
		for (int i = 0 ; i < nomsJoueurs.size() ; i++)
			vue.getPanelCentreCartes().setNomJoueur(nomsJoueurs.get(i), i);
		
		vue.invalidate();
		vue.validate();
		vue.repaint();
	}

	public void majJoueurJoue(int numJoueur)
	{
		// TODO Auto-generated method stub
		
	}

	public void majJoueurGagnePoints(int numJoueur, int nbPoints)
	{
		if (numJoueur == num || numJoueur == numPartenaire)
		{
			int ptsActuels = vue.getPoints();
			ptsActuels += nbPoints;
			vue.setPoints(ptsActuels);
		}
		else
		{
			int ptsActuels = vue.getPointsAdverses();
			ptsActuels += nbPoints;
			vue.setPointsAdverses(ptsActuels);
		}
	}

	public void majVainqueursPartie(final int numJoueur1, final int numJoueur2)
	{
		(new Thread(){
			public void run(){
		JOptionPane.showMessageDialog(vue, "Les vainqueurs de la partie sont les " +
				"joueurs " + vue.getPanelCentreCartes().getNomJoueur(numJoueur1) + 
				" et " + vue.getPanelCentreCartes().getNomJoueur(numJoueur2));
			}
		}).start();
		init();
	}

}
