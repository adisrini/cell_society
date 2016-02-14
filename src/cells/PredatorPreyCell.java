package cells;

/**
 * The Predator Prey Cell used for the Wa-Tor simulation.
 * @author Aditya Srinivasan, Harry Guo, and Michael Kuryshev
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cellsociety_team25.SimulationModel;
import edgeCases.IEdgeCase;
import edgeCases.InfiniteEdge;

public class PredatorPreyCell extends Cell {
	
	private static final int DEATH = 0;
	private static final int EMPTY = 0;
	private static final int FISH = 1;
	private static final int SHARK = 2;
	private int newX;
	private int newY;
	private static final boolean MOVE = true;
	private static final boolean SPAWN = true;
	
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
	public PredatorPreyCell(int x, int y, int[] neighboringX, int[] neighboringY, int state, IEdgeCase edgeType, Map<Integer, String> colorMap, Map<String, Double> parametersMap, SimulationModel myModel) {
		super(x, y, neighboringX, neighboringY, state, edgeType, colorMap, parametersMap, myModel);
		this.parametersMap = parametersMap;
		this.edgeType = edgeType;
		myModel.addParameter(parametersMap.get("SHARK_BREED_TIME").intValue(), x, y);
		myModel.addParameter(parametersMap.get("FISH_BREED_TIME").intValue(), x, y);
		myModel.addParameter(parametersMap.get("SHARK_INITIAL_ENERGY").intValue(), x, y);
	}
	
	public PredatorPreyCell() {
		
	}
	
	/**
	 * Based on the Predator-Prey conditions and parameters, updates the the next state of the current cell based on the condition that
	 * the cell is in.
	 */
	@Override
	public void updateCell() {
		
		if(edgeType instanceof InfiniteEdge && getDesiredNeighbors(myModel.getGridStates()[getX()][getY()]).size() < 4) {
			if(myModel.getGridStates()[getX()][getY()] == FISH || myModel.getGridStates()[getX()][getY()] == SHARK) {
				myModel.expand(0);
			}
		}
		
		if(myModel.getGridStates()[getX()][getY()] == FISH) {
			int fishBreedTime = (int) Math.abs(myModel.getParameter(1, getX(), getY()));
			myModel.setParameter(1, fishBreedTime-1, getX(), getY());
			if(getDesiredNeighbors(EMPTY).size() > 0) {
				selectNextLocation(EMPTY);
				
				if(fishBreedTime == 0) {
					if (myModel.getNextStates()[getX()][getY()] == SHARK){
						myModel.setNextGridState(SHARK, getX(), getY());
					}
					else {
						myModel.setNextGridState(FISH, getX(), getY());
						myModel.setParameter(1, parametersMap.get("FISH_BREED_TIME").intValue(), getX(), getY());
					}
				} else if (myModel.getNextStates()[getX()][getY()] == SHARK){
					myModel.setNextGridState(SHARK, getX(), getY());
				} else{
					myModel.setNextGridState(DEATH, getX(), getY());
				}
					
				myModel.setNextGridState(FISH, newX, newY);
				myModel.setParameter(1, myModel.getParameter(1, getX(), getY()), newX, newY);
			} else {
				// no movement
				if(fishBreedTime == 0) {
					myModel.setParameter(1, parametersMap.get("FISH_BREED_TIME").intValue(), getX(), getY());
				}
			}
		} 
		else if(myModel.getGridStates()[getX()][getY()] == SHARK) {
			int sharkBreedTime = (int) myModel.getParameter(0, getX(), getY());
			int sharkEnergy = (int) myModel.getParameter(2, getX(), getY());
			myModel.setParameter(0, sharkBreedTime-1, getX(), getY());
			myModel.setParameter(2, sharkEnergy-1, getX(), getY());
			if(getDesiredNeighbors(FISH).size() > 0) {
				selectNextLocation(FISH);
				
				if(sharkBreedTime == 0) {
					myModel.setNextGridState(SHARK, getX(), getY());
					myModel.setParameter(0, parametersMap.get("SHARK_BREED_TIME").intValue(), getX(), getY());
					myModel.setParameter(2, parametersMap.get("SHARK_INITIAL_ENERGY").intValue(), getX(), getY());
				} else {
					myModel.setNextGridState(DEATH, getX(), getY());
				}
				moveOrSpawnShark(newX, newY, myModel.getParameter(0, getX(), getY()), myModel.getParameter(2, getX(), getY()) + parametersMap.get("SHARK_EATS_FISH_ENERGY").intValue(), MOVE);
			} else if(getDesiredNeighbors(EMPTY).size() > 0) {
				selectNextLocation(EMPTY);
				
				moveOrSpawnShark(newX, newY, myModel.getParameter(0, getX(), getY()) + 1, myModel.getParameter(2, getX(), getY()), MOVE);
				if(sharkBreedTime == 0) {
					moveOrSpawnShark(newX, newY, parametersMap.get("SHARK_BREED_TIME").intValue(), parametersMap.get("SHARK_INITIAL_ENERGY").intValue(), SPAWN);
				} else {
					myModel.setNextGridState(DEATH, getX(), getY());
				}
			} else {
				if(sharkBreedTime == 0) {
					moveOrSpawnShark(newX, newY, parametersMap.get("SHARK_BREED_TIME").intValue(), parametersMap.get("SHARK_INITIAL_ENERGY").intValue(), SPAWN);
				}
				moveOrSpawnShark(newX, newY, myModel.getParameter(0, getX(), getY()), myModel.getParameter(2, getX(), getY()), MOVE);
			}
			if(sharkEnergy == 0) {
				myModel.setNextGridState(DEATH, newX, newY);
			}
		}
	}
	
	/**
	 * Creates a shark at the location given due to either movement or breeding.
	 * @param newX
	 * @param newY
	 * @param breedTime
	 * @param energyLeft
	 * @param move
	 */
	private void moveOrSpawnShark(int newX, int newY, double breedTime, double energyLeft, boolean move){
		myModel.setNextGridState(SHARK, newX, newY);
		myModel.setParameter(0, breedTime, newX, newY);
		myModel.setParameter(2, energyLeft, newX, newY);
	}
	
	/**
	 * Select the next location based on what neighbor types are around the current cell.
	 * @param neighborType
	 */
	private void selectNextLocation(int neighborType){
		List<int[]> neighbors = getDesiredNeighbors(neighborType);
		int randomIndex = (int) Math.round((neighbors.size()-1)*Math.random());
		newX = neighbors.get(randomIndex)[1];
		newY = neighbors.get(randomIndex)[2];
	}

	/**
	 * Get desired neighbor of a certain type of cell.
	 * @param type
	 * @return list of desired neighbors.
	 */
	public List<int[]> getDesiredNeighbors(int type) {
		List<int[]> neighbors = getNeighbors(myModel.getNextStates());
		List<int[]> desiredNeighbors = new ArrayList<int[]>();
		for(int i = 0; i < neighbors.size(); i++) {
			if(neighbors.get(i)[0] == type) {
				desiredNeighbors.add(neighbors.get(i));
			}
		}
		return desiredNeighbors;
	}
	
	/**
	 * After setting the next states of the cell, actually update the grid.
	 */
	public void updateState() {
		myModel.updateNextGridState(getX(), getY());
	}

	/**
	 * update Parameters of the cell based on what type of parameter it is. Works in conjunction with slider.
	 */
	@Override
	public void updateParameter(String parameter, double newValue) {
		parametersMap.put(parameter, newValue);
		if(parameter.equals("SHARK_BREED_TIME")) {
			myModel.setParameter(0, newValue, getX(), getY());
		} else if(parameter.equals("FISH_BREED_TIME")) {
			myModel.setParameter(1, newValue, getX(), getY());
		} else {
			myModel.setParameter(2, newValue, getX(), getY());
		}
	}
	
	
}