package util.ihm;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * ImageIcon subissant une rotation.
 * 
 * Les rotations possibles sont : 
 * 
 *   0 : aucune rotation.
 *   1 :  90 degres (sens des aiguilles).
 *   2 : 180 degres.
 *   3 : 270 degres (sens des aiguilles).
 *   
 */
public class RotatedImageIcon extends ImageIcon
{
	private int angle;

	public RotatedImageIcon(Image image, int angle)
	{
		super(image);
		angle = Math.abs(angle);
		this.angle = angle % 4;
	}
	
    public int getIconWidth()
    {
		if (angle % 2 == 0)
			return super.getIconWidth();
		else
			return super.getIconHeight();
    }

    public int getIconHeight()
    {
		if (angle % 2 == 0)
			return super.getIconHeight();
		else
			return super.getIconWidth();
    }

    public void paintIcon(Component c, Graphics g, int x, int y)
    {
    	if (angle == 0)
    	{
    		super.paintIcon(c, g, x, y);
    		return;
    	}
    	
		Graphics2D g2 = (Graphics2D)g.create();

		int cWidth = super.getIconWidth() / 2;
		int cHeight = super.getIconHeight() / 2;
		int xAdjustment = (super.getIconWidth() % 2) == 0 ? 0 : -1;
		int yAdjustment = (super.getIconHeight() % 2) == 0 ? 0 : -1;

    	if (angle == 1)
    	{
			g2.translate(x + cHeight, y + cWidth);
			g2.rotate(Math.toRadians(90));
			super.paintIcon(c, g2, -cWidth, yAdjustment - cHeight);
    	}
    	
    	else if (angle == 2)
    	{
			g2.translate(x + cWidth, y + cHeight);
			g2.rotate(Math.toRadians(180));
			super.paintIcon(c, g2, xAdjustment - cWidth, yAdjustment - cHeight);
    	}
    	
    	else if (angle == 3)
    	{
			g2.translate(x + cHeight, y + cWidth);
			g2.rotate(Math.toRadians(-90));
			super.paintIcon(c, g2, xAdjustment - cWidth, -cHeight);
    	}

    }

/*
	final static double DEGREE_90 = 90.0 * Math.PI / 180.0;

	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		double rotatedAngle = angle * 90;
		
		// convert rotatedAngle to a value from 0 to 360
		double originalAngle = rotatedAngle % 360;
		if (rotatedAngle != 0 && originalAngle == 0)
		{
			originalAngle = 360.0;
		}

		// convert originalAngle to a value from 0 to 90
		double angle = originalAngle % 90;
		if (originalAngle != 0.0 && angle == 0.0)
		{
			angle = 90.0;
		}

		double radian = Math.toRadians(angle);

		int iw = super.getIconWidth();
		int ih = super.getIconHeight();
		int w;
		int h;

		if ((originalAngle >= 0 && originalAngle <= 90) || (originalAngle > 180 && originalAngle <= 270))
		{
			w = (int) (iw * Math.sin(DEGREE_90 - radian) + ih
					* Math.sin(radian));
			h = (int) (iw * Math.sin(radian) + ih
					* Math.sin(DEGREE_90 - radian));
		}
		else
		{
			w = (int) (ih * Math.sin(DEGREE_90 - radian) + iw
					* Math.sin(radian));
			h = (int) (ih * Math.sin(radian) + iw
					* Math.sin(DEGREE_90 - radian));
		}

		Graphics2D g2d = (Graphics2D) g.create();

		// calculate the center of the icon.
		int cx = iw / 2;
		int cy = ih / 2;

		// move the graphics center point to the center of the icon.
		g2d.translate(w / 2, h / 2);

		// rotate the graphics about the center point of the icon
		g2d.rotate(Math.toRadians(originalAngle));

		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		super.paintIcon(c, g2d, -cx, -cy);

		g2d.dispose();
	}
*/
/*   
    public static void main(String[] args) throws IOException
    {
    	JFrame frame = new JFrame();
    	JPanel panel = new JPanel();
    	JLabel label = new JLabel();
    	Image image = ImageIO.read(new File("res/cartes/dos_v.png"));
    	RotatedImageIcon icone = new RotatedImageIcon(image, 0);
    	
    	label.setIcon(icone);
    	panel.add(label);
    	frame.add(panel);
    	frame.pack();
    	frame.setVisible(true);
    }
*/
}
