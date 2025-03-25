package UserInterface;

import controllers.TeacherHandler;
import businessLayer.Student;
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

public class StudentsTabController {

    private TeacherHandler teacherHandler;

    public void setTeacherHandler(TeacherHandler handler) {
        this.teacherHandler = handler;
        loadStudents();
    }

    @FXML
    private Button RemoveStudent;

    @FXML
    private TableView<Student> lst_Students;
    
    @FXML
    private TableColumn<Student, String> name_t;

    @FXML
    private TableColumn<Student, String> username_t;

    @FXML
    void handle_RemoveStudent(MouseEvent event) {
        // Get the selected student from the table view.
        Student selectedStudent = lst_Students.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Student Selected");
            alert.setContentText("Please select a student to remove.");
            alert.showAndWait();
            return;
        }

        // Get the class code from the current class in teacherHandler.
        String classCode = teacherHandler.getCurrentClass().getClassCode();
        
        // Attempt to remove the student using teacherHandler.
        boolean removed = teacherHandler.removeStudentFromClass(classCode, selectedStudent.getUsername());
        
        if (removed) {
            // If removal is successful, update the TableView.
            lst_Students.getItems().remove(selectedStudent);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Removal Successful");
            alert.setHeaderText(null);
            alert.setContentText("Student removed successfully.");
            alert.showAndWait();
        } else {
            // Notify the user of the failure.
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Removal Failed");
            alert.setHeaderText(null);
            alert.setContentText("Failed to remove the student.");
            alert.showAndWait();
        }
    }
    
    private void loadStudents() {
        if (teacherHandler != null && teacherHandler.getCurrentClass() != null) {
            String classCode = teacherHandler.getCurrentClass().getClassCode();
            // Use TeacherHandler's method to fetch students in the class.
            List<Student> students = teacherHandler.getStudentsInTheClass(classCode);
            ObservableList<Student> studentList = FXCollections.observableArrayList(students);
            lst_Students.setItems(studentList);
            
            // Set up table columns using property names that match your Student class getters.
            name_t.setCellValueFactory(new PropertyValueFactory<>("name"));
            username_t.setCellValueFactory(new PropertyValueFactory<>("username"));
        }
    }
}
