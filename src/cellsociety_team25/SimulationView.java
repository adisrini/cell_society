package cellsociety_team25;

/**
 * The simulation of the front-end
 * @author Aditya Srinivasan, Harry Guo, and Michael Kuryshev
 */

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import cellShapes.CellShape;
import cellShapes.ShapeFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The simulation of the front-end
 * 
 * @author Aditya Srinivasan
 */
public class SimulationView {

	public static final String DEFAULT_RESOURCE_PACKAGE = "resources/";
	public static final String XML_PROPERTIES = "XMLproperties";
	public static final String STYLESHEET = "default.css";

	private FileChooser fileChooser;

	public static final int GRID_WIDTH = 450;
	public static final int BORDER_WIDTH = 1;
	private static final int MIN_SPEED = 0;
	private static final int START_SPEED = 3;
	private static final int MAX_SPEED = 10;

	private Scene myScene;
	private Button myPlayButton;
	private Button myPauseButton;
	private Button myStepButton;
	private Button myResetButton;
	private Button myRandomButton;
	private Button myGridToggleButton;
	private Button myWeightButton;
	private Slider mySpeedSlider;
	private VBox probabilityPanel;
	private VBox parametersPanel;
	private ResourceBundle myResources;
	private SimulationModel myModel;
	private Group group;
	private HBox myPanel;
	
	private int gridSize;
	
	private MenuItem saveItem;

	private XMLFileChooser xmlFileChooser;
	private XMLFileSaver xmlFileSaver;

	private List<TextField> cellWeights;
	private List<Slider> parameterSliders;

	private boolean displayGrid;
	private MenuBar myMenuBar;

	private PopulationChart myPopulationChart;
	private LineChart<Number, Number> myLineChart;
	private Map<Integer, Integer> myProbabilities = new HashMap<Integer, Integer>();
	private MainController myMain;

	private Stage myStage;

	private KeyFrame frame;
	private Timeline timeline;

	private static final int MILLISECOND_DELAY = 1000 / 60;

	/**
	 * Set up graphics of the given a model, language, and Main Controller to run off of.
	 * @param model
	 * @param language
	 * @param main
	 */
	public SimulationView(SimulationModel model, String language, MainController main) {
		myModel = model;
		myMain = main;
		myStage = myMain.getPrimaryStage();

		myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + language);

		BorderPane root = new BorderPane();
		enableBorderPane(root);

		frame = new KeyFrame(Duration.millis(10 * MILLISECOND_DELAY), e -> {
			try {
				updateGraphics(myModel.step());
				myPopulationChart.addToQueue(myModel.getStateCounts());
			} catch (SimulationException ee) {
				showError(ee.getMessage());
			}
		});

		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(frame);

		myScene = new Scene(root, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT, Color.BLACK);
		myScene.getStylesheets().add(DEFAULT_RESOURCE_PACKAGE + STYLESHEET);

	}


	private void enableBorderPane(BorderPane root) {
		root.setCenter(makeGrid());
		root.setBottom(makeControlsPanel());
		root.setLeft(makeCellProbabilitiesPanel());
		root.setTop(makeToolBar());
		root.setRight(makeParametersPanel());
		enableButtons();
	}



	private void setTitle(String title) {
		myStage.setTitle(title);
	}

	/**
	 * Make Grid
	 * @return
	 */
	private Group makeGrid() {
		group = new Group();
		group.setOnMouseClicked(e -> {
			
			int row = 0;
			int col = 0;
			
			int gridSize = myModel.getGridStates().length;
			
			for(int i = 0; i < group.getChildren().size(); i++) {
				if(group.getChildren().get(i).contains(e.getX(), e.getY())) {
					row = i % gridSize;
					col = (int)Math.floor(i/gridSize);
				}
			}
			setCoordinatesToggled(row, col);
		});
		return group;
	}

	/**
	 * Play the simulation.
	 */
	private void play() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}
	
	/**
	 * Pause the simulation.
	 */
	private void pause() {
		timeline.stop();
	}

	/**
	 * Step through the simulation.
	 */
	private void step() {
		timeline.stop();
		timeline.setCycleCount(1);
		timeline.play();
	}

	/**
	 * Initializes the population chart at top of the window.
	 */
	public void initializePopulationChart() {
		myPopulationChart = new PopulationChart(myModel.numElements(), myModel.getColorMap());
		myLineChart = myPopulationChart.getLineChart();
		myLineChart.setPrefSize(600, 100);
		myPanel.getChildren().add(myLineChart);
	}

	private void reinitialize(int[][] grid) {
		myPanel.getChildren().remove(myLineChart);
		initializePopulationChart();
		timeline.stop();
		try {
			updateGraphics(grid);
		} catch (SimulationException e) {
		}
	}

	/**
	 * @return resource file
	 */
	public ResourceBundle getMyResources() {
		return myResources;
	}

	private void chosenProbabilityRepopulate(List<TextField> cellWeights) {
		myProbabilities.clear();
		timeline.stop();
		myPanel.getChildren().remove(myLineChart);
		initializePopulationChart();

		for (int i = 0; i < cellWeights.size(); i++) {
			myProbabilities.put(i, Integer.parseInt(cellWeights.get(i).getText()));
		}

		updateGraphics(myModel.chosenProbabilityLoad(myProbabilities));
	}

	/**
	 * Changes the borders of the grid.
	 */
	private void gridToggle() {
		displayGrid = !displayGrid;
		updateGraphics(myModel.load(myModel.getGridStates()));
	}

	/**
	 * Save the parameters and condition of the current simulation.
	 */
	private void save() {
		timeline.stop();
		xmlFileSaver = new XMLFileSaver(this, myModel);
	}

	/**
	 * Save the state of the current simulation into an XML file.
	 * @param title
	 */
	public void saveFile(String title) {
		myModel.writeXML(title);
	}

	/**
	 * 
	 */
	private void open() {
		xmlFileChooser = new XMLFileChooser(this);
	}

	
	/**
	 * Load the files for simulation.
	 */
	public void loadFiles() {
		File dataFile = xmlFileChooser.getDataFile();
		File styleFile = xmlFileChooser.getStyleFile();
		if (dataFile != null && correctXMLFormat(dataFile.toString()) && styleFile != null
				&& correctXMLFormat(styleFile.toString())) {
			try {
				if (myModel.getDataFromXMLFile(dataFile) && myModel.getStyleFromXMLFile(styleFile)) {
					try {
						if (myPanel.getChildren().contains(myLineChart)) {
							myPanel.getChildren().remove(myLineChart);
						}
						initializePopulationChart();
						initializeProbabilitiesPanel();
						initializeParametersPanel();
						
						displayGrid = myModel.getOutline();

						updateGraphics(myModel.normalLoad());
						setTitle(myModel.getTitle());

					} catch (SimulationException e) {
						showError(e.getMessage());
					}
					enableButtons();

				}
			} catch (SimulationException e) {
				showError(e.getMessage());
			}
		} else {
			showError(myResources.getString("ErrorNotXML"));
		}
	}

	/**
	 * Show an error with a certain message.
	 * @param message
	 */
	private void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(myResources.getString("ErrorTitle"));
		alert.setContentText(message);
		alert.show();
	}

	private void setCoordinatesToggled(int x, int y) {
		updateGraphics(myModel.toggleCell(x, y));
	}

	private void updateGraphics(int[][] gridStates) {
		gridSize = gridStates.length;
		group.getChildren().clear();
		ShapeFactory shapeFactory = new ShapeFactory();
		CellShape myShape = shapeFactory.getCellShape(myModel.getShapeType());
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				Shape cell = myShape.getShape(i, j, this);
				cell.setFill(Color.valueOf(myModel.getColorMap().get(gridStates[i][j])));
				if(displayGrid) {
					cell.setStroke(Color.BLACK);
					cell.setStrokeWidth(2);
				}
				group.getChildren().add(cell);
			}
		}
	}

	public int gridSize() {
		return gridSize;
	}

	private boolean correctXMLFormat(String fileString) {
		return fileString.substring(fileString.length() - 3).toLowerCase().equals(myResources.getString("xml"));
	}

	/**
	 * @return current scene
	 */
	public Scene getScene() {
		return myScene;
	}

	/**
	 * Enable the buttons at the bottom of the page.
	 */
	private void enableButtons() {
		disableUIElements(saveItem, myPlayButton, myPauseButton, myStepButton, myResetButton, mySpeedSlider, myRandomButton, myGridToggleButton);
	}

	/**
	 * Disable the user interface items.
	 * @param item
	 * @param UIElements
	 */
	private void disableUIElements(MenuItem item, Node... UIElements) {
		item.setDisable(!myModel.hasLoaded());
		for(Node element : UIElements) {
			element.setDisable(!myModel.hasLoaded());
		}
	}

	/**
	 * Make all the buttons required for action.
	 * @return
	 */
	private Node makeControlsPanel() {
		HBox panel = new HBox();
		myPlayButton = makeButton("PlayCommand", event -> play());
		panel.getChildren().add(myPlayButton);
		myPauseButton = makeButton("PauseCommand", event -> pause());
		panel.getChildren().add(myPauseButton);
		myStepButton = makeButton("StepCommand", event -> step());
		panel.getChildren().add(myStepButton);
		myResetButton = makeButton("ResetCommand", event -> reinitialize(myModel.normalLoad()));
		panel.getChildren().add(myResetButton);
		myRandomButton = makeButton("RandomCommand", event -> reinitialize(myModel.randomLoad()));
		panel.getChildren().add(myRandomButton);
		myGridToggleButton = makeButton("GridToggleCommand", event -> gridToggle());
		panel.getChildren().add(myGridToggleButton);
		mySpeedSlider = makeSlider(MIN_SPEED, MAX_SPEED, START_SPEED);
		changeSpeed(mySpeedSlider);
		panel.getChildren().add(mySpeedSlider);
		panel.setSpacing(10);
		panel.setTranslateY(-10);
		panel.setAlignment(Pos.CENTER);
		return panel;
	}

	/**
	 * Make the parameters panel.
	 * @return
	 */
	private Node makeParametersPanel() {
		parametersPanel = new VBox();
		parameterSliders = new ArrayList<Slider>();
		return parametersPanel;
	}

	/**
	 * Initialize parameters panel.
	 */
	private void initializeParametersPanel() {
		parametersPanel.getChildren().clear();
		Map<String, Double> parameters = myModel.getParameterMap();
		
		int i = 0;
		for(String parameter : parameters.keySet()) {
			Label sliderLabel = new Label(parameter);
			parameterSliders.add(makeSlider(0, Integer.parseInt(myResources.getString(parameter + "_MAX")), parameters.get(parameter)));
			
			parameterSliders.get(i).valueProperty().addListener((obs, old, newValue) -> {
				myModel.updateParameterForCells(parameter, (double)newValue);
			});
			
			parametersPanel.getChildren().addAll(sliderLabel, parameterSliders.get(i));
			i++;
		}
		
		parametersPanel.setAlignment(Pos.CENTER);
	}

	private Node makeCellProbabilitiesPanel() {
		probabilityPanel = new VBox();
		cellWeights = new ArrayList<TextField>();
		return probabilityPanel;
	}

	private void initializeProbabilitiesPanel() {
		int numPatches = myModel.getColorMap().size();
		for (int i = 0; i < cellWeights.size(); i++) {
			probabilityPanel.getChildren().remove(cellWeights.get(i));
		}
		probabilityPanel.getChildren().remove(myWeightButton);
		cellWeights.clear();

		for (int i = 0; i < numPatches; i++) {
			cellWeights.add(makeCellWeighter());
			cellWeights.get(i).setStyle("-fx-control-inner-background: " + myModel.getColorMap().get(i) + ";");
		}

		probabilityPanel.getChildren().addAll(cellWeights);

		myWeightButton = makeButton("WeightCommand", event -> chosenProbabilityRepopulate(cellWeights));
		probabilityPanel.getChildren().add(myWeightButton);
		probabilityPanel.setAlignment(Pos.CENTER);
	}

	/**
	 * Make a node to user input the weights of cells.
	 * @return
	 */
	private TextField makeCellWeighter() {
		TextField text = new TextField();
		text.setMaxWidth(100);
		return text;
	}

	/**
	 * Makes a toolbar to open/save XML's.
	 * @return
	 */
	private Node makeToolBar() {
		fileChooser = new FileChooser();
		myPanel = new HBox();
		myMenuBar = new MenuBar();

		Menu fileMenu = new Menu("File");
		MenuItem openItem = makeMenuItem("OpenCommand", event -> open());
		saveItem = makeMenuItem("SaveCommand", event -> save());

		fileMenu.getItems().addAll(openItem, saveItem);
		myMenuBar.getMenus().add(fileMenu);

		myPanel.getChildren().add(myMenuBar);
		return myPanel;
	}

	/**
	 * Method to make a button.
	 * @param property
	 * @param handler
	 * @return
	 */
	public Button makeButton(String property, EventHandler<ActionEvent> handler) {
		Button button = new Button();
		String label = myResources.getString(property);
		button.setText(label);
		button.setOnAction(handler);
		return button;
	}

	private MenuItem makeMenuItem(String property, EventHandler<ActionEvent> handler) {
		MenuItem menuItem = new MenuItem();
		String label = myResources.getString(property);
		menuItem.setText(label);
		menuItem.setOnAction(handler);
		return menuItem;
	}

	/**
	 * Method to make a slider.
	 * @param minValue
	 * @param maxValue
	 * @param start
	 * @return
	 */
	private Slider makeSlider(double minValue, double maxValue, double start) {
		Slider slider = new Slider(minValue, maxValue, start);
		slider.setShowTickMarks(true);
		slider.setShowTickLabels(true);
		slider.setMajorTickUnit((maxValue - minValue) / 5);
		slider.setBlockIncrement((maxValue - minValue) / 10);
		return slider;
	}

	/**
	 * Changes the speed of the simulation.
	 * @param slider
	 */
	private void changeSpeed(Slider slider) {
		slider.valueProperty().addListener((obs, old, newValue) -> {
			timeline.setRate((double) newValue);
		});
	}
}