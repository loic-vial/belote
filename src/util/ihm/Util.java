package util.ihm;

import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Quelques utilitaires graphiques
 * V 12.06.12
 */
public abstract class Util
{
	/**
	 * Retourne une Image avec son nom de fichier.
	 */
	public static Image getImage(String fileName)
	{
		try
		{
			return ImageIO.read(new File(fileName));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Idem que getImage, mais l'image est redimensionnee en fonction du 
	 * parametre "dim".
	 */
	public static Image getImage(String fileName, Dimension dim)
	{
		return getImage(fileName).getScaledInstance(
				dim.width, dim.height, Image.SCALE_SMOOTH);
	}
	
	/**
	 * Retourne un JLabel qui represente une image.
	 * Concretement, c'est un JLabel auquel on a fait un setIcon().
	 */
	public static JLabel getImageLabel(String fileName)
	{
		JLabel retour = new JLabel();
		retour.setIcon(new ImageIcon(getImage(fileName)));
		return retour;
	}
	
	/**
	 * Idem que getImageLabel, mais l'image est redimensionnee en fonction du
	 * parametre "dim". 
	 */
	public static JLabel getImageLabel(String fileName, Dimension dim)
	{
		JLabel retour = new JLabel();
		retour.setIcon(new ImageIcon(getImage(fileName, dim)));
		return retour;
	}

	/**
	 * Idem que getImageLabel, mais l'image subit une rotation d'angle "angle".
	 * Voir la classe RotatedImageIcon pour une description de l'angle.
	 */
	public static JLabel getImageLabel(String fileName, int angle)
	{
		JLabel retour = new JLabel();
		retour.setIcon(new RotatedImageIcon(getImage(fileName), angle));
		return retour;
	}
	
	/**
	 * Combine rotation + redimensionnement
	 */
	public static JLabel getImageLabel(String fileName, Dimension dim, int angle)
	{
		JLabel retour = new JLabel();
		retour.setIcon(new RotatedImageIcon(getImage(fileName, dim), angle));
		return retour;
	}
}
