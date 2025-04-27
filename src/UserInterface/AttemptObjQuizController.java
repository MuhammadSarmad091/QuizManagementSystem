package UserInterface;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AttemptObjQuizController {
    
    private StudentHandler studentHandler;
    private boolean editable;
    private Map<Integer, String> selectedAnswers;

    @FXML private ScrollPane CenteredScrollPane;
    @FXML private Button Close;
    @FXML private TextField StdName;
    @FXML private TextField TotalMarks;
    @FXML private Button Submit_Quiz;
    @FXML private TextField Date_txtfld;

    /**
     * Called to initialize this controller with data and editability.
     */
    public void initData(StudentHandler handler, boolean editable) {
        this.studentHandler = handler;
        this.editable = editable;
        selectedAnswers = new HashMap<>();
        // Show submit button only if editable
        Submit_Quiz.setVisible(editable);
        this.StdName.setText(this.studentHandler.getStudent().getUsername());
        this.StdName.setEditable(false);
        String your_marks= "?";
        if(this.studentHandler.getCurrentSubmission().getStatus().equalsIgnoreCase("Graded"))
        {
        	your_marks = String.valueOf(this.studentHandler.getCurrentSubmission().getTotalMarksObtained());
        }
        this.TotalMarks.setText(your_marks + " / "+ String.valueOf(this.studentHandler.getCurrentQuiz().getTotalMarks()));
        this.TotalMarks.setEditable(false);
        loadQuiz();
    }

    @FXML
    public void initialize() {

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
        // Build answers and assign marks based on correctness (1 or 0)
        selectedAnswers.forEach((qNo, ans) -> {
            Answer a = new Answer();
            a.setQuestionNumber(qNo);
            a.setAnswer(ans);
            quiz.getQuestions().stream()
                .filter(q -> q.getQuestionNo() == qNo)
                .findFirst()
                .ifPresent(q -> {
                    a.setMarksObtained(ans != null && ans.equals(q.getCorrectAnswer()) ? 1f : 0f);
                });
            sub.addAnswer(a);
        });
        sub.calculateTotalMarks();
        sub.setStatus("Graded");
        studentHandler.saveSubmission();
        Stage stage = (Stage) Close.getScene().getWindow();
        stage.close();
    }

    private void loadQuiz() {
        Quiz quiz = studentHandler.getCurrentQuiz();
        Submission sub = studentHandler.getCurrentSubmission();
        if (quiz == null || sub == null) return;
        
        ZonedDateTime zonedDateTime = sub.getSubmissionDateTime()
                .atZone(ZoneId.of("Asia/Karachi"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
        		"EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

        this.Date_txtfld.setText(zonedDateTime.format(formatter));
        this.Date_txtfld.setEditable(false);


        VBox vbox = new VBox(15);
        vbox.setStyle("-fx-padding: 15;");
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefWidth(600);

        StackPane wrapper = new StackPane(vbox);
        wrapper.setAlignment(Pos.CENTER);

        for (Question q : quiz.getQuestions()) {
            int qNo = q.getQuestionNo();
            VBox qBox = new VBox(10);
            qBox.setStyle("-fx-border-color: black; -fx-padding: 15; -fx-border-radius: 5; -fx-background-color: white;");
            qBox.setPrefWidth(550);

            Label stmt = new Label("Q" + qNo + ": " + q.getStatement());
            stmt.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            ToggleGroup group = new ToggleGroup();
            RadioButton rbA = makeRadio(q.getOptA(), "A", group);
            RadioButton rbB = makeRadio(q.getOptB(), "B", group);
            RadioButton rbC = makeRadio(q.getOptC(), "C", group);
            RadioButton rbD = makeRadio(q.getOptD(), "D", group);

            // preselect from submission
            sub.getAnswers().stream()
               .filter(a -> a.getQuestionNumber() == qNo)
               .findFirst().ifPresent(a -> {
                   String choice = a.getAnswer();
                   group.getToggles().stream()
                        .filter(t -> t.getUserData().equals(choice))
                        .findFirst()
                        .ifPresent(t -> group.selectToggle(t));
               });

            if (!editable) {
                group.getToggles().forEach(t -> ((RadioButton)t).setDisable(true));
            } else {
                group.selectedToggleProperty().addListener((obs, oldT, newT) -> {
                    if (newT != null) selectedAnswers.put(qNo, newT.getUserData().toString());
                    else selectedAnswers.remove(qNo);
                });
            }

            VBox opts = new VBox(5, rbA, rbB, rbC, rbD);
            Label marksLabel = new Label(String.format("Marks: %.1f", q.getMarks()));
            qBox.getChildren().addAll(stmt, opts, marksLabel);

            if ("Graded".equals(sub.getStatus())) {
                Label corr = new Label("Correct: " + q.getCorrectAnswer());
                corr.setStyle("-fx-text-fill: green;");
                sub.getAnswers().stream()
                  .filter(a -> a.getQuestionNumber() == qNo)
                  .findFirst().ifPresent(a -> {
                      Label your = new Label(String.format("Your marks: %.1f", a.getMarksObtained()));
                      qBox.getChildren().add(your);
                  });
                qBox.getChildren().add(corr);
            }

            vbox.getChildren().add(qBox);
        }

        CenteredScrollPane.setContent(wrapper);
        CenteredScrollPane.setFitToWidth(true);
        CenteredScrollPane.setPannable(true);
    }

    private RadioButton makeRadio(String text, String code, ToggleGroup grp) {
        RadioButton rb = new RadioButton(text);
        rb.setToggleGroup(grp);
        rb.setUserData(code);
        return rb;
    }
}
