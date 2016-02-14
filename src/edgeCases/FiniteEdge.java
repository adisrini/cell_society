package edgeCases;

/**
 * Designs the nature of the edges of the game based on the Finite edge case type.
 * @author Aditya Srinivasan, Harry Guo, and Michael Kuryshev
 */

public class FiniteEdge implements IEdgeCase{

	@Override
	public int coordinateAfterCrossingEdge(int current, int change, int edgeLength){
		int finalPosition = current + change;
		if(finalPosition < 0 || finalPosition >= edgeLength) {
			return -1;
		}
		return finalPosition;
	}
}