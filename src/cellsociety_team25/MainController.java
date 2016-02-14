package cellsociety_team25;

/**
 * The main controller that launches our Cell Society Simulation.
 * @authors Aditya Srininvasan, Harry Guo, and Michael Kuryshev
 */

import javafx.application.Application;
import javafx.stage.Stage;

public class MainController extends Application {
	
	private Stage myStage;
	public static final String TITLE = "CellSociety";
	public static final String LANGUAGE = "English";
	
	@Override
    public void start(Stage stage) {
		myStage = stage;
        SimulationModel model = new SimulationModel();

        SimulationView display = new SimulationView(model, LANGUAGE, this);
        stage.setTitle(TITLE);
        stage.setScene(display.getScene());
        stage.resizableProperty().setValue(false);
        stage.show();
    }
	
	public Stage getPrimaryStage() {
        return myStage;
    }

    public static void main (String[] args) {
        launch(args);
    }

}
