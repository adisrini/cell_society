package edgeCases;

/**
 * Designs the nature of the edges of the game based on the Toroidal edge case type.
 * @author Aditya Srinivasan, Harry Guo, and Michael Kuryshev
 */

public class ToroidEdge implements IEdgeCase {

	@Override
	public int coordinateAfterCrossingEdge(int current, int change, int edgeLength) {
		int finalPosition = current + change;
		if ( finalPosition < 0 ){
			return finalPosition + edgeLength;
		}
		else if (finalPosition >= edgeLength) {
			return finalPosition - edgeLength;
		}
		return finalPosition;
		
	}

}