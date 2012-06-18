package belote.ihm;

import java.awt.Dimension;
import javax.swing.JLabel;
import util.ihm.RotatedImageIcon;
import util.ihm.Util;

public class VueCarte extends JLabel implements Cloneable
{	
	private ModeleCarte modele;
	
	private boolean horizontal;
	
	private Dimension taille;
	
	public VueCarte(Dimension taille)
	{
		this(null, taille);
	}
	
	public VueCarte(ModeleCarte modele, Dimension taille)
	{
		this(modele, false, taille);
	}
	
	public VueCarte(boolean horizontal, Dimension taille)
	{
		this(null, horizontal, taille);
	}
	
	public VueCarte(ModeleCarte modele, boolean horizontal, Dimension taille)
	{
		this.horizontal = horizontal;
		this.taille = taille;
		int angle = 0;
		if (horizontal) angle = 1;
		String image = "";
		if (modele == null) image = "res/cartes/dos.png";
		else {
			image = "res/cartes/" + 
			modele.getValeur().toString().toLowerCase() + "_" + 
			modele.getCouleur().toString().toLowerCase() + ".png";
			this.setToolTipText("" + modele);
		}
		setIcon(new RotatedImageIcon(Util.getImage(image, taille), angle));
		this.modele = modele;
		
	}

	public ModeleCarte getModele()
	{
		return modele;
	}
	
	public VueCarte clone()
	{
		return new VueCarte(modele, horizontal, taille);
	}

}
