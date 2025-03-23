package UserInterface;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class CheckObjSubmissionController {
	
	public void initialize(String StudentName , String StudentCode) 
	{
		StdName.setText(StudentName);
		
		loadSubmission(StudentName,StudentCode);
    }

    private void loadSubmission(String studentName, String studentCode) {
		// TODO Auto-generated method stub
    	 ArrayList<String> questions = new ArrayList<>(Arrays.asList(
    	            "Statement", "Option A", "Option B", "Option C", "Option D", "CorrectOption", "Marks",
    	            "Statement2", "Option A", "Option B", "Option C", "Option D", "CorrectOption", "Marks",
    	            "Statement3", "Option A", "Option B", "Option C", "Option D", "CorrectOption", "Marks",
    	            "Statement4", "Option A", "Option B", "Option C", "Option D", "CorrectOption", "Marks"
    	        ));
    	 
    	 
    	 
    	 
    	// Create VBox with spacing for questions
    	 VBox vbox = new VBox(10); // Spacing between questions
    	 vbox.setStyle("-fx-padding: 10;");
    	 vbox.setAlignment(Pos.CENTER);
    	 vbox.setMaxWidth(400); // Restrict width to keep it centered
    	 vbox.setPrefHeight(Region.USE_COMPUTED_SIZE); // Let height adjust based on content

    	 // Centering VBox inside its parent using StackPane
    	 StackPane wrapperPane = new StackPane();
    	 wrapperPane.setAlignment(Pos.CENTER); 
    	 wrapperPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
    	 wrapperPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
    	 wrapperPane.getChildren().add(vbox); // Add VBox to the wrapper

    	 for (int i = 0; i < questions.size(); i += 7) {
    	     String statement = questions.get(i);
    	     String optionA = questions.get(i + 1);
    	     String optionB = questions.get(i + 2);
    	     String optionC = questions.get(i + 3);
    	     String optionD = questions.get(i + 4);
    	     String correctOption = questions.get(i + 5);
    	     String marks = questions.get(i + 6);

    	     VBox questionBox = new VBox(5);
    	     questionBox.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-border-radius: 5; -fx-background-color: white;");
    	     questionBox.setPrefWidth(500); // Set preferred width
    	     questionBox.setMaxWidth(500);  // Ensure it doesn’t expand beyond this width
    	     questionBox.setMinWidth(500);

    	     // Statement
    	     Label statementLabel = new Label("Q: " + statement);
    	     statementLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

    	     // Options using radio buttons
    	     ToggleGroup optionsGroup = new ToggleGroup();
    	     RadioButton rbA = new RadioButton(optionA);
    	     rbA.setToggleGroup(optionsGroup);
    	     RadioButton rbB = new RadioButton(optionB);
    	     rbB.setToggleGroup(optionsGroup);
    	     RadioButton rbC = new RadioButton(optionC);
    	     rbC.setToggleGroup(optionsGroup);
    	     RadioButton rbD = new RadioButton(optionD);
    	     rbD.setToggleGroup(optionsGroup);

    	     VBox optionsBox = new VBox(5, rbA, rbB, rbC, rbD);

    	     // Correct option
    	     Label correctOptionLabel = new Label("Correct: " + correctOption);
    	     correctOptionLabel.setStyle("-fx-text-fill: green;");

    	     // Marks
    	     Label marksLabel = new Label("Marks: " + marks);

    	     // Add everything to questionBox
    	     questionBox.getChildren().addAll(statementLabel, optionsBox, correctOptionLabel, marksLabel);
    	     vbox.getChildren().add(questionBox);
    	 }

    	 // Set wrapperPane as the ScrollPane content
    	 CenteredScrollPane.setContent(wrapperPane);
    	 CenteredScrollPane.setFitToWidth(true);  // Ensures content stretches to fit width
    	 CenteredScrollPane.setPannable(true);    // Enables scrolling if needed

    	 
    	 
	}

	@FXML
    private ScrollPane CenteredScrollPane;

	   @FXML
	    private TextField StdName;

	    @FXML
	    private DatePicker Submissiontime;

	    @FXML
	    private TextField TotalMarks;

}

