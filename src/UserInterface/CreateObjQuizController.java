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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class CreateObjQuizController {

    private TeacherHandler teacherHandler;
    
    // UI controls
    @FXML
    private ComboBox<String> Correct_opt;
    
    @FXML
    private DatePicker Deadline;
    
    @FXML
    private TableView<Question> Lst_Ques;
    
    @FXML
    private TextField OptionA;
    
    @FXML
    private TextField OptionB;
    
    @FXML
    private TextField OptionC;
    
    @FXML
    private TextField OptionD;
    
    @FXML
    private TextField QuizName;
    
    @FXML
    private TextArea Statement;
    
    @FXML
    private Button add_ques;
    
    @FXML
    private Button create_ObjQuiz;
    
    @FXML
    private TableColumn<Question, String> mcq_tbl; // to display statement
    @FXML
    private TableColumn<Question, Integer> num_tbl; // to display question number

    /**
     * This initData method is called by the calling controller and must be invoked
     * before the UI is shown to avoid null issues.
     */
    public void initData(TeacherHandler handler) {
        this.teacherHandler = handler;
        // Create a new quiz in teacherHandler if not already created
        teacherHandler.setCurrentQuiz(new Quiz());
        teacherHandler.getCurrentQuiz().setType("objective");
        // Populate Correct_opt with "A", "B", "C", "D", and select "A" by default.
        ObservableList<String> opts = FXCollections.observableArrayList("A", "B", "C", "D");
        Correct_opt.setItems(opts);
        Correct_opt.getSelectionModel().select("A");
        
        // Initialize the Lst_Ques table columns, if needed.
        mcq_tbl.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("statement"));
        num_tbl.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("questionNo"));
    }
    
    /**
     * Handle the Add Question button.
     * It validates that Statement, Options and Correct Option are provided,
     * then creates a new Question and adds it to teacherHandler's currentQuiz.
     * Finally, it refreshes the Lst_Ques table.
     */
    @FXML
    void handle_add_ques(MouseEvent event) {
        // Validate required fields.
        String stmt = Statement.getText().trim();
        String optA = OptionA.getText().trim();
        String optB = OptionB.getText().trim();
        String optC = OptionC.getText().trim();
        String optD = OptionD.getText().trim();
        String correct = Correct_opt.getValue();
        
        if (stmt.isEmpty() || optA.isEmpty() || optB.isEmpty() || optC.isEmpty() || optD.isEmpty() || correct == null || correct.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("All question fields must be filled and a correct option selected.");
            alert.showAndWait();
            return;
        }
        
        // Create a new Question with the provided fields.
        Question q = new Question();
        q.setStatement(stmt);
        q.setOptA(optA);
        q.setOptB(optB);
        q.setOptC(optC);
        q.setOptD(optD);
        q.setCorrectAnswer(correct);
        // You might want to decide a default type and marks for the objective question.
        q.setType("objective");
        q.setMarks(1.0f); // Set default mark (or implement additional UI for marks)
        
        // Add the question to the current quiz using teacherHandler.
        teacherHandler.addQuestion(stmt, "objective", optA, optB, optC, optD, correct, 1.0f);
        
        // Optionally clear input fields after adding.
        Statement.clear();
        OptionA.clear();
        OptionB.clear();
        OptionC.clear();
        OptionD.clear();
        Correct_opt.getSelectionModel().select("A");
        
        // Refresh the Lst_Ques table with updated questions list.
        List<Question> questions = teacherHandler.getCurrentQuiz().getQuestions();
        ObservableList<Question> qList = FXCollections.observableArrayList(questions);
        Lst_Ques.setItems(qList);
    }
    
    /**
     * Handle remove question button.
     * Remove the selected question from teacherHandler's currentQuiz and refresh the table.
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
        // Refresh table
        ObservableList<Question> qList = FXCollections.observableArrayList(teacherHandler.getCurrentQuiz().getQuestions());
        Lst_Ques.setItems(qList);
    }
    
    /**
     * Handle create objective quiz button.
     * Validates that Deadline and QuizName are provided. If valid, sets the quiz name and deadline
     * in teacherHandler's currentQuiz and then saves the quiz using the existing DB handler method.
     * Finally, the window is closed and the parent UI is reloaded (via teacherHandler.loadQuizzes(), assumed to exist).
     */
    @FXML
    void handle_create_ObjQuiz(MouseEvent event) {
        String quizName = QuizName.getText().trim();
        LocalDate date = Deadline.getValue();
        LocalDate nowDate = LocalDate.now();
        
        if (quizName.isEmpty() || date == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Quiz name and deadline must be provided.");
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
        
        if(date.isBefore(nowDate))
        {
        	Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select valid due date");
            alert.showAndWait();
            return;
        }
        
        // Set the quiz name and deadline in the current quiz.
        Quiz quiz = teacherHandler.getCurrentQuiz();
        quiz.setName(quizName);
        // Assume deadline time is set to 23:59 for the selected date.
        quiz.setDeadLine(LocalDateTime.of(date, LocalTime.of(23, 59)));
        
        // Save the quiz to the database using the current class's code.
        String classCode = teacherHandler.getCurrentClass().getClassCode();
        quizDBH.getDBH().createQuiz(quiz, classCode);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quiz Created");
        alert.setHeaderText(null);
        alert.setContentText("Objective quiz created successfully.");
        alert.showAndWait();
        
        this.teacherHandler.setCurrentQuiz(null);
        // Close the CreateObjQuiz window.
        Stage stage = (Stage) create_ObjQuiz.getScene().getWindow();
        stage.close();
            }
}