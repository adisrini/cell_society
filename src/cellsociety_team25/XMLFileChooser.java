package cellsociety_team25;

import java.io.File;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class XMLFileChooser {
	
	private BorderPane root;
	private File dataFile;
	private File styleFile;
	private SimulationView myView;
	private FileChooser fileChooser;
	private Stage myStage;
	
	private Button openDataButton;
	private Button openStyleButton;
	
	public XMLFileChooser(SimulationView myView) {
		root = new BorderPane();
		fileChooser = new FileChooser();
		this.myView = myView;
		dialogWindow();
		root.setCenter(makeOpenButtons());
		root.setBottom(makeCloseButtons());
		openStyleButton.setDisable(true);
		
	}
	
	private void dialogWindow() {
		myStage = new Stage();
		Scene scene = new Scene(root, 450, 450);
        myStage.setTitle("Select XML files...");
        myStage.setScene(scene);
        scene.getStylesheets().add(SimulationView.DEFAULT_RESOURCE_PACKAGE + SimulationView.STYLESHEET);
        myStage.show();
	}
	
	private VBox makeOpenButtons() {
		VBox myVBox = new VBox();
		openDataButton = myView.makeButton("OpenDataCommand", e -> openDataFile());
		openStyleButton = myView.makeButton("OpenStyleCommand", e -> openStyleFile());
		
		myVBox.setSpacing(20);
		
		myVBox.getChildren().addAll(openDataButton, openStyleButton);
		myVBox.setAlignment(Pos.CENTER);
		return myVBox;
	}
	
	private HBox makeCloseButtons() {
		HBox myHBox = new HBox();
		Button okayButton = myView.makeButton("Okay", e -> {
			myView.loadFiles();
			myStage.close();
		});
		Button cancelButton = myView.makeButton("Cancel", e -> {
			myStage.close();
		});
		myHBox.getChildren().addAll(okayButton, cancelButton);
		myHBox.setAlignment(Pos.CENTER);
		myHBox.setSpacing(20);
		myHBox.setTranslateY(-20);
		return myHBox;
	}
	
	private void openDataFile() {
		dataFile = fileChooser.showOpenDialog(myStage);
		openStyleButton.setDisable(false);
	}
	
	private void openStyleFile() {
		styleFile = fileChooser.showOpenDialog(myStage);
	}
	
	public File getDataFile() {
		return dataFile;
	}
	
	public File getStyleFile() {
		return styleFile;
	}

}
