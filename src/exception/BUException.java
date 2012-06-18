package exception;

/**
 * Bad Use Exception
 * 
 * Version 1.0.0 : 1 constructeur, toString, equals
 * Version 1.1.0 : methode getRaison
 * Version 1.2.0 : extends RuntimeException + methode clone
 */
public class BUException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	String m_raison;
	
	public BUException(String raison)
	{
		m_raison = raison;
	}

	public String getRaison()
	{
		return m_raison;
	}
	
	public String toString()
	{
		String retour = "3 : Mauvaise utilisation.\n";
		retour += "Raison : " + m_raison + "\n";
		return retour;
	}

	public boolean equals(Object o)
	{
		if (!(o instanceof BUException))
			return false;
		return (((BUException)o).m_raison.equals(m_raison));
	}
	
	public Object clone()
	{
		return new BUException(m_raison);
	}
}
