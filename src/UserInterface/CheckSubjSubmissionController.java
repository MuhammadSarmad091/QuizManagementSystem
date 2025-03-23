package UserInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.UnaryOperator;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;

public class CheckSubjSubmissionController {

    @FXML
    private ScrollPane CenteredScrollPane;

    @FXML
    private TextField StdName;

    @FXML
    private DatePicker Submissiontime;

    @FXML
    private TextField TotalMarks;
    
    @FXML
    private Button Save_Checking;
    
    @FXML
    void handle_Save_Checking(MouseEvent event) {

    }
    
    
    //=================================================================//
    public void initialize(String StudentName , String StudentCode) 
	{
		StdName.setText(StudentName);
		
		loadSubmission(StudentName,StudentCode);
    }


    private void loadSubmission(String studentName, String studentCode) {
		// TODO Auto-generated method stub
    	 ArrayList<String> questions = new ArrayList<>(Arrays.asList(
    	            "Statement", "Answer text here...", "10", "",
    	            "Statement2", "Another answer...", "8", "",
    	            "Statement3", "Yet another answer...", "7", "",
    	            "Statement4", "Final answer...", "9", ""
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
    	            answerArea.setEditable(false);
    	            answerArea.setPrefHeight(70);

    	            // Total Marks Label
    	            Label totalMarksLabel = new Label("Total Marks: " + totalMarks);
    	            totalMarksLabel.setStyle("-fx-font-size: 12px;");

    	            // Assigned Marks (Editable TextField)
    	            Label assignedMarksLabel = new Label("Assign Marks: ");
    	            TextField assignedMarksField = new TextField();
    	            assignedMarksField.setPrefWidth(50);

    	            // Restrict input to numbers only
    	            UnaryOperator<TextFormatter.Change> filter = change -> {
    	                String newText = change.getControlNewText();
    	                if (newText.matches("\\d*")) { // Allows only digits
    	                    return change;
    	                }
    	                return null;
    	            };

    	            TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), 0, filter);
    	            assignedMarksField.setTextFormatter(textFormatter);
    	            

    	            // HBox for total marks & assigned marks
    	            HBox marksBox = new HBox(10, totalMarksLabel, assignedMarksLabel, assignedMarksField);
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
