package xmlHandling;

/**
 * This is the primary class to parse information from the XML File.
 * @author Aditya Srinivasan, Harry Guo, and Michael Kuryshev
 */

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import cellsociety_team25.MainController;
import cellsociety_team25.SimulationException;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class XMLDataReader {

	private Document doc;
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private File xmlFile;
	public static final String XML_RESOURCE_PACKAGE = "resources/XMLproperties";
	private ResourceBundle myXMLResources;

	/**
	 * Takes in an xml file and initializes a factory and builder to parse the information (DATA ONLY).
	 * @param xml file
	 */
	public XMLDataReader(File xml) {
		myXMLResources = ResourceBundle.getBundle(XML_RESOURCE_PACKAGE+MainController.LANGUAGE);
		try {
			xmlFile = xml;
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			doc = builder.parse(xmlFile);
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
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
	
	/**
	 * Parses the parameters of the given simulation in the XML file.
	 * @return map of parameters
	 */
	public Map<String, Double> getParameterMap(){
		Map<String, Double> parameterMap = new HashMap<String, Double>();
		try {

			NodeList parameterList = doc.getElementsByTagName(myXMLResources.getString("Parameter"));
		
			for (int i = 0; i < parameterList.getLength(); i++){
				Node nNode = parameterList.item(i);
				Element eElement = (Element) nNode;
				String name = eElement.getAttribute(myXMLResources.getString("Name"));
				double value = Double.parseDouble(eElement.getAttribute(myXMLResources.getString("Value")));
				parameterMap.put(name, value);
			}	
			return parameterMap;
		} catch(Exception e) {
			throw new SimulationException("ErrorParameter");
		}
	}
	
	/**
	 * Parses the colorMap of the given simulation in the XML file.
	 * @return map of colors
	 */
	public Map<Integer, String> getColorMap(){
		Map<Integer, String> colorMap = new HashMap<Integer, String>();
		NodeList colorMapList = doc.getElementsByTagName(myXMLResources.getString("Color"));
		for (int i = 0; i < colorMapList.getLength(); i++){
			Node nNode = colorMapList.item(i);
			Element eElement = (Element) nNode;
			Integer state = new Integer(Integer.parseInt(eElement.getAttribute(myXMLResources.getString("State"))));
			String color = eElement.getAttribute(myXMLResources.getString("Color"));
			colorMap.put(state, color);
		}	
		return colorMap;
	}
	
	/**
	 * Parses the grid data of the XML file and returns an 2-dimensional integer array of the grid with the respective
	 * states of the grid at each location.
	 * @param gridNumber
	 * @return 2-d grid 
	 */
	public int[][] getGridStates() {
		NodeList cellList = doc.getElementsByTagName("row");
		int gridSize = cellList.getLength();
		int[][] gridStates = new int[gridSize][gridSize];
		for(int i = 0; i < cellList.getLength(); i++) {
			Node nNode = cellList.item(i);
			Element eElement = (Element) nNode;
			String[] cellRowStates = eElement.getAttribute( myXMLResources.getString("States") ).split(",", -1);
			for(int j = 0; j < cellRowStates.length; j++) {
				gridStates[i][j] = Integer.parseInt(cellRowStates[j]);
			}
		}
		return gridStates;
	}
}