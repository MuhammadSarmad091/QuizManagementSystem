package UserInterface;

import controllers.TeacherHandler;
import businessLayer.Quiz;
import businessLayer.Submission;
import businessLayer.Student;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class QuizesTabController {

    private TeacherHandler teacherHandler; // Will be set by initData

    public void initData(TeacherHandler handler) {
        this.teacherHandler = handler;
        loadQuizzes();
    }

    @FXML
    private TableView<Quiz> quizTable;
    @FXML
    private TableColumn<Quiz, Integer> colQuizNo;
    @FXML
    private TableColumn<Quiz, String> colQuizName;   // Display the quiz's "name" field
    @FXML
    private TableColumn<Quiz, String> colQuizType;
    @FXML
    private TableColumn<Quiz, String> colDue;        // We'll convert LocalDateTime to String
    @FXML
    private TableColumn<Quiz, Float> colMarks;

    @FXML
    private TableView<Submission> submissionTable;
    @FXML
    private TableColumn<Submission, Integer> colSubNo;
    @FXML
    private TableColumn<Submission, String> colStdName;
    @FXML
    private TableColumn<Submission, String> colSubDate;
    @FXML
    private TableColumn<Submission, String> colStatus;
    @FXML
    private TableColumn<Submission, Float> colTotalMarks;

    @FXML
    private Button Obj_Quiz;

    @FXML
    private Button Open_one_submission;

    @FXML
    private Button Open_submissions;

    @FXML
    private Button Subj_Quiz;

    // Loads quizzes for the current class into the quizTable.
    private void loadQuizzes() {
        String classCode = teacherHandler.getCurrentClass().getClassCode();
        List<Quiz> quizzes = teacherHandler.getQuizzes(classCode);
        ObservableList<Quiz> quizList = FXCollections.observableArrayList(quizzes);
        quizTable.setItems(quizList);

        // Set up table columns (ensure your Quiz class has appropriate getters)
        colQuizNo.setCellValueFactory(new PropertyValueFactory<>("quizNo"));
        colQuizName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQuizType.setCellValueFactory(new PropertyValueFactory<>("type"));
        
        // Convert LocalDateTime to String (e.g., "2025-12-31 00:00")
        colDue.setCellValueFactory(cellData -> {
            LocalDateTime dueDate = cellData.getValue().getDeadLine();
            if (dueDate == null) {
                return new SimpleStringProperty("");
            }
            // Format the date/time as desired
            String formatted = dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            return new SimpleStringProperty(formatted);
        });

        colMarks.setCellValueFactory(new PropertyValueFactory<>("totalMarks"));
    }

    @FXML
    void handle_OpenSubmissions(MouseEvent event) {
        Quiz selectedQuiz = quizTable.getSelectionModel().getSelectedItem();
        if (selectedQuiz == null) {
            // Optionally show an error if no quiz is selected.
            return;
        }
        int quizNo = selectedQuiz.getQuizNo();
        String classCode = teacherHandler.getCurrentClass().getClassCode();
        List<Submission> submissions = teacherHandler.getSubmissions(quizNo, classCode);
        ObservableList<Submission> subList = FXCollections.observableArrayList(submissions);
        submissionTable.setItems(subList);

        // Set up submission table columns (ensure your Submission class has appropriate getters)
        colSubNo.setCellValueFactory(new PropertyValueFactory<>("submissionNo"));
        colStdName.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getStudent().getUsername())
        );
        // If you have a LocalDateTime or Date in Submission for submissionDateTime, do a similar conversion:
        colSubDate.setCellValueFactory(new PropertyValueFactory<>("submissionDateTime"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colTotalMarks.setCellValueFactory(new PropertyValueFactory<>("totalMarksObtained"));
    }

    @FXML
    void handle_Open_one_submission(MouseEvent event) {
        // Implement if needed.
    }

    @FXML
    void handle_Obj_Quiz(MouseEvent event) {
        OpenObjQuizFile();
    }

    @FXML
    void handle_Subj_Quiz(MouseEvent event) {
        OpenSubjQuizFile();
    }

    private void OpenObjQuizFile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateObjQuiz.fxml"));
            Parent root = loader.load();
            // Pass teacherHandler to the CreateObjQuizController BEFORE showing the window.
            CreateObjQuizController controller = loader.getController();
            controller.initData(teacherHandler);
            
            Stage newStage = new Stage();
            newStage.setTitle("Objective Quiz");
            newStage.setScene(new Scene(root));
            
            // When the new view closes, refresh the quizzes in the main view.
            newStage.setOnHidden(event -> {
                loadQuizzes();
            });
            
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void OpenSubjQuizFile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateSubjQuiz.fxml"));
            Parent root = loader.load();
            // Pass teacherHandler to the CreateSubjQuizController BEFORE showing the window.
            CreateSubjQuizController controller = loader.getController();
            controller.initData(teacherHandler);
            
            Stage newStage = new Stage();
            newStage.setTitle("Subjective Quiz");
            newStage.setScene(new Scene(root));
            
            // When the child page is closed, call loadQuizzes() to refresh the parent UI.
            newStage.setOnHidden(event -> {
                loadQuizzes();
            });
            
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
