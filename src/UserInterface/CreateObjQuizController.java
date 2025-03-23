package UserInterface;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class CreateObjQuizController {

    @FXML
    private ComboBox<?> Correct_opt;

    @FXML
    private DatePicker Deadline;

    @FXML
    private TableView<?> Lst_Ques;

    @FXML
    private TextField Marks;

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
    private Button rmv_ques;

    @FXML
    void handle_add_ques(MouseEvent event) {

    }

    @FXML
    void handle_create_ObjQuiz(MouseEvent event) {

    }

    @FXML
    void handle_rmv_ques(MouseEvent event) {

    }

}
