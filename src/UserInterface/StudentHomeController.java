package UserInterface;

import UserInterface.OpenClassController;
import controllers.StudentHandler;
import businessLayer.Class;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	StudentHandler studentHandler;

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

    }
    
    public void initData(StudentHandler handler)
    {
    	this.studentHandler = handler;
    	if (studentHandler != null) {
            UsernameText.setText(studentHandler.getStudent().getUsername());
            NameText.setText(studentHandler.getStudent().getName());
        }
        loadDataFromDatabase();
    }

    
    private void ReloadPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/StudentHome.fxml"));
            Parent studentrHomeRoot = loader.load();
            
            StudentHomeController controller = loader.getController();
            if (controller != null) {
                controller.initData(studentHandler);
            }
            
            Stage stage = (Stage) Home.getScene().getWindow();
            stage.setScene(new Scene(studentrHomeRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	

    private Object openJoinClassWindow() 
    {
		// TODO Auto-generated method stus
        
        ////loading createNewClass/////
        try 
        {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/JoinClass.fxml"));
            Parent newClassRoot = loader.load();
            
            JoinClassController controller = loader.getController();
    	    if (controller != null) 
    	    {
    	        controller.initData(studentHandler);
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
	    // Fetch the list of classes the student is enrolled in
	    List<Class> classes = studentHandler.getClasses();

	    // Create a GridPane for displaying courses
	    GridPane gridPane = new GridPane();
	    gridPane.setHgap(20);
	    gridPane.setVgap(20);
	    gridPane.setStyle("-fx-background-color: #ffffff; -fx-padding: 20px; -fx-border-radius: 10px; -fx-border-color: #cccccc;");
	    gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

	    // Configure GridPane to expand dynamically (3 columns)
	    for (int i = 0; i < 3; i++) {
	        ColumnConstraints colConst = new ColumnConstraints();
	        colConst.setHgrow(Priority.ALWAYS);
	        colConst.setPercentWidth(100.0 / 3);
	        gridPane.getColumnConstraints().add(colConst);
	    }

	    int row = 0, col = 0;
	    for (Class cls : classes) {
	        String courseName = cls.getClassName();
	        String courseCode = cls.getClassCode();

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

	        // Set action on click: open class details
	        courseBox.setOnMouseClicked(e -> openCourseDetails(courseName, courseCode));

	        // Course name label
	        Label courseLabel = new Label(courseName);
	        courseLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
	        courseLabel.setStyle("-fx-text-fill: #333;");

	        // Course code label
	        Label codeLabel = new Label(courseCode);
	        codeLabel.setFont(Font.font("Arial", 12));
	        codeLabel.setStyle("-fx-text-fill: gray;");

	        courseBox.getChildren().addAll(courseLabel, codeLabel);
	        GridPane.setMargin(courseBox, new Insets(20));
	        gridPane.add(courseBox, col, row);

	        col++;
	        if (col > 2) {
	            col = 0;
	            row++;
	        }
	    }

	    // "Join New Class" button at the end
	    Button joinClassButton = new Button("+ Join New Class");
	    joinClassButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 15px; -fx-border-radius: 5px;");
	    joinClassButton.setMinSize(180, 40);
	    joinClassButton.setOnAction(e -> openJoinClassWindow());

	    VBox buttonBox = new VBox(joinClassButton);
	    buttonBox.setAlignment(Pos.CENTER);
	    GridPane.setMargin(buttonBox, new Insets(40));
	    gridPane.add(buttonBox, col, row);

	    // Wrap GridPane in a ScrollPane
	    ScrollPane scrollPane = new ScrollPane(gridPane);
	    scrollPane.setFitToWidth(true);
	    scrollPane.setPannable(true);
	    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
	    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
	    scrollPane.setStyle("-fx-background-color: transparent;");

	    // Add to CenterPane
	    StackPane wrapper = new StackPane(scrollPane);
	    wrapper.setAlignment(Pos.CENTER);
	    wrapper.prefWidthProperty().bind(CenterPane.widthProperty());
	    wrapper.prefHeightProperty().bind(CenterPane.heightProperty());

	    CenterPane.getChildren().setAll(wrapper);
	}


}
