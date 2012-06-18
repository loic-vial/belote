package socket;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * Socket serveur permettant la connexion de clients.
 * Version 12.06.04 
 */
public class ServerSocket
{
	/**
	 * Socket java support
	 */
	private java.net.ServerSocket ssock;
	
	/**
	 * Liste des ecouteurs de la socket
	 */
	private ArrayList<ServerSocketListener> listeners;
	
	/**
	 * Booleen de controle du thread interne de gestion des connexions
	 */
	private boolean stop;
	
	/**
	 * Thread interne gerant les connexions
	 */
	private InternalThread thread;
	
	/**
	 * Constructeur.
	 * @param port : Le port d'ecoute.
	 */
	public ServerSocket(int port) throws IOException
	{
		ssock = new java.net.ServerSocket(port);
		listeners = new ArrayList<ServerSocketListener>();
		stop = false;
		thread = new InternalThread();
		thread.start();
	}
	
	/**
	 * Ajoute un ecouteur a la socket.
	 */
	public void addServerSocketListener(ServerSocketListener listener)
	{
		if (listener == null) throw new NullPointerException();
		listeners.add(listener);
	}
	
	/**
	 * Arrete le serveur.
	 */
	public synchronized void stop()
	{
		if (!stop)
		{
			stop = true;
			try {
				ssock.close();
			} catch (IOException e) {}
			for (ServerSocketListener nl : listeners)
				new SocketClosedThread(nl).start();
		}
	}
	
	/**
	 * @return le port d'ecoute.
	 */
	public int getPort()
	{
		return ssock.getLocalPort();
	}
	
	/**
	 * Classe interne permettant la gestion des connexions
	 */
	private class InternalThread extends Thread
	{
		/**
		 * Constructeur
		 */
		public InternalThread()
		{
			super("ServerSocket");
		}
		
		/**
		 * Lance le thread
		 */
		public void run()
		{
			ClientSocket client = null;
			while (!stop)
			{
				try
				{
					client = new ClientSocket(ssock.accept());
					for (ServerSocketListener nl : listeners)
						new ClientConnectedThread(nl, client).start();
				}
				// socket serveur fermee (seulement ?)
				catch (SocketException e)
				{
					ServerSocket.this.stop();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Thread gerant l'envoi d'un message a un ecouteur lorsque la socket est fermee
	 */
	private class SocketClosedThread extends Thread
	{
		/**
		 * L'ecouteur
		 */
		private ServerSocketListener listener;

		/**
		 * Constructeur
		 */
		public SocketClosedThread(ServerSocketListener listener)
		{
			this.listener = listener;
		}
		
		/**
		 * Lance le thread
		 */
		public void run()
		{
			listener.socketClosed(ServerSocket.this);
		}
	}
	
	/**
	 * Thread gerant l'envoi d'un message a un ecouteur lors de la connexion d'un client
	 */
	private class ClientConnectedThread extends Thread
	{
		/**
		 * L'ecouteur
		 */
		private ServerSocketListener listener;
		
		/**
		 * La socket vers le client
		 */
		private ClientSocket client;

		/**
		 * Constructeur
		 */
		public ClientConnectedThread(ServerSocketListener listener, ClientSocket client)
		{
			this.listener = listener;
			this.client   = client;
		}
		
		/**
		 * Lance le thread
		 */
		public void run()
		{
			listener.clientConnected(client);
		}
	}
}

