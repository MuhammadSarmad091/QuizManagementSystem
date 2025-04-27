package UserInterface;

import controllers.TeacherHandler;
import dbHandlers.quizDBH;
import businessLayer.Question;
import businessLayer.Quiz;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class CreateSubjQuizController {

    private TeacherHandler teacherHandler;
    
    @FXML
    private DatePicker Deadline;
    
    @FXML
    private TableView<Question> Lst_Ques;
    
    @FXML
    private TextField Marks;
    
    @FXML
    private TextField QuizName;
    
    @FXML
    private TextArea Statement;
    
    @FXML
    private Button add_ques;
    
    @FXML
    private Button create_SubjQuiz;
    
    @FXML
    private TableColumn<Question, String> mcq_tbl; // to display question statement
    @FXML
    private TableColumn<Question, Integer> num_tbl; // to display question number
    
    /**
     * initData must be called before the view is displayed.
     * It saves the teacherHandler and creates a new quiz in teacherHandler.
     * Also, it attaches a filter to the Marks TextField to allow only numeric input.
     */
    public void initData(TeacherHandler handler) {
        this.teacherHandler = handler;
        // Create a new quiz in teacherHandler
        teacherHandler.setCurrentQuiz(new Quiz());
        teacherHandler.getCurrentQuiz().setType("subjective");
        
        // Attach a listener to the Marks TextField to allow only numbers (and optional decimal point)
        Marks.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                Marks.setText(oldValue);
            }
        });
        
        // Initialize table columns for displaying current quiz questions.
        mcq_tbl.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("statement"));
        num_tbl.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("questionNo"));
    }
    
    /**
     * Handle Add Question button.
     * Validates that Statement and Marks are provided, then creates a new subjective question.
     * Options are set to null, correct answer is null.
     * Updates the Lst_Ques TableView.
     */
    @FXML
    void handle_add_ques(MouseEvent event) {
        String stmt = Statement.getText().trim();
        String marksText = Marks.getText().trim();
        
        if (stmt.isEmpty() || marksText.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Both the question statement and marks must be provided.");
            alert.showAndWait();
            return;
        }
        
        float marks;
        try {
            marks = Float.parseFloat(marksText);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Marks must be a valid number.");
            alert.showAndWait();
            return;
        }
        
        // Create a new subjective question.
        // For subjective questions, options and correct answer are null.
        Question q = new Question();
        q.setStatement(stmt);
        q.setType("subjective");
        q.setMarks(marks);
        q.setOptA(null);
        q.setOptB(null);
        q.setOptC(null);
        q.setOptD(null);
        q.setCorrectAnswer(null);
        
        // Add the question to the current quiz via teacherHandler.
        teacherHandler.addQuestion(stmt, "subjective", null, null, null, null, null, marks);
        
        // Clear the input fields.
        Statement.clear();
        Marks.clear();
        
        // Refresh the Lst_Ques table.
        List<Question> questions = teacherHandler.getCurrentQuiz().getQuestions();
        ObservableList<Question> qList = FXCollections.observableArrayList(questions);
        Lst_Ques.setItems(qList);
    }
    
    /**
     * Handle Remove Question button.
     * Removes the selected question from teacherHandler's currentQuiz and refreshes the table.
     */
    @FXML
    void handle_rmv_ques(MouseEvent event) {
        Question selected = Lst_Ques.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Remove Error");
            alert.setHeaderText(null);
            alert.setContentText("No question is selected.");
            alert.showAndWait();
            return;
        }
        teacherHandler.removeQuestion(selected.getQuestionNo());
        ObservableList<Question> qList = FXCollections.observableArrayList(teacherHandler.getCurrentQuiz().getQuestions());
        Lst_Ques.setItems(qList);
    }
    
    /**
     * Handle Create Subjective Quiz button.
     * Validates that QuizName and Deadline are provided, then updates teacherHandler's currentQuiz and saves it.
     * After saving, closes this window and reloads the parent UI's quizzes.
     */
    @FXML
    void handle_create_SubjQuiz(MouseEvent event) {
        String quizName = QuizName.getText().trim();
        LocalDate selectedDate = Deadline.getValue();
        LocalDate nowDate = LocalDate.now();
        
        if (quizName.isEmpty() || selectedDate == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Quiz Name and Deadline must be provided.");
            alert.showAndWait();
            return;
        }
        List<Question> questions = this.teacherHandler.getCurrentQuiz().getQuestions();
        
        if (questions == null || questions.isEmpty())
        {
        	Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Quiz must contain at least 1 question");
            alert.showAndWait();
            return;
        }
        
        if(selectedDate.isBefore(nowDate))
        {
        	Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select valid due date");
            alert.showAndWait();
            return;
        }
        
        // Update teacherHandler's currentQuiz with quiz name and deadline.
        Quiz quiz = teacherHandler.getCurrentQuiz();
        quiz.setName(quizName);
        // Set the deadline at end of day (23:59) for the selected date.
        quiz.setDeadLine(LocalDateTime.of(selectedDate, LocalTime.of(23, 59)));
        
        // Save the quiz to the database.
        String classCode = teacherHandler.getCurrentClass().getClassCode();
        quizDBH.getDBH().createQuiz(quiz, classCode);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quiz Created");
        alert.setHeaderText(null);
        alert.setContentText("Subjective quiz created successfully.");
        alert.showAndWait();
        
        this.teacherHandler.setCurrentQuiz(null);

        // Close this CreateSubjQuiz window.
        Stage stage = (Stage) create_SubjQuiz.getScene().getWindow();
        stage.close();
        
}
}