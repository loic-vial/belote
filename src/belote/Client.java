package belote;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import socket.ClientSocket;
import belote.ihm.Vue;

public class Client
{
	public Client(HashMap config, Vue vue) throws UnknownHostException, IOException
	{
		ClientSocket socket = new ClientSocket((String) config.get("ip"), 
												(Integer) config.get("port"));
		socket.setOwner(vue);
	}
}
