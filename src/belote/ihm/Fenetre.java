package belote.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import util.ihm.Util;
import util.ihm.VerticalFlowLayout;

public class Fenetre extends JFrame
{
	private JPanel coucheBackground;
	private JPanel coucheCartesVerticales;
	private JPanel coucheCartesHorizontales;
	private JPanel coucheCartesCentre;
	private JPanel panelSud;
	private JPanel panelOuest;
	private JPanel panelNord;
	private JPanel panelEst;
	private JPanel panelSudCartes;
	private JPanel panelOuestCartes;
	private PanelCentreCartes panelCentreCartes;
	private JPanel panelNordCartes;
	private JPanel panelEstCartes;
	private OverlapLayout layoutSudCartes;
	private OverlapLayout layoutEstCartes;
	private OverlapLayout layoutOuestCartes;
	private OverlapLayout layoutNordCartes;
	private JPanel containerVueSablier;
	private VueCarte cartePoints;
	private VueCarte cartePointsAdverses;
	private JLabel atout;

	public Fenetre(HashMap config)
	{
		Dimension taille = (Dimension) config.get("tailleFenetre");
		String imageFond = (String) config.get("imageFond");
		Dimension tailleCarte = (Dimension) config.get("tailleCarte");
		Font fontCompteurPoints = (Font) config.get("fontCompteurPoints");
		Color couleurCompteurPoints = (Color) config.get("couleurCompteurPoints");
		Font fontAtout = (Font) config.get("fontAtout");
		Color couleurAtout = (Color) config.get("couleurAtout");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Belote");
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.setBackground(Color.red);
		setContentPane(contentPane);
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setSize(taille);
		layeredPane.setPreferredSize(taille);
		
		// *** Couche background
		coucheBackground = new JPanel(new GridLayout(1, 0));
		coucheBackground.setSize(taille);
		coucheBackground.setPreferredSize(taille);
		coucheBackground.setBackground(Color.decode("#24A36E"));
		coucheBackground.add(Util.getImageLabel(imageFond, taille));
		layeredPane.add(coucheBackground, new Integer(5));

		// *** Couche cartes au centre
		coucheCartesCentre = new JPanel(new BorderLayout());
		coucheCartesCentre.setSize(taille);
		coucheCartesCentre.setPreferredSize(taille);
		coucheCartesCentre.setOpaque(false);
		
		Dimension marginH = new Dimension(0, (int)(taille.height / 4.5));
		Dimension marginV = new Dimension((int)(taille.width / 4.5), 0);

			// marge haut
			JPanel panelMarginTop = new JPanel(new BorderLayout());
			panelMarginTop.setOpaque(false);
			panelMarginTop.setSize(marginH);
			panelMarginTop.setPreferredSize(marginH);
					
				// gauche : atout
				JPanel panelAtout = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.TOP));
				panelAtout.setOpaque(false);
					atout = new JLabel();
					atout.setText("Pas d'atout");
					atout.setFont(fontAtout);
					atout.setForeground(couleurAtout);
				panelAtout.add(atout);
			panelMarginTop.add(panelAtout, BorderLayout.WEST);
			
				// droite : compteur de points adverses
				JPanel panelPointsAdverses = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.TOP));
				panelPointsAdverses.setOpaque(false);
					cartePointsAdverses = new VueCarte(true, tailleCarte);
					cartePointsAdverses.setText("0");
					cartePointsAdverses.setFont(fontCompteurPoints);
					cartePointsAdverses.setForeground(couleurCompteurPoints);
					cartePointsAdverses.setHorizontalTextPosition(JLabel.LEFT);
				panelPointsAdverses.add(cartePointsAdverses);
			panelMarginTop.add(panelPointsAdverses, BorderLayout.EAST);
		
		coucheCartesCentre.add(panelMarginTop, BorderLayout.NORTH);
			
			// marge gauche
			JPanel panelMarginLeft = new JPanel();
			panelMarginLeft.setOpaque(false);
			panelMarginLeft.setSize(marginV);
			panelMarginLeft.setPreferredSize(marginV);
		coucheCartesCentre.add(panelMarginLeft, BorderLayout.WEST);

			// marge bas
			JPanel panelMarginBottom = new JPanel(new BorderLayout());
			panelMarginBottom.setOpaque(false);
			panelMarginBottom.setSize(marginH);
			panelMarginBottom.setPreferredSize(marginH);
				
				// gauche : compteur de points
				JPanel panelPoints = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.BOTTOM));
				panelPoints.setOpaque(false);
					cartePoints = new VueCarte(tailleCarte);
					cartePoints.setText("0");
					cartePoints.setForeground(couleurCompteurPoints);
					cartePoints.setFont(fontCompteurPoints);
				panelPoints.add(cartePoints);
			panelMarginBottom.add(panelPoints, BorderLayout.WEST);
			
				// droite : sablier TempsG
				containerVueSablier = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.BOTTOM));
				containerVueSablier.setOpaque(false);
			panelMarginBottom.add(containerVueSablier, BorderLayout.EAST);
				
		coucheCartesCentre.add(panelMarginBottom, BorderLayout.SOUTH);
		
			// marge droite
			JPanel panelMarginRight = new JPanel(new BorderLayout());
			panelMarginRight.setOpaque(false);
			panelMarginRight.setSize(marginV);
			panelMarginRight.setPreferredSize(marginV);
		coucheCartesCentre.add(panelMarginRight, BorderLayout.EAST);
		
			panelCentreCartes = new PanelCentreCartes(config);
		coucheCartesCentre.add(panelCentreCartes, BorderLayout.CENTER);
		
		layeredPane.add(coucheCartesCentre, new Integer(10));
		
		// *** Couche cartes verticales
		coucheCartesVerticales = new JPanel(new BorderLayout());
		coucheCartesVerticales.setSize(taille);
		coucheCartesVerticales.setPreferredSize(taille);
		coucheCartesVerticales.setOpaque(false);
		
			// panel sud
			panelSud = new JPanel(new BorderLayout());
			panelSud.setOpaque(false);

				// centre : cartes du joueur
				JPanel panelSudCartesEnglobant = new JPanel(new FlowLayout(FlowLayout.CENTER));
				panelSudCartesEnglobant.setOpaque(false);
					layoutSudCartes = new OverlapLayout(new Point(50, 0));
					layoutSudCartes.setPopupInsets(new Insets(20, 0, 0, 0));
					panelSudCartes = new JPanel(layoutSudCartes);
					panelSudCartes.setOpaque(false);
				panelSudCartesEnglobant.add(panelSudCartes);
			panelSud.add(panelSudCartesEnglobant, BorderLayout.CENTER);
			
		coucheCartesVerticales.add(panelSud, BorderLayout.SOUTH);

			// panel nord
			panelNord = new JPanel(new FlowLayout(FlowLayout.CENTER));
			panelNord.setOpaque(false);
			
			layoutNordCartes = new OverlapLayout(new Point(50, 0));
			layoutNordCartes.setPopupInsets(new Insets(0, 0, 20, 0));
			panelNordCartes = new JPanel(layoutNordCartes);
			panelNordCartes.setOpaque(false);
			panelNord.add(panelNordCartes);
		
		coucheCartesVerticales.add(panelNord, BorderLayout.NORTH);

		layeredPane.add(coucheCartesVerticales, new Integer(20));
		
		// *** Couche cartes horizontales
		coucheCartesHorizontales = new JPanel(new BorderLayout());
		coucheCartesHorizontales.setSize(taille);
		coucheCartesHorizontales.setPreferredSize(taille);
		coucheCartesHorizontales.setOpaque(false);
		
			// panel ouest
			panelOuest = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.CENTER));
			panelOuest.setOpaque(false);
			
			layoutOuestCartes = new OverlapLayout(new Point(0, 50));
			layoutOuestCartes.setPopupInsets(new Insets(0, 0, 0, 20));
			panelOuestCartes = new JPanel(layoutOuestCartes);
			panelOuestCartes.setOpaque(false);
			panelOuest.add(panelOuestCartes);
			
		coucheCartesHorizontales.add(panelOuest, BorderLayout.WEST);

			// panel est
			panelEst = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.CENTER));
			panelEst.setOpaque(false);
			
			layoutEstCartes = new OverlapLayout(new Point(0, 50));
			layoutEstCartes.setPopupInsets(new Insets(0, 20, 0, 0));
			panelEstCartes = new JPanel(layoutEstCartes);
			panelEstCartes.setOpaque(false);
			panelEst.add(panelEstCartes);

		coucheCartesHorizontales.add(panelEst, BorderLayout.EAST);

		layeredPane.add(coucheCartesHorizontales, new Integer(30));
		
		contentPane.add(layeredPane, BorderLayout.CENTER);
		
		pack();
		setResizable(false);
	}

	public JPanel getPanelSudCartes()
	{
		return panelSudCartes;
	}

	public JPanel getPanelOuestCartes()
	{
		return panelOuestCartes;
	}

	public PanelCentreCartes getPanelCentreCartes()
	{
		return panelCentreCartes;
	}

	public JPanel getPanelNordCartes()
	{
		return panelNordCartes;
	}

	public JPanel getPanelEstCartes()
	{
		return panelEstCartes;
	}

	public OverlapLayout getLayoutSudCartes()
	{
		return layoutSudCartes;
	}

	public OverlapLayout getLayoutEstCartes()
	{
		return layoutEstCartes;
	}

	public OverlapLayout getLayoutOuestCartes()
	{
		return layoutOuestCartes;
	}

	public OverlapLayout getLayoutNordCartes()
	{
		return layoutNordCartes;
	}

	public JPanel getContainerVueSablier()
	{
		return containerVueSablier;
	}

	public int getPoints()
	{
		return Integer.valueOf(cartePoints.getText());
	}

	public int getPointsAdverses()
	{
		return Integer.valueOf(cartePointsAdverses.getText());
	}

	public void setPoints(int val)
	{
		cartePoints.setText("" + val);
	}
	
	public void setPointsAdverses(int val)
	{
		cartePointsAdverses.setText("" + val);
	}
	
	public void setAtout(String couleur, String joueur){
		atout.setText(joueur + " a pris " + couleur);
	}
}
