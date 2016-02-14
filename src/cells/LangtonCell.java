package cells;

/**
 * The Langton Cell used for the Langton Loop simulation
 * @author Aditya Srinivasan, Harry Guo, and Michael Kuryshev
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cellsociety_team25.SimulationModel;
import edgeCases.IEdgeCase;


public class LangtonCell extends Cell {
	

	private LangtonRules langtonRules;

	/**
	 * The constructor of the class extending from the Cell super-class.
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
	public LangtonCell(int x, int y, int[] neighboringX, int[] neighboringY, int state, IEdgeCase edgeType, Map<Integer, String> colorMap, HashMap<String, Double> parametersMap, SimulationModel myModel) {
		super(x, y, neighboringX, neighboringY, state, edgeType, colorMap, parametersMap, myModel);
			this.langtonRules = new LangtonRules();
	
	}

	public LangtonCell() {}

	/**
	 * Based on Langton's rules, update the the next state of the current cell based on the condition that
	 * the cell is in.
	 */
	@Override
	public void updateCell() {

		for(int i = 0; i < neighborsList().size(); i++) {
			if(neighborsList().size() < 4 && neighborsList().get(i)[0] != 0) {
				myModel.expand(0);
			}
		}



		List<String> rules = langtonRules.getRulesForState(myModel.getGridStates()[getX()][getY()]);

		String mySurroundingStates = "";
		for(int i = 0; i < neighborsList().size(); i++) {
			mySurroundingStates += neighborsList().get(i)[0];
		}

		for(int i = 0; i < rules.size(); i++) {
			String rule = rules.get(i).substring(0, 4);
			if(rule.equals(mySurroundingStates) || rule.equals(shift(mySurroundingStates, 1)) || rule.equals(shift(mySurroundingStates, 2)) || rule.equals(shift(mySurroundingStates, 3))) {
				myModel.setNextGridState(Integer.parseInt(rules.get(i).substring(4)), getX(), getY());
				return;
			}
		}	
	}

	/**
	 * Shifts the String by the given amount but still preserving order. 
	 * Ex. String = ABCDE, shifting by 1 -> BCDEA
	 * 
	 * @param str
	 * @param amt
	 * @return a shifted version of the string
	 */
	private String shift(String str, int amt) {
		String temp = "";
		int n = str.length();
		for(int i = 0; i < n; i++) {
			temp += str.charAt((i + amt) % n);
		}
		return temp;
	}

	/**
	 * After setting the next states of the cell, actually update the grid.
	 */
	public void updateState() {
		myModel.updateNextGridState(getX(), getY());
	}
	
	/**
	 * Not used for this class since there are no parameters given.
	 */
	@Override
	public void updateParameter(String parameter, double newValue) {}
}