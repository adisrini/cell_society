package cells;

/**
 * The TreeCell used for the Spreading Fire cellular automata.
 * @author Aditya Srinivasan, Harry Guo, Michael Kuryshev
 */

import java.util.Map;

import cellsociety_team25.SimulationModel;
import edgeCases.IEdgeCase;
import edgeCases.InfiniteEdge;

public class TreeCell extends Cell{

	private int EMPTY = 0;
	private int TREE = 1;
	private int BURNING = 2;

	private Map<String, Double> parametersMap;
	private IEdgeCase edgeType;

	/**
	 * Constructor to extend from the cell super-class.
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
	public TreeCell(int x, int y, int[] neighboringX, int[] neighboringY, int state, IEdgeCase edgeType, Map<Integer, String> colorMap, Map<String, Double> parametersMap, SimulationModel myModel) {
		super(x, y, neighboringX, neighboringY, state, edgeType, colorMap, parametersMap, myModel);
		this.parametersMap = parametersMap;
		this.edgeType = edgeType;
	}

	public TreeCell() {

	}

	/**
	 * Based on the Segregation conditions and parameters, updates the the next state of the current cell based on the condition that
	 * the cell is in.
	 */
	@Override
	public void updateCell(){

		if(neighborsList().size() < 4 && (edgeType instanceof InfiniteEdge)) {
			if(myModel.getGridStates()[getX()][getY()] == BURNING) {
				myModel.expand(1);
			}
		}

		if(myModel.getGridStates()[getX()][getY()] != TREE) {
			myModel.setNextGridState(EMPTY, getX(), getY());
			return;
		}

		for(int i = 0; i < neighborsList().size(); i++) {
			if(neighborsList().get(i) != null) {
				if(neighborsList().get(i)[0] == BURNING && Math.random() < parametersMap.get("PROBABILITY_OF_CATCHING_FIRE")) {
					myModel.setNextGridState(BURNING, getX(), getY());
					return; 
				}
			}
		}
		myModel.setNextGridState(myModel.getGridStates()[getX()][getY()], getX(), getY());
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