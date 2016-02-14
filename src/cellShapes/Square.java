package cellShapes;

/**
 * The Square sub-class for the square grid shapes.
 * @author Aditya Srinivasan, Harry Guo, and Michael Kuryshev
 */

import cellsociety_team25.SimulationView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Square extends CellShape {
	
	private int x;
	private int y;
	
	/**
	 * Sets the dimensions of the square and places it onto the grid based on it's coordinates.
	 */
	@Override
	public Shape getShape(int row, int col, SimulationView myView) {
		x = row;
		y = col;
		int gridSize = myView.gridSize();
		int gridWidth = SimulationView.GRID_WIDTH;
		double cellWidth = (double) gridWidth/gridSize;
		Rectangle square = new Rectangle(col*(cellWidth), row*(cellWidth), cellWidth, cellWidth);
		square.setFill(Color.GRAY);
		return square;
	}
	
	/**
	 * Get the x coordinate of the shape.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Get the y coordinate of the shape.
	 */
	public int getY() {
		return y;
	}

}
