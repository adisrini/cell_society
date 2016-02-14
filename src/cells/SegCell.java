package cells;

/**
 * The SegCell used for the Segregation cellular automata
 * @author Aditya Srinivasan, Harry Guo, and Michael Kuryshev
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cellsociety_team25.SimulationModel;
import edgeCases.IEdgeCase;

public class SegCell extends Cell implements Cloneable{

	private int EMPTY = 0;

	private Map<String, Double> parametersMap;

	/**
	 * Constructor to extend from the Cell super-class
	 * @param x
	 * @param y
	 * @param neighboringX
	 * @param neighboringY
	 * @param state
	 * @param edgeType
	 * @param colorMap
	 * @param parametersMap
	 * @param myModel
	 */
	public SegCell(int x, int y, int[] neighboringX, int[] neighboringY, int state, IEdgeCase edgeType, Map<Integer, String> colorMap, Map<String, Double> parametersMap, SimulationModel myModel) {
		super(x, y, neighboringX, neighboringY, state, edgeType, colorMap, parametersMap, myModel);
		this.parametersMap = parametersMap;
	}

	public SegCell() {

	}

	/**
	 * Based on the Segregation conditions and parameters, updates the the next state of the current cell based on the condition that
	 * the cell is in.
	 */
	@Override
	public void updateCell() {
		if(!isHappy() && myModel.getGridStates()[getX()][getY()] != EMPTY) {

			List<int[]> emptyCells = getEmptyCells();
			int randomIndex = (int) Math.round((emptyCells.size()-1)*Math.random());
			int newX = emptyCells.get(randomIndex)[0];
			int newY = emptyCells.get(randomIndex)[1];

			myModel.setNextGridState(EMPTY, getX(), getY());
			myModel.setNextGridState(myModel.getGridStates()[getX()][getY()], newX, newY);
		} else if(isHappy() && myModel.getGridStates()[getX()][getY()] != EMPTY) {
			myModel.setNextGridState(myModel.getGridStates()[getX()][getY()], getX(), getY());


		} else {
			// do nothing
		}
	}

	/**
	 * Returns a boolean to see if the current cell is happy or not.
	 * @return true if happy, false if not
	 */
	public boolean isHappy() {

		if(neighborsList().size() == 0){
			return true;
		}

		int same = getNeighborsCount(neighborsList(), myModel.getGridStates()[getX()][getY()]);
		int empty = getNeighborsCount(neighborsList(), EMPTY);

		if(neighborsList().size() - empty == 0) {
			return true;
		}

		double happiness = same / (double)(neighborsList().size() - empty);
		return (happiness >= parametersMap.get("THRESHOLD_OF_HAPPINESS"));
	}

	/**
	 * Gets empty Cells in the grid
	 * @return List of Empty Cells
	 */

	public List<int[]> getEmptyCells(){
		List<int[]> emptyCells = new ArrayList<int[]>();

		for(int i = 0; i < myModel.getGridStates().length; i++) {
			for(int j = 0; j < myModel.getGridStates()[i].length; j++) {
				if(myModel.getNextStates()[i][j] == EMPTY) {
					emptyCells.add(new int[] {i, j});
				}
			}
		}
		return emptyCells;
	}

	/**
	 * Get neighbors count of certain type from neighbors list
	 * @param neighbors
	 * @param type
	 * @return
	 */
	public int getNeighborsCount(List<int[]> neighbors, int type){

		int typeCount = 0;
		for (int i = 0; i < neighbors.size(); i++){
			if(neighbors.get(i)[0] == type){
				typeCount++;
			}
		}
		return typeCount;
	}

	/**
	 * After setting the next states of the cell, actually update the grid.
	 */
	public void updateState() {
		myModel.updateNextGridState(getX(), getY());
	}

	/**
	 * Update Parameters of the cell based on what type of parameter it is. Works in conjuction with slider.
	 */
	@Override
	public void updateParameter(String parameter, double newValue) {
		parametersMap.put(parameter, newValue);
	}
}