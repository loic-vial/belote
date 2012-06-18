package util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Outil de serialisation XML
 */
public class XML
{
	private static XStream xstream = new XStream(new DomDriver());
	
	/**
	 * Serialise un objet
	 */
	public static boolean store(Object obj, String fileName)
	{
		try
		{
			if (!fileName.endsWith(".xml")) fileName += ".xml";
			File file = new File(fileName);
			file.getParentFile().mkdirs();
			FileWriter fileWriter = new FileWriter(file);
			xstream.toXML(obj, fileWriter);
			fileWriter.close();
			System.out.println("Enregistrement du fichier " + fileName + " : OK");
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	/**
	 * Deserialise un objet
	 */
	public static Object load(String fileName)
	{
		try
		{
			if (!fileName.endsWith(".xml")) fileName += ".xml";
			File file = new File(fileName);
			FileReader fileReader = new FileReader(file);
			Object obj = xstream.fromXML(fileReader);
			fileReader.close();
			System.out.println("Chargement du fichier " + fileName + " : OK");
			return obj;
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
