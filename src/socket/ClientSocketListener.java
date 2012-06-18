package socket;

/**
 * Ecouteur de socket client.
 * Version 12.06.04
 */
public interface ClientSocketListener
{
	/**
	 * Un message est recu.
	 * @param obj : le message.
	 * @parem src : la socket source, dont le message provient.
	 */
	public void messageReceived(Object obj, ClientSocket src);
	
	/**
	 * La socket vient de se fermer.
	 * @parem src : la socket qui vient de se fermer.
	 */
	public void socketClosed(ClientSocket src);
}
