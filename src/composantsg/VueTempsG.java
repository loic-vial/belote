package composantsg;
//
// IUT de Nice / Departement informatique / Module APO-Java
// Annee 2011_2012 - Composants generiques
//
// Classe ModeleTempsG : partie Modele (MVC) d'un compteur/decompteur de 
//                       temps
//
// Edition Draft    : externalisation d'un modele de donnees observable
//                    exploite par les demonstrateurs de la classe EspionG
//                    
//    + Version 0.0.0	: mise en oeuvre de l'interface Observer
//    + Version 0.1.0   : la vue devient compilable sans modele
//    + Version 0.2.0   : la vue n'inclut plus le cadre support (JFrame) 
//                        et n'en derive donc plus
//    + Version 0.3.0   : la vue derive d'un panneau standard (JPanel)
//    + Version 0.4.0   : exploitation d'un dictionnaire de configuration
//                        du composant
//
// Edition A        : le modele de donnees transmet un dictionnaire de
//                    modifications a chaque notification
//
//    + Version 1.0.0	: ajout methode publique setColors pour pouvoir
//                        modifier dynamiquement les couleurs de la vue
//    + Version 1.1.0   : modification de la signature du constructeur
//                        pour s'adapter a l'introduction du controleur
//   
// Auteur : A. Thuaire
//

import java.awt.*;
import javax.swing.*;
import java.util.*;

public class VueTempsG extends JPanel implements Observer {
private Container hamecon;
private JLabel label;

// ---                                               Constructeur normal  
	
   public VueTempsG (Object hamecon, HashMap config) 
                                     throws RuntimeException {
   
      super();
      
      // Controler la validite du premier parametre
      //
      if (hamecon == null) throw new RuntimeException ("-2.1");
      
      // Controler la valeur du second parametre
      //
      if (config == null) config= new HashMap();
      
      // Memoriser l'hamecon comme attribut
      //
      this.hamecon= (Container)hamecon;
      
      // Installer le gestionnaire de positionnement
      //
      LayoutManager layout= (LayoutManager)config.get("placement");
      if (layout == null) layout= new GridLayout(1,0);
      setLayout(layout);
      
      // Ajouter la vue au cadre support
      //
      this.hamecon.add(this);
      
      // Fixer la couleur de fond du panneau
      //
      Color couleurFond= (Color)config.get("arrierePlan");
      if (couleurFond == null) couleurFond= Color.yellow;
      setBackground(couleurFond);
      
      // Construire le champ de visualisation des donnees (JLabel)
      //
      String labelInitial= (String)config.get("labelInitial");
      if (labelInitial == null) labelInitial= "";
      label= new JLabel(labelInitial); 
      
      Font police= (Font)config.get("police");
      if (police == null) police= new Font("DS-digital", 
                                           Font.TYPE1_FONT, 60);
      label.setFont(police);
      label.setHorizontalAlignment(JLabel.CENTER);
      label.setVerticalAlignment(JLabel.CENTER);
      
      // Fixer la couleur du champ de visualisation
      //
      Color couleurTexte= (Color)config.get("avantPlan");
      if (couleurTexte == null) couleurFond= Color.black;
      label.setForeground(couleurTexte);

      // Ajouter le champ de visualisation au panneau
      //
      add(label);
   }
   
// ---                                                    Methode update

   public void update(Observable o, Object modifs) {
   	  
   	  // Extraire du second parametre la valeur du temps courant
   	  //
   	  String tempsCourant= (String)((HashMap)modifs).get("tempsCourant");
   	  
   	  // Rafraichir la valeur du temps courant
   	  //   	
      label.setText(tempsCourant);  
   }
   
// ---                                                 Methode setColors

   public void setColors (Color c1, Color c2) {
   	
      // Appliquer le premier parametre, apres controle de validite
      //
      if (c1 != null) setBackground(c1);
      
      // Appliquer le second parametre, apres controle de validite
      //
      if (c2 != null) label.setForeground(c2);
   }   
}
