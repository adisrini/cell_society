package cellShapes;

/**
 * The Cell Shape Super-class in which to extend all shapes from.
 * @author Aditya Srinivasan, Harry Guo, and Michael Kuryshev
 */

import cellsociety_team25.SimulationView;
import javafx.scene.shape.Shape;

public abstract class CellShape {
	
	/**
	 * Places and sets the parameters of the shape in the visual grid based on its location.
	 * @param row
	 * @param col
	 * @param myView
	 * @return
	 */
	public abstract Shape getShape(int row, int col, SimulationView myView);
	
	/**
	 * Get location of the x-coordinate of the shape.
	 * @return x val
	 */
	public abstract int getX();
	
	/**
	 * Get location of the y-coordinate of the shape.
	 * @return y val
	 */
	public abstract int getY();
	
}
