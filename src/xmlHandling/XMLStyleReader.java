package xmlHandling;

/**
 * This is the primary class to parse information from the XML File.
 * @author Aditya Srinivasan, Harry Guo, and Michael Kuryshev
 */

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import cellsociety_team25.SimulationException;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class XMLStyleReader {

	private Document doc;
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private File xmlFile;

	/**
	 * Takes in an xml file and initializes a factory and builder to parse the information (STYLE ONLY).
	 * @param xml file
	 */
	public XMLStyleReader(File xml) {
		try {
			xmlFile = xml;
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			doc = builder.parse(xmlFile);
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SimulationException();
		}

	}
	
	/**
	 * Takes a tag name and attribute and parses the value of the attribute
	 * @param tagName of the element
	 * @param attribute
	 * @return value of the attribute
	 */
	public String getXMLTagInformation(String tagName, String attribute){
		NodeList list = doc.getElementsByTagName(tagName);
		Node nNode = list.item(0);
		Element eElement = (Element) nNode;
		String value = eElement.getAttribute(attribute);
		return value;
	}
}