package composantsg;
//
// IUT de Nice / Departement informatique / Module APO-Java
// Annee 2011_2012 - Composants generiques
//
// Classe ControleurTempsG : partie Controleur (MVC) d'un compteur/
//                           decompteur de temps
//
// Edition A        : controle et pilotage externes
//
//    + Version 1.0.0   : report des besoins des demonstrateurs V 2.X.0
//    + Version 1.1.0   : introduction des methodes publiques demarrer, 
//                        attendre et arreter
//    + Version 1.2.0   : introduction nouvelle signature de demarrer
//
// Edition B        : identification et choix de la vue par parametre de 
//                    configuration et reflexivite
//
//    + Version 2.0.0   : organisation du dictionnaire de configuration a
//                        deux niveaux (fonctionnel et vue) pour avoir
//                        la possibilite de ne pas creer de vue 
//    + Version 2.1.0   : creation de la vue a partir du nom de classe
//                        fourni par parametre de configuration
//    + Version 2.2.0   : introduction methodes publiques presenceVue 
//                        et ajouterObservateur                      
// 
// Auteur : A. Thuaire
//

import java.util.*;
import java.lang.reflect.*;

public class ControleurTempsG {
private final HashMap config;
private final Observer vue;
private ModeleTempsG modele;
private String mode;

// ---                                             Constructeur normal

   public ControleurTempsG(Object hamecon, 
                           HashMap config) {
   	
      // Controler la valeur du second parametre
      //
      if (config == null) config= new HashMap();
      
      // Memoriser la configuration courante en attribut
      //
      this.config= config;
      
      // Extraire et memoriser le mode de fonctionnement
      //
      mode= (String)config.get("mode");
      if (mode == null) mode= "horloge";
      
      // Controler la validite du mode cible
      //
      boolean p1= mode.equals("horloge");
      boolean p2= mode.equals("chronometre");
      boolean p3= mode.equals("sablier");
      
      if (!p1 && !p2 && !p3) throw new RuntimeException ("-3.1");
      
      // Creer une vue configuree 
      //
      vue= construireVue(hamecon, config); 
   }
   
// ---                                           Methode getTempsCourant

   public String getTempsCourant() {return modele.getTempsCourant();}
   
// ---                                                   Methode arreter

   public void arreter() {modele.resetStatus();}
   
// ---                                                  Methode setButee

   public void setButee(String cible) {modele.setButee(cible);}
   
// ---                                                  Methode attendre

   public static void attendre(int duree) {
   	
      // Controler la validite du parametre
      //
      if (duree < 0) throw new RuntimeException ("-3.4");
      
      // Suspendre le thread courant
      //
      try {Thread.sleep(duree);} 
      catch (InterruptedException e) {}	
   }       
   
// ---                                                  Methode demarrer

   public void demarrer() {
   	
   	  // Creer le modele configure pour les modes autres que "sablier"
   	  //
   	  if (!mode.equals("sablier")) modele= new ModeleTempsG(config);
   	  
   	  else {
   	  	
   	     // Extraire du dictionnaire de configuration le temps total
   	     // du sablier
         //
         Integer tempsTotal= (Integer)config.get("tempsTotal");
         if (tempsTotal == null) tempsTotal= new Integer(0);
         
         // Controler la validite de ce parametre
         //
         if (tempsTotal.intValue() < 0) throw new RuntimeException ("-3.3");
         
         // Creer le modele configure d'un sablier
         //
         modele = new ModeleTempsG(config, tempsTotal.intValue()); 
   	  }
      
      // Ajouter la vue comme observateur des modifications du 
      // modele de donnees
      //
      modele.addObserver(vue);
      
      // Lancer le thread sous jacent
      //
      new Thread(modele).start();
   }

   public void demarrer(int tempsTotal) {
   	
      // Controler la validite du parametre
      //
      if (tempsTotal < 0) throw new RuntimeException ("-3.3");
      
      // Creer un modele configure d'un sablier
      //
      modele= new ModeleTempsG(config, tempsTotal);
      
      // Ajouter la vue comme observateur des modifications 
      // du modele de donnees
      //
      if (vue != null) modele.addObserver(vue);
      
      // Lancer le thread sous jacent
      //
      new Thread(modele).start();
   }
   
// ---                                           Methode construireVue

   private Observer construireVue(Object hamecon,
                                   HashMap config) {
                                   	
      // Extraire l'identificateur de la classe de la vue
      //
      String classeVue= (String)config.get("classeVue");
      if (classeVue == null) return null;
      
      // Extraire le dictionnaire de configuration de la vue
      //
      HashMap configVue= (HashMap)config.get("configVue");
      
      // Controler la description du chemin d'acces et creer le cas
      // echeant une vue par defaut
      //
      if (configVue == null) return new VueTempsG(hamecon, null);

      // Creer la vue cible par usage de la reflexivite
      //
      try{
      Class cible= Class.forName(classeVue);
      Constructor[] constructeurs= cible.getDeclaredConstructors();
      
      return (Observer)constructeurs[0].newInstance(hamecon, configVue);
      } catch (Exception e) {
    	  throw new RuntimeException(e);
      }
   }
   
// ---                                              Methode presenceVue
    
   public boolean presenceVue() {return vue != null;}
   
// ---                                        Methode ajouterObservateur
    
   public boolean ajouterObservateur(Observer obs) {
   	
      // Controler la validite du parametre
      //
      if (obs == null) return false;
      
      // Ajouter le parametre comme observateur du modele
      //
      modele.addObserver(obs);
      
      return true;
   }    
   
   public void retirerObservateur(Observer obs)
   {
	   if (obs != null) modele.deleteObserver(obs);
   }
}
