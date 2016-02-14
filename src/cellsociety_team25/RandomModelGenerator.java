package cellsociety_team25;

/**
 * Generates a random grid using data taken from the XML file to match the simulation type and parameters.
 * Edit: Sliders added to hand select random concentrations of cell types
 * @author Aditya Srinivasan, Harry Guo, and Michael Kuryshev
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RandomModelGenerator {

	private Map<Integer, String> colorMap;
	private int gridSize;
	
	/**
	 * Constructor to take in a colorMap and a grid size
	 * @param colorMap
	 * @param gridSize
	 */
	public RandomModelGenerator(Map<Integer, String> colorMap, int gridSize) {
		this.colorMap = colorMap;
		this.gridSize = gridSize;
	}
	
	/**
	 * Returns a grid of equal size with randomly generated states at the locations.
	 * @return
	 */
	public int[][] createRandomGrid(){

		int[][] randGrid = new int[gridSize][gridSize];

		List<Integer> randStates = new ArrayList<Integer>();
		randStates.addAll(colorMap.keySet());
		
		for(int i = 0; i < gridSize; i++){
			for(int j = 0; j < gridSize; j++){
				Random r = new Random();
				int randNum = r.nextInt(randStates.size());
				randGrid[i][j] = randNum;
			}
		}
		return randGrid;
	}
}
