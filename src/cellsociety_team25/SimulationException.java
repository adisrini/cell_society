package cellsociety_team25;

/**
 * The Simulation Exception that is thrown in case of Exceptions.
 * @author Aditya Srinivasan, Harry Guo, and Michael Kuryshev
 */

public class SimulationException extends RuntimeException {

	public SimulationException() {
	}
	
	public SimulationException(String message) {
		super(message);
	}
	
	public SimulationException(Throwable cause) {
		super(cause);
	}
	
	public SimulationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public SimulationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
}

