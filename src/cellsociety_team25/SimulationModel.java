package cellsociety_team25;

/**
 * The back-end of the simulation that produces the model.
 * @author Aditya Srinivasan, Harry Guo, and Michael Kuryshev
 */

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import cells.Cell;
import xmlHandling.XMLDataReader;
import xmlHandling.XMLStyleReader;
import xmlHandling.XMLWriter;

/**
 * The back-end of the simulation
 * @author Aditya Srinivasan
 *
 */
public class SimulationModel {

	public static final String ERROR_RESOURCE_PACKAGE = "resources/Errors";
	public static final String XML_RESOURCE_PACKAGE = "resources/XMLproperties";
	private String gameType;
	private int gridSize;
	private String edgeType;
	private String title;
	private String author;
	private String shapeType;
	private String neighbors;
	private boolean outline;

	private Map<String, Double> parameterMap;
	private Map<Integer, String> colorMap;
	private Map<Integer, Integer> stateCounts;

	private int[][] originalGridStates;
	private int[][] gridStates;
	private int[][] nextStates;

	private CellGenerator cg;

	private ArrayList<Double> parameterGrid;
	private Cell[][] cellGrid;
	private ResourceBundle myErrorResources;
	private ResourceBundle myXMLResources;

	/**
	 * Constructor to instantiate some of the private instance variables.
	 */
	public SimulationModel() {
		myErrorResources = ResourceBundle.getBundle(ERROR_RESOURCE_PACKAGE+MainController.LANGUAGE);
		myXMLResources = ResourceBundle.getBundle(XML_RESOURCE_PACKAGE+MainController.LANGUAGE);
		stateCounts = new HashMap<Integer, Integer>();
		parameterGrid = new ArrayList<Double>();
		cg = new CellGenerator();
	}

	/**
	 * Load the original file conditions
	 * @return
	 */
	public int[][] normalLoad(){		
		return load(originalGridStates);
	}

	/**
	 * Load a randomly generated Grid
	 * @return
	 */
	public int[][] randomLoad(){
		RandomModelGenerator r = new RandomModelGenerator(colorMap, gridSize);
		return load(r.createRandomGrid());
	}


	/**
	 * Write an xml file into the data sheets folder.
	 * @param title
	 */
	public void writeXML(String title) {
		XMLWriter xmlWriter = new XMLWriter(this);
		xmlWriter.create(title);
	}

	public int[][] chosenProbabilityLoad(Map<Integer, Integer> colorProbabilities){
		ChosenProbabilityModelGenerator choose = new ChosenProbabilityModelGenerator(gridSize, colorProbabilities);
		return load(choose.createWeightedGrid());
	}

	public int[][] load(int[][] gridStates) {

		gridSize = gridStates.length;
		cellGrid = new Cell[gridSize][gridSize];

		getColorStateCounts();

		nextStates = new int[gridSize][gridSize];
		this.gridStates = new int[gridSize][gridSize];

		try {
			for(int i = 0; i < gridSize; i++) {
				for(int j = 0; j < gridSize; j++) {
					cellGrid[i][j] = cg.generateCell(i, j, gridStates[i][j], shapeType, gameType, edgeType, colorMap, parameterMap, this);
					this.gridStates[i][j] = gridStates[i][j];
					this.nextStates[i][j] = gridStates[i][j];
				}
			}
		} catch(SimulationException e) {
			throw new SimulationException(myErrorResources.getString("ErrorSubtitle"));
		}

		for(int i = 0; i < gridSize; i++) {
			for(int j = 0; j < gridSize; j++) {
				stateCounts.put(gridStates[i][j], stateCounts.get(gridStates[i][j])+1);
			}
		}

		return gridStates;
	}

	private void getColorStateCounts() {
		for(Integer color : colorMap.keySet()) {
			stateCounts.put(color, 0);
		}
	}

	public void expand(int state) {
		int[][] expandedStates = new int[gridSize+2][gridSize+2];
		int[][] expandedNextStates = new int[gridSize+2][gridSize+2];
		Cell[][] expandedCellGrid = new Cell[gridSize+2][gridSize+2];

		expandFillCenter(expandedStates, expandedNextStates, expandedCellGrid);
		expandFillOuter(state, expandedStates, expandedNextStates, expandedCellGrid);
		gridSize = gridSize + 2;
		gridStates = expandedStates;
		nextStates = expandedNextStates;
		cellGrid = null;
		cellGrid = new Cell[gridSize][gridSize];
		cellGrid = expandedCellGrid;
		expandedStates = null;
		expandedNextStates = null;

	}

	private void expandFillCenter(int[][] expandedStates, int[][] expandedNextStates, Cell[][] expandedCellGrid) {
		for(int i = 1; i < gridSize + 1; i++) {
			for(int j = 1; j < gridSize + 1; j++) {
				expandedStates[i][j] = gridStates[i-1][j-1];
				expandedNextStates[i][j] = nextStates[i-1][j-1];
				expandedCellGrid[i][j] = cellGrid[i-1][j-1];
				expandedCellGrid[i][j].setX(expandedCellGrid[i][j].getX() + 1);
				expandedCellGrid[i][j].setY(expandedCellGrid[i][j].getY() + 1);
			}
		}
	}
	
	private void expandFillOuter(int state, int[][] expandedStates, int[][] expandedNextStates,
			Cell[][] expandedCellGrid) {
		for(int i = 0; i < gridSize + 2; i++) {
			expandedStates[i][0] = state;
			expandedStates[0][i] = state;
			expandedStates[i][gridSize+1] = state;
			expandedStates[gridSize+1][i] = state;
			expandedNextStates[i][0] = state;
			expandedNextStates[0][i] = state;
			expandedNextStates[i][gridSize+1] = state;
			expandedNextStates[gridSize+1][i] = state;
			expandedCellGrid[i][0] = cg.generateCell(i, 0, expandedStates[i][0], shapeType, gameType, edgeType, colorMap, parameterMap, this);
			expandedCellGrid[0][i] = cg.generateCell(0, i, expandedStates[0][i], shapeType, gameType, edgeType, colorMap, parameterMap, this);
			expandedCellGrid[i][gridSize+1] = cg.generateCell(i, gridSize+1, expandedStates[i][gridSize+1], shapeType, gameType, edgeType, colorMap, parameterMap, this);
			expandedCellGrid[gridSize+1][i] = cg.generateCell(gridSize+1, i, expandedStates[gridSize+1][i], shapeType, gameType, edgeType, colorMap, parameterMap, this);
		}
	}

	/**
	 * Get Error Resources
	 * @return error resource bundle
	 */
	public ResourceBundle getErrorResources() {
		return myErrorResources;
	}

	/**
	 * Update the parameters with their new values for each cell.
	 * @param parameter
	 * @param newValue
	 */
	public void updateParameterForCells(String parameter, double newValue) {
		for(int i = 0; i < cellGrid.length; i++) {
			for(int j = 0; j < cellGrid.length; j++) {
				cellGrid[i][j].updateParameter(parameter, newValue);
			}
		}
		parameterMap.put(parameter, newValue);
	}


	public int[][] toggleCell(int x, int y) {
		outerloop:
			for(int i = 0; i < gridSize; i++) {
				for(int j = 0; j < gridSize; j++) {
					if(j == x && i == y) {
						gridStates[i][j] = (gridStates[i][j] + 1) % colorMap.size();
						break outerloop;
					}
				}
			}
	return gridStates;
	}

	/**
	 * Get the shape type
	 * @return
	 */
	public String getShapeType() {
		return shapeType;
	}

	/**
	 * See if the game has correctly initialized and loaded.
	 * @return
	 */
	public boolean hasLoaded() {
		return gameType != null;
	}

	/**
	 * Step one iteration through the grid States
	 * @return the grid iterated one step
	 */
	public int[][] step() {
		getColorStateCounts();

		try {
			for(int i = 0; i < gridSize; i++) {
				for(int j = 0; j < gridSize; j++) {
					cellGrid[i][j].updateCell();
				}
			}
		} catch(SimulationException e) {
			throw new SimulationException(myErrorResources.getString("ErrorSubtitle"));
		}

		for(int i = 0; i < gridSize; i++) {
			for(int j = 0; j < gridSize; j++) {
				stateCounts.put(gridStates[i][j], stateCounts.get(gridStates[i][j])+ 1);
				cellGrid[i][j].updateState();
			}
		}
		return gridStates;
	}

	/**
	 * @return number of elements in color map
	 */
	public int numElements() {
		return colorMap.size();
	}

	/**
	 * @return map of stateCounts
	 */
	public Map<Integer, Integer> getStateCounts() {
		return stateCounts;
	}

	/**
	 * Parse data from an XML file
	 * @param file
	 * @return
	 */
	public boolean getDataFromXMLFile(File file) {
		XMLDataReader xmlReader = new XMLDataReader(file);

		try {
			gameType = xmlReader.getXMLTagInformation(myXMLResources.getString("GameType"), myXMLResources.getString("Name"));
			title = xmlReader.getXMLTagInformation(myXMLResources.getString("Title"), myXMLResources.getString("Name"));
			author = xmlReader.getXMLTagInformation(myXMLResources.getString("Author"), myXMLResources.getString("Name"));
			parameterMap = xmlReader.getParameterMap();
			colorMap = xmlReader.getColorMap();
			gridStates = xmlReader.getGridStates();
			originalGridStates = xmlReader.getGridStates();
			gridSize = gridStates.length;
			return true;
		} catch (Exception e) {
			throw new SimulationException(myErrorResources.getString("ErrorNotSquare"));
		}
	}

	/**
	 * Parse style formatting from XML file
	 * @param file
	 * @return
	 */
	public boolean getStyleFromXMLFile(File file) {
		try {
			XMLStyleReader xmlStyleReader = new XMLStyleReader(file);
			edgeType = xmlStyleReader.getXMLTagInformation(myXMLResources.getString("Edge"), myXMLResources.getString("EdgeType"));
			shapeType = xmlStyleReader.getXMLTagInformation(myXMLResources.getString("Shape"), myXMLResources.getString("ShapeType"));
			neighbors = xmlStyleReader.getXMLTagInformation(myXMLResources.getString("Neighbor"), myXMLResources.getString("Neighbors"));
			outline = Boolean.parseBoolean(xmlStyleReader.getXMLTagInformation(myXMLResources.getString("Outlines"), myXMLResources.getString("Outline")));
			return true;
		} catch (Exception e) {
			throw new SimulationException(myErrorResources.getString("ErrorNotSquare"));
		}
	}

	/**
	 * @return title of simulation
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @return game type of simulation
	 */
	public String getGameType() {
		return this.gameType;
	}

	/**
	 * @return author of simulation
	 */
	public String getAuthor() {
		return this.author;
	}

	/**
	 * @return edge type of simulation
	 */
	public String getEdgeType() {
		return this.edgeType;
	}

	/**
	 * @return outline
	 */
	public boolean getOutline() {
		return this.outline;
	}

	/**
	 * @return parameter map of simulation
	 */
	public Map<String, Double> getParameterMap() {
		return this.parameterMap;
	}

	/**
	 * @return grid of next states
	 */
	public int[][] getNextStates() {
		return this.nextStates;
	}

	/**
	 * @return gridStates
	 */
	public int[][] getGridStates() {
		return this.gridStates;
	}

	public void setNextGridState(int state, int x, int y) {
		outerloop:
			for(int i = 0; i < this.gridStates.length; i++) {
				for(int j = 0; j < this.gridStates[i].length; j++) {
					if(i == x && j == y) {
						this.nextStates[i][j] = state;
						break outerloop;
					}
				}
			}
	}

	/**
	 * Set parameters
	 * @param index
	 * @param value
	 * @param x
	 * @param y
	 */
	public void setParameter(int index, double value, int x, int y) {
		this.parameterGrid.set(index, value);
	}

	/**
	 * Add to parameter grid
	 * @param value
	 * @param x
	 * @param y
	 */
	public void addParameter(double value, int x, int y) {
		this.parameterGrid.add(value);
	}

	public double getParameter(int index, int x, int y) {
		return this.parameterGrid.get(index);
	}

	/**
	 * @return color map of simulation
	 */
	public Map<Integer, String> getColorMap() {
		return this.colorMap;
	}

	public void updateNextGridState(int x, int y) {
		outerloop:
			for(int i = 0; i < nextStates.length; i++) {
				for(int j = 0; j < nextStates.length; j++) {
					if(i == x && j == y) {
						gridStates[i][j] = nextStates[i][j];
						break outerloop;
					}
				}
			}
	}

}