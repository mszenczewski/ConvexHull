package szenczewski.cse355.project.convexhull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class ConvexHullApplication extends Application
{
	private Stage primaryStage;
	private PointSet pointSet;
	private PointSet convexHull;
	private AnglePointSet sortedPointSet;
	private LineChart<Number,Number> pointChart;
	private LineChart<Number,Number> convexChart;
	private NumberAxis xAxis = new NumberAxis(0, 0, 0);
	private NumberAxis yAxis = new NumberAxis(0, 0, 0);
    private XYChart.Series<Number, Number> dataSeries;
	private ObservableList<String> points = FXCollections.observableArrayList();
	private ObservableList<String> convexHullPoints = FXCollections.observableArrayList();

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		this.primaryStage = primaryStage; //shadowing
		
		BorderPane root = new BorderPane();

		MenuBar menuBar = MenuBar();
		
		TabPane tabPane = TabPane();
				
		root.setTop(menuBar);
		root.setCenter(tabPane);
		
		Scene scene = new Scene(root, 500, 500);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Convex Hull Application");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	private MenuBar MenuBar()
	{
		MenuItem openMenuItem = new MenuItem("Open File...");
		MenuItem randomMenuItem = new MenuItem("Generate Random...");
		MenuItem exitMenuItem = new MenuItem("Exit...");
		MenuItem howMenuItem = new MenuItem("How To...");
		MenuItem aboutMenuItem = new MenuItem("About");
		MenuItem exportCHMenuItem = new MenuItem("Export Convex Hull...");
		
		Menu file = new Menu("File", null, openMenuItem, randomMenuItem, exportCHMenuItem, new SeparatorMenuItem(), exitMenuItem);
		Menu help = new Menu("Help", null, howMenuItem, aboutMenuItem);
		
		MenuBar menuBar = new MenuBar(file, help);
		
		openMenuItem.setOnAction(OpenFile);
		exitMenuItem.setOnAction(Exit); 
		howMenuItem.setOnAction(HowTo); 
		aboutMenuItem.setOnAction(About); 
		randomMenuItem.setOnAction(Random);
		exportCHMenuItem.setOnAction(ExportCH);
		
		return menuBar;
	}
	
	private TabPane TabPane()
	{
		StackPane chartStack = Chart();
		
		HBox pointSet = PointSet();
		
		points.add("No data available."); //default value
		convexHullPoints.add(""); //default value

		Tab dataTab = new Tab("Data", pointSet);
		Tab graphTab = new Tab("Graph", chartStack);
		
		dataTab.setClosable(false);
		graphTab.setClosable(false);
						
		TabPane tabPane = new TabPane(dataTab, graphTab);
				
		return tabPane;
	}
	
	private StackPane Chart()
	{
	    pointChart = new LineChart<>(xAxis, yAxis);
	    convexChart = new LineChart<>(xAxis, yAxis);
	    
		xAxis.setTickLabelsVisible(false);
		yAxis.setTickLabelsVisible(false);
		
		xAxis.setAutoRanging(true);
		yAxis.setAutoRanging(true);

	    pointChart.setAnimated(false);
	    convexChart.setAnimated(false);
	    
	    pointChart.setCreateSymbols(true);
	    convexChart.setCreateSymbols(true);
	    
	    pointChart.setPadding(new Insets(0, 20, 0, 0));
	    convexChart.setPadding(new Insets(0, 20, 0, 0));
	    
		pointChart.setLegendVisible(false);
		convexChart.setLegendVisible(false);
		
		pointChart.setOpacity(0);
		convexChart.setOpacity(0);
		
		pointChart.getStylesheets().add("pointChart.css");
		convexChart.getStylesheets().add("convexChart.css");
		
		StackPane sp = new StackPane(pointChart, convexChart);
		
		return sp;
	}
	
	private HBox PointSet() 
	{			
		ListView<String> pointsList = new ListView<String>(points);
		ListView<String> convexList = new ListView<String>(convexHullPoints);

		Label pointsLabel = new Label("Point Set:");
		Label convexLabel = new Label("Convex Hull:");
		
		pointsLabel.setPadding(new Insets(4, 0, 4, 0));
		convexLabel.setPadding(new Insets(4, 0, 4, 0));

		VBox pointsBox = new VBox(pointsLabel, pointsList);
		VBox convexBox = new VBox(convexLabel, convexList);
		
		pointsBox.setPadding(new Insets(0, 12, 12, 4));
		convexBox.setPadding(new Insets(0, 4, 12, 0));
		
		HBox hbox = new HBox(pointsBox, convexBox);
		
		hbox.setPadding(new Insets(8, 8, 8, 8));
				
		return hbox;
	}
	
	private PointSet ConvexHull()
	{		
		sortedPointSet = new AnglePointSet(pointSet);
				
		sortedPointSet.Sort();
				
		Stack<Point> pointStack = new Stack<Point>(); 
		
		int numPoints = sortedPointSet.GetNumPoints();
		
		pointStack.push(sortedPointSet.GetPointAtIndex(0));
		pointStack.push(sortedPointSet.GetPointAtIndex(1));
		
		for	(int i = 2; i < numPoints; i++)
		{	
			while (	pointStack.size() >= 2 &&
					CCW(pointStack.elementAt(pointStack.size()-2), pointStack.elementAt(pointStack.size()-1), sortedPointSet.GetPointAtIndex(i)) <= 0)
			{
				pointStack.pop();
			}
			
			pointStack.push(sortedPointSet.GetPointAtIndex(i));
		}
		
		int numCH = pointStack.size();
		
		PointSet ps = new PointSet();
		
		for (int i = 0; i < numCH; i++)
		{
			Point p = pointStack.elementAt(i);
			ps.AddPoint(p);
		}
		
		return ps;
	}
	
	private int CCW(Point a, Point b, Point c)
	{
	    return (b.GetX() - a.GetX()) * (c.GetY() - a.GetY()) - (b.GetY() - a.GetY()) * (c.GetX() - a.GetX());
	}

	private EventHandler<ActionEvent> Exit = new EventHandler<ActionEvent>() 
	{
		public void handle(ActionEvent t) 
		{
			System.exit(0);
		}
	};
	
	private EventHandler<ActionEvent> HowTo = new EventHandler<ActionEvent>() 
	{
		public void handle(ActionEvent t) 
		{
			Alert a = new Alert(AlertType.NONE);
	        
			a.setHeaderText("How To Use Convex Hull Application");
	        a.setResizable(false);
	        
	        String readme = "help.txt not found";
	        
			try
			{
				Scanner s = new Scanner(new FileReader("src/help.txt"));
				
				readme = "";
				
				while (s.hasNextLine())
				{
					readme += s.nextLine();
					readme += "\n";
				}
				
				s.close();
				
			} 
			catch (FileNotFoundException e) {}
	          
			a.setContentText(readme);
			a.getDialogPane().getButtonTypes().add(ButtonType.OK);
	        a.showAndWait();
		}
	};
	
	private EventHandler<ActionEvent> About = new EventHandler<ActionEvent>() 
	{
		public void handle(ActionEvent t) 
		{
			Alert a = new Alert(AlertType.NONE);
	        
			a.setHeaderText("About");
	        a.setResizable(false);
	        	
	        String about = "about.txt not found";
	        
			try
			{
				Scanner s = new Scanner(new FileReader("src/about.txt"));
				
				about = "";
				
				while (s.hasNextLine())
				{
					about += s.nextLine();
					about += "\n";
				}
				
				s.close();
				
			} 
			catch (FileNotFoundException e) {} 
	        
	        a.setContentText(about);
			a.getDialogPane().getButtonTypes().add(ButtonType.OK);
	        a.showAndWait();
		}
	};
	
	private EventHandler<ActionEvent> Random = new EventHandler<ActionEvent>() 
	{
		public void handle(ActionEvent t)
		{
			PrintStream output = null;
			
			try
			{
				output = new PrintStream(new File("random.txt"));
			} 
			catch (FileNotFoundException e) {} 
			
			int max = ThreadLocalRandom.current().nextInt(10, 100 + 1);
			
			output.println(max);
			
			for	(int i = 0; i < max; i++)
			{
				String s = "[";
				
				s += ThreadLocalRandom.current().nextInt(-200, 200 + 1);
				
				s += ",";
				
				s += ThreadLocalRandom.current().nextInt(-200, 200 + 1);
				
				s += "]";
				
				output.println(s);
			}
		}
	};
	
	private EventHandler<ActionEvent> ExportCH = new EventHandler<ActionEvent>() 
	{
		public void handle(ActionEvent t)
		{
			PrintStream output = null;
			
			try
			{
				output = new PrintStream(new File("convex_hull.txt"));
			} 
			catch (FileNotFoundException e) {} 
			
			output.println(convexHull.GetNumPoints());
			
			for	(int i = 0; i < convexHull.numPoints; i++)
			{
				String s = "[";
				
				s += convexHull.GetPointAtIndex(i).GetX();
				
				s += ",";
				
				s += convexHull.GetPointAtIndex(i).GetY();
				
				s += "]";
				
				output.println(s);
			}
		}
	};

	private EventHandler<ActionEvent> OpenFile = new EventHandler<ActionEvent>() 
	{
		@SuppressWarnings("unchecked")
		public void handle(ActionEvent t) 
		{
			FileChooser fc = new FileChooser();
			
			fc.setTitle("Open Point Data File");
			fc.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
			
			File inputFile = fc.showOpenDialog(primaryStage);
			
			try
			{
				PointSet newPointSet = new PointSet();
				
				Scanner inputFromFile = new Scanner(new FileReader(inputFile));
				
				newPointSet.Read(inputFromFile);
								
				pointSet = newPointSet;
								
				int numPoints = pointSet.GetNumPoints();
				
				points.clear();
				convexHullPoints.clear();
				pointChart.getData().clear();
				convexChart.getData().clear();
								
				for (int i = 0; i < numPoints; i++)
				{		
					points.add(pointSet.GetPointAtIndex(i).toString());
				}
												
				convexHull = ConvexHull();

				int convexPoints = convexHull.GetNumPoints();
				
				for (int i = 0; i < convexPoints; i++)
				{		
					convexHullPoints.add(convexHull.GetPointAtIndex(i).toString());
				}
				
				pointChart.setOpacity(100);
				convexChart.setOpacity(100);
				
				dataSeries = new XYChart.Series<Number, Number>(); 
								
				for	(int i = 0; i < numPoints; i++)
				{
					dataSeries.getData().add(new XYChart.Data<Number, Number>(pointSet.GetPointAtIndex(i).GetX(), pointSet.GetPointAtIndex(i).GetY()));
				}
																
				pointChart.getData().addAll(dataSeries);
												
				for (int i = 1; i < convexHull.GetNumPoints(); i++)
				{
					Point p1 = convexHull.GetPointAtIndex(i-1);
					Point p2 = convexHull.GetPointAtIndex(i);
					
					XYChart.Series<Number, Number> xys = new XYChart.Series<Number, Number>();
					
					xys.getData().add(new XYChart.Data<Number, Number>(p1.GetX(), p1.GetY()));
					xys.getData().add(new XYChart.Data<Number, Number>(p2.GetX(), p2.GetY()));
					
					convexChart.getData().addAll(xys);
				}
								
				Point p1 = convexHull.GetPointAtIndex(convexHull.GetNumPoints() - 1);
				Point p2 = convexHull.GetPointAtIndex(0);
				
				XYChart.Series<Number, Number> xys = new XYChart.Series<Number, Number>();
				
				xys.getData().add(new XYChart.Data<Number, Number>(p1.GetX(), p1.GetY()));
				xys.getData().add(new XYChart.Data<Number, Number>(p2.GetX(), p2.GetY()));
				
				convexChart.getData().addAll(xys);
			}
			catch(Exception e) {}
		}
	};
}
