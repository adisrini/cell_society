package cellsociety_team25;

/**
 * Class to create the Cell Grid
 * @author Aditya Srinivasan, Harry Guo, and Michael Kuryshev
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CellGrid {

	private List<Map<Integer, Integer>> states;
	
	public CellGrid() {
		this.states = new ArrayList<Map<Integer, Integer>>();
	}
	
	public void add(Map<Integer, Integer> statesFrequency) {
		states.add(statesFrequency);
	}
	
	public void display() {
		for(int i = 0; i < Math.sqrt(states.size()); i++) {
			for(int j = 0; j < Math.sqrt(states.size()); j++) {
				System.out.print(states.get(i) + " | ");
			} System.out.println();
		}
	}
	
}
