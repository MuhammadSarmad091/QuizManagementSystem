package UserInterface;

import controllers.TeacherHandler;
import java.io.IOException;
import java.util.List;
import businessLayer.Class;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class DisplayClassController {
    
    private TeacherHandler teacherHandler; // New field to hold TeacherHandler
    
    // This method now accepts teacherHandler
    public void initData(TeacherHandler handler) {
        this.teacherHandler = handler;
        loadTabPane();
    }

    // Existing initialize method to set class info and load tabs.
    public void initialize(String ClassName, String ClassCode) {
        classname.setText(ClassName);
        classcode.setText(ClassCode);
    }

    @FXML
    private TextField classname;

    @FXML
    private TextField classcode;
    
    @FXML
    private TabPane TabPane_Sec;
    
    @FXML
    private Button Add_teacher;
    
    @FXML
    private Button remove_class;
    
    // When a tab is selected, load the corresponding FXML.
    private void loadTabPane() {
        Tab selectedTab = TabPane_Sec.getSelectionModel().getSelectedItem();
        if (selectedTab != null) {
            loadFXMLForTab(selectedTab);
        }
    }

    
    private void loadFXMLForTab(Tab tab) {
        String tabTitle = tab.getText(); // Get the tab's title
        String fxmlFile = "";
        switch (tabTitle) {
            case "Quizzes":
                fxmlFile = "QuizesTab.fxml";
                break;
            case "Students":
                fxmlFile = "StudentsTab.fxml";
                break;
            case "Teachers":
                fxmlFile = "TeachersTab.fxml";
                break;
            default:
                return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent newContent = loader.load();
            
            // Pass teacherHandler to the tab controller if applicable
            Object controller = loader.getController();
            if (controller instanceof QuizesTabController) {
                ((QuizesTabController) controller).initData(teacherHandler);
            }
            // Similarly, if you have controllers for Students and Teachers tabs, call initData(handler) on them.
            
            StackPane wrapper = new StackPane();
            wrapper.getChildren().add(newContent);
            wrapper.prefWidthProperty().bind(TabPane_Sec.widthProperty());
            wrapper.prefHeightProperty().bind(TabPane_Sec.heightProperty());
            tab.setContent(wrapper);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void handle_Add_teacher(MouseEvent event) 
    {
    	
    	try {
	        // Load the FXML file 
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/AddTeacherCLass.fxml"));
	        Parent root = loader.load();

	        // Get controller instance
	        AddTeacherClassController controller = loader.getController();
	        
	        // Pass student data to initialize
	        if (controller != null) {
	            
				controller.initialize();
	        }

	        // Create new stage
	        Stage stage = new Stage();
	        stage.setTitle("Check Objective Submission");
	        stage.setScene(new Scene(root));
	        stage.show();

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
    	

    }

    @FXML
    void handle_remove_class(MouseEvent event) {

    }

}
