package cellsociety_team25;

/**
 * Creates the Population Chart that displays on the Viewer.
 * @author Aditya Srinivasan, Harry Guo, and Michael Kuryshev
 */

import javafx.animation.AnimationTimer;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PopulationChart {
	
	private int numberOfEntities;
	private Map<Integer, String> myColorMap;
	
	public PopulationChart(int numberOfEntities, Map<Integer, String> colorMap) {
		this.numberOfEntities = numberOfEntities;
		this.myColorMap = colorMap;
		init();
		prepareTimeline();
		
		int i = 0;
		for(Integer color : myColorMap.keySet()) {
        	series.get(i).nodeProperty().get().setStyle("-fx-stroke: " + myColorMap.get(color) + ";");
        	i++;
        }
	}

    private static final int MAX_DATA_POINTS = 500;
    private int time = 0;
    
    private ArrayList<XYChart.Series<Number, Number>> series = new ArrayList<XYChart.Series<Number, Number>>();
    
    private ArrayList<ConcurrentLinkedQueue<Number>> dataQueues = new ArrayList<ConcurrentLinkedQueue<Number>>();

    private NumberAxis xAxis;
    
    private LineChart<Number, Number> myLineChart;
 
    private void init() {
    	
    	for(int i = 0; i < numberOfEntities; i++) {
    		series.add(new XYChart.Series<Number, Number>());
    		dataQueues.add(new ConcurrentLinkedQueue<Number>());
    	}

        xAxis = new NumberAxis(0, MAX_DATA_POINTS, MAX_DATA_POINTS / 10);
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);

        NumberAxis yAxis = new NumberAxis();
        
        myLineChart = new LineChart<Number, Number>(xAxis, yAxis) {
            @Override
            protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) {
            }
        };

        myLineChart.setAnimated(false);
        myLineChart.setHorizontalGridLinesVisible(true);
        
        myLineChart.getData().addAll(series);
        
    }

    private class AddToQueue {
    	
    	private AddToQueue(Map<Integer, Integer> stateCounts) {
    		
    		ArrayList<Integer> counts = new ArrayList<Integer>();
    		
    		for(Integer state : stateCounts.keySet()) {
    			counts.add(stateCounts.get(state));
    		}
    		
    		for(int i = 0; i < dataQueues.size(); i++) {
    			dataQueues.get(i).add(counts.get(i));
    		}
    		
    	}
    }

    //-- Timeline gets called in the JavaFX Main thread
    private void prepareTimeline() {
        // Every frame to take any data from queue and add to chart
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                addDataToSeries();
            }
        }.start();
    }

    private void addDataToSeries() {
        for (int i = 0; i < 20; i++) { //-- add 20 numbers to the plot+
            if (dataQueues.get(0).isEmpty()) break;
            for(int j = 0; j < series.size(); j++) {
            	series.get(j).getData().add(new XYChart.Data<>(time++, dataQueues.get(j).remove()));
            }
        }
        for(int i = 0; i < series.size(); i++) {
        	if(series.get(i).getData().size() > MAX_DATA_POINTS) {
        		series.get(i).getData().remove(0, series.get(i).getData().size() - MAX_DATA_POINTS);
        	}
        }
        // update
        xAxis.setLowerBound(time - MAX_DATA_POINTS);
        xAxis.setUpperBound(time - 1);
    }
    
    public LineChart<Number, Number> getLineChart() {
    	return myLineChart;
    }
    
    public void addToQueue(Map<Integer, Integer> stateCounts) {
    	AddToQueue addToQueue = new AddToQueue(stateCounts);
    }
    
}