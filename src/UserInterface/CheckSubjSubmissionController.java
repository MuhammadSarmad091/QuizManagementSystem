package UserInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

import businessLayer.Answer;
import businessLayer.Question;
import businessLayer.Quiz;
import businessLayer.Submission;
import controllers.TeacherHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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

    private TeacherHandler teacherHandler;

    public void initData(TeacherHandler handler) {
        this.teacherHandler = handler;
        initialize();
    }

    private void initialize() {
        if (teacherHandler == null || teacherHandler.getCurrentSubmission() == null || teacherHandler.getCurrentQuiz() == null)
            return;

        Submission submission = teacherHandler.getCurrentSubmission();
        List<Question> questions = teacherHandler.getCurrentQuiz().getQuestions();
        List<Answer> answers = submission.getAnswers(); // Assuming this method exists

        StdName.setText(submission.getStudent().getUsername());
        Submissiontime.setValue(submission.getSubmissionDateTime().toLocalDate()); // Assuming LocalDateTime
        TotalMarks.setText(String.valueOf(submission.getTotalMarksObtained())); // Optional

        VBox vbox = new VBox(15);
        vbox.setStyle("-fx-padding: 15;");
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefWidth(600);

        StackPane wrapperPane = new StackPane();
        wrapperPane.setAlignment(Pos.CENTER);
        wrapperPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
        wrapperPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        wrapperPane.getChildren().add(vbox);

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            Answer answer = findAnswerByQuestionNo(answers, i + 1);

            VBox questionBox = new VBox(10);
            questionBox.setStyle("-fx-border-color: black; -fx-padding: 15; -fx-border-radius: 5; -fx-background-color: white;");
            questionBox.setPrefWidth(550);

            Label statementLabel = new Label("Q: " + question.getStatement());
            statementLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            TextArea answerArea = new TextArea(answer != null ? answer.getAnswer() : "Not Answered");
            answerArea.setWrapText(true);
            answerArea.setEditable(false);
            answerArea.setPrefHeight(70);

            Label totalMarksLabel = new Label("Total Marks: " + question.getMarks());
            totalMarksLabel.setStyle("-fx-font-size: 12px;");

            Label assignedMarksLabel = new Label("Assign Marks: ");
            TextField assignedMarksField = new TextField();

            float prevAssigned = (answer != null) ? answer.getMarksObtained() : 0;
            assignedMarksField.setText(String.valueOf(prevAssigned));

            // Allow only valid float input
            UnaryOperator<Change> floatFilter = change -> {
                String newText = change.getControlNewText();
                return newText.matches("\\d*(\\.\\d{0,2})?") ? change : null;
            };

            TextFormatter<String> formatter = new TextFormatter<>(floatFilter);
            assignedMarksField.setTextFormatter(formatter);

            int questionNo = i + 1;
            assignedMarksField.textProperty().addListener((obs, oldVal, newVal) -> {
                try {
                    float marks = Float.parseFloat(newVal);
                    float maxMarks = question.getMarks();

                    if (marks > maxMarks) {
                        marks = maxMarks;
                        assignedMarksField.setText(String.valueOf(marks));
                    }

                    teacherHandler.updateMarks(questionNo, marks);

                    // Recalculate total marks obtained from all answers
                    float total = 0;
                    for (Answer a : submission.getAnswers()) {
                        total += a.getMarksObtained();
                    }

                    TotalMarks.setText(String.valueOf(total));
                    submission.setTotalMarksObtained(total);

                } catch (NumberFormatException e) {
                    // Ignore invalid input
                }
            });

            HBox marksBox = new HBox(10, totalMarksLabel, assignedMarksLabel, assignedMarksField);
            marksBox.setAlignment(Pos.CENTER_LEFT);

            questionBox.getChildren().addAll(statementLabel, answerArea, marksBox);
            vbox.getChildren().add(questionBox);
        }

        CenteredScrollPane.setContent(wrapperPane);
        CenteredScrollPane.setFitToWidth(true);
        CenteredScrollPane.setPannable(true);
    }


    private Answer findAnswerByQuestionNo(List<Answer> answers, int questionNo) {
        for (Answer ans : answers) {
            if (ans.getQuestionNumber() == questionNo) {
                return ans;
            }
        }
        return null;
    }

    @FXML
    void handle_Save_Checking(MouseEvent event) {
        if (teacherHandler != null) {
            teacherHandler.saveSubmission();

            // Show confirmation message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Saved successfully.");
            alert.showAndWait();

            // Close the current window
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }
    }

}

