package UserInterface;
import controllers.TeacherHandler;
import businessLayer.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.util.List;

public class TeachersTabController {

    private TeacherHandler teacherHandler;

    public void setTeacherHandler(TeacherHandler handler) {
        this.teacherHandler = handler;
        loadTeachers();
    }

    @FXML
    private Button RemoveTeacher;

    @FXML
    private TableView<Teacher> lst_Teachers;
    
    @FXML
    private TableColumn<Teacher, String> name_t;

    @FXML
    private TableColumn<Teacher, String> username_t;

    @FXML
    void handle_RemoveTeacher(MouseEvent event) {
        // Retrieve the selected teacher from the TableView.
        Teacher selectedTeacher = lst_Teachers.getSelectionModel().getSelectedItem();
        if (selectedTeacher == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Teacher Selected");
            alert.setContentText("Please select a teacher to remove.");
            alert.showAndWait();
            return;
        }
        
        // Get the class code from the current class in teacherHandler.
        String classCode = teacherHandler.getCurrentClass().getClassCode();
        
        // Attempt to remove the teacher using teacherHandler.
        boolean removed = teacherHandler.removeTeacherFromClass(classCode, selectedTeacher.getUsername());
        
        if (removed) {
            // If removal is successful, update the TableView.
            lst_Teachers.getItems().remove(selectedTeacher);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Removal Successful");
            alert.setHeaderText(null);
            alert.setContentText("Teacher removed successfully.");
            alert.showAndWait();
        } else {
            // Notify the user of the failure.
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Removal Failed");
            alert.setHeaderText(null);
            alert.setContentText("Failed to remove the teacher.");
            alert.showAndWait();
        }
    }
    
    private void loadTeachers() {
        if (teacherHandler != null && teacherHandler.getCurrentClass() != null) {
            String classCode = teacherHandler.getCurrentClass().getClassCode();
            List<Teacher> teachers = teacherHandler.getTeachersInTheClass(classCode);
            ObservableList<Teacher> teacherList = FXCollections.observableArrayList(teachers);
            lst_Teachers.setItems(teacherList);
            
            name_t.setCellValueFactory(new PropertyValueFactory<>("name"));
            username_t.setCellValueFactory(new PropertyValueFactory<>("username"));
        }
    }
}
