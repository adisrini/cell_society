/**
 * Cell logic for SlimeMold
 * @author Michael Kuryshev
 */

package cells;

import java.util.Map;

import cellsociety_team25.SimulationModel;
import edgeCases.IEdgeCase;

public class SlimeCell extends Cell{
	
	private static final int SLIME = 88;
	private static final int EMPTY = 99;
	private int WIGGLE_BIAS;
	private int WIGGLE_ANGLE;
	private int DETECTION_THRESHOLD;
	private int SNIFF_ANGLE;
	
	private Map<String, Double> parametersMap;
	
	public SlimeCell(int x, int y, int[] neighboringX, int[] neighboringY, int currentState, IEdgeCase edgeType, Map<Integer, String> colorMap,  Map<String, Double> parametersMap, SimulationModel myModel){
		super(x, y, neighboringX, neighboringY, currentState, edgeType, colorMap, parametersMap, myModel);
		
		DETECTION_THRESHOLD = parametersMap.get("DETECTION_THRESHOLD").intValue();
		SNIFF_ANGLE = parametersMap.get("SNIFF_ANGLE").intValue();
		WIGGLE_BIAS = parametersMap.get("WIGGLE_BIAS").intValue();
		WIGGLE_ANGLE = parametersMap.get("WIGGLE_ANGLE").intValue();
		this.parametersMap = parametersMap;
	}
	
	public SlimeCell(){	
	}
	
	@Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
	
	@Override
	public void updateCell(){
		//start aiming upwards for convenience when we worry about angle
		int[] bestLocation = whereToMove(DETECTION_THRESHOLD);	
		moveSlime(bestLocation);
		//dropCAMP(bestLocation);
	}
	
	public int[] whereToMove(int range){
		int[] bestLocation = bestLocationInRange(range);

		bestLocation[0] = getX()+setDirection(bestLocation, 0);
		bestLocation[1] = getY()+setDirection(bestLocation, 1);
		return bestLocation;
	}
	
	public int[] bestLocationInRange(int range){
		int highValue = 0;
		int lowDistance = DETECTION_THRESHOLD;
		int[] bestLoc = new int[2];
		
		for (int n = -range; n < range; n++){
			for(int m = -(range - Math.abs(n)); m < -(range - Math.abs(n)); m++){
				//read the ground cAMP state instead of slime 
				if (myModel.getGridStates()[getX()+n][getY()+m] >= highValue){
					if(distanceFromPatch(getX()+n, getY()+m) < lowDistance){
						bestLoc[0] = getX()+n;
						bestLoc[1] = getY()+m;
					}
				}
			}
		}
		return bestLoc;
	}
	
	public int distanceFromPatch(int x, int y){
		int slimeX = getX();
		int slimeY = getY();
		
		int distance = Math.abs(slimeX - x) + Math.abs(slimeY - y);
		return distance;
	}
	
	public int setDirection(int[] location, int index){
		if(location[index] == getY()){
			return 0;
		} else if(location[index] > 0){ //do I have my Y's flipped?
			return 1;
		} 
		return -1;
	}
	
	public void moveSlime(int[] moveTo){
		//state of mold will not change so 0 could be a good set value for it
		myModel.setNextGridState(88, moveTo[0], moveTo[1]);
	}
	/*
	public void dropCAMP(int[] newLocation){
		//myModel in this case is for the ground...
		GroundCell.increaseCAMP(newLocation[0], newLocation[1]);
	}
	*/
	public void updateState() {
		myModel.updateNextGridState(getX(), getY());
	}
	
	@Override
	public void updateParameter(String parameter, double newValue) {
		// TODO Auto-generated method stub
		
	}
}
