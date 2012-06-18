package belote.ihm;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;

/**
 * Fenetre dans laquelle on va proposer un atout au joueur
 */
public class FenetreAtout extends JFrame
{
	/**
	 * Panneau autour de la carte "atout"s
	 */
	private JPanel panelAtout;
	
	/**
	 * Carte "atout"
	 */
	private VueCarte atout;
	
	/**
	 * Bouton "Prendre"
	 */
	private JButton btnPrendre;
	
	/**
	 * Bouton "Ne pas prendre"
	 */
	private JButton btnPasPrendre;
	
	/**
	 * Reference vers le dernier bouton presse
	 */
	private JButton lastBtnPressed;
	
	/**
	 * Constructeur
	 */
	public FenetreAtout(JFrame owner)
	{
		//super(owner, true);
		super();
		JPanel contentPane = new JPanel(new BorderLayout());
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		JLabel lblCarteProposeeEn = new JLabel("Carte proposee en tant qu'atout");
		panel.add(lblCarteProposeeEn);
		
		panelAtout = new JPanel();
		contentPane.add(panelAtout, BorderLayout.CENTER);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.SOUTH);
		
		btnPrendre = new JButton("Je prends");
		btnPrendre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
				synchronized (FenetreAtout.this) {
					lastBtnPressed = btnPrendre;
					FenetreAtout.this.notifyAll();
				}
			}
		});
		panel_2.add(btnPrendre);
		
		btnPasPrendre = new JButton("Je passe");
		btnPasPrendre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
				synchronized (FenetreAtout.this) {
					lastBtnPressed = btnPasPrendre;
					FenetreAtout.this.notifyAll();
				}
			}
		});
		panel_2.add(btnPasPrendre);
		
		lastBtnPressed = null;
		atout = null;
		pack();
	}
	
	public void setAtout(VueCarte atout)
	{
		if (this.atout != null) panelAtout.remove(this.atout);
		this.atout = atout.clone();
		panelAtout.add(this.atout);
		pack();
	}
	
	public JButton getBtnPrendre()
	{
		return btnPrendre;
	}
	
	public JButton getBtnPasPrendre()
	{
		return btnPasPrendre;
	}
	
	public JButton getLastBtnPressed()
	{
		return lastBtnPressed;
	}

	public void setLastBtnPressed(JButton btn)
	{
		lastBtnPressed = btn;
	}
}
