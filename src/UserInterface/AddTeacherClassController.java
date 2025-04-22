package UserInterface;

import controllers.TeacherHandler;
import businessLayer.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.util.List;

public class AddTeacherClassController {

    private TeacherHandler teacherHandler;

    public void initData(TeacherHandler handler) {
        this.teacherHandler = handler;
        setupUserNameFilter();
        loadAllTeachers();
    }

    @FXML
    private Button AddBtn;

    @FXML
    private TableView<Teacher> List_Teachers;

    @FXML
    private Button Search;

    @FXML
    private TextField UserName;

    @FXML
    private TableColumn<Teacher, String> name_t;

    @FXML
    private TableColumn<Teacher, String> username_t;

    // Filter the UserName field to allow only alphabets or spaces.
    private void setupUserNameFilter(){
         UserName.textProperty().addListener((obs, oldText, newText) -> {
              if (!newText.matches("[A-Za-z ]*")) {
                  UserName.setText(oldText);
              }
         });
    }

    // Load all teachers using teacherHandler.getAllTeachers().
    private void loadAllTeachers(){
         if(teacherHandler != null){
             List<Teacher> teachers = teacherHandler.getAllTeachers();
             ObservableList<Teacher> teacherList = FXCollections.observableArrayList(teachers);
             List_Teachers.setItems(teacherList);
             name_t.setCellValueFactory(new PropertyValueFactory<>("name"));
             username_t.setCellValueFactory(new PropertyValueFactory<>("username"));
         }
    }

    // Handle search: Get teachers by name and update the table.
    @FXML
    void handle_Search(MouseEvent event) {
         if (teacherHandler != null) {
             String filter = UserName.getText().trim();
             List<Teacher> teachers = teacherHandler.getTeacherByName(filter);
             ObservableList<Teacher> teacherList = FXCollections.observableArrayList(teachers);
             List_Teachers.setItems(teacherList);
         }
    }

    // Handle add teacher: add the selected teacher to the current class.
    @FXML
    void handle_AddBtn(MouseEvent event) {
         Teacher selected = List_Teachers.getSelectionModel().getSelectedItem();
         if(selected == null){
             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("Add Teacher Error");
             alert.setHeaderText(null);
             alert.setContentText("Please select a teacher from the list.");
             alert.showAndWait();
             return;
         }
         String teacherID = selected.getUsername();
         String classCode = teacherHandler.getCurrentClass().getClassCode();
         boolean success = teacherHandler.addTeacherInClass(teacherID, classCode);
         if(success){
             Alert alert = new Alert(Alert.AlertType.INFORMATION);
             alert.setTitle("Success");
             alert.setHeaderText(null);
             alert.setContentText("Teacher added to the class successfully.");
             alert.showAndWait();
             loadAllTeachers(); // Optionally reload the list if changes occur.
         } else {
             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("Error");
             alert.setHeaderText(null);
             alert.setContentText("Teacher is already in the class.");
             alert.showAndWait();
         }
    }
}
