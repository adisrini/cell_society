package cellShapes;

/**
 * The Hexagon sub-class for the Hexagon grid shapes.
 * @author Aditya Srinivasan, Harry Guo, and Michael Kuryshev
 */

import cellsociety_team25.SimulationView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class Hexagon extends CellShape {
	
	private int x;
	private int y;

	/**
	 * Sets the dimensions of the hexagon and places it onto the grid based on it's coordinates.
	 */
	@Override
	public Shape getShape(int row, int col, SimulationView myView) {
		x = row;
		y = col;
		int gridSize = myView.gridSize();
		int gridWidth = SimulationView.GRID_WIDTH;
		
		double hexLength = gridWidth/(gridSize + (gridSize + 1) * 0.5);
		double hexHeight = hexLength *Math.sqrt(3);
		double triangleHeight = (hexLength * (Math.sqrt(3) / 2));
		
		int index = row * gridSize + col;
		
		double x1, x2, x3, x4, x5, x6, y1, y2, y3, y4, y5, y6;

		if(index % 2 == 0) {
			int shiftIndex = index % gridSize;
			x1 = 0.5 * hexLength + 3 * hexLength * (shiftIndex / 2);
			y1 = row * hexHeight;
			x2 = x1 + hexLength;
			y2 = y1;
			x3 = x2 + 0.5 * hexLength;
			y3 = y1 + triangleHeight;
			x4 = x2;
			y4 = y1 + hexHeight;
			x5 = x1;
			y5 = y4;
			x6 = x1 - 0.5 * hexLength;
			y6 = y3;
		} else {
			int shiftIndex = (index - 1) % gridSize;
			x1 = 2 * hexLength + 3 * hexLength * (shiftIndex / 2);
			y1 = row * hexHeight + triangleHeight;
			x2 = x1 + hexLength;
			y2 = y1;
			x3 = x2 + 0.5 * hexLength;
			y3 = y1 + triangleHeight;
			x4 = x2;
			y4 = y1 + hexHeight;
			x5 = x1;
			y5 = y4;
			x6 = x1 - 0.5 * hexLength;
			y6 = y3;
		}
		
		Double[] coordinates = new Double[] {x1, y1, x2, y2, x3, y3, x4, y4, x5, y5, x6, y6};
		
		for(int i = 0; i < coordinates.length; i++) {
			coordinates[i] = coordinates[i]/1.25;
		}
		
		Polygon hex = new Polygon();
		hex.getPoints().addAll(coordinates);
		hex.setStroke(Color.LIGHTGRAY);
		hex.setStrokeWidth(1);
		
		return hex;
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
