package UserInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AttemptObjQuizController {

    @FXML
    private ScrollPane CenteredScrollPane;

    @FXML
    private Button Close;

    @FXML
    private TextField StdName;

    @FXML
    private DatePicker Submissiontime;

    @FXML
    private Button Submit_Quiz;

    @FXML
    private TextField TotalMarks;

    @FXML
    void handle_Close(MouseEvent event) {
    	// and remaining
    	Stage stage = (Stage) Close.getScene().getWindow();
        stage.close();

    }

    @FXML
    void handle_Submit_Quiz(MouseEvent event) {
    	
    	// get selected option
//    	for (Map.Entry<Integer, String> entry : selectedAnswers.entrySet()) {
//    	    System.out.println("Question " + entry.getKey() + ": " + entry.getValue());
//    	}
    	
    	// and remaining
    	Stage stage = (Stage) Close.getScene().getWindow();
        stage.close();

    }
    
    private HashMap<Integer, String> selectedAnswers;
    private String status_quiz;
    
    //=============================================================
    public void initialize(String StudentName , String StudentCode,String status_q) 
	{
    	selectedAnswers = new HashMap<>();
		StdName.setText(StudentName);
		status_quiz = status_q;
		
		loadQuiz(StudentName,StudentCode);
    }

    private void loadQuiz(String studentName, String studentCode) {
        ArrayList<String> questions = new ArrayList<>(Arrays.asList(
            "Statement", "Option A", "Option B", "Option C", "Option D", "CorrectOption", "Marks", //total for unmarked and obtained for marked
            "Statement2", "Option A", "Option B", "Option C", "Option D", "CorrectOption", "Marks",
            "Statement3", "Option A", "Option B", "Option C", "Option D", "CorrectOption", "Marks",
            "Statement4", "Option A", "Option B", "Option C", "Option D", "CorrectOption", "Marks"
        ));

        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-padding: 10;");
        vbox.setAlignment(Pos.CENTER);
        vbox.setMaxWidth(400);

        StackPane wrapperPane = new StackPane();
        wrapperPane.setAlignment(Pos.CENTER);
        wrapperPane.getChildren().add(vbox);

        for (int i = 0, qNum = 1; i < questions.size(); i += 7, qNum++) {
            String statement = questions.get(i);
            String optionA = questions.get(i + 1);
            String optionB = questions.get(i + 2);
            String optionC = questions.get(i + 3);
            String optionD = questions.get(i + 4);
            
            String marks = questions.get(i + 6);

            VBox questionBox = new VBox(5);
            questionBox.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-border-radius: 5; -fx-background-color: white;");
            questionBox.setPrefWidth(500);
            questionBox.setMaxWidth(500);
            questionBox.setMinWidth(500);

            Label statementLabel = new Label("Q" + qNum + ": " + statement);
            statementLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            ToggleGroup optionsGroup = new ToggleGroup();

            RadioButton rbA = new RadioButton(optionA);
            rbA.setToggleGroup(optionsGroup);
            rbA.setUserData("A");

            RadioButton rbB = new RadioButton(optionB);
            rbB.setToggleGroup(optionsGroup);
            rbB.setUserData("B");

            RadioButton rbC = new RadioButton(optionC);
            rbC.setToggleGroup(optionsGroup);
            rbC.setUserData("C");

            RadioButton rbD = new RadioButton(optionD);
            rbD.setToggleGroup(optionsGroup);
            rbD.setUserData("D");

            // Save selection to global map on change
            final int currentQuestion = qNum;
            optionsGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    String selected = newVal.getUserData().toString();
                    selectedAnswers.put(currentQuestion, selected);
                } else {
                    selectedAnswers.remove(currentQuestion);
                }
            });

            
            if (status_quiz == "Marked")
            {
            	String correctOption = questions.get(i + 5); // Don't show it here
            	// Correct option
            	
          	     Label correctOptionLabel = new Label("Correct: " + correctOption);
          	     correctOptionLabel.setStyle("-fx-text-fill: green;");
                   
                 VBox optionsBox = new VBox(5, rbA, rbB, rbC, rbD);

                 Label marksLabel = new Label("Marks: " + marks);

                 questionBox.getChildren().addAll(statementLabel, optionsBox,correctOptionLabel, marksLabel);
            }
            else
            {
            	 VBox optionsBox = new VBox(5, rbA, rbB, rbC, rbD);

                 Label marksLabel = new Label("Marks: " + marks);

                 questionBox.getChildren().addAll(statementLabel, optionsBox, marksLabel);
            }
         
            vbox.getChildren().add(questionBox);
        }

        CenteredScrollPane.setContent(wrapperPane);
        CenteredScrollPane.setFitToWidth(true);
        CenteredScrollPane.setPannable(true);
    }


}
