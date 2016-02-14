package cellShapes;

/**
 * The Triangle sub-class for the triangle grid shapes.
 * @author Aditya Srinivasan, Harry Guo, and Michael Kuryshev
 */

import cellsociety_team25.SimulationView;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class Triangle extends CellShape {

	private int x;
	private int y;

	/**
	 * Sets the dimensions of the square and places it onto the grid based on it's coordinates.
	 */
	@Override
	public Shape getShape(int row, int col, SimulationView myView) {
		int gridSize = myView.gridSize();
		int gridWidth = SimulationView.GRID_WIDTH;

		double sideLength = 2.0 * gridWidth / (gridSize + 1);
		double triangleHeight = sideLength * (Math.sqrt(3) / 2);

		double x1 = 0, x2 = 0, x3 = 0, y1 = 0, y2 = 0, y3 = 0;

		int index = row * gridSize + col;

		if (row % 2 == 0 && index % 2 == 0) {
			int shiftIndex = index % gridSize;
			x1 = (sideLength * (shiftIndex / 2)) + sideLength / 2;
			y1 = triangleHeight * (row) + triangleHeight;
			x2 = x1 - sideLength / 2;
			y2 = y1 - triangleHeight;
			x3 = x1 + sideLength / 2;
			y3 = y2;
		} else if (row % 2 == 0 && index % 2 == 1) {
			int shiftIndex = (index - 1) % gridSize;
			x2 = (sideLength * ((shiftIndex) / 2)) + sideLength / 2;
			y2 = triangleHeight * (row) + triangleHeight;
			x1 = x2 + sideLength / 2;
			y1 = y2 - triangleHeight;
			x3 = x2 + sideLength;
			y3 = y2;
		} else if (row % 2 == 1 && index % 2 == 0) {
			int shiftIndex = index % (gridSize);
			x1 = (sideLength * (shiftIndex / 2)) + sideLength / 2;
			y1 = (row - 1) * triangleHeight + triangleHeight;
			x2 = x1 - sideLength / 2;
			y2 = y1 + triangleHeight;
			x3 = x1 + sideLength / 2;
			y3 = y2;
		} else if (row % 2 == 1 && index % 2 == 1) {
			int shiftIndex = (index - 1) % gridSize;
			x2 = (sideLength * ((shiftIndex) / 2)) + sideLength / 2;
			y2 = (row - 1) * triangleHeight + triangleHeight;
			x1 = x2 + sideLength / 2;
			y1 = y2 + triangleHeight;
			x3 = x2 + sideLength;
			y3 = y2;
		}

		Double[] coordinates = new Double[] { x1, y1, x2, y2, x3, y3 };

		for(int i = 0; i < coordinates.length; i++) {
			coordinates[i] = coordinates[i]/(2);
		}

		Polygon thisTriangle = new Polygon();
		thisTriangle.getPoints().addAll(coordinates);
		return thisTriangle;
	}

	/**
	 * Get the x coordinate of the shape.
	 */
	@Override
	public int getX() {
		return this.x;
	}

	/**
	 * Get the x coordinate of the shape.
	 */
	@Override
	public int getY() {
		return this.y;
	}
}
