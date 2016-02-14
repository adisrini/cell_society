/**
 * Logic for SlimeMold ground cells
 * @author Michael Kuryshev
 */

package cells;

import java.util.Map;

import cellsociety_team25.SimulationModel;
import edgeCases.IEdgeCase;

public class SlimeGroundCell extends Cell implements Cloneable{
	
	//must set up a color gradient of some sort or else will have too many colors
	private int CAMP_BOOST_VALUE;
	private int EVAPORATION_RATE;
	
	private Map<String, Double> parametersMap;
	public SlimeGroundCell(int x, int y, int[] neighboringX, int[] neighboringY, int currentState, IEdgeCase edgeType, Map<Integer, String> colorMap,  Map<String, Double> parametersMap, SimulationModel myModel){
		super(x, y, neighboringX, neighboringY, currentState, edgeType, colorMap, parametersMap, myModel);
		
		CAMP_BOOST_VALUE = parametersMap.get("CAMP_BOOST_VALUE").intValue();
		EVAPORATION_RATE = parametersMap.get("EVAPORATION_RATE").intValue();
		myModel.addParameter(CAMP_BOOST_VALUE, x, y);
		myModel.addParameter(EVAPORATION_RATE, x, y);
		
		this.parametersMap = parametersMap;
	}
	
	public SlimeGroundCell(){	
	}
	
	@Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
	
	@Override
	public void updateCell() {
		decreaseCAMP();
	}
	
	public void decreaseCAMP(){
		if (getCAMP() >= EVAPORATION_RATE){
			myModel.setNextGridState(getCAMP() - EVAPORATION_RATE, getX(), getY());
		} else{
			myModel.setNextGridState(0, getX(), getY());
		}		
	}
	
	public int getCAMP(){
		return myModel.getGridStates()[getX()][getY()];
	}
	
	public void increaseCAMP(int x, int y){
		//no max value set up, should be in groundCell but just know this is for ground state model
		myModel.setNextGridState(getCAMP() + CAMP_BOOST_VALUE, x, y);
	}
	
	public void updateState() {
		myModel.updateNextGridState(getX(), getY());
	}
	
	@Override
	public void updateParameter(String parameter, double newValue) {
		parametersMap.put(parameter, newValue);
	}
}
