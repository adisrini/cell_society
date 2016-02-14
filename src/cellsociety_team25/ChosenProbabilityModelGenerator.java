package cellsociety_team25;

/**
 * Generates a grid of cell's based on probability to produce each cell passed by user
 * @author Aditya Srinivasan, Harry Guo, and Michael Kuryshev
 */

import java.util.Map;
import java.util.Random;

public class ChosenProbabilityModelGenerator {
	
	private Random randomVar = new Random();
	private Map<Integer, Integer> myProbabilities;
	private int gridSize;
	
	public ChosenProbabilityModelGenerator(int gridSize, Map<Integer, Integer> colorProbabilities){
		this.gridSize = gridSize;
		
		myProbabilities = colorProbabilities;
		
		createWeightedGrid();
	}
	
	public int[][] createWeightedGrid(){
		int sumOfWeights = getSumOfProbabilities(myProbabilities);
		int[][] weightedGrid = new int[gridSize][gridSize];
		
		for (int n=0; n<gridSize; n++){
			for (int m=0; m<gridSize; m++){
				weightedGrid[n][m] = makeWeightedCell(n, m, sumOfWeights);
			}
		}
		return weightedGrid;
	}
	
	private int getSumOfProbabilities(Map<Integer, Integer> probabilities){
		int probabilityOutOf = 0;
		
		for (int n: probabilities.keySet()){
			probabilityOutOf += probabilities.get(n);
		}
		return probabilityOutOf;
	}
	
	public int makeWeightedCell(int n, int m, int outOf){
		double rand = randomVar.nextInt(outOf);
		int sumOfRands = 0;
		
		while (sumOfRands <= outOf){
			for (int color: myProbabilities.keySet()){
				if(rand <= (sumOfRands + myProbabilities.get(color)-1)){
					return color;
				}
				sumOfRands += myProbabilities.get(color);
			}
		}
		//in case of failure, return 0.
		return 0;
	}
}
