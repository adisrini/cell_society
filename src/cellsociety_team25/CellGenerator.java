package cellsociety_team25;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import cells.Cell;
import cells.SugarCell;
import cells.LangtonCell;
import cells.LifeCell;
import cells.PredatorPreyCell;
import cells.SegCell;
import cells.TreeCell;
import edgeCases.FiniteEdge;
import edgeCases.IEdgeCase;
import edgeCases.InfiniteEdge;
import edgeCases.ToroidEdge;

/**
 * A factory for generating cells. The class properly decides which subclass of Cell and other characteristics such as
 * neighbors (NSEW or all surrounding), and edge type the cell should be constructed with.
 * @author Aditya Srinivasan, Harry Guo, and Michael Kuryshev
 */

public class CellGenerator {

	public static final String GAME_TYPES_PACKAGE = "resources/GameTypes";
	private Map<String, Class> cellClassMap;
	private Map<String, int[]> neighborsXMap;
	private Map<String, int[]> neighborsYMap;
	private Map<String, IEdgeCase> edgeMap;

	public static final int[] NSEWneighborsX = {-1, 0, 1, 0};
	public static final int[] NSEWneighborsY = {0, 1, 0, -1};

	public static final int[] everywhereNeighborsX = {-1, 1, 0, 0, -1, 1, -1, 1};
	public static final int[] everywhereNeighborsY = {0, 0, 1, -1, -1, -1, 1, 1};

	private ResourceBundle gameTypeResources;

	/**
	 * Constructor to instantiate all the simulations, neighbors, and edgeType.
	 */
	public CellGenerator() {
		gameTypeResources = ResourceBundle.getBundle(GAME_TYPES_PACKAGE+MainController.LANGUAGE);
		cellClassMap = new HashMap<String, Class>();
		neighborsXMap = new HashMap<String, int[]>();
		neighborsYMap = new HashMap<String, int[]>();
		edgeMap = new HashMap<String, IEdgeCase>();		

		initCellClassMaps();
		initAllNeighborMaps();
		initEdgeMaps();
	}

	/**
	 * Initializes the neighborsMap in both the X and Y direction
	 * @param key
	 */
	private void initNeighborsMap(String key, int[] valueX, int[] valueY){
		neighborsXMap.put(key, valueX);
		neighborsYMap.put(key, valueY);
	}
	
	private void initAllNeighborMaps() {
		initNeighborsMap(gameTypeResources.getString("Fire"), NSEWneighborsX, NSEWneighborsY);
		initNeighborsMap(gameTypeResources.getString("GameOfLife"), everywhereNeighborsX, everywhereNeighborsY);
		initNeighborsMap(gameTypeResources.getString("Segregation"), everywhereNeighborsX, everywhereNeighborsY);
		initNeighborsMap(gameTypeResources.getString("PredPrey"), NSEWneighborsX, NSEWneighborsY);
		initNeighborsMap(gameTypeResources.getString("Langton"), NSEWneighborsX, NSEWneighborsY);
		initNeighborsMap(gameTypeResources.getString("Segregation"), NSEWneighborsX, NSEWneighborsY);
		initNeighborsMap(gameTypeResources.getString("SlimeCell"), NSEWneighborsX, NSEWneighborsY);
		initNeighborsMap(gameTypeResources.getString("Sugar"), everywhereNeighborsX, everywhereNeighborsY);
	}
	
	private void initCellClassMaps() {
		cellClassMap.put(gameTypeResources.getString("Fire"), new TreeCell().getClass());
		cellClassMap.put(gameTypeResources.getString("GameOfLife"), new LifeCell().getClass());
		cellClassMap.put(gameTypeResources.getString("Segregation"), new SegCell().getClass());
		cellClassMap.put(gameTypeResources.getString("PredPrey"), new PredatorPreyCell().getClass());
		cellClassMap.put(gameTypeResources.getString("Langton"), new LangtonCell().getClass());
		cellClassMap.put(gameTypeResources.getString("Sugar"), new SugarCell().getClass());
	}
	
	private void initEdgeMaps() {
		edgeMap.put(gameTypeResources.getString("FiniteEdge"), new FiniteEdge());
		edgeMap.put(gameTypeResources.getString("ToroidalSurface"), new ToroidEdge());
		edgeMap.put(gameTypeResources.getString("InfiniteEdge"), new InfiniteEdge());
	}

	/**
	 * Create the cell based on the parameters parsed.
	 * @param x
	 * @param y
	 * @param currentState
	 * @param shapeType
	 * @param gameType
	 * @param edgeType
	 * @param colorMap
	 * @param parameterMap
	 * @param myModel
	 * @return a Cell
	 */
	public Cell generateCell(int x, int y, int currentState, String shapeType, String gameType, String edgeType, Map<Integer, String> colorMap, Map<String, Double> parameterMap, SimulationModel myModel) {
		try {
			int[] neighborsX = neighborsXMap.get(gameType);
			int[] neighborsY = neighborsYMap.get(gameType);

			IEdgeCase edgeCase = edgeMap.get(edgeType);

			Constructor<?>[] constructors = cellClassMap.get(gameType).getConstructors();
			Constructor<?> constructor = constructors[0];

			Cell cell = null;
			cell = (Cell) constructor.newInstance(x, y, neighborsX, neighborsY, currentState, edgeCase, colorMap, parameterMap, myModel);

			return cell;
		} catch(Exception e) {
			throw new SimulationException();
		}
	}
}

