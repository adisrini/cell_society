package cellsociety_team25;

import java.util.Optional;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import xmlHandling.XMLWriter;

public class XMLFileSaver {
	
	private SimulationView myView;
	private SimulationModel myModel;
	private BorderPane root;
	private Stage myStage;
	
	public XMLFileSaver(SimulationView myView, SimulationModel myModel) {
		this.myView = myView;
		this.myModel = myModel;
		root = new BorderPane();
		dialogWindow();
		root.setCenter(makeSavingField());
		root.setBottom(makeCloseButtons());
	}
	
	private void dialogWindow() {
		myStage = new Stage();
		Scene scene = new Scene(root, 450, 450);
        myStage.setTitle("Save new XML files...");
        myStage.setScene(scene);
        scene.getStylesheets().add(SimulationView.DEFAULT_RESOURCE_PACKAGE + SimulationView.STYLESHEET);
        myStage.show();
	}
	
	private HBox makeSavingField() {
		HBox myHBox = new HBox();
		TextField xmlTitle = new TextField();
		xmlTitle.setStyle("-fx-text-fill: black;");
		Button cancelButton = myView.makeButton("Save", e -> {
			myView.saveFile(xmlTitle.getText());
			myStage.close();
		});
		myHBox.getChildren().addAll(xmlTitle, cancelButton);
		myHBox.setAlignment(Pos.CENTER);
		myHBox.setSpacing(20);
		myHBox.setTranslateY(-20);
		return myHBox;
	}
	
	private HBox makeCloseButtons() {
		HBox myHBox = new HBox();
		Button cancelButton = myView.makeButton("Cancel", e -> {
			myStage.close();
		});
		myHBox.getChildren().addAll(cancelButton);
		myHBox.setAlignment(Pos.CENTER);
		myHBox.setSpacing(20);
		myHBox.setTranslateY(-20);
		return myHBox;
	}

	public void saveFile() {
		String title = "";
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(myView.getMyResources().getString("DialogTitle"));
		dialog.setHeaderText(myView.getMyResources().getString("DialogHeader"));
		dialog.setContentText(myView.getMyResources().getString("DialogContent"));
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
		    title = result.get();
		}
		XMLWriter xmlWriter = new XMLWriter(myModel);
		xmlWriter.create(title);
	}
	
}
