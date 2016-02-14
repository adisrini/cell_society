package cells;

/**
 * The rules for the Langton simulation.
 * @author Aditya Srinivasan, Harry Guo, and Michael Kuryshev
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

import cellsociety_team25.MainController;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class LangtonRules {

	private Map<Integer, List<String>> rulesMap;
	public static final String ERROR_RESOURCE_PACKAGE = "resources/Errors";
	public static final String LANGTON_RULES_FILE = "src/resources/langton-rules.txt";
	private ResourceBundle myErrorResources;

	/**
	 * Constructor to create a rules map based on the Langton simulation.
	 * @throws FileNotFoundException if there is no file to be found
	 */
	
	public LangtonRules() {
		myErrorResources = ResourceBundle.getBundle(ERROR_RESOURCE_PACKAGE+MainController.LANGUAGE);
		Scanner s;
		List<String> rulesList;
		rulesMap = new HashMap<Integer, List<String>>();
		try {
			s = new Scanner(new File("src/resources/langton-rules.txt"));
			rulesList = new ArrayList<String>();
			while (s.hasNext()){
			    rulesList.add(s.next());
			}
			s.close();
			
			for(String line : rulesList) {
				int state = Integer.parseInt(line.substring(0,1));
				if(!rulesMap.containsKey(state)) {
					rulesMap.put(state, new ArrayList<String>());
				}
				String rule = line.substring(1);
				List<String> rules = rulesMap.get(state);
				rules.add(rule);
				rulesMap.put(state, rules);
			}
			
		} catch (FileNotFoundException e) {
			showError(e.getMessage());
		}
	}
	
	/**
	 * Display given message as an error in the GUI.
	 */
	public void showError (String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(myErrorResources.getString("ErrorFileNotFound"));
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	/**
	 * Gets the rules for the state
	 * @param state of the cell
	 * @return a list of the rules for input state
	 */
	public List<String> getRulesForState(int state) {
		return rulesMap.get(state);
	}

}