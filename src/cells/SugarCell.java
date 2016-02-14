/**
 * Cell logic for Sugarscape
 * @author Aditya Srinivasan
 */

package cells;

import java.util.List;
import java.util.Map;

import cellsociety_team25.SimulationModel;
import edgeCases.IEdgeCase;

public class SugarCell extends Cell {

	private Map<String, Double> parametersMap;
	private IEdgeCase edgeType;

	private int ANT = 5;

	private boolean loadingForFirstTime;

	private static final int SUGAR_GROWBACK_INTERVAL = 1;
	private static final int SUGAR_GROWBACK_RATE = 1;

	private int sugarGrowbackInterval;

	public SugarCell(int x, int y, int[] neighboringX, int[] neighboringY, int currentState, IEdgeCase edgeType,
			Map<Integer, String> colorMap, Map<String, Double> parametersMap, SimulationModel myModel) {
		super(x, y, neighboringX, neighboringY, currentState, edgeType, colorMap, parametersMap, myModel);
		this.parametersMap = parametersMap;
		this.edgeType = edgeType;
		myModel.addParameter(currentState, x, y);
		myModel.addParameter(0, x, y);
		this.loadingForFirstTime = true;
		this.sugarGrowbackInterval = SUGAR_GROWBACK_INTERVAL;
	}

	public SugarCell() {
	}

	@Override
	public void updateCell() {
		int index = ((getX() * myModel.getGridStates().length + getY()) * 2);
		loadAnts(index);
		growbackTick(index);
		if(myModel.getGridStates()[getX()][getY()] == ANT) {
			consumeSugar(index);
			if (myModel.getParameter(index + 1, getX(), getY()) <= 0) {
				// ant dies
				antDeath(index);
			} else {
				antMove(index);
			}
		}
	}

	public void loadAnts(int index) {
		if (loadingForFirstTime) {
			double probabilityOfPlacingAnt = parametersMap.get("NUM_ANTS")
					/ Math.pow(myModel.getGridStates().length, 2);
			if (Math.random() < probabilityOfPlacingAnt) {
				myModel.setNextGridState(ANT, getX(), getY());
				myModel.setParameter(index, parametersMap.get("INITIAL_SUGAR"), getX(), getY());
			}
			loadingForFirstTime = false;
		}
	}

	public void antMove(int index) {
		List<int[]> neighbors = getNeighbors(myModel.getNextStates());
		int maxSugar = 0;
		int x = 0;
		int y = 0;
		for(int i = 0; i < neighbors.size(); i++) {
			if(myModel.getNextStates()[neighbors.get(i)[1]][neighbors.get(i)[2]] != ANT) {
				int neighborIndex = ((neighbors.get(i)[1] * myModel.getGridStates().length + neighbors.get(i)[2]) * 2);
				if(myModel.getParameter(neighborIndex, getX(), getY()) >= maxSugar) {
					maxSugar = (int) myModel.getParameter(neighborIndex, getX(), getY());
					x = neighbors.get(i)[1];
					y = neighbors.get(i)[2];
				}
			}
		}

		myModel.setNextGridState(0, getX(), getY());
		myModel.setNextGridState(ANT, x, y);
		myModel.setParameter((x * myModel.getGridStates().length + y) * 2 + 1, 0,
				//myModel.getParameter((getX() * myModel.getGridStates().length + getY()) * 2 + 1, x, y)
				//		- parametersMap.get("SUGAR_METABOLISM_MAX"),
				x, y);
		myModel.setParameter(index + 1, 0, getX(), getY());
	}

	public void antDeath(int index) {
		myModel.setNextGridState((int) myModel.getParameter(index, getX(), getY()), getX(), getY());
	}

	public void consumeSugar(int index) {
		// increase ant sugar
		myModel.setParameter(index + 1,
				myModel.getParameter(index + 1, getX(), getY()) + myModel.getParameter(index, getX(), getY()), getX(),
				getY());
		// reduce sugar patch to 0
		myModel.setParameter(index, 0, getX(), getY());
	}

	public void growbackTick(int index) {
		if (sugarGrowbackInterval == 0) {
			sugarGrowbackInterval = SUGAR_GROWBACK_INTERVAL;
			int cellSugarLevel = (int) myModel.getParameter(index, getX(), getY());
			if (cellSugarLevel < 4) {
				myModel.setParameter(index, myModel.getParameter(index, getX(), getY()) + SUGAR_GROWBACK_RATE, getX(),
						getY());
			}
			if(myModel.getNextStates()[getX()][getY()] != ANT) {
				myModel.setNextGridState((int) myModel.getParameter(index, getX(), getY()), getX(), getY());
			} 
		}else {
			sugarGrowbackInterval--;
		}
	}

	public void updateState() {
		myModel.updateNextGridState(getX(), getY());
	}

	@Override
	public void updateParameter(String parameter, double newValue) {
		parametersMap.put(parameter, newValue);
	}
}
