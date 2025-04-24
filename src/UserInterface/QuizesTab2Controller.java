package UserInterface;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class QuizesTab2Controller {

    @FXML
    private TableView<?> Lst_Quizes;

    @FXML
    private Button Open_Quiz;

    @FXML
    void handle_OpenQuiz(MouseEvent event) 
    {
    	loadObjQuiz();
    	//loadSubjQuiz();
    	

    }
    
    
    
    
    
    
    
    
    
    
    //==================================================================
    private void loadSubjQuiz() {
		// TODO Auto-generated method stub
    	
    	String studentName = "ALIAhmad";
		String studentCode = "139812";
		
		String status_Quiz = "Unmarked";
		
		// Opening Submissions
		try {
	        // Load the FXML file for CheckSubjSubmission
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/AttemptSubjQuiz.fxml"));
	        Parent root = loader.load();

	        // Get controller instance
	        AttemptSubjQuizController controller = loader.getController();
	        
	        // Pass student data to initialize
	        if (controller != null) {
	            
				controller.initialize(studentName, studentCode,status_Quiz);
	        }

	        // Create new stage
	        Stage stage = new Stage();
	        stage.setTitle("Attempt Subjective Quiz");
	        stage.setScene(new Scene(root));
	        stage.show();

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		
	}

	private void loadObjQuiz() {
		// TODO Auto-generated method stub
    	String studentName = "ALI";
		String studentCode = "139812";
		String status_Quiz = "Unmarked";
		
		// Opening Submissions
		try {
	        // Load the FXML file for CheckObjSubmission
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/AttemptObjQuiz.fxml"));
	        Parent root = loader.load();

	        // Get controller instance
	        AttemptObjQuizController controller = loader.getController();

	        // Pass student data to initialize
	        if (controller != null) {
	            
				controller.initialize(studentName, studentCode,status_Quiz);
	        }

	        // Create new stage
	        Stage stage = new Stage();
	        stage.setTitle("Attempt Objective Quiz");
	        stage.setScene(new Scene(root));
	        stage.show();

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
    	
	}

}
