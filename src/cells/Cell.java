package cells;

/**
 * Super class of all other cells
 * @author Aditya Srinivasan, Harry Guo, and Michael Kuryshev
 **/

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import cellsociety_team25.SimulationModel;

import edgeCases.IEdgeCase;

public abstract class Cell {

	private int x;
	private int y;
	private int[] neighboringX;
	private int[] neighboringY;
	private IEdgeCase edgeType;
	private Map<Integer, String> colorMap;	
	private Map<String, Double> parametersMap;
	
	protected SimulationModel myModel;
	
	/**
	 * Takes in the current positions, neighbors, state, edgeType, colorMap, parameters, and a model to construct the cell.
	 * @param x
	 * @param y
	 * @param neighboringX
	 * @param neighboringY
	 * @param currentState
	 * @param edgeType
	 * @param colorMap
	 * @param parametersMap
	 * @param myModel
	 */
	public Cell(int x, int y, int[] neighboringX, int[] neighboringY, int currentState, IEdgeCase edgeType, Map<Integer, String> colorMap,  Map<String, Double> parametersMap, SimulationModel myModel){
		this.x = x;
		this.y = y;
		this.edgeType = edgeType;
		this.colorMap = colorMap;
		this.parametersMap = parametersMap;
		this.neighboringX = neighboringX;
		this.neighboringY = neighboringY;
		this.myModel = myModel;
	}
	
	public Cell() {
		
	}
	
	/**
	 * Takes the neighbors from the grid and returns a list of neighbors.
	 * @param gridStates
	 * @param neighboringX
	 * @param neighboringY
	 * @return
	 */
	public List<int[]> neighborsList() {
		int[][] gridStates = myModel.getGridStates();
		List<int[]> neighbors = new ArrayList<int[]>();
		for(int i = 0; i < neighboringX.length; i++) {
			int neighborX = edgeType.coordinateAfterCrossingEdge(x, neighboringX[i], gridStates.length);
			int neighborY = edgeType.coordinateAfterCrossingEdge(y, neighboringY[i], gridStates.length);
			if(neighborX != -1 && neighborY != -1) {
				neighbors.add(new int[] {gridStates[neighborX][neighborY], neighborX, neighborY});
			}
		}
		return neighbors;
	}
	
	public List<int[]> getNeighbors(int[][] gridStates) {
		List<int[]> neighbors = new ArrayList<int[]>();
		for(int i = 0; i < neighboringX.length; i++) {
			int neighborX = edgeType.coordinateAfterCrossingEdge(x, neighboringX[i], gridStates.length);
			int neighborY = edgeType.coordinateAfterCrossingEdge(y, neighboringY[i], gridStates.length);
			if(neighborX != -1 && neighborY != -1) {
				neighbors.add(new int[] {gridStates[neighborX][neighborY], neighborX, neighborY});
			}
		}
		return neighbors;
	}
	
	/**
	 * Abstract method to update Cell based on the behavior of the model.
	 */
    public abstract void updateCell();
    
    /**
     * Abstract method to update each individual cell within the grid.
     */
    public abstract void updateState();
    
    /**
     * Updates the parameters within the model.
     * @param parameter
     * @param newValue
     */
    public abstract void updateParameter(String parameter, double newValue);
    
    /**
     * return the x-coordinates of the neighbors along the X-axis.
     * @return array of x-coordinates
     */
    public int[] getXNeighbors() {
    	return this.neighboringX;
    }
    
    /**
     * return the y-coordinates of the neighbors along the Y-axis.
     * @return array of x-coordinates
     */
    public int[] getYNeighbors() {
    	return this.neighboringY;
    }
    
    /**
     * Returns X value of current cell.
     * @return x coordinate
     */
    public int getX() {
    	return this.x;
    }
    
    /**
     * Returns Y value of the current cell.
     * @returny y coordinate
     */
    public int getY() {
    	return this.y;
    }

    /**
     * Set the x value of the current cell.
     * @param x
     */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Set the y value of the current cell.
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}

}