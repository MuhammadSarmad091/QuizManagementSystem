package UserInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import controllers.StudentHandler;
import businessLayer.Answer;
import businessLayer.Question;
import businessLayer.Quiz;
import businessLayer.Submission;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class AttemptSubjQuizController {
    private StudentHandler studentHandler;
    private boolean editable;
    private Map<Integer, TextArea> answerAreas;

    @FXML private ScrollPane CenteredScrollPane;
    @FXML private TextField StdName;
    @FXML private TextField TotalMarks;
    @FXML private Button Submit_Quiz;
    @FXML private Button Close;
    @FXML private TextField Submissiontime;

    /**
     * Initialize controller with handler and editability.
     */
    public void initData(StudentHandler handler, boolean editable) {
        this.studentHandler = handler;
        this.editable = editable;
        answerAreas = new HashMap<>();
        // Show submit button only if editable
        Submit_Quiz.setVisible(editable);
        loadQuiz();
    }

    @FXML
    public void initialize() {
        // hide submit until initData
        if (Submit_Quiz != null) {
            Submit_Quiz.setVisible(false);
        }
    }

    @FXML
    void handle_Close(MouseEvent event) {
        if (editable) {
            Alert confirm = new Alert(AlertType.CONFIRMATION,
                "Are you sure leaving now will result in zero marks?");
            confirm.setHeaderText(null);
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Stage stage = (Stage) Close.getScene().getWindow();
                stage.close();
            }
        } else {
            Stage stage = (Stage) Close.getScene().getWindow();
            stage.close();
        }
    }


    @FXML
    void handle_Submit_Quiz(MouseEvent event) {
        Submission sub = studentHandler.getCurrentSubmission();
        sub.getAnswers().clear();
        Quiz quiz = studentHandler.getCurrentQuiz();
        // collect answers
        answerAreas.forEach((qNo, area) -> {
            Answer a = new Answer();
            a.setQuestionNumber(qNo);
            a.setAnswer(area.getText());
            sub.addAnswer(a);
        });
        sub.setStatus("Not Graded");
        studentHandler.saveSubmission();
        Stage stage = (Stage) Close.getScene().getWindow();
        stage.close();
    }

    /**
     * Build UI for subjective quiz.
     */
    private void loadQuiz() {
        Quiz quiz = studentHandler.getCurrentQuiz();
        Submission sub = studentHandler.getCurrentSubmission();
        if (quiz == null || sub == null) return;

        StdName.setText(sub.getStudent().getUsername());
        StdName.setEditable(false);
        
        //
        ZonedDateTime zonedDateTime = sub.getSubmissionDateTime()
                .atZone(ZoneId.of("Asia/Karachi"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
        		"EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

        Submissiontime.setText(zonedDateTime.format(formatter));
        Submissiontime.setEditable(false);
        
        //
        String your_marks= "?";
        if(this.studentHandler.getCurrentSubmission().getStatus().equalsIgnoreCase("Graded"))
        {
        	your_marks = String.valueOf(this.studentHandler.getCurrentSubmission().getTotalMarksObtained());
        }
        this.TotalMarks.setText(your_marks + " / "+ String.valueOf(this.studentHandler.getCurrentQuiz().getTotalMarks()));
        this.TotalMarks.setEditable(false);

        VBox vbox = new VBox(15);
        vbox.setStyle("-fx-padding: 15;");
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefWidth(600);

        StackPane wrapper = new StackPane(vbox);
        wrapper.setAlignment(Pos.CENTER);

        List<Question> questions = quiz.getQuestions();
        for (Question q : questions) {
            int qNo = q.getQuestionNo();
            VBox qBox = new VBox(10);
            qBox.setStyle(
               "-fx-border-color: black; -fx-padding: 15; -fx-border-radius: 5; -fx-background-color: white;"
            );
            qBox.setPrefWidth(550);

            Label stmt = new Label("Q" + qNo + ": " + q.getStatement());
            stmt.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            TextArea answerArea = new TextArea();
            // prefill existing answer
            sub.getAnswers().stream()
               .filter(a -> a.getQuestionNumber() == qNo)
               .findFirst()
               .ifPresent(a -> answerArea.setText(a.getAnswer()));

            answerArea.setWrapText(true);
            answerArea.setEditable(editable);
            if (editable) answerAreas.put(qNo, answerArea);
            answerArea.setPrefHeight(70);

            Label totalMarksLabel = new Label(
               String.format("Total Marks: %.1f", q.getMarks())
            );
            totalMarksLabel.setStyle("-fx-font-size: 12px;");

            HBox marksBox = new HBox(10, totalMarksLabel);
            marksBox.setAlignment(Pos.CENTER_LEFT);

            if ("Graded".equals(sub.getStatus())) {
                sub.getAnswers().stream()
                   .filter(a -> a.getQuestionNumber() == qNo)
                   .findFirst()
                   .ifPresent(a -> {
                       Label your = new Label(
                          String.format("Your marks: %.1f", a.getMarksObtained())
                       );
                       marksBox.getChildren().add(your);
                   });
            }

            qBox.getChildren().addAll(stmt, answerArea, marksBox);
            vbox.getChildren().add(qBox);
        }

        CenteredScrollPane.setContent(wrapper);
        CenteredScrollPane.setFitToWidth(true);
        CenteredScrollPane.setPannable(true);
    }
}
