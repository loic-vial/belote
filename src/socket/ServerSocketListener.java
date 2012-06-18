package socket;

/**
 * Ecouteur de socket serveur.
 * Version 12.06.04
 */
public interface ServerSocketListener
{
	/**
	 * Un client vient de se connecter.
	 * @param client : la socket vers le client.
	 */
	public void clientConnected(ClientSocket client);
	
	/**
	 * La socket vient de se fermer.
	 * @parem src : la socket qui vient de se fermer.
	 */
	public void socketClosed(ServerSocket src);
}
