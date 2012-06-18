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
//    + Version 0.0.0	: derivation de la classe Observable
//    + Version 0.1.0   : l'heure courante est notifiee a l'observateur
//    + Version 0.2.0   : exploitation d'un fichier de configuration du
//                        composant TempsG
//    + Version 0.3.0   : ajout d'un attribut de controle d'execution du
//                        thread et accesseur de modification associe
//
// Edition A        : ajout des moyens de pilotage externe du thread sous
//                    jacent
//
//    + Version 1.0.0   : ajout d'un attribut de controle d'execution du
//                        thread et accesseur de modification associe
//    + Version 1.1.0   : ajout d'une butee cible eventuelle pour fin  
//                        d'execution du thread, definie par parametre 
//                        de configuration
//    + Version 1.2.0   : notifications simultanees de plusieurs modifs.
//                        eventuelles de donnees
//
// Edition B        : ajout de plusieurs modes de fonctionnement du modele
//                    (choix par parametre de configuration)
//
//    + Version 2.0.0	: reorganisation du code pour preparer l'ajout des
//                        modes de fonctionnement 
//    + Version 2.1.0	: introduction du mode "chronometre"
//    + Version 2.2.0   : introduction du mode "sablier"
//    + Version 2.3.0   : ajout methode privee notifier
//                        + ajout notification aux observateurs avant la 
//                        premiere attente (correction du decalage initial
//                        de visualisation)              
//
// Auteur : A. Thuaire
//

import java.util.*;

public class ModeleTempsG extends Observable implements Runnable {
private String tempsCourant= "0 : 00 : 00";
private String separateur;
private boolean statusThread= true;
private Integer increment;
private String mode;
private String buteeCible;

// ---                                      Premier constructeur normal

   public ModeleTempsG(HashMap config) throws RuntimeException {
   	
      // Controler la valeur du parametre
      //
      if (config == null) config= new HashMap();
      
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
      
      // Extraire et memoriser le separateur de champs
      //
      separateur= (String)config.get("separateur");
      if (separateur == null) separateur= " : ";
     
      // Extraire et memoriser l'unite de temps
      //
      increment= (Integer)config.get("increment");
      if (increment == null) increment= new Integer(1);
      
      // Extraire et memoriser la butee cible eventuelle
      //
      buteeCible= (String)config.get("butee");  
   }
   
// ---                                       Second constructeur normal

   public ModeleTempsG(HashMap config, int tempsTotal) 
                                       throws RuntimeException {
      
      // Invoquer le premier constructeur
      //
      this (config);
      
      // Controler la validite du second parametre
      //
      if (tempsTotal < 0) throw new RuntimeException ("-2.2");
      
      // Decomposer le second parametre en heures, minutes et secondes
      //
      int heures= 0, minutes=0, secondes= 0;
      
      secondes= tempsTotal;
      
      if (secondes >= 60) { 	
         minutes= secondes / 60;
         secondes -= minutes*60; 
      }
      
      if (minutes >= 60) {
         heures= minutes / 60;
         minutes -= heures*60; 
      }
      
      // Memoriser le resultat dans l'attribut "tempsCourant"
      //
      tempsCourant= timeToString(separateur, heures, minutes, secondes);                               
   }
   
// ---                                           Methode getTempsCourant

   public String getTempsCourant() {return tempsCourant;}
   
// ---                                               Methode resetStatus

   public void resetStatus() {statusThread= false;}
   
// ---                                                  Methode setButee

   public void setButee(String cible) {buteeCible= cible;}    

// ---                                                       Methode run
	
   public void run() {
   boolean buteeAtteinte;
   	
   	  // Notifier l'etat initial aux observateurs
   	  //
   	  buteeAtteinte= notifier();
   	  
   	  // Suspendre le thread courant de la duree specifiee par le
      // parametre de configuration "increment"
      //
      if (!buteeAtteinte) {
         try {Thread.sleep(increment.intValue()*1000);} 
         catch (InterruptedException e) {}
      }
   
      while(statusThread) {
      	
         // Acquerir et controler le mode de fonctionnement du modele
         //
         if (mode.equals("horloge")) 
         
            // Executer le mode "horloge"
            //
            executerHorloge();

         else if (mode.equals("chronometre"))
         
                 // Executer le mode "chronometre"
                 //
                 executerChronometre();
                 
              else 
                 // Executer le mode "sablier"
                 //
                 executerSablier();
         
         // Notifier l'etat courant aux observateurs
   	     //
   	     buteeAtteinte= notifier();
         
         // Stopper la boucle si la butee cible a ete atteinte
         //
         if (buteeAtteinte) break;
	     
         // Suspendre le thread courant de la duree specifiee par le
         // parametre de configuration "increment"
         //
         try {Thread.sleep(increment.intValue()*1000);} 
         catch (InterruptedException e) {}
      }
   }

// ---                                            Methode timeToString

    private static String timeToString (String separateur,
                                        int heures, 
                                        int minutes,
                                        int secondes) {
    String resultat= "";
                                      	
       resultat=  heures + separateur;	
         
       if (minutes < 10) resultat += "0" + minutes;
       else resultat += minutes;
         
       resultat += separateur;
         
       if (secondes < 10) resultat += "0" + secondes;
       else resultat += secondes;
      
       return resultat;	
   }
   
// ---                                             Methode stringToTime

   private static int stringToTime (String separateur,
                                    String label) {
   int heures, minutes, secondes;
                                      	
      // Fractionner le label en trois champs suivant le separateur
      // et convertir en numerique
      //
      String[] champsLabel= label.split(" : ");
      
      heures  = new Integer(champsLabel[0]).intValue();
      minutes = new Integer(champsLabel[1]).intValue();
      secondes= new Integer(champsLabel[2]).intValue();
      
      // Restituer le resultat
      //
      return 3600*heures + 60*minutes + secondes;	
   }
      
// ---                                            Methode controleButee

   private static boolean controleButee (String separateur,
                                         String temps,
                                         String butee,
                                         int seuil) {
                                         	
      // Controler la validite du second parametre
      //
      if (butee == null) return false;
      
      // Convertir le label du temps en numerique
      //
      int op1= stringToTime(separateur, temps);
      
      // Convertir le label de la butee en numerique
      //
      int op2= stringToTime(separateur, butee);
      
      // Restituer le resultat
      //
      return Math.abs(op1-op2) < seuil;                                   	
   }   
   
// ---                                         Methode executerHorloge

   private void executerHorloge() {
   Calendar calendrier; 
   int heures= 0, minutes=0, secondes= 0;
   
      // Acquerir et fractionner la description du temps
      //
      calendrier = Calendar.getInstance();	
         
      heures= calendrier.get(Calendar.HOUR_OF_DAY);
      minutes= calendrier.get(Calendar.MINUTE);
      secondes= calendrier.get(Calendar.SECOND);
         
      // Mettre a jour le temps courant
      //
      tempsCourant= timeToString(separateur, heures, minutes, secondes);
   } 
   
// ---                                     Methode executerChronometre

   private void executerChronometre() {
   int heures= 0, minutes=0, secondes= 0;
   
      // Fractionner le label du temps courant en trois champs et
      // convertir en numerique
      //
      String[] champsTempsCourant= tempsCourant.split(" : ");
      
      heures  = new Integer(champsTempsCourant[0]).intValue();
      minutes = new Integer(champsTempsCourant[1]).intValue();
      secondes= new Integer(champsTempsCourant[2]).intValue();
      
      // Determiner le rafraichissement du champ de droite (secondes) 
      // du label tempsCourant
      //
      int deltaMinutes = 0;
      
      secondes += increment;
      
      if (secondes >= 60) { 	
         deltaMinutes= secondes / 60;
         secondes -= deltaMinutes*60; 
      }
      
      // Determiner le rafraichissement du champ du centre (minutes) 
      // du label tempsCourant
      //
      int deltaHeures = 0;
      
      minutes += deltaMinutes;
      if (minutes >= 60) {
      	 deltaHeures= minutes / 60;
         minutes -= deltaHeures*60; 
      }
      
      // Determiner le rafraichissement du champ de gauche (heures) 
      // du label tempsCourant
      //
      heures += deltaHeures;
      
      // Mettre a jour le temps courant
      //
      tempsCourant= timeToString(separateur, heures, minutes, secondes);
   } 
   
// ---                                    Methode executerSablier

   private void executerSablier() {
   int heures= 0, minutes=0, secondes= 0;
   
      // Fractionner le label du temps courant en trois champs et
      // convertir en numerique
      //
      String[] champsTempsCourant= tempsCourant.split(" : ");
      
      heures  = new Integer(champsTempsCourant[0]).intValue();
      minutes = new Integer(champsTempsCourant[1]).intValue();
      secondes= new Integer(champsTempsCourant[2]).intValue();
      
      // Traiter le cas de l'arret du sablier pour temps disponible
      // epuise
      //
      if (heures == 0 && minutes == 0 && secondes == 0) resetStatus();
      
      // Determiner le rafraichissement du champ de droite (secondes) 
      // du label tempsCourant
      //
      int deltaMinutes = 0;
      
      secondes -= increment;
      
      if (secondes < 0) { 	
         deltaMinutes= 1;
         secondes= 59; 
      }
      
      // Determiner le rafraichissement du champ du centre (minutes) 
      // du label tempsCourant
      //
      int deltaHeures = 0;
      
      minutes -= deltaMinutes;
      if (minutes < 0) {
         deltaHeures= 1;
         minutes= 59; 
      }
      
      // Determiner le rafraichissement du champ de gauche (heures) 
      // du label tempsCourant
      //
      heures -= deltaHeures; 
   
      // Mettre a jour le temps courant
      //
      tempsCourant= timeToString(separateur, heures, minutes, secondes);
   } 
   
// ---                                                  Methode notifier

   private boolean notifier () {
   
      // Etablir si la butee eventuelle a ete atteinte
      //
      boolean buteeAtteinte= controleButee(separateur, tempsCourant, 
                                           buteeCible, increment);
                                      
      // Construire le dictionnaire des modifications
      //
      HashMap modifs= new HashMap();
      modifs.put("tempsCourant", tempsCourant);
      modifs.put("buteeAtteinte", new Boolean(buteeAtteinte));
             
      // Fournir l'etat courant aux observateurs 
      //              
      setChanged();
      notifyObservers(modifs);
      
      // Mettre a jour le status du thread courant
      //
      if (buteeAtteinte) resetStatus();
      
      // Restituer le resultat
      //
      return buteeAtteinte;
   }    
}
