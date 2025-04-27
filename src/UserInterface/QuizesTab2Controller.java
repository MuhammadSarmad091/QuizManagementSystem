package UserInterface;

import java.io.IOException;

import java.util.Date;
import java.util.List;

import businessLayer.Submission_Quiz;
import controllers.StudentHandler;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class QuizesTab2Controller {

    private StudentHandler studentHandler;

    public void initData(StudentHandler studentHandler) {
        this.studentHandler = studentHandler;
        loadQuizzes();
    }

    @FXML
    private TableView<Submission_Quiz> Lst_Quizes;

    @FXML private TableColumn<Submission_Quiz, Integer> colQuizNo;
    @FXML private TableColumn<Submission_Quiz, String>  colName;
    @FXML private TableColumn<Submission_Quiz, String>  colType;
    @FXML private TableColumn<Submission_Quiz, Date>    colDue;
    @FXML private TableColumn<Submission_Quiz, Integer> colTotalMarks;
    @FXML private TableColumn<Submission_Quiz, String>  colStatus;
    @FXML private TableColumn<Submission_Quiz, Integer> colMarksGiven;

    @FXML
    private Button Open_Quiz;

    @FXML
    public void initialize() {
        // wire up columns to bean properties
        colQuizNo.setCellValueFactory(new PropertyValueFactory<>("quizNo"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colDue.setCellValueFactory(new PropertyValueFactory<>("due"));
        colTotalMarks.setCellValueFactory(new PropertyValueFactory<>("total_marks"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colMarksGiven.setCellValueFactory(new PropertyValueFactory<>("marks_given"));
    }

    private void loadQuizzes() {
    	System.out.println("Here\n");
        // fetch and show data
        List<Submission_Quiz> list = studentHandler.getSubmission_Quizzes();
        if(list == null || list.isEmpty())
        {
        	System.out.println("Empty List\n");
        }
        Lst_Quizes.setItems(FXCollections.observableArrayList(list));
    }

    @FXML
    void handle_OpenQuiz(MouseEvent event) 
    {
    	
    	Submission_Quiz selected = Lst_Quizes.getSelectionModel().getSelectedItem();
    	boolean editable = false;
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a quiz first.").showAndWait();
            return;
        }
        int quizNo = selected.getQuizNo();
        String status = selected.getStatus();
        String type = selected.getType();

        if ("Assigned".equalsIgnoreCase(status)) {
            studentHandler.startQuiz(quizNo);
            editable=true;
        } else {
            studentHandler.viewSubmission(quizNo);
        }

        if ("subjective".equalsIgnoreCase(type)) {
            loadSubjQuiz(editable);
        } else {
            loadObjQuiz(editable);
        }
    	

    }
    
    //==================================================================
    private void loadSubjQuiz(boolean editable) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/AttemptSubjQuiz.fxml"));
            Parent root = loader.load();

            AttemptSubjQuizController controller = loader.getController();
            if (controller != null) {
                controller.initData(studentHandler, editable);
            }

            Stage stage = new Stage();
            stage.setTitle("Attempt Subjective Quiz");
            stage.setScene(new Scene(root));
            
            // Show and wait until closed
            stage.showAndWait();
            
            // After the window is closed, reload quizzes
            loadQuizzes();

        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}

    private void loadObjQuiz(boolean editable) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/AttemptObjQuiz.fxml"));
            Parent root = loader.load();

            AttemptObjQuizController controller = loader.getController();
            if (controller != null) {
                controller.initData(studentHandler, editable);
            }

            Stage stage = new Stage();
            stage.setTitle("Attempt Objective Quiz");
            stage.setScene(new Scene(root));
            
            // Show and wait until closed
            stage.showAndWait();
            
            // After the window is closed, reload quizzes
            loadQuizzes();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
