package UserInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.UnaryOperator;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

public class AttemptSubjQuizController {

    @FXML
    private ScrollPane CenteredScrollPane;

    @FXML
    private TextField StdName;

    @FXML
    private DatePicker Submissiontime;

    @FXML
    private Button Submit_Quiz;
    
    @FXML
    private Button Close;

    @FXML
    private TextField TotalMarks;
    
    @FXML
    void handle_Close(MouseEvent event) {
    	
    	Stage stage = (Stage) Close.getScene().getWindow();
        stage.close();

    }

    @FXML
    void handle_Submit_Quiz(MouseEvent event) 
    {
    	// get answers from  AttemptedAnswers and save in DB
    	
    	// and remaining
    	Stage stage = (Stage) Close.getScene().getWindow();
        stage.close();
    }
    
    private String status_quiz;
    
    
  //=================================================================//
    public void initialize(String StudentName , String StudentCode,String status_q) 
	{
		StdName.setText(StudentName);
		status_quiz = status_q;
		loadQuiz(StudentName,StudentCode);
    }
    
    private ArrayList<TextArea> AttemptedAnswers = new ArrayList<>();


    private void loadQuiz(String studentName, String studentCode) {
		// TODO Auto-generated method stub
    	 ArrayList<String> questions = new ArrayList<>(Arrays.asList(
    	            "Statement", "...", "10", "",
    	            "Statement2", "...", "8", "",
    	            "Statement3", "...", "7", "",
    	            "Statement4", "...", "9", ""
    	        ));

    	        // Create VBox with spacing for questions
    	        VBox vbox = new VBox(15);
    	        vbox.setStyle("-fx-padding: 15;");
    	        vbox.setAlignment(Pos.CENTER);
    	        vbox.setPrefWidth(600); // Increased width
    	        vbox.setPrefHeight(Region.USE_COMPUTED_SIZE);

    	        // Centering VBox inside its parent using StackPane
    	        StackPane wrapperPane = new StackPane();
    	        wrapperPane.setAlignment(Pos.CENTER);
    	        wrapperPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
    	        wrapperPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
    	        wrapperPane.getChildren().add(vbox); // Add VBox to the wrapper

    	        for (int i = 0; i < questions.size(); i += 4) {
    	            String statement = questions.get(i);
    	            String answerText = questions.get(i + 1);
    	            String totalMarks = questions.get(i + 2);
    	            String assignedMarks = questions.get(i+3);
    	            
    	            // Create question box
    	            VBox questionBox = new VBox(10);
    	            questionBox.setStyle("-fx-border-color: black; -fx-padding: 15; -fx-border-radius: 5; -fx-background-color: white;");
    	            questionBox.setPrefWidth(550); // Increased width
    	            questionBox.setMaxWidth(550);
    	            questionBox.setMinWidth(550);

    	            // Statement Label
    	            Label statementLabel = new Label("Q: " + statement);
    	            statementLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

    	            // Answer TextArea (Non-editable)
    	            TextArea answerArea = new TextArea(answerText);
    	            answerArea.setWrapText(true);
    	            
    	            if(status_quiz == "Unmarked")
    	            {
    	            	answerArea.setEditable(true);
    	            	//Store the answers in AttemptedAnswers for global submission after submit
        	            AttemptedAnswers.add(answerArea);
    	            }
    	            else
    	            {
    	            	answerArea.setEditable(false);
    	            }
    	            
    	            answerArea.setPrefHeight(70);
    	            
    	            // Total Marks Label
    	            Label totalMarksLabel = new Label("Total Marks: " + totalMarks);
    	            totalMarksLabel.setStyle("-fx-font-size: 12px;");
    	            
    	            // Assigned Marks (Editable TextField)
    	            Label assignedMarksLabel = new Label("Assigned Marks: " + assignedMarks);
    	            assignedMarksLabel.setStyle("-fx-font-size: 12px;");


    	            // HBox for total marks & assigned marks
    	            HBox marksBox = new HBox(10, totalMarksLabel,assignedMarksLabel);
    	            marksBox.setAlignment(Pos.CENTER_LEFT);

    	            // Add components to questionBox
    	            questionBox.getChildren().addAll(statementLabel, answerArea, marksBox);
    	            vbox.getChildren().add(questionBox);
    	        }

    	 // Set wrapperPane as the ScrollPane content
    	 CenteredScrollPane.setContent(wrapperPane);
    	 CenteredScrollPane.setFitToWidth(true);  // Ensures content stretches to fit width
    	 CenteredScrollPane.setPannable(true);    // Enables scrolling if needed
	 
	}

}
