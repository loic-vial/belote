package belote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import socket.ClientSocket;
import socket.ClientSocketListener;
import socket.ServerSocket;
import socket.ServerSocketListener;
import belote.ihm.Vue;
import belote.moteur.Belote;

public class Serveur implements ServerSocketListener, ClientSocketListener
{
	private class Client implements Vue
	{
		private ClientSocket socket;

		public Client(ClientSocket socket)
		{
			this.socket = socket;
		}

		public void init()
		{
			socket.sendRequest("init");
		}
		
		public String askCarte(ArrayList<String> main)
		{
			return (String) socket.sendRequest("askCarte", main);
		}

		public void majAtoutPropose(String atout)
		{
			socket.sendRequest("majAtoutPropose", atout);
		}
		
		public void majDernierPli(ArrayList<String> pli) 
		{
			socket.sendRequest("majDernierPli", pli);
		}

		public boolean askPrendreAtout()
		{
			return (Boolean) socket.sendRequest("askPrendreAtout");
		}

		public String askCouleurAtout()
		{
			return (String) socket.sendRequest("askCouleurAtout");
		}

		public void majJoueurGagneCarte(int joueur, String carte)
		{
			socket.sendRequest("majJoueurGagneCarte", joueur, carte);
		}

		public void majJoueurJoueCarte(int joueur, String carte)
		{
			socket.sendRequest("majJoueurJoueCarte", joueur, carte);			
		}

		public void majPli(int numPli)
		{
			socket.sendRequest("majPli", numPli);
		}

		public void majJoueurPrendAtout(int numJoueur, String atout)
		{
			socket.sendRequest("majJoueurPrendAtout", numJoueur, atout);
		}

		public String askNom()
		{
			return (String) socket.sendRequest("askNom");
		}

		public void majJoueurGagneCarte(int joueur)
		{
			socket.sendRequest("majJoueurGagneCarte", joueur);
		}

		public void majJoueurGagneCartes(int numJoueur, int nbCartes)
		{
			socket.sendRequest("majJoueurGagneCartes", numJoueur, nbCartes);
		}

		public void majJoueurGagneCartes(int numJoueur, ArrayList<String> cartes)
		{
			socket.sendRequest("majJoueurGagneCartes", numJoueur, cartes);			
		}

		public void majVainqueurPli(int numJoueur)
		{
			socket.sendRequest("majVainqueurPli", numJoueur);
		}

		public void majPlacement(int num)
		{
			socket.sendRequest("majPlacement", num);
		}

		public void majNoms(ArrayList<String> nomsJoueurs)
		{
			socket.sendRequest("majNoms", nomsJoueurs);
		}

		public void majJoueurJoue(int numJoueur)
		{
			socket.sendRequest("majJoueurJoue", numJoueur);			
		}

		public void majJoueurGagnePoints(int numJoueur, int nbPoints)
		{
			socket.sendRequest("majJoueurGagnePoints", numJoueur, nbPoints);						
		}

		public void majVainqueursPartie(int numJoueur1, int numJoueur2)
		{
			socket.sendRequest("majVainqueursPartie", numJoueur1, numJoueur2);		
		}

	}
	
	/**
	 * Liste des clients (en clef la socket de liaison)
	 */
	private HashMap<ClientSocket, Client> clients;
	
	/**
	 * Socket serveur
	 */
	private ServerSocket socket;
	
	/**
	 * Moteur de jeu
	 */
	private Belote belote;
		
	/**
	 * Constructeur.
	 */
	public Serveur(int port, Belote belote) throws IOException
	{
		this.belote = belote;
		clients = new HashMap<ClientSocket, Client>();
		socket = new ServerSocket(port);
		socket.addServerSocketListener(this);
		System.out.println("Serveur cree, en attente des clients");
	}

	public synchronized void clientConnected(ClientSocket client_socket)
	{
		int nbClients = clients.size();
		if (nbClients >= 4) return;
		client_socket.addClientSocketListener(this);
		Client client_vue = new Client(client_socket);
		clients.put(client_socket, client_vue);
		belote.addJoueur(client_vue);
		nbClients++;
		System.out.println("Nouveau client connecte au serveur (" + nbClients + ")");
		if (nbClients == 4) new ThreadBelote(belote).start();
	}

	public synchronized void socketClosed(ServerSocket server_socket)
	{
		System.out.println("Serveur ferme");
	}

	public void messageReceived(Object obj, ClientSocket client_socket)
	{
		System.out.println("Serveur a recu : " + obj);
	}

	public synchronized void socketClosed(ClientSocket client_socket)
	{
		belote.removeJoueur(clients.get(client_socket));
		clients.remove(client_socket);
		System.out.println("Client deconnecte du serveur (" + clients.size() + ")");
	}
	
	/**
	 * Thread interne qui va lancer le moteur de jeu
	 */
	private class ThreadBelote extends Thread
	{
		private Belote belote;
		
		public ThreadBelote(Belote belote)
		{
			this.belote = belote;
		}
		
		public void run()
		{
			while(true)
			{
				belote.start();
			}
		}
	}
}
