package cellShapes;

/**
 * Get the type of shape to create.
 * @author Aditya Srinivasan, Harry Guo, and Michael Kuryshev.
 */

import java.util.Map;

public class ShapeFactory {
	
	Map<String, CellShape> cellShapeMap;
	
	public ShapeFactory() {
		
	}
	
	public CellShape getCellShape(String shapeType) {
		if(shapeType.equals("Square")) {
			return new Square();
		} else if(shapeType.equals("Hexagon")) {
			return new Hexagon();
		} else if(shapeType.equals("Triangle")) {
			return new Triangle();
		}
		return null;
	}

}
