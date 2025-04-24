package UserInterface;

import UserInterface.OpenClassController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class StudentHomeController {

    @FXML
    private AnchorPane CenterPane;

    @FXML
    private Button Home;

    @FXML
    private TextField NameText;

    @FXML
    private Button SignOutbutton;

    @FXML
    private TextField UsernameText;

    @FXML
    private ImageView profile_img;

    @FXML
    void HandleHome(MouseEvent event) {
    	ReloadPage();

    }

    @FXML
    void SignOutbutton(MouseEvent event) {
    	loadLoginPage();

    }
    
    
    
    public void initialize() 
	{
        loadDataFromDatabase();
    }

    
    private void ReloadPage() {
		// TODO Auto-generated method stub
    	try {
    	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/StudentHome.fxml"));
    	    Parent StudentHomeRoot = loader.load();

    	    // Get the controller instance
    	    StudentHomeController controller = loader.getController();
    	    
    	    // Explicitly call initialize() (optional, JavaFX calls it automatically)
    	    if (controller != null) {
    	        controller.initialize();
    	    }

    	    // Get the current stage
    	    Stage stage = (Stage) Home.getScene().getWindow();
    	    // Set the new scene
    	    stage.setScene(new Scene(StudentHomeRoot));
    
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
		
	}
	
	

    private Object openJoinClassWindow() {
		// TODO Auto-generated method stus
        
        ////loading createNewClass/////
        try 
        {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/JoinClass.fxml"));
            Parent newClassRoot = loader.load();
            
            JoinClassController controller = loader.getController();
    	    if (controller != null) 
    	    {
    	        controller.initialize();
    	    }

            // Load FXML inside CenterPane (without changing the whole scene)
    	    StackPane wrapper = new StackPane(newClassRoot);
    	    wrapper.setAlignment(Pos.CENTER);

    	    CenterPane.getChildren().clear();
    	    CenterPane.getChildren().add(wrapper);

    	    // Ensure it resizes properly
    	    wrapper.prefWidthProperty().bind(CenterPane.widthProperty());
    	    wrapper.prefHeightProperty().bind(CenterPane.heightProperty());
    
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
	    
		return null;
	}

	private Object openCourseDetails(String courseName, String courseCode) {
		// TODO Auto-generated method stub
        /////////////////////Open ////////////////Courses files///////////
		try {
		    FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/OpenCLass.fxml"));
		    Parent newClassRoot = loader.load();

		    OpenClassController controller = loader.getController();
		    if (controller != null) {
		        controller.initialize(courseName, courseCode);
		    }

		    // Wrap in AnchorPane to manually set constraints
		    AnchorPane wrapper = new AnchorPane(newClassRoot);
		    AnchorPane.setTopAnchor(newClassRoot, 20.0); // Adds spacing from the top

		    // Clear previous content
		    CenterPane.getChildren().clear();
		    CenterPane.getChildren().add(wrapper);

		    // Ensure it resizes properly
		    wrapper.prefWidthProperty().bind(CenterPane.widthProperty());
		    wrapper.prefHeightProperty().bind(CenterPane.heightProperty());

		} catch (IOException e) {
		    e.printStackTrace();
		}


		return null;

	}
	
	private void loadLoginPage() {
		// TODO Auto-generated method stub
    	try {
    	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/Login.fxml"));
    	    Parent teacherHomeRoot = loader.load();

    	    // Get the current stage
    	    Stage stage = (Stage) Home.getScene().getWindow();
    	    // Set the new scene
    	    stage.setScene(new Scene(teacherHomeRoot));
    
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
		
	}



	//////LOADING DATA
    private void loadDataFromDatabase() {
    	ArrayList<String> courses = new ArrayList<>(Arrays.asList(
    		    "Software Engineering", "CS1004",
    		    "Software Architecture", "CS1003",
    		    "Software Design", "CS1002",
    		    "Software Ddesign", "CS1004",
    		    "Software Dedsign", "CS1005",
    		    "Software Dedsign", "CS1006",
    		    "Software Dedsign", "CS1007",
    		    "Software Dedsign", "CS1008",
    		    "Software Dedsign", "CS1009",
    		    "Software Dedsign", "CS1010",
    		    "Software Dedsign", "CS1011",
    		    "Software Dedsign", "CS1012",
    		    "Software Dedsign", "CS1013",
    		    "Software Dedsign", "CS1014",
    		    "Software Dedsign", "CS1015",
    		    "Software Dedsign", "CS1016"
    		));

    		// Create a GridPane for displaying courses
    		GridPane gridPane = new GridPane();
    		gridPane.setHgap(20);
    		gridPane.setVgap(20);
    		gridPane.setStyle("-fx-background-color: #ffffff; -fx-padding: 20px; -fx-border-radius: 10px; -fx-border-color: #cccccc;");

    		// Ensure GridPane fills available space
    		gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

    		// Configure GridPane to expand dynamically
    		for (int i = 0; i < 3; i++) { // 3 columns
    		    ColumnConstraints colConst = new ColumnConstraints();
    		    colConst.setHgrow(Priority.ALWAYS);
    		    colConst.setPercentWidth(100.0 / 3); // Equal width
    		    gridPane.getColumnConstraints().add(colConst);
    		}

    		int row = 0, col = 0;
    		for (int i = 0; i < courses.size(); i += 2) {
    		    String courseName = courses.get(i);
    		    String courseCode = courses.get(i + 1);

    		    // Create a VBox for each course
    		    VBox courseBox = new VBox(8);
    		    courseBox.setStyle("-fx-border-color: #444; " +
    		                       "-fx-border-width: 1px; " +
    		                       "-fx-background-color: linear-gradient(to bottom, #f8f9fa, #e9ecef); " +
    		                       "-fx-background-radius: 10px; " +
    		                       "-fx-border-radius: 10px; " +
    		                       "-fx-padding: 15px; " +
    		                       "-fx-alignment: center;");
    		    courseBox.setMinSize(180, 120);
    		    courseBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

    		    // Set action on click
    		    courseBox.setOnMouseClicked(event -> openCourseDetails(courseName, courseCode));

    		    // Labels
    		    Label courseLabel = new Label(courseName);
    		    courseLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    		    courseLabel.setStyle("-fx-text-fill: #333;");

    		    Label codeLabel = new Label(courseCode);
    		    codeLabel.setFont(Font.font("Arial", 12));
    		    codeLabel.setStyle("-fx-text-fill: gray;");

    		    courseBox.getChildren().addAll(courseLabel, codeLabel);
    		    GridPane.setMargin(courseBox, new Insets(20));

    		    gridPane.add(courseBox, col, row);

    		    col++;
    		    if (col > 2) { // 3 columns per row
    		        col = 0;
    		        row++;
    		    }
    		}

    		// 🔹 ADD "Create New Class" BUTTON AT THE END 🔹
    		Button createClassButton = new Button("+ Join New Class");
    		createClassButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 15px; -fx-border-radius: 5px;");
    		createClassButton.setMinSize(180, 40);
    		createClassButton.setOnAction(event -> openJoinClassWindow());

    		// Wrap button in a VBox for alignment
    		VBox buttonBox = new VBox(createClassButton);
    		buttonBox.setAlignment(Pos.CENTER);
    		GridPane.setMargin(buttonBox, new Insets(40));

    		gridPane.add(buttonBox, col, row); // Add at the next available grid position

    		// 🔹 WRAP GridPane in a SCROLLPANE 🔹
    		ScrollPane scrollPane = new ScrollPane(gridPane);
    		scrollPane.setFitToWidth(true);  // Allow content to stretch horizontally
    		scrollPane.setPannable(true);    // Enable scrolling
    		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Hide horizontal scrollbar
    		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Show vertical scrollbar when needed

    		// Set ScrollPane to not overlap with TopHead
    		scrollPane.setStyle("-fx-background-color: transparent;");
    		scrollPane.setPrefHeight(CenterPane.getHeight()); // Ensuring it fits within CenterPane

    		StackPane wrapper = new StackPane();
    		wrapper.getChildren().add(scrollPane);
    		wrapper.setAlignment(Pos.CENTER);

    		// Bind size so that it resizes correctly
    		wrapper.prefWidthProperty().bind(CenterPane.widthProperty());
    		wrapper.prefHeightProperty().bind(CenterPane.heightProperty());

    		CenterPane.getChildren().clear();
    		CenterPane.getChildren().add(wrapper);
	}

}
