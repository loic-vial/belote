package exception;

/**
 * Invalid Parameter Exception
 * 
 * version 1.0.0 : 4 constructeurs, toString, getRaison, equals
 * version 1.1.0 : extends IllegalArgumentException
 */
public class IPException extends IllegalArgumentException
{
	private static final long serialVersionUID = 1L;
	
	int m_num_exception;
	
	String m_raison;
	
	public IPException()
	{
		this(-1, "");
	}

	public IPException(String raison)
	{
		this(-1, raison);
	}
	
	public IPException(int num_exception)
	{
		this(num_exception, "");
	}

	public IPException(int num_exception, String raison)
	{
		m_num_exception = num_exception;
		m_raison = raison;
	}
	
	public String getRaison()
	{
		return m_raison;
	}
	
	public String toString()
	{
		String retour = "2.";
		if (m_num_exception == -1)
			retour += "1 : Parametre invalide.";
		else
			retour += m_num_exception + " : Parametre numero " + m_num_exception + " invalide.";
		if (!m_raison.equals(""))
			retour += "\nRaison : " + m_raison + "\n";
		return retour;
	}

	public boolean equals(Object o)
	{
		return (o instanceof IPException &&
				((IPException)o).m_num_exception == m_num_exception &&
				((IPException)o).m_raison.equals(m_raison));
	}
}
