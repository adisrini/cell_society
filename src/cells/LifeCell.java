package cells;

/**
 * The LifeCell used for the the Game Of Life simulation.
 * @author Aditya Srinivasan, Harry Guo, and Michael Kuryshev
 */

import java.util.List;
import java.util.Map;
import cellsociety_team25.SimulationModel;
import edgeCases.IEdgeCase;

public class LifeCell extends Cell {

	private int DEATH = 0;
	private int LIFE = 1;

	/**
	 * Constructor to extend from the Cell super-class.
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
	public LifeCell(int x, int y, int[] neighboringX, int[] neighboringY, int state, IEdgeCase edgeType, Map<Integer, String> colorMap, Map<String, Double> parametersMap, SimulationModel myModel) {
		super(x, y, neighboringX, neighboringY, state, edgeType, colorMap, parametersMap, myModel);
	}

	public LifeCell() {

	}

	/**
	 * Based on the Game of Life conditions, update the the next state of the current cell based on the condition that
	 * the cell is in.
	 */
	@Override
	public void updateCell(){

		if(neighborsList().size() < 6 && myModel.getGridStates()[getX()][getY()] == LIFE) {
			myModel.expand(0);
		}

		int liveNeighbors = countLiveNeighbors();

		if (myModel.getGridStates()[getX()][getY()] == LIFE) {
			if( liveNeighbors < 2 || liveNeighbors > 3){
				myModel.setNextGridState(DEATH, getX(), getY());
			} else {
				myModel.setNextGridState(LIFE, getX(), getY());
			}
		} else {
			if (liveNeighbors == 3){
				myModel.setNextGridState(LIFE, getX(), getY());
			}
		}
	}

	/**
	 * @param neighbors
	 * @return numbers of live neighbors
	 */
	private int countLiveNeighbors() {
		int liveNeighbors = 0;
		for(int i = 0; i < neighborsList().size(); i++) {
			if(neighborsList().get(i) != null) {
				if(neighborsList().get(i)[0] == LIFE) {
					liveNeighbors++;
				}
			}
		}
		return liveNeighbors;
	}

	/**
	 * After setting the next states of the cell, actually update the grid.
	 */
	public void updateState() {
		myModel.updateNextGridState(getX(), getY());
	}

	/**
	 * Not used for this class since there are no parameters.
	 */
	@Override
	public void updateParameter(String parameter, double newValue) {
	}
}