package socket;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Socket client permettant l'envoi de messages, la reception de messages, et 
 * l'envoi de requetes (autrement dit l'appel de methode a distance)
 * Version 12.06.06
 */
public class ClientSocket
{
	/**
	 * Socket java support
	 */
	private Socket csock;
	
	/**
	 * Liste des ecouteurs de la socket
	 */
	private ArrayList<ClientSocketListener> listeners;
	
	/**
	 * Buffer de sortie
	 */
	private ObjectOutputStream oos;
	
	/**
	 * Buffer d'entree
	 */
	private ObjectInputStream ois;
	
	/**
	 * Objet auquel renvoyer les requetes
	 */
	private Object owner;

	/**
	 * Booleen de controle du thread interne
	 */
	private boolean stop;
	
	/**
	 * Thread interne de reception des messages/requetes/reponses
	 */
	private InternalThread thread;
	
	/**
	 * Liste des requetes en attente d'une reponse
	 */
	private List<Request> requests;
	
	/**
	 * Liste des reponses aux requetes
	 */
	private Map<Integer, Answer> answers;
	
	/**
	 * Classe interne representant une requete
	 */
	private static class Request implements Serializable
	{
		/**
		 * L'id de la requete
		 */
		public int id;
		
		/**
		 * Le nom de la methode
		 */
		public String method;
		
		/**
		 * Les arguments de la methode
		 */
		public Object[] args;
		
		/**
		 * Constructeur
		 */
		public Request(int id, String method, Object[] args)
		{
			this.id = id;
			this.method = method;
			this.args = args;
		}
	}
	
	/**
	 * Classe interne reprentant une reponse a une requete
	 */
	private static class Answer implements Serializable
	{
		/**
		 * L'id de la requete
		 */
		public int id;
		
		/**
		 * Le retour de la methode
		 */
		public Object ans;
		
		/**
		 * true == une erreur s'est produite lors de l'appel a la methode
		 */
		public boolean errorFlag;
		
		/**
		 * Constructeur (erreur lors de l'appel a la methode)
		 */
		public Answer(int id)
		{
			this.id = id;
			this.ans = null;
			errorFlag = true;
		}
		
		/**
		 * Constructeur (aucune erreur lors de l'appel a la methode)
		 */
		public Answer(int id, Object ans)
		{
			this.id = id;
			this.ans = ans;
			errorFlag = false;
		}
	}

	/**
	 * Constructeur.
	 * @param host : l'adresse ip du serveur sur lequel se connecter
	 * @param port : le port du serveur sur lequel se connecter
	 */
	public ClientSocket(String host, int port) throws UnknownHostException, IOException
	{
		this(new Socket(host, port));
	}

	/**
	 * Constructeur.
	 * @param socket : la socket support vers le client.
	 */
	public ClientSocket(Socket socket)
	{
		if (socket == null) throw new NullPointerException();
		csock = socket;
		listeners = new ArrayList<ClientSocketListener>();
		try
		{
			oos = new ObjectOutputStream(csock.getOutputStream());
			ois = new ObjectInputStream(csock.getInputStream());
		} 
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		owner = null;
		stop = false;
		requests = Collections.synchronizedList(new ArrayList<Request>());
		answers = Collections.synchronizedMap(new HashMap<Integer, Answer>());
		thread = new InternalThread();
		thread.start();
	}
	
	/**
	 * Definit l'objet qui est cense repondre aux requetes
	 */
	public void setOwner(Object owner)
	{
		this.owner = owner;
	}

	/**
	 * Ajoute un ecouteur sur la socket.
	 */
	public void addClientSocketListener(ClientSocketListener listener)
	{
		if (listener == null) throw new NullPointerException();
		listeners.add(listener);
	}
	
	/**
	 * Envoi un message.
	 */
	public void sendMessage(Object obj)
	{
		try
		{
			oos.writeObject(obj);
			oos.flush();
			oos.reset();
		}
		// socket fermee (seulement ?)
		catch (SocketException e)
		{
			stop();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Envoi une requete et retourne la reponse.
	 */
	public Object sendRequest(String method, Object... args)
	{
		// *** Envoi de la requete
		int id = requests.size();
		Request req = new Request(id, method, args);
		requests.add(req);
		answers.put(id, null);
		sendMessage(req);

		// *** Attente de la reponse
		synchronized (answers)
		{
			while (answers.get(id) == null)
			{
				try {
					answers.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		// *** Reception de la reponse
		Answer ans = answers.get(id);
		
		// *** Si le drapeau d'erreur est leve : exception
		if (ans.errorFlag) throw new RuntimeException("Erreur lors de l'appel " +
				"a la methode \"" + method + "\" !");
		
		// *** Sinon retour de la reponse
		return answers.get(id).ans;
	}

	/**
	 * Ferme la connexion.
	 */
	public synchronized void stop()
	{
		if (!stop)
		{
			stop = true;
			try {
				csock.close();
			} catch (IOException e) {}
			for (ClientSocketListener nl : listeners)
				new SocketClosedThread(nl).start();
		}
	}
	
	/**
	 * @return l'adresse IP distante, sous la forme "xxx.xxx.xxx.xxx"
	 */
	public String getRemoteIP()
	{
		return csock.getInetAddress().getHostAddress();
	}
	
	/**
	 * @return le port local sur lequel la socket est connectee
	 */
	public int getLocalPort()
	{
		return csock.getLocalPort();
	}
	
	/**
	 * @return le port distant sur lequel la socket est connectee
	 */
	public int getRemotePort()
	{
		return csock.getPort();
	}
	
	/**
	 * Thread interne de reception des messages / requetes / reponses.
	 */
	private class InternalThread extends Thread
	{
		/**
		 * Constructeur
		 */
		public InternalThread()
		{
			super("ClientSocket");
		}

		/**
		 * Lance le thread
		 */
		public void run()
		{
			Object obj = null;
			Request req = null;
			Answer ans = null;
			int id;
			String methodName = null;
			Object[] args = null;
			while (!stop)
			{
				try
				{
					// *** Reception d'un message
					obj = ois.readObject();
					
					// *** Cas d'une reponse a une requete
					if (obj instanceof Answer)
					{
						ans = (Answer) obj;
						answers.put(ans.id, ans);
						synchronized (answers) {
							answers.notifyAll();
						}
					}
					
					// *** Cas d'une requete a laquelle on doit repondre
					else if (obj instanceof Request)
					{
						req = (Request) obj;
						id = req.id;
						methodName = req.method;
						args = req.args;
						ans = new Answer(id);
						for (Method met : owner.getClass().getMethods())
						{
							if (!met.getName().equals(methodName)) continue;
							try {
								ans = new Answer(id, met.invoke(owner, args));
								break;
							} catch (InvocationTargetException e) {
								e.getTargetException().printStackTrace();
							} catch (Exception e) {}
						}
						sendMessage(ans);
					}
					
					// *** Cas general : autre message
					else
					{
						for (ClientSocketListener nl : listeners)
							new RequestReceivedThread(nl, obj).start();
					}
				}
				// socket fermee
				catch (NullPointerException e)
				{
					ClientSocket.this.stop();
				}
				// socket fermee
				catch (EOFException e)
				{
					ClientSocket.this.stop();
				}
				// serveur fermee (seulement ?)
				catch (SocketException e)
				{
					ClientSocket.this.stop();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Thread gerant l'envoi d'un message a un ecouteur quand la socket est fermee
	 */
	private class SocketClosedThread extends Thread
	{
		/**
		 * L'ecouteur
		 */
		private ClientSocketListener listener;

		/**
		 * Constructeur
		 */
		public SocketClosedThread(ClientSocketListener listener)
		{
			this.listener = listener;
		}
		
		/**
		 * Lance le thread
		 */
		public void run()
		{
			listener.socketClosed(ClientSocket.this);
		}
	}
	
	/**
	 * Thread gerant l'envoi d'un message a un ecouteur quand un message est arrive
	 * sur cette socket.
	 */
	private class RequestReceivedThread extends Thread
	{
		/**
		 * L'ecouteur
		 */
		private ClientSocketListener listener;
		
		/**
		 * Le message
		 */
		private Object message;
		
		/**
		 * Constructeur
		 */
		public RequestReceivedThread(ClientSocketListener client, Object message)
		{
			this.message = message;
			this.listener = client;
		}
		
		/**
		 * Lance le thread
		 */
		public void run()
		{
			listener.messageReceived(message, ClientSocket.this);
		}
	}
}