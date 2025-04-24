package UserInterface;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class JoinClassController {

    @FXML
    private TextField class_code;

    @FXML
    private TextField class_name;

    @FXML
    private Button go_back;

    @FXML
    private Button join_class_btn;

    @FXML
    void handle_go_back(MouseEvent event) {
    	loadStudentHome();

    }

    @FXML
    void handle_join_class_btn(MouseEvent event) {
    	///
    	Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Class Alert");
        alert.setContentText("Class Joined Successfully");
        alert.showAndWait();
       
        ReloadPage();
    }
    public void initialize() 
	{
        ReloadPage();
    }
    
    private void ReloadPage() {
		// TODO Auto-generated method stub
    	class_code.setText("");
    	class_name.setText("");
	}
    
    private void loadStudentHome() 
    {
    	try {
    	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/StudentHome.fxml"));
    	    Parent teacherHomeRoot = loader.load();

    	    // Get the controller instance
    	    StudentHomeController controller = loader.getController();
    	    
    	    // Explicitly call initialize() (optional, JavaFX calls it automatically)
    	    if (controller != null) {
    	        controller.initialize();
    	    }

    	    // Get the current stage
    	    Stage stage = (Stage) go_back.getScene().getWindow();
    	    // Set the new scene
    	    stage.setScene(new Scene(teacherHomeRoot));
    
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
    	
    	
	}

}
